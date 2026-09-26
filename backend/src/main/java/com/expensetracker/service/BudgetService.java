package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.*;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages monthly budgets (income, overall budget) and per-category budgets.
 */
@Service
public class BudgetService {

    @Autowired private MonthlyBudgetRepository monthlyBudgetRepository;
    @Autowired private CategoryBudgetRepository categoryBudgetRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Transactional
    public BudgetResponse saveMonthlyBudget(String email, BudgetRequest request) {
        User user = getUser(email);
        MonthlyBudget budget = monthlyBudgetRepository
                .findByUserAndMonthAndYear(user, request.getMonth(), request.getYear())
                .orElse(new MonthlyBudget());
        budget.setUser(user);
        budget.setMonth(request.getMonth());
        budget.setYear(request.getYear());
        budget.setIncome(request.getIncome());
        budget.setOverallBudget(request.getOverallBudget());
        monthlyBudgetRepository.save(budget);
        return getBudget(email, request.getMonth(), request.getYear());
    }

    public BudgetResponse getBudget(String email, int month, int year) {
        User user = getUser(email);
        MonthlyBudget budget = monthlyBudgetRepository
                .findByUserAndMonthAndYear(user, month, year)
                .orElse(null);

        BigDecimal income = budget != null ? budget.getIncome() : BigDecimal.ZERO;
        BigDecimal overallBudget = budget != null ? budget.getOverallBudget() : BigDecimal.ZERO;
        BigDecimal totalSpent = expenseRepository.sumByUserAndMonthYear(user, month, year);
        if (totalSpent == null) totalSpent = BigDecimal.ZERO;
        BigDecimal remaining = income.subtract(totalSpent);

        // Per-category budgets
        List<CategoryBudget> catBudgets = categoryBudgetRepository.findByUserAndMonthAndYear(user, month, year);
        List<Map<String, Object>> catBudgetList = catBudgets.stream().map(cb -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("categoryId", cb.getCategory().getId());
            map.put("categoryName", cb.getCategory().getName());
            map.put("budgetAmount", cb.getBudgetAmount());
            BigDecimal spent = expenseRepository.sumByCategoryAndMonthYear(user, cb.getCategory().getId(), month, year);
            if (spent == null) spent = BigDecimal.ZERO;
            map.put("spent", spent);
            return map;
        }).collect(Collectors.toList());

        BudgetResponse response = new BudgetResponse();
        response.setMonth(month);
        response.setYear(year);
        response.setIncome(income);
        response.setOverallBudget(overallBudget);
        response.setTotalSpent(totalSpent);
        response.setRemaining(remaining);
        response.setCategoryBudgets(catBudgetList);
        return response;
    }

    @Transactional
    public void saveCategoryBudget(String email, CategoryBudgetRequest request) {
        User user = getUser(email);
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        CategoryBudget cb = categoryBudgetRepository
                .findByUserAndCategoryIdAndMonthAndYear(user, request.getCategoryId(), request.getMonth(), request.getYear())
                .orElse(new CategoryBudget());
        cb.setUser(user);
        cb.setCategory(category);
        cb.setMonth(request.getMonth());
        cb.setYear(request.getYear());
        cb.setBudgetAmount(request.getBudgetAmount());
        categoryBudgetRepository.save(cb);
    }
}
