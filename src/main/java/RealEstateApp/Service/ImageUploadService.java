package RealEstateApp.Service;



import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.ImageAsset;
import RealEstateApp.Pojo.ImageCategory;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.TenantProfile;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.EmployeeProfileDao;
import RealEstateApp.dao.ImageAssetDao;
import RealEstateApp.dao.TenantProfileDao;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import net.coobird.thumbnailator.Thumbnails;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageUploadService {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            "image/png", "image/jpeg", "image/jpg", "image/webp", "image/gif"
    );

    private final ImageAssetDao imageAssetDao;
    private final Path uploadRoot;
    private final EmployeeProfileDao employeeProfileDao;
    private final TenantProfileDao tenantProfileDao;

    
    
    public ImageUploadService(ImageAssetDao imageAssetDao,
    		                 TenantProfileDao tenantProfileDao,
    		                 EmployeeProfileDao employeeProfileDao,
                              @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.employeeProfileDao =employeeProfileDao;
    	this.tenantProfileDao = tenantProfileDao;
    	this.imageAssetDao = imageAssetDao;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    public ImageAsset upload(MultipartFile file,
                             ImageCategory category,
                             Long contextId,
                             HttpSession session) throws IOException {

        if (file == null || file.isEmpty()) throw new IllegalArgumentException("Image file is empty.");

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) throw new IllegalStateException("Not logged in.");

        Company company = currentUser.getCompany();
        if (company == null) throw new IllegalStateException("User is not associated with a company.");

        String contentType = file.getContentType() == null ? "" : file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only image uploads allowed (png, jpg, webp, gif).");
        }

        // uploads/company/{companyId}/{category}/(contextId optional)/
        Path targetDir = uploadRoot
                .resolve("company")
                .resolve(String.valueOf(company.getId()))
                .resolve(category.name().toLowerCase())
                .normalize();

        if (contextId != null) {
            targetDir = targetDir.resolve(String.valueOf(contextId)).normalize();
        }

        Files.createDirectories(targetDir);

        String safeOriginalName = sanitize(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "_" + safeOriginalName;

        Path destination = targetDir.resolve(storedName).normalize();
        if (!destination.startsWith(targetDir)) throw new IllegalArgumentException("Invalid file path.");

        try (OutputStream out = Files.newOutputStream(
                destination,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            Thumbnails.of(file.getInputStream())
                    .size(512, 512)
                    .outputFormat("jpg")
                    .outputQuality(0.8)
                    .toOutputStream(out);
        }

        // 3) Store a WEB path (recommended) rather than destination.toString()
        // /uploads maps to uploadRoot via ResourceHandler
        String webPath = "/uploads/company/" + company.getId() + "/"
                + category.name().toLowerCase()
                + (contextId != null ? "/" + contextId : "")
                + "/" + storedName;
                	
        ImageAsset asset = new ImageAsset();
        asset.setCompany(company);
        asset.setUser(currentUser);
        asset.setCategory(category);
        asset.setContextId(contextId);
        asset.setOriginalFileName(safeOriginalName);
        asset.setContentType(contentType);
        asset.setSizeBytes(file.getSize());
        asset.setStoragePath(webPath);
        asset.setUploadedAt(Instant.now());
      

        return imageAssetDao.save(asset);
    }
    
    
    @Transactional
    public ImageAsset uploadProfileImage(MultipartFile file,
                             ImageCategory category,
                             Long contextId,
                             HttpSession session) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty.");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) throw new IllegalStateException("Not logged in.");

        Company company = currentUser.getCompany();
        if (company == null) throw new IllegalStateException("User is not associated with a company.");

        String contentType = file.getContentType() == null ? "" : file.getContentType();
        if (!ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Only image uploads allowed (png, jpg, webp, gif).");
        }

        // uploads/company/{companyId}/{category}/(contextId optional)/
        Path targetDir = uploadRoot
                .resolve("company")
                .resolve(String.valueOf(company.getId()))
                .resolve(category.name().toLowerCase())
                .normalize();

        if (contextId != null) {
            targetDir = targetDir.resolve(String.valueOf(contextId)).normalize();
        }

        Files.createDirectories(targetDir);

        String safeOriginalName = sanitize(file.getOriginalFilename());

        // If your goal is "replace old", you can keep a stable filename:
        // e.g. profile image overwrites consistently.
        // Otherwise keep UUID. I'll keep UUID but delete old after DB update.
        String storedName = UUID.randomUUID() + "_" + safeOriginalName.replaceAll("\\s+", "_");
        storedName = storedName.replaceAll("[^a-zA-Z0-9._-]", "_");

        Path destination = targetDir.resolve(storedName).normalize();
        if (!destination.startsWith(targetDir)) throw new IllegalArgumentException("Invalid file path.");

        // 1) Look up existing asset BEFORE writing new file (so we can delete old after success)
        ImageAsset existingOpt =
                imageAssetDao.findTopByCompanyIdAndUserIdAndCategoryOrderByUploadedAtDesc(
                                company.getId(), currentUser.getId(), category
                        );

        ImageAsset existing = existingOpt.orElse(null);
        String oldStoragePath = (existing != null) ? existing.getStoragePath() : null;

        // 2) Write the NEW image (resize/compress) — only after this succeeds do we delete old
        try (OutputStream out = Files.newOutputStream(
                destination,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )) {
            Thumbnails.of(file.getInputStream())
                    .size(512, 512)
                    .outputFormat("jpg")
                    .outputQuality(0.8)
                    .toOutputStream(out);
        }

        // 3) Store a WEB path (recommended) rather than destination.toString()
        // /uploads maps to uploadRoot via ResourceHandler
        String webPath = "/uploads/company/" + company.getId() + "/"
                + category.name().toLowerCase()
                + (contextId != null ? "/" + contextId : "")
                + "/" + storedName;

        // 4) Upsert: update existing row or create new
        ImageAsset asset = (existing != null) ? existing : new ImageAsset();
        asset.setCompany(company);
        asset.setUser(currentUser);
        asset.setCategory(category);
        asset.setContextId(contextId);
        asset.setOriginalFileName(safeOriginalName);
        asset.setContentType("image/jpeg");           // because we outputFormat("jpg")
        asset.setSizeBytes(Files.size(destination));  // actual resized file size
        asset.setStoragePath(webPath);
        asset.setUploadedAt(Instant.now());

        ImageAsset saved = imageAssetDao.save(asset);

        // 5) Delete old file AFTER DB save (safe) — but only if it’s a different file
        deleteOldFileIfLocal(oldStoragePath, webPath);
        
     // Save pointer onto tenant profile (only if tenant)
        if (currentUser.getRole() == Role.TENANT) {
            TenantProfile tp = tenantProfileDao.findById(currentUser.getId())
                    .orElseThrow(() -> new IllegalStateException("Tenant profile not found"));
            tp.setProfileImageUrl(saved.getStoragePath());
            tenantProfileDao.save(tp);
        }

        // Save pointer onto employee profile (only if employee)
        if (currentUser.getRole() == Role.EMPLOYEE) {
            EmployeeProfile ep = employeeProfileDao.findById(currentUser.getId())
                    .orElseThrow(() -> new IllegalStateException("Employee profile not found"));
            ep.setProfileImageUrl(saved.getStoragePath());
            employeeProfileDao.save(ep);
        }

        return saved;
    }

    private void deleteOldFileIfLocal(String oldWebPath, String newWebPath) {
        try {
            if (oldWebPath == null || oldWebPath.isBlank()) return;
            if (oldWebPath.equals(newWebPath)) return;
            if (!oldWebPath.startsWith("/uploads/")) return;

            // Convert "/uploads/..." -> uploadRoot/...
            Path root = uploadRoot.toAbsolutePath().normalize();
            Path oldDiskPath = root.resolve(oldWebPath.replaceFirst("^/uploads/", "")).normalize();
            if (!oldDiskPath.startsWith(root)) return; // safety

            Files.deleteIfExists(oldDiskPath);
        } catch (Exception ignored) {
            // don’t fail the upload if delete fails
        }
    }

    
    
    
    
    
    
    
    

    public ImageAsset getRequired(Long id) {
        return imageAssetDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Image not found: " + id));
    }

    public Resource loadAsResource(Long id) {
        ImageAsset asset = getRequired(id);
        return new FileSystemResource(asset.getStoragePath());
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) return "image";
        return name.replace("\\", "_")
                .replace("/", "_")
                .replace("..", "_")
                .trim();
    }
}
