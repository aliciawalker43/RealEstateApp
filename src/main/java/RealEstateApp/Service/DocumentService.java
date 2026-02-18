package RealEstateApp.Service;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.UserDao;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.time.Instant;
import java.util.UUID;

@Service
public class DocumentService {

    private final DocumentDao documentDao;
    private final UserDao userDao;
    private final Path uploadRoot;

    public DocumentService(DocumentDao documentDao
                            , UserDao userDao,
                           @Value("${app.upload.dir:uploads/documents}") String uploadDir) {
        this.userDao = userDao;
    	this.documentDao = documentDao;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Upload a document for the currently logged-in session user.
     * Saves file to disk and saves metadata + company + user to DB.
     */
    public Document uploadForSessionUser(MultipartFile file, HttpSession session) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new IllegalStateException("No logged-in user found in session.");
        }

        Company company = currentUser.getCompany();
        if (company == null) {
            throw new IllegalStateException("Current user is not associated with a company.");
        }

        // Folder: uploads/company/{companyId}/documents/
        Path companyDocsDir = uploadRoot
                .resolve("company")
                .resolve(String.valueOf(company.getId()))
                .resolve("documents")
                .normalize();

        Files.createDirectories(companyDocsDir);

        String safeOriginalName = sanitize(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "_" + safeOriginalName;

        Path destination = companyDocsDir.resolve(storedName).normalize();

        // Prevent path traversal attacks
        if (!destination.startsWith(companyDocsDir)) {
            throw new IllegalArgumentException("Invalid file path.");
        }

        // Save file
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }

        // Save DB record
        Document doc = new Document();
        doc.setCompany(company);
        doc.setUser(currentUser);
        doc.setOriginalFileName(safeOriginalName);
        doc.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        doc.setSizeBytes(file.getSize());
        doc.setStoragePath(destination.toString());
        doc.setUploadedAt(Instant.now()); // optional because your entity defaults it
        doc.setUploadedBy(currentUser);

        return documentDao.save(doc);
    }
    
    
    public Document uploadForLandlordSessionUser(Long tenantId, MultipartFile file, HttpSession session) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty.");
        }

        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            throw new IllegalStateException("No logged-in user found in session.");
        }

        Company company = currentUser.getCompany();
        if (company == null) {
            throw new IllegalStateException("Current user is not associated with a company.");
        }

        // Folder: uploads/company/{companyId}/documents/
        Path companyDocsDir = uploadRoot
                .resolve("company")
                .resolve(String.valueOf(company.getId()))
                .resolve("documents")
                .normalize();

        Files.createDirectories(companyDocsDir);

        String safeOriginalName = sanitize(file.getOriginalFilename());
        String storedName = UUID.randomUUID() + "_" + safeOriginalName;

        Path destination = companyDocsDir.resolve(storedName).normalize();

        // Prevent path traversal attacks
        if (!destination.startsWith(companyDocsDir)) {
            throw new IllegalArgumentException("Invalid file path.");
        }

        // Save file
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, destination, StandardCopyOption.REPLACE_EXISTING);
        }
       

     // Find the tenant user (document owner)
     User tenant = (tenantId != null) ? userDao.findUserById(tenantId) : null;
     User documentOwner = (tenant != null) ? tenant : currentUser;

        

        // Save DB record
        Document doc = new Document();
        doc.setCompany(company);
        doc.setUser( documentOwner); // Set the document owner to the tenant);
        doc.setOriginalFileName(safeOriginalName);
        doc.setContentType(file.getContentType() == null ? "application/octet-stream" : file.getContentType());
        doc.setSizeBytes(file.getSize());
        doc.setStoragePath(destination.toString());
        doc.setUploadedAt(Instant.now()); // optional because your entity defaults it
        doc.setUploadedBy(currentUser);

        return documentDao.save(doc);
    }

    private String sanitize(String name) {
        if (name == null || name.isBlank()) return "file";
        return name.replace("\\", "_")
                   .replace("/", "_")
                   .replace("..", "_")
                   .trim();
    }
}


