package RealEstateApp.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Message;



public interface MessageDao extends JpaRepository<Message, Long> {
}
