package com.expensetracker.service;

import com.expensetracker.dto.*;
import com.expensetracker.entity.*;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for expense CRUD, filtering, pagination, and totals.
 */
@Service
public class ExpenseService {

    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private CategoryRepository categoryRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private ExpenseResponse toResponse(Expense e) {
        ExpenseResponse r = new ExpenseResponse();
        r.setId(e.getId());
        r.setTitle(e.getTitle());
        r.setAmount(e.getAmount());
        r.setExpenseDate(e.getExpenseDate());
        r.setDescription(e.getDescription());
        r.setCreatedAt(e.getCreatedAt());
        if (e.getCategory() != null) {
            r.setCategoryId(e.getCategory().getId());
            r.setCategoryName(e.getCategory().getName());
        }
        return r;
    }

    /**
     * Get filtered, sorted, paginated expenses for the current user.
     * Also returns the filtered total amount.
     */
    public PagedExpenseResponse getFiltered(String email, String search, Long categoryId,
                                            LocalDate startDate, LocalDate endDate,
                                            String sortBy, int page, int size) {
        User user = getUser(email);

        // Build sort
        Sort sort = switch (sortBy != null ? sortBy : "newest") {
            case "oldest"  -> Sort.by("expenseDate").ascending();
            case "highest" -> Sort.by("amount").descending();
            case "lowest"  -> Sort.by("amount").ascending();
            case "title"   -> Sort.by("title").ascending();
            default        -> Sort.by("expenseDate").descending().and(Sort.by("createdAt").descending());
        };

        Pageable pageable = PageRequest.of(page, size, sort);

        // Null search means no search filter
        String searchParam = (search != null && !search.isBlank()) ? search.trim() : null;

        Page<Expense> expensePage = expenseRepository.findFiltered(
                user, searchParam, categoryId, startDate, endDate, pageable);

        BigDecimal filteredTotal = expenseRepository.sumFiltered(
                user, searchParam, categoryId, startDate, endDate);
        if (filteredTotal == null) filteredTotal = BigDecimal.ZERO;

        PagedExpenseResponse response = new PagedExpenseResponse();
        response.setContent(expensePage.getContent().stream().map(this::toResponse).collect(Collectors.toList()));
        response.setPageNumber(expensePage.getNumber());
        response.setPageSize(expensePage.getSize());
        response.setTotalElements(expensePage.getTotalElements());
        response.setTotalPages(expensePage.getTotalPages());
        response.setFilteredTotal(filteredTotal);
        return response;
    }

    public ExpenseResponse getById(String email, Long id) {
        User user = getUser(email);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        return toResponse(expense);
    }

    @Transactional
    public ExpenseResponse create(String email, ExpenseRequest request) {
        User user = getUser(email);
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        Expense expense = new Expense();
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());
        expense.setCategory(category);
        expense.setUser(user);
        return toResponse(expenseRepository.save(expense));
    }

    @Transactional
    public ExpenseResponse update(String email, Long id, ExpenseRequest request) {
        User user = getUser(email);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        Category category = categoryRepository.findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(request.getExpenseDate());
        expense.setDescription(request.getDescription());
        expense.setCategory(category);
        return toResponse(expenseRepository.save(expense));
    }

    @Transactional
    public void delete(String email, Long id) {
        User user = getUser(email);
        Expense expense = expenseRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found with id: " + id));
        expenseRepository.delete(expense);
    }
}
