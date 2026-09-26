package com.expensetracker.repository;

import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    Optional<Expense> findByIdAndUser(Long id, User user);

    @Query("SELECT e FROM Expense e WHERE e.user = :user " +
           "AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate)")
    Page<Expense> findFiltered(
        @Param("user") User user,
        @Param("search") String search,
        @Param("categoryId") Long categoryId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate,
        Pageable pageable
    );

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user " +
           "AND (:search IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "  OR LOWER(e.description) LIKE LOWER(CONCAT('%', :search, '%'))) " +
           "AND (:categoryId IS NULL OR e.category.id = :categoryId) " +
           "AND (:startDate IS NULL OR e.expenseDate >= :startDate) " +
           "AND (:endDate IS NULL OR e.expenseDate <= :endDate)")
    BigDecimal sumFiltered(
        @Param("user") User user,
        @Param("search") String search,
        @Param("categoryId") Long categoryId,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND e.expenseDate = :date")
    BigDecimal sumByUserAndDate(@Param("user") User user, @Param("date") LocalDate date);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    BigDecimal sumByUserAndMonthYear(@Param("user") User user, @Param("month") int month, @Param("year") int year);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND YEAR(e.expenseDate) = :year")
    BigDecimal sumByUserAndYear(@Param("user") User user, @Param("year") int year);

    @Query("SELECT e.category.name, COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year AND e.category IS NOT NULL GROUP BY e.category.name ORDER BY SUM(e.amount) DESC")
    List<Object[]> sumByCategory(@Param("user") User user, @Param("month") int month, @Param("year") int year);

    @Query("SELECT MONTH(e.expenseDate), COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND YEAR(e.expenseDate) = :year GROUP BY MONTH(e.expenseDate) ORDER BY MONTH(e.expenseDate)")
    List<Object[]> monthlyTrend(@Param("user") User user, @Param("year") int year);

    List<Expense> findTop5ByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND e.category.id = :categoryId AND MONTH(e.expenseDate) = :month AND YEAR(e.expenseDate) = :year")
    BigDecimal sumByCategoryAndMonthYear(@Param("user") User user, @Param("categoryId") Long categoryId, @Param("month") int month, @Param("year") int year);

    boolean existsByUserAndCategoryId(User user, Long categoryId);
}
