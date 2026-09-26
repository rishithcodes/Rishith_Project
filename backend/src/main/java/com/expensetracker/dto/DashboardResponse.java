package com.expensetracker.dto;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DashboardResponse {
    private BigDecimal todayTotal;
    private BigDecimal monthTotal;
    private BigDecimal yearTotal;
    private String topCategory;
    private List<ExpenseResponse> recentExpenses;
    private List<Map<String, Object>> monthlyTrend;
    private List<Map<String, Object>> categoryBreakdown;

    public BigDecimal getTodayTotal() { return todayTotal; }
    public void setTodayTotal(BigDecimal todayTotal) { this.todayTotal = todayTotal; }
    public BigDecimal getMonthTotal() { return monthTotal; }
    public void setMonthTotal(BigDecimal monthTotal) { this.monthTotal = monthTotal; }
    public BigDecimal getYearTotal() { return yearTotal; }
    public void setYearTotal(BigDecimal yearTotal) { this.yearTotal = yearTotal; }
    public String getTopCategory() { return topCategory; }
    public void setTopCategory(String topCategory) { this.topCategory = topCategory; }
    public List<ExpenseResponse> getRecentExpenses() { return recentExpenses; }
    public void setRecentExpenses(List<ExpenseResponse> recentExpenses) { this.recentExpenses = recentExpenses; }
    public List<Map<String, Object>> getMonthlyTrend() { return monthlyTrend; }
    public void setMonthlyTrend(List<Map<String, Object>> monthlyTrend) { this.monthlyTrend = monthlyTrend; }
    public List<Map<String, Object>> getCategoryBreakdown() { return categoryBreakdown; }
    public void setCategoryBreakdown(List<Map<String, Object>> categoryBreakdown) { this.categoryBreakdown = categoryBreakdown; }
}
