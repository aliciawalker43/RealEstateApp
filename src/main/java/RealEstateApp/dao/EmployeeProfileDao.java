package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.EmployeeProfile;
import RealEstateApp.Pojo.User;

public interface EmployeeProfileDao extends JpaRepository<EmployeeProfile, Long> {

	

	List<EmployeeProfile> findByUser_Company_Id(Long id);

}