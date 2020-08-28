package GC.Capstone6.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import GC.Capstone6.Pojo.User;

public interface UserDao extends JpaRepository<User, Long>{

	User findByUsername(String name);

	
	
}
