package RealEstateApp;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import RealEstateApp.dao.DocumentDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;

@Controller
@RequestMapping("/tenant")
public class TenantDashboardController {

    private final PaymentDao paymentDao;
    private final DocumentDao documentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
    private final MessageDao messageDao;

    public TenantDashboardController(PaymentDao paymentDao, DocumentDao documentDao,
                                     MaintenanceRequestDao maintenanceRequestDao,
                                     MessageDao messageDao) {
        this.paymentDao = paymentDao;
        this.documentDao = documentDao;
        this.maintenanceRequestDao = maintenanceRequestDao;
        this.messageDao = messageDao;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("payments", paymentDao.findAll());
        model.addAttribute("documents", documentDao.findAll());
        model.addAttribute("requests", maintenanceRequestDao.findAll());
        model.addAttribute("messageCount", messageDao.count());
        return "tenant/dashboard";
    }
}
