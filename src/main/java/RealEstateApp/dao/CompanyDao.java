package RealEstateApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Company;



public interface CompanyDao extends JpaRepository<Company, Long> {
}
