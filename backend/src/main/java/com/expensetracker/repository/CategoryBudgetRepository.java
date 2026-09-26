package com.expensetracker.repository;

import com.expensetracker.entity.CategoryBudget;
import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CategoryBudgetRepository extends JpaRepository<CategoryBudget, Long> {
    List<CategoryBudget> findByUserAndMonthAndYear(User user, Integer month, Integer year);
    Optional<CategoryBudget> findByUserAndCategoryIdAndMonthAndYear(User user, Long categoryId, Integer month, Integer year);
    void deleteByUserAndCategoryIdAndMonthAndYear(User user, Long categoryId, Integer month, Integer year);
}
