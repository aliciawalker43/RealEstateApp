package RealEstateApp.Service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import RealEstateApp.Pojo.Property;

@Service
public class BillingDateService {

    public LocalDate calculateDueDate(Property p, LocalDate reference) {
        int dueDay = p.getRentDueDay();
        int safeDay = Math.min(dueDay, reference.lengthOfMonth());
        return reference.withDayOfMonth(safeDay);
    }

    public LocalDate calculateLateInvoiceDate(Property p,
                                              LocalDate reference,
                                              int lateDaysAfterDue) {
        return calculateDueDate(p, reference).plusDays(lateDaysAfterDue);
    }
}

