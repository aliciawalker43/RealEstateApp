package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.SubscriptionPlan;



public interface SubscriptionPlanDao extends JpaRepository<SubscriptionPlan, Long> {
}