package RealEstateApp;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import RealEstateApp.dao.PropertyDao;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.MaintenanceRequestDao;
import RealEstateApp.dao.MessageDao;
import RealEstateApp.dao.PaymentDao;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    private final CompanyDao companyDao;
    private final PropertyDao propertyDao;
    private final PaymentDao paymentDao;
    private final MaintenanceRequestDao maintenanceRequestDao;
    private final MessageDao messageDao;

    public AdminDashboardController(CompanyDao companyDao, PropertyDao propertyDao,
                                    PaymentDao paymentDao,
                                    MaintenanceRequestDao maintenanceRequestDao,
                                    MessageDao messageDao) {
        this.companyDao = companyDao;
        this.propertyDao = propertyDao;
        this.paymentDao= paymentDao;
        this.maintenanceRequestDao = maintenanceRequestDao;
        this.messageDao = messageDao;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("companyCount", companyDao.count());
        model.addAttribute("propertyCount", propertyDao.count());
        model.addAttribute("paymentCount", paymentDao.count());
        model.addAttribute("openMaintenanceCount", maintenanceRequestDao.count());
        model.addAttribute("messageCount", messageDao.count());
        return "admin/dashboard";
    }
}
