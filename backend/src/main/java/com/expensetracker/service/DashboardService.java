package com.expensetracker.service;

import com.expensetracker.dto.DashboardResponse;
import com.expensetracker.dto.ExpenseResponse;
import com.expensetracker.entity.Expense;
import com.expensetracker.entity.User;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides dashboard summary data: totals, trends, category breakdown, recent transactions.
 */
@Service
public class DashboardService {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ExpenseResponse toResponse(Expense e) {
        ExpenseResponse r = new ExpenseResponse();
        r.setId(e.getId()); r.setTitle(e.getTitle()); r.setAmount(e.getAmount());
        r.setExpenseDate(e.getExpenseDate()); r.setDescription(e.getDescription());
        r.setCreatedAt(e.getCreatedAt());
        if (e.getCategory() != null) { r.setCategoryId(e.getCategory().getId()); r.setCategoryName(e.getCategory().getName()); }
        return r;
    }

    public DashboardResponse getDashboard(String email, int year) {
        User user = getUser(email);
        LocalDate today = LocalDate.now();
        int currentMonth = today.getMonthValue();

        BigDecimal todayTotal   = expenseRepository.sumByUserAndDate(user, today);
        BigDecimal monthTotal   = expenseRepository.sumByUserAndMonthYear(user, currentMonth, year);
        BigDecimal yearTotal    = expenseRepository.sumByUserAndYear(user, year);

        // Category breakdown for current month
        List<Object[]> catData = expenseRepository.sumByCategory(user, currentMonth, year);
        String topCategory = catData.isEmpty() ? "N/A" : (String) catData.get(0)[0];

        List<Map<String, Object>> categoryBreakdown = catData.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("category", row[0]);
            map.put("amount", row[1]);
            return map;
        }).collect(Collectors.toList());

        // Monthly trend for the selected year
        List<Object[]> trendData = expenseRepository.monthlyTrend(user, year);
        List<Map<String, Object>> monthlyTrend = trendData.stream().map(row -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("month", row[0]);
            map.put("amount", row[1]);
            return map;
        }).collect(Collectors.toList());

        // Recent 5 transactions
        List<ExpenseResponse> recent = expenseRepository
                .findTop5ByUserOrderByCreatedAtDesc(user)
                .stream().map(this::toResponse).collect(Collectors.toList());

        DashboardResponse response = new DashboardResponse();
        response.setTodayTotal(todayTotal != null ? todayTotal : BigDecimal.ZERO);
        response.setMonthTotal(monthTotal != null ? monthTotal : BigDecimal.ZERO);
        response.setYearTotal(yearTotal != null ? yearTotal : BigDecimal.ZERO);
        response.setTopCategory(topCategory);
        response.setCategoryBreakdown(categoryBreakdown);
        response.setMonthlyTrend(monthlyTrend);
        response.setRecentExpenses(recent);
        return response;
    }
}
