package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import RealEstateApp.Pojo.PaymentSource;
import RealEstateApp.Pojo.RentPayment;
import RealEstateApp.Pojo.User;



public interface RentPaymentDao extends JpaRepository<RentPayment, Long> {
	
	
	List<RentPayment> findByUser (User tenant);
	
	List<RentPayment> findAllByCompanyId(Long id);
	
    List <RentPayment> findAllBySourceIn(List<PaymentSource> sources);
    
    List <RentPayment> findAllByCompanyIdAndSourceIn(Long id, List<PaymentSource> sources);
	
	

	
	
}
