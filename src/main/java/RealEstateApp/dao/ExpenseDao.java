package RealEstateApp.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import RealEstateApp.Pojo.Expense;



public interface ExpenseDao extends JpaRepository<Expense, Long> {
}