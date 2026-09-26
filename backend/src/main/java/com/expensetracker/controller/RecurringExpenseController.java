package com.expensetracker.controller;

import com.expensetracker.dto.RecurringExpenseRequest;
import com.expensetracker.dto.RecurringExpenseResponse;
import com.expensetracker.service.RecurringExpenseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recurring")
public class RecurringExpenseController {

    @Autowired private RecurringExpenseService recurringService;

    @GetMapping
    public ResponseEntity<List<RecurringExpenseResponse>> getAll(Authentication auth) {
        return ResponseEntity.ok(recurringService.getAll(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<RecurringExpenseResponse> create(Authentication auth,
                                                           @Valid @RequestBody RecurringExpenseRequest request) {
        return ResponseEntity.status(201).body(recurringService.create(auth.getName(), request));
    }

    @PatchMapping("/{id}/toggle")
    public ResponseEntity<Void> toggle(Authentication auth, @PathVariable Long id) {
        recurringService.toggle(auth.getName(), id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        recurringService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
