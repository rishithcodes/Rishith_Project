package com.expensetracker.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class CategoryBudgetRequest {
    @NotNull private Long categoryId;
    @NotNull private Integer month;
    @NotNull private Integer year;
    @NotNull @DecimalMin("0") private BigDecimal budgetAmount;
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
}
