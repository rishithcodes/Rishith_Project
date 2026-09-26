import { useEffect, useState } from 'react'
import { fetchCategories } from '../api/categoryApi'

function ExpenseFilters({ filters, onFilterChange, onReset }) {
  const [categories, setCategories] = useState([])

  useEffect(() => {
    fetchCategories().then(r => setCategories(r.data)).catch(() => {})
  }, [])

  const setQuickDate = (type) => {
    const today = new Date()
    const pad = (n) => String(n).padStart(2, '0')
    const fmt = (d) => `${d.getFullYear()}-${pad(d.getMonth()+1)}-${pad(d.getDate())}`
    const todayStr = fmt(today)
    if (type === 'today') {
      onFilterChange('startDate', todayStr)
      onFilterChange('endDate', todayStr)
    } else if (type === 'week') {
      const start = new Date(today)
      start.setDate(today.getDate() - today.getDay())
      onFilterChange('startDate', fmt(start))
      onFilterChange('endDate', todayStr)
    } else if (type === 'month') {
      onFilterChange('startDate', `${today.getFullYear()}-${pad(today.getMonth()+1)}-01`)
      onFilterChange('endDate', todayStr)
    } else if (type === 'year') {
      onFilterChange('startDate', `${today.getFullYear()}-01-01`)
      onFilterChange('endDate', todayStr)
    }
  }

  return (
    <div className="filters-bar">
      <div className="filter-row">
        <div className="filter-group">
          <label htmlFor="search-input">Search</label>
          <input
            id="search-input"
            type="search"
            placeholder="Search expenses..."
            value={filters.search}
            onChange={e => onFilterChange('search', e.target.value)}
          />
        </div>
        <div className="filter-group">
          <label htmlFor="category-filter">Category</label>
          <select id="category-filter" value={filters.categoryId} onChange={e => onFilterChange('categoryId', e.target.value)}>
            <option value="">All Categories</option>
            {categories.map(c => <option key={c.id} value={c.id}>{c.name}</option>)}
          </select>
        </div>
        <div className="filter-group">
          <label htmlFor="sort-filter">Sort By</label>
          <select id="sort-filter" value={filters.sortBy} onChange={e => onFilterChange('sortBy', e.target.value)}>
            <option value="newest">Newest First</option>
            <option value="oldest">Oldest First</option>
            <option value="highest">Highest Amount</option>
            <option value="lowest">Lowest Amount</option>
            <option value="title">Title (A-Z)</option>
          </select>
        </div>
      </div>
      <div className="filter-row">
        <div className="filter-group">
          <label htmlFor="start-date">From</label>
          <input id="start-date" type="date" value={filters.startDate} onChange={e => onFilterChange('startDate', e.target.value)} />
        </div>
        <div className="filter-group">
          <label htmlFor="end-date">To</label>
          <input id="end-date" type="date" value={filters.endDate} onChange={e => onFilterChange('endDate', e.target.value)} />
        </div>
        <div className="filter-group quick-dates">
          <label>Quick Date</label>
          <div className="quick-date-buttons">
            <button type="button" onClick={() => setQuickDate('today')}>Today</button>
            <button type="button" onClick={() => setQuickDate('week')}>This Week</button>
            <button type="button" onClick={() => setQuickDate('month')}>This Month</button>
            <button type="button" onClick={() => setQuickDate('year')}>This Year</button>
          </div>
        </div>
        <div className="filter-group">
          <label>&nbsp;</label>
          <button type="button" className="btn-secondary" onClick={onReset}>Clear Filters</button>
        </div>
      </div>
    </div>
  )
}
export default ExpenseFilters
