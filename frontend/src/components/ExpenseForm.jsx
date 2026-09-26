import { useState, useEffect } from 'react'
import { fetchCategories } from '../api/categoryApi'
import { createExpense, updateExpense } from '../api/expenseApi'

function ExpenseForm({ expenseToEdit, onSuccess, onCancelEdit }) {
  const [form, setForm] = useState({ title: '', amount: '', expenseDate: '', description: '', categoryId: '' })
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')

  useEffect(() => {
    fetchCategories().then(r => setCategories(r.data)).catch(() => setError('Could not load categories'))
  }, [])

  useEffect(() => {
    if (expenseToEdit) {
      setForm({
        title: expenseToEdit.title,
        amount: expenseToEdit.amount,
        expenseDate: expenseToEdit.expenseDate,
        description: expenseToEdit.description || '',
        categoryId: expenseToEdit.categoryId || '',
      })
    } else {
      setForm({ title: '', amount: '', expenseDate: '', description: '', categoryId: '' })
    }
  }, [expenseToEdit])

  const set = (field) => (e) => setForm(f => ({ ...f, [field]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    if (!form.title.trim()) return setError('Title is required')
    if (!form.amount || Number(form.amount) <= 0) return setError('Amount must be greater than 0')
    if (!form.expenseDate) return setError('Date is required')
    if (!form.categoryId) return setError('Category is required')

    setLoading(true)
    try {
      const payload = {
        title: form.title,
        amount: Number(form.amount),
        expenseDate: form.expenseDate,
        description: form.description,
        categoryId: Number(form.categoryId),
      }
      if (expenseToEdit) {
        await updateExpense(expenseToEdit.id, payload)
      } else {
        await createExpense(payload)
        setForm({ title: '', amount: '', expenseDate: '', description: '', categoryId: '' })
      }
      onSuccess()
    } catch (err) {
      const msgs = err.response?.data?.errors
      setError(msgs ? msgs.join(', ') : err.response?.data?.message || 'Failed to save expense')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="form-card">
      <h2 id="expense-form-title">{expenseToEdit ? '✏️ Edit Expense' : '➕ Add New Expense'}</h2>
      {error && <p className="error-msg" role="alert">{error}</p>}
      <form onSubmit={handleSubmit} aria-labelledby="expense-form-title" noValidate>
        <div className="form-grid">
          <div className="form-group">
            <label htmlFor="exp-title">Title *</label>
            <input id="exp-title" type="text" placeholder="e.g. Grocery Shopping"
              value={form.title} onChange={set('title')} required maxLength={255} />
          </div>
          <div className="form-group">
            <label htmlFor="exp-amount">Amount (₹) *</label>
            <input id="exp-amount" type="number" placeholder="e.g. 500"
              value={form.amount} onChange={set('amount')} min="0.01" step="0.01" required />
          </div>
          <div className="form-group">
            <label htmlFor="exp-date">Date *</label>
            <input id="exp-date" type="date" value={form.expenseDate} onChange={set('expenseDate')} required />
          </div>
          <div className="form-group">
            <label htmlFor="exp-category">Category *</label>
            <select id="exp-category" value={form.categoryId} onChange={set('categoryId')} required>
              <option value="">-- Select Category --</option>
              {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
            </select>
          </div>
          <div className="form-group form-group-full">
            <label htmlFor="exp-desc">Description</label>
            <input id="exp-desc" type="text" placeholder="Optional note..."
              value={form.description} onChange={set('description')} maxLength={500} />
          </div>
        </div>
        <div className="form-buttons">
          <button type="submit" className="btn-primary" disabled={loading}>
            {loading ? 'Saving...' : expenseToEdit ? 'Update Expense' : 'Add Expense'}
          </button>
          {expenseToEdit && (
            <button type="button" className="btn-secondary" onClick={onCancelEdit} disabled={loading}>
              Cancel
            </button>
          )}
        </div>
      </form>
    </div>
  )
}
export default ExpenseForm
