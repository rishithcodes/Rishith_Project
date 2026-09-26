import { useState, useEffect } from 'react'
import { fetchDashboard } from '../api/dashboardApi'
import SummaryCards from '../components/SummaryCards'
import TrendChart from '../components/TrendChart'
import CategoryChart from '../components/CategoryChart'
import RecentTransactions from '../components/RecentTransactions'
import LoadingSpinner from '../components/LoadingSpinner'
import ErrorMessage from '../components/ErrorMessage'

function DashboardPage() {
  const [dashboard, setDashboard] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [year, setYear] = useState(new Date().getFullYear())

  const load = () => {
    setLoading(true)
    setError(null)
    fetchDashboard(year)
      .then(r => setDashboard(r.data))
      .catch(() => setError('Failed to load dashboard data'))
      .finally(() => setLoading(false))
  }

  useEffect(() => { load() }, [year])

  if (loading) return <LoadingSpinner message="Loading dashboard..." />
  if (error) return <ErrorMessage message={error} onRetry={load} />

  return (
    <div className="page">
      <div className="page-header">
        <h1>📊 Dashboard</h1>
        <select value={year} onChange={e => setYear(Number(e.target.value))} aria-label="Select year">
          {[0,1,2].map(i => new Date().getFullYear() - i).map(y => (
            <option key={y} value={y}>{y}</option>
          ))}
        </select>
      </div>
      <SummaryCards data={dashboard} />
      <div className="charts-grid">
        <TrendChart data={dashboard.monthlyTrend} />
        <CategoryChart data={dashboard.categoryBreakdown} />
      </div>
      <RecentTransactions expenses={dashboard.recentExpenses} />
    </div>
  )
}
export default DashboardPage
