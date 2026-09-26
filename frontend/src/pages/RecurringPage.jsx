import { useState, useEffect } from 'react'
import { fetchRecurring, createRecurring, toggleRecurring, deleteRecurring } from '../api/recurringApi'
import { fetchCategories } from '../api/categoryApi'
import { formatINR, formatDate } from '../utils/formatters'
import LoadingSpinner from '../components/LoadingSpinner'
import ErrorMessage from '../components/ErrorMessage'

function RecurringPage() {
  const [items, setItems] = useState([])
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [saving, setSaving] = useState(false)
  const [form, setForm] = useState({ title: '', amount: '', description: '', categoryId: '', frequency: 'MONTHLY', startDate: '' })
  const [formError, setFormError] = useState('')

  const load = async () => {
    setLoading(true); setError(null)
    try {
      const [recRes, catRes] = await Promise.all([fetchRecurring(), fetchCategories()])
      setItems(recRes.data); setCategories(catRes.data)
    } catch { setError('Failed to load recurring expenses') }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [])

  const set = (f) => (e) => setForm(x => ({ ...x, [f]: e.target.value }))

  const handleCreate = async (e) => {
    e.preventDefault(); setFormError('')
    if (!form.title.trim() || !form.amount || !form.startDate) return setFormError('Title, amount and start date are required')
    setSaving(true)
    try {
      await createRecurring({ ...form, amount: Number(form.amount), categoryId: form.categoryId ? Number(form.categoryId) : null })
      setForm({ title: '', amount: '', description: '', categoryId: '', frequency: 'MONTHLY', startDate: '' })
      load()
    } catch (err) {
      setFormError(err.response?.data?.message || 'Failed to create recurring expense')
    } finally { setSaving(false) }
  }

  const handleToggle = async (id) => { await toggleRecurring(id); load() }
  const handleDelete = async (id) => {
    if (confirm('Delete this recurring expense?')) { await deleteRecurring(id); load() }
  }

  return (
    <div className="page">
      <div className="page-header"><h1>🔄 Recurring Expenses</h1></div>

      <div className="form-card">
        <h3>Add Recurring Expense</h3>
        {formError && <p className="error-msg" role="alert">{formError}</p>}
        <form onSubmit={handleCreate}>
          <div className="form-grid">
            <div className="form-group">
              <label htmlFor="rec-title">Title *</label>
              <input id="rec-title" type="text" value={form.title} onChange={set('title')} placeholder="e.g. Netflix" required />
            </div>
            <div className="form-group">
              <label htmlFor="rec-amount">Amount (₹) *</label>
              <input id="rec-amount" type="number" min="0.01" step="0.01" value={form.amount} onChange={set('amount')} required />
            </div>
            <div className="form-group">
              <label htmlFor="rec-freq">Frequency</label>
              <select id="rec-freq" value={form.frequency} onChange={set('frequency')}>
                <option value="DAILY">Daily</option>
                <option value="WEEKLY">Weekly</option>
                <option value="MONTHLY">Monthly</option>
                <option value="YEARLY">Yearly</option>
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="rec-start">Start Date *</label>
              <input id="rec-start" type="date" value={form.startDate} onChange={set('startDate')} required />
            </div>
            <div className="form-group">
              <label htmlFor="rec-cat">Category</label>
              <select id="rec-cat" value={form.categoryId} onChange={set('categoryId')}>
                <option value="">-- Optional --</option>
                {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
              </select>
            </div>
            <div className="form-group">
              <label htmlFor="rec-desc">Description</label>
              <input id="rec-desc" type="text" value={form.description} onChange={set('description')} placeholder="Optional" />
            </div>
          </div>
          <button type="submit" className="btn-primary" disabled={saving}>
            {saving ? 'Saving...' : 'Add Recurring Expense'}
          </button>
        </form>
      </div>

      {loading && <LoadingSpinner />}
      {error && <ErrorMessage message={error} onRetry={load} />}

      {!loading && (
        <div className="list-card">
          <h3>Your Recurring Expenses</h3>
          {items.length === 0 ? (
            <p className="empty-state">No recurring expenses set up yet.</p>
          ) : (
            <div className="table-wrapper">
              <table>
                <thead><tr><th>Title</th><th>Amount</th><th>Frequency</th><th>Next Due</th><th>Status</th><th>Actions</th></tr></thead>
                <tbody>
                  {items.map(item => (
                    <tr key={item.id} className={!item.isActive ? 'row-inactive' : ''}>
                      <td>{item.title}</td>
                      <td className="amount-cell">{formatINR(item.amount)}</td>
                      <td><span className="category-badge">{item.frequency}</span></td>
                      <td>{formatDate(item.nextDueDate)}</td>
                      <td><span className={`status-badge ${item.isActive ? 'active' : 'paused'}`}>{item.isActive ? 'Active' : 'Paused'}</span></td>
                      <td className="action-buttons">
                        <button className="btn-edit" onClick={() => handleToggle(item.id)}>{item.isActive ? 'Pause' : 'Resume'}</button>
                        <button className="btn-delete" onClick={() => handleDelete(item.id)}>Delete</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>
      )}
    </div>
  )
}
export default RecurringPage
