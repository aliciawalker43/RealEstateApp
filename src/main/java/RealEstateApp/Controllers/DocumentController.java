package RealEstateApp.Controllers;





import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
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
	    	User currentUser= (User)session.getAttribute("user");
			Long userId =  currentUser.getId();
			 User user = userDao.findById(userId)
		                .orElseThrow(() -> new IllegalStateException("User not found"));
			 
			 Long companyId = currentUser.getCompany().getId();
			 model.addAttribute("docs",
			     documentRepo.findByCompanyIdOrderByUploadedAtDesc(companyId)
			 );
			 
			 var docs = documentRepo.findByCompanyIdOrderByUploadedAtDesc(companyId);
			 
		     model.addAttribute("user", user);
	        model.addAttribute("docs", documentRepo.findByUser(user));
	        return "/tenant/dashboard";
	    }
	}