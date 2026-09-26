package com.expensetracker.service;

import com.expensetracker.dto.RecurringExpenseRequest;
import com.expensetracker.dto.RecurringExpenseResponse;
import com.expensetracker.entity.*;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages recurring expenses.
 * A @Scheduled task runs daily to generate due expenses (no duplicates).
 */
@Service
public class RecurringExpenseService {

    @Autowired private RecurringExpenseRepository recurringRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private RecurringExpenseResponse toResponse(RecurringExpense r) {
        RecurringExpenseResponse resp = new RecurringExpenseResponse();
        resp.setId(r.getId()); resp.setTitle(r.getTitle()); resp.setAmount(r.getAmount());
        resp.setDescription(r.getDescription()); resp.setFrequency(r.getFrequency());
        resp.setStartDate(r.getStartDate()); resp.setNextDueDate(r.getNextDueDate());
        resp.setIsActive(r.getIsActive());
        if (r.getCategory() != null) { resp.setCategoryId(r.getCategory().getId()); resp.setCategoryName(r.getCategory().getName()); }
        return resp;
    }

    public List<RecurringExpenseResponse> getAll(String email) {
        User user = getUser(email);
        return recurringRepository.findByUser(user).stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public RecurringExpenseResponse create(String email, RecurringExpenseRequest request) {
        User user = getUser(email);
        RecurringExpense re = new RecurringExpense();
        re.setTitle(request.getTitle()); re.setAmount(request.getAmount());
        re.setDescription(request.getDescription()); re.setFrequency(request.getFrequency());
        re.setStartDate(request.getStartDate()); re.setNextDueDate(request.getStartDate());
        re.setUser(user);
        if (request.getCategoryId() != null) {
            categoryRepository.findByIdAndUser(request.getCategoryId(), user).ifPresent(re::setCategory);
        }
        return toResponse(recurringRepository.save(re));
    }

    @Transactional
    public void toggle(String email, Long id) {
        User user = getUser(email);
        RecurringExpense re = recurringRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recurring expense not found"));
        re.setIsActive(!re.getIsActive());
        recurringRepository.save(re);
    }

    @Transactional
    public void delete(String email, Long id) {
        User user = getUser(email);
        RecurringExpense re = recurringRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Recurring expense not found"));
        recurringRepository.delete(re);
    }

    /**
     * Runs every day at midnight. Generates expenses for all due recurring templates.
     * Uses nextDueDate to avoid duplicates.
     */
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void generateDueExpenses() {
        LocalDate today = LocalDate.now();
        List<RecurringExpense> due = recurringRepository.findByIsActiveTrueAndNextDueDateLessThanEqual(today);
        for (RecurringExpense re : due) {
            // Create the actual expense
            Expense expense = new Expense();
            expense.setTitle(re.getTitle()); expense.setAmount(re.getAmount());
            expense.setDescription(re.getDescription()); expense.setExpenseDate(today);
            expense.setCategory(re.getCategory()); expense.setUser(re.getUser());
            expenseRepository.save(expense);
            // Advance nextDueDate
            re.setNextDueDate(advanceDate(re.getNextDueDate(), re.getFrequency()));
            recurringRepository.save(re);
        }
    }

    private LocalDate advanceDate(LocalDate date, String frequency) {
        return switch (frequency.toUpperCase()) {
            case "DAILY"   -> date.plusDays(1);
            case "WEEKLY"  -> date.plusWeeks(1);
            case "MONTHLY" -> date.plusMonths(1);
            case "YEARLY"  -> date.plusYears(1);
            default        -> date.plusMonths(1);
        };
    }
}
