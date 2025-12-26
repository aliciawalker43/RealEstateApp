package RealEstateApp.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Document;

public interface DocumentDao extends JpaRepository<Document, Long> {
}

