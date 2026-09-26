import { useState, useEffect } from 'react'
import { fetchBudget, saveBudget, saveCategoryBudget } from '../api/budgetApi'
import { fetchCategories } from '../api/categoryApi'
import BudgetPanel from '../components/BudgetPanel'
import LoadingSpinner from '../components/LoadingSpinner'
import ErrorMessage from '../components/ErrorMessage'
import { formatINR } from '../utils/formatters'

function BudgetPage() {
  const now = new Date()
  const [month, setMonth] = useState(now.getMonth() + 1)
  const [year, setYear] = useState(now.getFullYear())
  const [budget, setBudget] = useState(null)
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [saving, setSaving] = useState(false)
  const [form, setForm] = useState({ income: '', overallBudget: '' })
  const [catForm, setCatForm] = useState({ categoryId: '', budgetAmount: '' })
  const [successMsg, setSuccessMsg] = useState('')

  const load = async () => {
    setLoading(true); setError(null)
    try {
      const [budgetRes, catRes] = await Promise.all([
        fetchBudget(month, year),
        fetchCategories()
      ])
      setBudget(budgetRes.data)
      setCategories(catRes.data)
      setForm({ income: budgetRes.data.income || '', overallBudget: budgetRes.data.overallBudget || '' })
    } catch { setError('Failed to load budget data') }
    finally { setLoading(false) }
  }

  useEffect(() => { load() }, [month, year])

  const handleSaveBudget = async (e) => {
    e.preventDefault(); setSaving(true)
    try {
      await saveBudget({ month, year, income: Number(form.income) || 0, overallBudget: Number(form.overallBudget) || 0 })
      setSuccessMsg('Budget saved!'); load()
      setTimeout(() => setSuccessMsg(''), 3000)
    } catch { setError('Failed to save budget') }
    finally { setSaving(false) }
  }

  const handleSaveCatBudget = async (e) => {
    e.preventDefault(); setSaving(true)
    try {
      await saveCategoryBudget({ categoryId: Number(catForm.categoryId), month, year, budgetAmount: Number(catForm.budgetAmount) })
      setSuccessMsg('Category budget saved!'); load(); setCatForm({ categoryId: '', budgetAmount: '' })
      setTimeout(() => setSuccessMsg(''), 3000)
    } catch { setError('Failed to save category budget') }
    finally { setSaving(false) }
  }

  const MONTHS = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec']

  return (
    <div className="page">
      <div className="page-header">
        <h1>🎯 Budget</h1>
        <div className="month-picker">
          <select value={month} onChange={e => setMonth(Number(e.target.value))} aria-label="Month">
            {MONTHS.map((m, i) => <option key={i} value={i+1}>{m}</option>)}
          </select>
          <select value={year} onChange={e => setYear(Number(e.target.value))} aria-label="Year">
            {[0,1,2].map(i => now.getFullYear() - i).map(y => <option key={y} value={y}>{y}</option>)}
          </select>
        </div>
      </div>

      {successMsg && <p className="success-msg" role="status">{successMsg}</p>}
      {loading && <LoadingSpinner />}
      {error && <ErrorMessage message={error} onRetry={load} />}

      {!loading && (
        <>
          <div className="budget-forms-grid">
            <div className="form-card">
              <h3>Set Income & Overall Budget</h3>
              <form onSubmit={handleSaveBudget}>
                <div className="form-group">
                  <label htmlFor="income">Monthly Income (₹)</label>
                  <input id="income" type="number" min="0" step="0.01" value={form.income}
                    onChange={e => setForm(f => ({...f, income: e.target.value}))} placeholder="e.g. 50000" />
                </div>
                <div className="form-group">
                  <label htmlFor="overall-budget">Overall Budget (₹)</label>
                  <input id="overall-budget" type="number" min="0" step="0.01" value={form.overallBudget}
                    onChange={e => setForm(f => ({...f, overallBudget: e.target.value}))} placeholder="e.g. 30000" />
                </div>
                <button type="submit" className="btn-primary" disabled={saving}>
                  {saving ? 'Saving...' : 'Save Budget'}
                </button>
              </form>
            </div>

            <div className="form-card">
              <h3>Set Category Budget</h3>
              <form onSubmit={handleSaveCatBudget}>
                <div className="form-group">
                  <label htmlFor="cat-select">Category</label>
                  <select id="cat-select" value={catForm.categoryId}
                    onChange={e => setCatForm(f => ({...f, categoryId: e.target.value}))} required>
                    <option value="">-- Select Category --</option>
                    {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
                  </select>
                </div>
                <div className="form-group">
                  <label htmlFor="cat-budget">Budget Amount (₹)</label>
                  <input id="cat-budget" type="number" min="0" step="0.01" value={catForm.budgetAmount}
                    onChange={e => setCatForm(f => ({...f, budgetAmount: e.target.value}))} placeholder="e.g. 5000" />
                </div>
                <button type="submit" className="btn-primary" disabled={saving || !catForm.categoryId}>
                  {saving ? 'Saving...' : 'Set Category Budget'}
                </button>
              </form>
            </div>
          </div>

          <BudgetPanel budget={budget} />
        </>
      )}
    </div>
  )
}
export default BudgetPage
