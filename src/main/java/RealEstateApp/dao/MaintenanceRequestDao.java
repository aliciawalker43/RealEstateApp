package RealEstateApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.MaintenanceRequest;



public interface MaintenanceRequestDao extends JpaRepository<MaintenanceRequest, Long> {
}
