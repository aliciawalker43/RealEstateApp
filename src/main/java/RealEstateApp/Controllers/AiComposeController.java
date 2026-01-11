package RealEstateApp.Controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import RealEstateApp.Pojo.AiEmailDraftRequest;
import RealEstateApp.Pojo.User;
import RealEstateApp.Service.AiComposeService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/landlord/ai")
public class AiComposeController {

  private final AiComposeService aiComposeService;

  public AiComposeController(AiComposeService aiComposeService) {
    this.aiComposeService = aiComposeService;
  }

  @PostMapping("/email-draft")
  public Map<String, String> draft(@RequestBody AiEmailDraftRequest req, HttpSession session) {
    User currentUser = (User) session.getAttribute("user");
    if (currentUser == null || currentUser.getCompany() == null) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }

    return aiComposeService.generateEmailDraft(currentUser.getCompany(), req);
  }
}
