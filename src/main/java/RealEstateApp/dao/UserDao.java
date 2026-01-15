package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
	
	
	//Admin Data

	List<User> findAllByOrderByCompany_NameAscLastnameAscFirstnameAsc();

	List<User> findByCompanyIdOrderByLastnameAscFirstnameAsc(Long companyId);
	
	
	 @Query("""
		        SELECT u.company.id, COUNT(u)
		        FROM User u
		        WHERE u.company IS NOT NULL
		        GROUP BY u.company.id
		    """)
		    List<Object[]> countUsersByCompany();
	
	
	

	
	
}
