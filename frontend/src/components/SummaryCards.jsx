import { formatINR } from '../utils/formatters'

function SummaryCards({ data }) {
  const cards = [
    { label: 'Today', value: formatINR(data.todayTotal), icon: '📅', cls: 'card-today' },
    { label: 'This Month', value: formatINR(data.monthTotal), icon: '📆', cls: 'card-month' },
    { label: 'This Year', value: formatINR(data.yearTotal), icon: '🗓️', cls: 'card-year' },
    { label: 'Top Category', value: data.topCategory || 'N/A', icon: '🏆', cls: 'card-top' },
  ]
  return (
    <div className="summary-cards">
      {cards.map(c => (
        <div key={c.label} className={`summary-card ${c.cls}`}>
          <div className="card-icon" aria-hidden="true">{c.icon}</div>
          <div className="card-content">
            <p className="card-label">{c.label}</p>
            <p className="card-value">{c.value}</p>
          </div>
        </div>
      ))}
    </div>
  )
}
export default SummaryCards
