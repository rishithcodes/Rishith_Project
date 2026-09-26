package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.service.BudgetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * Budget management endpoints.
 */
@RestController
@RequestMapping("/api/budget")
public class BudgetController {

    @Autowired private BudgetService budgetService;

    /** GET /api/budget?month=6&year=2024 */
    @GetMapping
    public ResponseEntity<BudgetResponse> getBudget(
            Authentication auth,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(budgetService.getBudget(auth.getName(), month, year));
    }

    /** POST /api/budget - Set income and overall budget */
    @PostMapping
    public ResponseEntity<BudgetResponse> saveMonthlyBudget(
            Authentication auth,
            @Valid @RequestBody BudgetRequest request) {
        return ResponseEntity.ok(budgetService.saveMonthlyBudget(auth.getName(), request));
    }

    /** POST /api/budget/category - Set per-category budget */
    @PostMapping("/category")
    public ResponseEntity<Void> saveCategoryBudget(
            Authentication auth,
            @Valid @RequestBody CategoryBudgetRequest request) {
        budgetService.saveCategoryBudget(auth.getName(), request);
        return ResponseEntity.ok().build();
    }
}
