package com.expensetracker.dto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class BudgetResponse {
    private Integer month;
    private Integer year;
    private BigDecimal income;
    private BigDecimal overallBudget;
    private BigDecimal totalSpent;
    private BigDecimal remaining;
    private List<Map<String, Object>> categoryBudgets;

    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public BigDecimal getOverallBudget() { return overallBudget; }
    public void setOverallBudget(BigDecimal overallBudget) { this.overallBudget = overallBudget; }
    public BigDecimal getTotalSpent() { return totalSpent; }
    public void setTotalSpent(BigDecimal totalSpent) { this.totalSpent = totalSpent; }
    public BigDecimal getRemaining() { return remaining; }
    public void setRemaining(BigDecimal remaining) { this.remaining = remaining; }
    public List<Map<String, Object>> getCategoryBudgets() { return categoryBudgets; }
    public void setCategoryBudgets(List<Map<String, Object>> categoryBudgets) { this.categoryBudgets = categoryBudgets; }
}
