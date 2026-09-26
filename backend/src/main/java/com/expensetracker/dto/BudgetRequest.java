package com.expensetracker.dto;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class BudgetRequest {
    @NotNull private Integer month;
    @NotNull private Integer year;
    @DecimalMin("0") private BigDecimal income = BigDecimal.ZERO;
    @DecimalMin("0") private BigDecimal overallBudget = BigDecimal.ZERO;
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public BigDecimal getOverallBudget() { return overallBudget; }
    public void setOverallBudget(BigDecimal overallBudget) { this.overallBudget = overallBudget; }
}
