import { PieChart, Pie, Cell, Tooltip, Legend, ResponsiveContainer } from 'recharts'

const COLORS = ['#4f46e5','#06b6d4','#10b981','#f59e0b','#ef4444','#8b5cf6','#ec4899','#6b7280']

function CategoryChart({ data }) {
  if (!data || data.length === 0) return (
    <div className="chart-card">
      <h3>🥧 Category Breakdown</h3>
      <p className="empty-state">No data for this month yet.</p>
    </div>
  )

  const chartData = data.map(d => ({ name: d.category, value: Number(d.amount) }))

  return (
    <div className="chart-card">
      <h3>🥧 Category Breakdown (This Month)</h3>
      <ResponsiveContainer width="100%" height={250}>
        <PieChart>
          <Pie data={chartData} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={90} label={({name, percent}) => `${name} ${(percent*100).toFixed(0)}%`}>
            {chartData.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
          </Pie>
          <Tooltip formatter={(v) => `₹${Number(v).toLocaleString('en-IN')}`} />
          <Legend />
        </PieChart>
      </ResponsiveContainer>
    </div>
  )
}
export default CategoryChart
