import { formatINR, formatDate } from '../utils/formatters'

function RecentTransactions({ expenses = [] }) {
  return (
    <div className="chart-card">
      <h3>🕐 Recent Transactions</h3>
      {expenses.length === 0 ? (
        <p className="empty-state">No recent transactions.</p>
      ) : (
        <ul className="recent-list">
          {expenses.map(exp => (
            <li key={exp.id} className="recent-item">
              <div className="recent-info">
                <span className="recent-title">{exp.title}</span>
                <span className="category-badge">{exp.categoryName || 'N/A'}</span>
              </div>
              <div className="recent-right">
                <span className="amount-cell">{formatINR(exp.amount)}</span>
                <span className="recent-date">{formatDate(exp.expenseDate)}</span>
              </div>
            </li>
          ))}
        </ul>
      )}
    </div>
  )
}
export default RecentTransactions
