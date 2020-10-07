package RealEstateApp.dao;

import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import RealEstateApp.Pojo.Task;

public interface TaskDao extends JpaRepository< Task, Long> {

	//@Query(UPDATE task Set complete ="Yes" Where id= "?")
	//public Task updateComplete();
	
}
