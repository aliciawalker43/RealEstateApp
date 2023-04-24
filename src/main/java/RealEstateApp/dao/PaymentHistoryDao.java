package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.PaymentHistory;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.User;

public interface PaymentHistoryDao extends JpaRepository< PaymentHistory, Long>  {

	
	List<PaymentHistory> findAllByProperty(String property);
}
