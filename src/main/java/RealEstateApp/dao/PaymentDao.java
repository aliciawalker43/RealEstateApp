package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Payment;
import RealEstateApp.Pojo.User;



public interface PaymentDao extends JpaRepository<Payment, Long> {
	List<Payment> findByUser (User tenant);
}
