package com.expensetracker.controller;

import com.expensetracker.dto.CategoryRequest;
import com.expensetracker.dto.CategoryResponse;
import com.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST endpoints for user-owned categories.
 * All endpoints require JWT authentication.
 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    @Autowired private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(Authentication auth) {
        return ResponseEntity.ok(categoryService.getAll(auth.getName()));
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(Authentication auth, @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.status(201).body(categoryService.create(auth.getName(), request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> rename(Authentication auth, @PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest request) {
        return ResponseEntity.ok(categoryService.rename(auth.getName(), id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication auth, @PathVariable Long id) {
        categoryService.delete(auth.getName(), id);
        return ResponseEntity.noContent().build();
    }
}
