package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Employee;



public interface EmployeeDao extends JpaRepository<Employee, Long> {
}
