package RealEstateApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import RealEstateApp.Pojo.Property;

public interface PropertyDao extends JpaRepository< Property, Long> {

	 Property findPropertyById(Long id);
}
