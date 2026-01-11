package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Role;
import RealEstateApp.Pojo.User;


public interface UserDao extends JpaRepository<User, Long>{

	User findByUsername(String name);
	
	 User findUserById(Long id);
	
	List<User> findAllById(Long id);

	Object findByCompanyIdAndRoleIn(Long id, List<Role> of);

	 

	boolean existsByEmail(String email);

	List<User> findAllByCompanyId(Long id);

	List<User>  findByCompanyIdAndRole(Long companyId, Role tenant);
	
	
	

	
	
}
