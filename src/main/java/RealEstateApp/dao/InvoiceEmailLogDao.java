package RealEstateApp.dao;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.InvoiceEmailLog;

public interface InvoiceEmailLogDao extends JpaRepository<InvoiceEmailLog, Long> {
   
	boolean existsByCompanyIdAndTenantIdAndForMonthAndType(Long companyId, Long tenantId, LocalDate forMonth, String type);

	boolean existsByCompanyIdAndTenantIdAndPropertyIdAndForMonthAndType(Long id, Long id2, Long id3,
			LocalDate ForMonth, String string);

	Object findAllByCompanyId(Long companyId);

	
}