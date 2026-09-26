package com.expensetracker.controller;

import com.expensetracker.dto.*;
import com.expensetracker.service.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.Map;

/**
 * REST endpoints for expense CRUD with filtering, sorting, and pagination.
 * All endpoints require JWT authentication.
 */
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    @Autowired private ExpenseService expenseService;

    /**
     * GET /api/expenses?search=&categoryId=&startDate=&endDate=&sortBy=newest&page=0&size=10
     * Returns paginated, filtered expenses + filtered total.
     */
    @GetMapping
    public ResponseEntity<PagedExpenseResponse> getAll(
            Authentication auth,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(defaultValue = "newest") String sortBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(expenseService.getFiltered(
                auth.getName(), search, categoryId, startDate, endDate, sortBy, page, size));
    }

    /** GET /api/expenses/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<ExpenseResponse> getById(Authentication auth, @PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getById(auth.getName(), id));
    }

    /** POST /api/expenses */
    @PostMapping
    public ResponseEntity<ExpenseResponse> create(Authentication auth, @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.status(201).body(expenseService.create(auth.getName(), request));
    }

    /** PUT /api/expenses/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<ExpenseResponse> update(Authentication auth, @PathVariable Long id,
                                                  @Valid @RequestBody ExpenseRequest request) {
        return ResponseEntity.ok(expenseService.update(auth.getName(), id, request));
    }

    /** DELETE /api/expenses/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(Authentication auth, @PathVariable Long id) {
        expenseService.delete(auth.getName(), id);
        return ResponseEntity.ok(Map.of("message", "Expense deleted successfully"));
    }
}
