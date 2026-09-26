package com.expensetracker.service;

import com.expensetracker.dto.CategoryRequest;
import com.expensetracker.dto.CategoryResponse;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.exception.BadRequestException;
import com.expensetracker.exception.ResourceNotFoundException;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.ExpenseRepository;
import com.expensetracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business logic for user-owned categories.
 */
@Service
public class CategoryService {

    @Autowired private CategoryRepository categoryRepository;
    @Autowired private ExpenseRepository expenseRepository;
    @Autowired private UserRepository userRepository;

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public List<CategoryResponse> getAll(String email) {
        User user = getUser(email);
        return categoryRepository.findByUserOrderByNameAsc(user).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public CategoryResponse create(String email, CategoryRequest request) {
        User user = getUser(email);
        if (categoryRepository.existsByNameAndUser(request.getName(), user)) {
            throw new BadRequestException("Category '" + request.getName() + "' already exists");
        }
        Category category = new Category(request.getName(), user);
        category = categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }

    @Transactional
    public CategoryResponse rename(String email, Long id, CategoryRequest request) {
        User user = getUser(email);
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (categoryRepository.existsByNameAndUser(request.getName(), user) &&
            !category.getName().equalsIgnoreCase(request.getName())) {
            throw new BadRequestException("Category '" + request.getName() + "' already exists");
        }
        category.setName(request.getName());
        category = categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName());
    }

    @Transactional
    public void delete(String email, Long id) {
        User user = getUser(email);
        Category category = categoryRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (expenseRepository.existsByUserAndCategoryId(user, id)) {
            throw new BadRequestException(
                "Cannot delete category '" + category.getName() + "' because it has associated expenses. " +
                "Please reassign or delete those expenses first.");
        }
        categoryRepository.delete(category);
    }
}
