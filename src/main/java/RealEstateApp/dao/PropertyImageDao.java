package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.PropertyImage;



public interface PropertyImageDao extends JpaRepository<PropertyImage, Long> {
}