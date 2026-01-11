package RealEstateApp.dao;


import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.MaintenanceRequest;
import RealEstateApp.Pojo.User;



public interface MaintenanceRequestDao extends JpaRepository<MaintenanceRequest, Long> {
	List<MaintenanceRequest> findByUser (User user);

	List<MaintenanceRequest> findAllByCompanyId(Long id);
	
	List<MaintenanceRequest> findByCompanyIdOrderByCreatedAtDesc(Long companyId);
}
