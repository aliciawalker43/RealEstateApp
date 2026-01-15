package RealEstateApp.Controllers;





import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.DocumentService;
import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.UserDao;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/documents")
public class DocumentController {

	   private final DocumentService documentService;
		private final UserDao userDao;
	    private final DocumentDao documentRepo;

	    public DocumentController(DocumentService documentService, UserDao userDao, DocumentDao documentRepo) {
	        this.documentService = documentService;
	        this.documentRepo = documentRepo;
	        this.userDao = userDao;
	    }

	    @GetMapping("/upload")
	    public String uploadGetRedirect() {
	        return "redirect:/documents";
	    }
	  

	    @PostMapping("/upload")
	    public String upload(@RequestParam("file") MultipartFile file, HttpSession session) {
	    	User currentUser= (User)session.getAttribute("user");
			Long userId =  currentUser.getId();
			 User user = userDao.findById(userId)
		                .orElseThrow(() -> new IllegalStateException("User not found"));

	        try {
	            documentService.uploadForSessionUser(file, session);
	            return "redirect:/";
	        } catch (Exception e) {
	            return "redirect:/documents?error=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
	        }
	    }
	    
	    @GetMapping
	    public String list(Model model, HttpSession session) {
	    	 User currentUser = (User) session.getAttribute("user");
	    	    if (currentUser == null) return "redirect:/login";

	    	    User user = userDao.findById(currentUser.getId())
	    	            .orElseThrow(() -> new IllegalStateException("User not found"));

	    	    // TENANT: show only their documents
	    	    if (user.getRole() == Role.TENANT) {
	    	        model.addAttribute("user", user);
	    	        model.addAttribute("docs", documentRepo.findByUser(user));
	    	        return "tenant/dashbboard"; // create templates/tenant/documents.html
	    	        // OR: return "redirect:/tenant/dashboard";  (if dashboard already shows docs)
	    	    }

	    	    // LANDLORD / EMPLOYEE: show company documents (optionally grouped by tenant)
	    	    if (user.getRole() == Role.LANDLORD || user.getRole() == Role.EMPLOYEE) {
	    	        if (user.getCompany() == null) return "redirect:/login";

	    	        Long companyId = user.getCompany().getId();
	    	        
	    	        model.addAttribute("user", user);
	    	        model.addAttribute("docs", documentRepo.findByCompanyIdOrderByUploadedAtDesc(companyId));
	    	        return "landlord/dashboard"; // templates/landlord/documents.html
	    	        // OR: return "redirect:/landlord/dashboard";
	    	    }

	    	    // APP ADMIN (if you have it)
	    	    return "redirect:/admin/dashboard";
	    	}
	    

	    // ✅ Viewer page (HTML template with embedded viewer + download button)
	    @GetMapping("/{id}/view")
	    public String viewPage(@PathVariable Long id, Model model, HttpSession session) {
	        User currentUser = (User) session.getAttribute("user");
	        if (currentUser == null || currentUser.getCompany() == null) return "redirect:/login";

	        Long companyId = currentUser.getCompany().getId();

	        Document doc = documentRepo.findById(id)
	                .orElseThrow(() -> new IllegalStateException("Document not found"));

	        // ✅ Security: prevent cross-company access
	        if (doc.getCompany() == null || !companyId.equals(doc.getCompany().getId())) {
	            throw new IllegalStateException("Not authorized to view this document.");
	        }

	        model.addAttribute("doc", doc);
	        model.addAttribute("user", currentUser);

	        // endpoints used inside the template
	        model.addAttribute("inlineUrl", "/documents/" + id + "/inline");
	        model.addAttribute("downloadUrl", "/documents/" + id + "/download");

	        return "documents/viewer";
	    }

	    // ✅ Inline (browser viewer)
	    @GetMapping("/{id}/inline")
	    public ResponseEntity<Resource> inline(@PathVariable Long id, HttpSession session) throws Exception {
	        return serveFile(id, session, false);
	    }

	    
	    // ✅ Download (forced)
	    @GetMapping("/{id}/download")
	    public ResponseEntity<Resource> download(@PathVariable Long id, HttpSession session) throws Exception {
	        return serveFile(id, session, true);
	    }

	    private ResponseEntity<Resource> serveFile(Long id, HttpSession session, boolean forceDownload) throws Exception {
	        User currentUser = (User) session.getAttribute("user");
	        if (currentUser == null || currentUser.getCompany() == null) {
	            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }

	        Long companyId = currentUser.getCompany().getId();

	        Document doc = documentRepo.findById(id)
	                .orElseThrow(() -> new IllegalStateException("Document not found"));

	        if (doc.getCompany() == null || !companyId.equals(doc.getCompany().getId())) {
	            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	        }

	        // Your entity field: storagePath (absolute or relative). This assumes it's a real file path.
	        Path filePath = Paths.get(doc.getStoragePath()).normalize();

	        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
	        }

	        Resource resource = new UrlResource(filePath.toUri());
	        String contentType = (doc.getContentType() == null || doc.getContentType().isBlank())
	                ? "application/octet-stream"
	                : doc.getContentType();

	        // Use originalFileName for better UX
	        String filename = Optional.ofNullable(doc.getOriginalFileName()).orElse("download");

	        ContentDisposition disposition = forceDownload
	                ? ContentDisposition.attachment().filename(filename, StandardCharsets.UTF_8).build()
	                : ContentDisposition.inline().filename(filename, StandardCharsets.UTF_8).build();

	        return ResponseEntity.ok()
	                .contentType(MediaType.parseMediaType(contentType))
	                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
	                .contentLength(Files.size(filePath))
	                .body(resource);
	    }
	    
	    
	}