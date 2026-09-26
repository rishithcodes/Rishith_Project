import { useState } from 'react'
import { deleteExpense } from '../api/expenseApi'
import { formatINR, formatDate, exportToCSV } from '../utils/formatters'
import ConfirmDialog from './ConfirmDialog'
import LoadingSpinner from './LoadingSpinner'
import ErrorMessage from './ErrorMessage'

function ExpenseList({ data, loading, error, onEdit, onDeleted, onRetry, filters }) {
  const [confirmId, setConfirmId] = useState(null)
  const [confirmTitle, setConfirmTitle] = useState('')
  const [deleting, setDeleting] = useState(false)

  const { content: expenses = [], totalPages, pageNumber, totalElements, filteredTotal } = data

  const handleDeleteClick = (exp) => { setConfirmId(exp.id); setConfirmTitle(exp.title) }
  const handleDeleteConfirm = async () => {
    setDeleting(true)
    try {
      await deleteExpense(confirmId)
      setConfirmId(null)
      onDeleted()
    } catch { alert('Failed to delete expense') }
    finally { setDeleting(false) }
  }

  const handleExport = () => {
    if (expenses.length === 0) return
    exportToCSV(expenses)
  }

  if (loading) return <LoadingSpinner message="Loading expenses..." />
  if (error) return <ErrorMessage message={error} onRetry={onRetry} />

  return (
    <div className="list-card">
      <div className="list-header">
        <div>
          <h2>📋 Expenses ({totalElements})</h2>
          <div className="filtered-total">Filtered Total: <strong>{formatINR(filteredTotal)}</strong></div>
        </div>
        <button className="btn-export" onClick={handleExport} disabled={expenses.length === 0}
          aria-label="Export to CSV">
          ⬇️ Export CSV
        </button>
      </div>

      {expenses.length === 0 ? (
        <div className="empty-state" role="status">
          <p>🔍 No expenses found.</p>
          <p>Try adjusting your filters or add a new expense.</p>
        </div>
      ) : (
        <>
          {/* Desktop Table */}
          <div className="table-wrapper" role="region" aria-label="Expenses table">
            <table>
              <thead>
                <tr>
                  <th>#</th><th>Title</th><th>Amount</th><th>Date</th>
                  <th>Category</th><th>Description</th><th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {expenses.map((exp, i) => (
                  <tr key={exp.id}>
                    <td>{(pageNumber * 10) + i + 1}</td>
                    <td>{exp.title}</td>
                    <td className="amount-cell">{formatINR(exp.amount)}</td>
                    <td>{formatDate(exp.expenseDate)}</td>
                    <td><span className="category-badge">{exp.categoryName || 'N/A'}</span></td>
                    <td className="desc-cell">{exp.description || '-'}</td>
                    <td className="action-buttons">
                      <button className="btn-edit" onClick={() => onEdit(exp)} aria-label={`Edit ${exp.title}`}>
                        ✏️ Edit
                      </button>
                      <button className="btn-delete" onClick={() => handleDeleteClick(exp)} aria-label={`Delete ${exp.title}`}>
                        🗑️ Delete
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>

          {/* Mobile Cards */}
          <div className="expense-cards" aria-label="Expenses list">
            {expenses.map(exp => (
              <div key={exp.id} className="expense-card">
                <div className="card-header">
                  <strong>{exp.title}</strong>
                  <span className="amount-cell">{formatINR(exp.amount)}</span>
                </div>
                <div className="card-meta">
                  <span className="category-badge">{exp.categoryName || 'N/A'}</span>
                  <span>{formatDate(exp.expenseDate)}</span>
                </div>
                {exp.description && <p className="card-desc">{exp.description}</p>}
                <div className="card-actions">
                  <button className="btn-edit" onClick={() => onEdit(exp)}>✏️ Edit</button>
                  <button className="btn-delete" onClick={() => handleDeleteClick(exp)}>🗑️ Delete</button>
                </div>
              </div>
            ))}
          </div>
        </>
      )}

      {/* Confirm Delete Dialog */}
      {confirmId && (
        <ConfirmDialog
          message={`Delete "${confirmTitle}"? This cannot be undone.`}
          onConfirm={handleDeleteConfirm}
          onCancel={() => setConfirmId(null)}
        />
      )}
    </div>
  )
}
export default ExpenseList
