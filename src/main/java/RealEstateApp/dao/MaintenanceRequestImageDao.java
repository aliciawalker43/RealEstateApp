package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.MaintenanceRequestImage;


	public interface MaintenanceRequestImageDao extends JpaRepository<MaintenanceRequestImage, Long> {

		   List<MaintenanceRequestImage> findByRequestIdIn(List<Long> requestIds);

		    List<MaintenanceRequestImage> findByRequestId(Long requestId); // optional (detail page)

}
