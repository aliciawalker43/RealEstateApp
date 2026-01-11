package RealEstateApp.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Property;
import RealEstateApp.Pojo.User;

public interface PropertyDao extends JpaRepository< Property, Long> {

	 Property findPropertyById(Long id);

	List<Property> findAllByCompany(Company company);

	@Query("select p from Property p where p.tenant is not null and p.company is not null")
	List<Property> findAllWithTenant();

	
}
