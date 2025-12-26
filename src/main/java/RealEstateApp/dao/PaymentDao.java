package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Payment;



public interface PaymentDao extends JpaRepository<Payment, Long> {
}
