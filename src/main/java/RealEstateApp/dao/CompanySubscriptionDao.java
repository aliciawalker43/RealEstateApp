package RealEstateApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.CompanySubscription;


public interface CompanySubscriptionDao extends JpaRepository<CompanySubscription, Long> {
}