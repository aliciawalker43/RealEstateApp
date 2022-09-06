package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.User;


public interface UserDao extends JpaRepository<User, Long>{

	User findByUsername(String name);
	
	 User findUserById(Long id);
	
	List<User> findAllById(Long id);
	
	List<User>  findAllByAccessStatus(String accessStatus);
	

	
	
}
