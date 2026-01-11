package RealEstateApp.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.User;



public interface CompanyDao extends JpaRepository<Company, Long> {

	
}
