package com.expensetracker.repository;

import com.expensetracker.entity.RecurringExpense;
import com.expensetracker.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecurringExpenseRepository extends JpaRepository<RecurringExpense, Long> {
    List<RecurringExpense> findByUser(User user);
    Optional<RecurringExpense> findByIdAndUser(Long id, User user);
    List<RecurringExpense> findByIsActiveTrueAndNextDueDateLessThanEqual(LocalDate date);
}
