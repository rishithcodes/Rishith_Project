import { useState } from 'react'
import { formatINR } from '../utils/formatters'

function BudgetBar({ label, spent, budget }) {
  const pct = budget > 0 ? Math.min((spent / budget) * 100, 100) : 0
  const over = spent > budget && budget > 0
  const warn = pct >= 80 && !over
  return (
    <div className="budget-bar-row">
      <div className="budget-bar-labels">
        <span>{label}</span>
        <span>{formatINR(spent)} / {formatINR(budget)}</span>
      </div>
      <div className="budget-bar-track" aria-label={`${label}: ${pct.toFixed(0)}% used`}>
        <div
          className={`budget-bar-fill ${over ? 'over' : warn ? 'warn' : ''}`}
          style={{ width: `${pct}%` }}
        />
      </div>
      {over && <p className="budget-warning">⚠️ Over budget by {formatINR(spent - budget)}</p>}
      {warn && <p className="budget-caution">⚡ {pct.toFixed(0)}% of budget used</p>}
    </div>
  )
}

function BudgetPanel({ budget }) {
  if (!budget) return null
  const { income, overallBudget, totalSpent, remaining, categoryBudgets = [] } = budget

  return (
    <div className="budget-panel">
      <div className="budget-summary">
        <div className="budget-stat">
          <span>💵 Income</span>
          <strong>{formatINR(income)}</strong>
        </div>
        <div className="budget-stat">
          <span>💸 Spent</span>
          <strong className={totalSpent > income ? 'text-danger' : ''}>{formatINR(totalSpent)}</strong>
        </div>
        <div className="budget-stat">
          <span>💰 Remaining</span>
          <strong className={remaining < 0 ? 'text-danger' : 'text-success'}>{formatINR(remaining)}</strong>
        </div>
      </div>

      {overallBudget > 0 && (
        <BudgetBar label="Overall Budget" spent={totalSpent} budget={overallBudget} />
      )}

      {categoryBudgets.length > 0 && (
        <div className="category-budgets">
          <h4>Category Budgets</h4>
          {categoryBudgets.map(cb => (
            <BudgetBar key={cb.categoryId} label={cb.categoryName}
              spent={Number(cb.spent)} budget={Number(cb.budgetAmount)} />
          ))}
        </div>
      )}
    </div>
  )
}
export default BudgetPanel
