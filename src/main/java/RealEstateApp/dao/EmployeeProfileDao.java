package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.EmployeeProfile;

public interface EmployeeProfileDao extends JpaRepository<EmployeeProfile, Long> {}
