package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Expenses;



 
	public interface ExpensesDao extends JpaRepository< Expenses, Long> {
}
