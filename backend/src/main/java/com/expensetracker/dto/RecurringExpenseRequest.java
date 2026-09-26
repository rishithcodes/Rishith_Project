package com.expensetracker.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class RecurringExpenseRequest {
    @NotBlank @Size(max = 255) private String title;
    @NotNull @DecimalMin("0.01") private BigDecimal amount;
    @Size(max = 500) private String description;
    private Long categoryId;
    @NotBlank private String frequency; // DAILY, WEEKLY, MONTHLY, YEARLY
    @NotNull private LocalDate startDate;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
}
