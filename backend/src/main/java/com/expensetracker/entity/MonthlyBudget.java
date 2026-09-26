package com.expensetracker.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/** Monthly budget and income settings per user per month/year */
@Entity
@Table(name = "monthly_budgets")
public class MonthlyBudget {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;

    @Column(precision = 12, scale = 2)
    private BigDecimal income = BigDecimal.ZERO;

    @Column(name = "overall_budget", precision = 12, scale = 2)
    private BigDecimal overallBudget = BigDecimal.ZERO;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    public BigDecimal getIncome() { return income; }
    public void setIncome(BigDecimal income) { this.income = income; }
    public BigDecimal getOverallBudget() { return overallBudget; }
    public void setOverallBudget(BigDecimal overallBudget) { this.overallBudget = overallBudget; }
}
