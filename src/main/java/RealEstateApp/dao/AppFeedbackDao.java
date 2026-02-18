package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.AppFeedback;




	public interface AppFeedbackDao extends JpaRepository< AppFeedback, Long> {

}
