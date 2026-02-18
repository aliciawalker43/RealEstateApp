package RealEstateApp.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;



import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.PaymentMethod;
import RealEstateApp.Pojo.PaymentSource;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.User;
import RealEstateApp.dao.CompanyDao;
import RealEstateApp.dao.InvoiceEmailLogDao;
import RealEstateApp.dao.RentPaymentDao;
import RealEstateApp.dao.PropertyDao;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	 private final EmailService emailService;
	    private final PropertyDao propertyDao;
	    private final InvoiceEmailLogDao logDao;
	    private final CompanyDao companyDao;
	    private final RentPaymentDao paymentDao;

	    public PaymentService(EmailService emailService,
	                                PropertyDao propertyDao,
	                                InvoiceEmailLogDao logDao,
	                                CompanyDao companyDao,
	                                RentPaymentDao paymentDao) {
	        this.emailService = emailService;
	        this.propertyDao = propertyDao;
	        this.logDao = logDao;
	        this.companyDao =companyDao;
	        this.paymentDao =paymentDao;
	    }

	        @Transactional
	        public RentPayment recordPayment(Company company,
	                                     Property property,
	                                     User tenant,
	                                     BigDecimal amount,
	                                     LocalDate paymentDate,
	                                     PaymentMethod method,
	                                     PaymentSource source,
	                                     String externalTxnId) {

	            if (company == null) throw new IllegalArgumentException("Company required");
	            if (property == null) throw new IllegalArgumentException("Property required");
	            if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
	                throw new IllegalArgumentException("Amount must be > 0");

	            RentPayment p = new RentPayment();
	            p.setCompany(company);
	            p.setProperty(property);
	            p.setUser(tenant); // if your Payment has tenant; if not, skip
	            p.setAmount(amount);
	            p.setPaymentDate(paymentDate != null ? paymentDate : LocalDate.now());
	            p.setMethod(method != null ? method : PaymentMethod.OTHER);
	            p.setSource(source != null ? source : PaymentSource.MANUAL);
	            p.setTransactionId(externalTxnId);

	            RentPayment saved = paymentDao.save(p);

	            // Email tenant receipt
	            if (tenant != null && tenant.getEmail() != null && !tenant.getEmail().isBlank()) {
	                emailService.sendCompanyEmail(
	                    tenant.getEmail(),
	                    company.getName() + " – Payment Receipt",
	                    buildTenantReceipt(company, tenant, property, saved)
	                );
	            }

	           /* // Email company notification (use a company email field if you have one)
	            if (company.getEmail() != null && !company.getEmail().isBlank()) {
	                emailService.sendPlainText(
	                    company.getEmail(),
	                    "Payment received – " + safeAddress(property),
	                    buildCompanyPaymentNotice(company, tenant, property, saved)
	                );
	            }*/

	            return saved;
	        }

	        private String safeAddress(Property p) {
	            return (p.getRentalAddress() != null && !p.getRentalAddress().isBlank())
	                    ? p.getRentalAddress()
	                    : "Property #" + p.getId();
	        }

	        private String buildTenantReceipt(Company c, User t, Property prop, RentPayment pay) {
	            return """
	            Hello %s,

	            We received your payment.

	            Company: %s
	            Property: %s
	            Amount: $%s
	            Date: %s
	            Method: %s

	            Thank you.
	            """.formatted(
	                displayName(t),
	                c.getName(),
	                safeAddress(prop),
	                pay.getAmount(),
	                pay.getPaymentDate(),
	                pay.getMethod()
	            );
	        }

	        private String buildCompanyPaymentNotice(Company c, User tenant, Property prop, RentPayment pay) {
	            String tenantLine = (tenant != null)
	                    ? (displayName(tenant) + " (" + tenant.getEmail() + ")")
	                    : "Unknown tenant";

	            return """
	            Payment received.

	            Tenant: %s
	            Property: %s
	            Amount: $%s
	            Date: %s
	            Source: %s
	            Txn ID: %s
	            """.formatted(
	                tenantLine,
	                safeAddress(prop),
	                pay.getAmount(),
	                pay.getPaymentDate(),
	                pay.getSource(),
	                pay.getTransactionId() != null ? pay.getTransactionId() : "-"
	            );
	        }

	        private String displayName(User u) {
	            String fn = u.getFirstname() != null ? u.getFirstname() : "";
	            String ln = u.getLastname() != null ? u.getLastname() : "";
	            String full = (fn + " " + ln).trim();
	            if (!full.isBlank()) return full;
	            if (u.getUsername() != null && !u.getUsername().isBlank()) return u.getUsername();
	            return "Resident";
	        }
	    
}