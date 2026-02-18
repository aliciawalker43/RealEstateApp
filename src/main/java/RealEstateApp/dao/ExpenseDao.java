package RealEstateApp.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Company;
import RealEstateApp.Pojo.Expense;



public interface ExpenseDao extends JpaRepository<Expense, Long> {

	List <Expense> findAllByCompanyId(Long long1);
}