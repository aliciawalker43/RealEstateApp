package RealEstateApp.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.EmailAudience;
import RealEstateApp.Pojo.InvoiceEmailLog;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.InvoiceEmailLogDao;
import RealEstateApp.dao.PaymentDao;
import RealEstateApp.dao.PropertyDao;
import jakarta.transaction.Transactional;

@Service
public class AutoInvoiceScheduler {

    private final EmailService emailService;
    private final PropertyDao propertyDao;
    private final InvoiceEmailLogDao logDao;
    private final CompanyDao companyDao;
    private final PaymentDao paymentDao;

    public AutoInvoiceScheduler(EmailService emailService,
                                PropertyDao propertyDao,
                                InvoiceEmailLogDao logDao,
                                CompanyDao companyDao,
                                PaymentDao paymentDao) {
        this.emailService = emailService;
        this.propertyDao = propertyDao;
        this.logDao = logDao;
        this.companyDao =companyDao;
        this. paymentDao= paymentDao;
    }
    
    
    
    @Scheduled(cron = "0 55 23 * * *", zone = "America/Toronto") // once nightly
    @Transactional
    public void nightlyAutoInvoices() {
        List<Company> companies = companyDao.findAll();

        for (Company company : companies) {
            ZoneId zone = ZoneId.of(
                (company.getTimeZone() == null || company.getTimeZone().isBlank())
                    ? "America/Detroit"
                    : company.getTimeZone()
            );

            LocalDate today = LocalDate.now(zone);

            int lateDays = (company.getLateNoticeDaysAfterDue() != null)
                    ? company.getLateNoticeDaysAfterDue()
                    : 3;

            runCompanyInvoices(company, today, lateDays);
        }
    }
    
    
    private void runCompanyInvoices(Company company, LocalDate today, int lateDays) {
        LocalDate periodStart = today.withDayOfMonth(1);
        LocalDate periodEnd = today.withDayOfMonth(today.lengthOfMonth());
        LocalDate periodMonth = periodStart;

        List<Property> props = propertyDao.findAllWithTenant();

        for (Property p : props) {
            if (p.getTenant() == null) continue;

            // Due date this month (cap by month length)
            int dueDay = p.getRentDueDay();
            LocalDate dueDate = periodStart.withDayOfMonth(Math.min(dueDay, periodStart.lengthOfMonth()));

            // When to send late invoice
            LocalDate invoiceDay = dueDate.plusDays(lateDays);
            if (!today.isEqual(invoiceDay)) continue;

            BigDecimal rent = p.getRentAmount() == null ? BigDecimal.ZERO : p.getRentAmount();
            BigDecimal lateFee = p.getLateFee() == null ? BigDecimal.ZERO : p.getLateFee();

           BigDecimal paid = paymentDao.sumPaidForPeriod(company.getId(), p.getId(), periodStart, periodEnd);

            // If late, total due includes late fee
            BigDecimal totalDue = rent.add(lateFee);

            BigDecimal outstanding = totalDue.subtract(paid);
            if (outstanding.compareTo(BigDecimal.ZERO) <= 0) continue; // nothing owed

            // Prevent duplicates
            boolean alreadySent = logDao.existsByCompanyIdAndTenantIdAndPropertyIdAndForMonthAndType(
                company.getId(), p.getTenant().getId(), p.getId(), periodMonth, "LATE_INVOICE"
            );
            if (alreadySent) continue;

            // Send invoice email (amount due = outstanding)
            String subject = company.getName() + " - Invoice: Balance Due";
            String body = buildLateInvoiceBody(company, p, dueDate, outstanding, paid, totalDue);

            emailService.sendCompanyEmail(p.getTenant().getEmail(), subject, body);

            logDao.save(makeLog(company, p.getTenant(), p, periodMonth, "LATE_INVOICE", body, subject));
        }
    }



    private InvoiceEmailLog makeLog(Company c, User t, Property p, LocalDate forMonth, String type, String body, String subject) {
        InvoiceEmailLog log = new InvoiceEmailLog();
        log.setCompany(c);
        log.setTenant(t);
        log.setProperty(p);
        log.setForMonth(forMonth);
        log.setType(type);
        log.setBody(body);
        log.setSubject(subject);
        log.setSentAt(Instant.now());
        
        return log;
    }

    public LocalDate calculateDueDate(Property p, LocalDate referenceDate) {
        int dueDay = p.getRentDueDay();
        int safeDay = Math.min(dueDay, referenceDate.lengthOfMonth());
        return referenceDate.withDayOfMonth(safeDay);
    }

    public LocalDate calculateLateInvoiceDate(Property p, LocalDate referenceDate, int lateDaysAfterDue) {
        return calculateDueDate(p, referenceDate).plusDays(lateDaysAfterDue);
    }

    private String buildLateInvoiceBody(Company company,
            Property property,
            LocalDate dueDate,
            BigDecimal outstanding,
            BigDecimal paid,
            BigDecimal totalDue) {

String companyName = company.getName() != null
? company.getName()
: "Your Property Management Company";

String propertyAddress = property.getRentalAddress() != null
? property.getRentalAddress()
: "your rental property";

return """
Hello,

This is a reminder that rent payment for the following property is past due:

Property: %s
Original Due Date: %s

Payment Summary:
------------------------
Total Amount Due: $%s
Amount Paid: $%s
Outstanding Balance: $%s

Please submit payment as soon as possible to avoid additional fees or actions.

If you have already made a payment, please disregard this message.

Thank you,
%s
""".formatted(
propertyAddress,
dueDate,
totalDue.setScale(2),
paid.setScale(2),
outstanding.setScale(2),
companyName
);
}
    

    private String safeName(User u) {
        String name = (u.getFirstname() != null ? u.getFirstname() : "");
        if (name.isBlank()) name = (u.getUsername() != null ? u.getUsername() : "Resident");
        return name;
    }
}

