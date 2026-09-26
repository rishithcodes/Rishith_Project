package com.expensetracker;

import com.expensetracker.dto.*;
import com.expensetracker.entity.Category;
import com.expensetracker.entity.User;
import com.expensetracker.repository.CategoryRepository;
import com.expensetracker.repository.UserRepository;
import com.expensetracker.service.AuthService;
import com.expensetracker.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExpenseTrackerApplicationTests {

    @Autowired private AuthService authService;
    @Autowired private ExpenseService expenseService;
    @Autowired private UserRepository userRepository;
    @Autowired private CategoryRepository categoryRepository;

    private String userEmail = "test@example.com";
    private Long categoryId;

    @BeforeEach
    void setUp() {
        // Register test user (creates default categories)
        if (!userRepository.existsByEmail(userEmail)) {
            RegisterRequest req = new RegisterRequest();
            req.setName("Test User"); req.setEmail(userEmail); req.setPassword("password123");
            authService.register(req);
        }
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        categoryId = categoryRepository.findByUserOrderByNameAsc(user).get(0).getId();
    }

    @Test
    void contextLoads() {
        assertNotNull(authService);
        assertNotNull(expenseService);
    }

    @Test
    void testUserRegistration() {
        assertTrue(userRepository.existsByEmail(userEmail));
        User user = userRepository.findByEmail(userEmail).orElseThrow();
        assertNotNull(user.getId());
        // Default categories should be created
        assertTrue(categoryRepository.findByUserOrderByNameAsc(user).size() >= 7);
    }

    @Test
    void testCreateExpense() {
        ExpenseRequest req = new ExpenseRequest();
        req.setTitle("Test Expense"); req.setAmount(new BigDecimal("100.00"));
        req.setExpenseDate(LocalDate.now()); req.setCategoryId(categoryId);
        ExpenseResponse response = expenseService.create(userEmail, req);
        assertNotNull(response.getId());
        assertEquals("Test Expense", response.getTitle());
        assertEquals(0, new BigDecimal("100.00").compareTo(response.getAmount()));
    }

    @Test
    void testUpdateExpense() {
        ExpenseRequest req = new ExpenseRequest();
        req.setTitle("Original"); req.setAmount(new BigDecimal("50.00"));
        req.setExpenseDate(LocalDate.now()); req.setCategoryId(categoryId);
        ExpenseResponse created = expenseService.create(userEmail, req);

        req.setTitle("Updated"); req.setAmount(new BigDecimal("75.00"));
        ExpenseResponse updated = expenseService.update(userEmail, created.getId(), req);
        assertEquals("Updated", updated.getTitle());
        assertEquals(0, new BigDecimal("75.00").compareTo(updated.getAmount()));
    }

    @Test
    void testDeleteExpense() {
        ExpenseRequest req = new ExpenseRequest();
        req.setTitle("To Delete"); req.setAmount(new BigDecimal("25.00"));
        req.setExpenseDate(LocalDate.now()); req.setCategoryId(categoryId);
        ExpenseResponse created = expenseService.create(userEmail, req);
        expenseService.delete(userEmail, created.getId());
        assertThrows(Exception.class, () -> expenseService.getById(userEmail, created.getId()));
    }

    @Test
    void testFilteredExpenses() {
        ExpenseRequest req = new ExpenseRequest();
        req.setTitle("Pizza Night"); req.setAmount(new BigDecimal("300.00"));
        req.setExpenseDate(LocalDate.now()); req.setCategoryId(categoryId);
        expenseService.create(userEmail, req);

        PagedExpenseResponse result = expenseService.getFiltered(
            userEmail, "Pizza", null, null, null, "newest", 0, 10);
        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getContent().stream().anyMatch(e -> e.getTitle().contains("Pizza")));
    }
}
