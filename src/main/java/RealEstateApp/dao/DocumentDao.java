package RealEstateApp.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Document;
import RealEstateApp.Pojo.User;

public interface DocumentDao extends JpaRepository<Document, Long> {
	List<Document> findByCompanyIdOrderByUploadedAtDesc(Long Id);
	List<Document> findByUser (User user);
	Object findByCompanyOrderByUploadedAtDesc(Company company);
	List<Document> findAllByCompany(Company company);
}

