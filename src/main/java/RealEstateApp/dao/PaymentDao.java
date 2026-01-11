package RealEstateApp.dao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import RealEstateApp.Pojo.Payment;
import RealEstateApp.Pojo.User;



public interface PaymentDao extends JpaRepository<Payment, Long> {
	List<Payment> findByUser (User tenant);
	
	List<Payment> findAllByCompanyId(Long id);
	
	
	@Query("""
		    SELECT COALESCE(SUM(p.amount), 0)
		    FROM Payment p
		    WHERE p.company.id = :companyId
		      AND p.property.id = :propertyId
		      AND p.paymentDate BETWEEN :startDate AND :endDate
		      AND p.status = 'SUCCESS'
		""")
	BigDecimal sumPaidForPeriod(
	        @Param("companyId") Long companyId,
	        @Param("propertyId") Long propertyId,
	        @Param("startDate") LocalDate startDate,
	        @Param("endDate") LocalDate endDate
	);
	
	
}
