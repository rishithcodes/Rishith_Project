// Format number as Indian Rupees
export const formatINR = (amount) => {
  return new Intl.NumberFormat('en-IN', {
    style: 'currency',
    currency: 'INR',
    minimumFractionDigits: 2,
  }).format(amount || 0)
}

// Format date as DD/MM/YYYY
export const formatDate = (dateStr) => {
  if (!dateStr) return ''
  const d = new Date(dateStr)
  return d.toLocaleDateString('en-IN')
}

// Get today's date in YYYY-MM-DD format
export const todayStr = () => new Date().toISOString().split('T')[0]

// Get first day of current month
export const firstOfMonth = () => {
  const d = new Date()
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-01`
}

// Export expenses array to CSV and trigger download
export const exportToCSV = (expenses, filename = 'expenses.csv') => {
  const headers = ['Title', 'Amount', 'Date', 'Category', 'Description']
  const rows = expenses.map(e => [
    `"${e.title}"`,
    e.amount,
    e.expenseDate,
    `"${e.categoryName || ''}"`,
    `"${e.description || ''}"`,
  ])
  const csv = [headers.join(','), ...rows.map(r => r.join(','))].join('\n')
  const blob = new Blob([csv], { type: 'text/csv' })
  const url = URL.createObjectURL(blob)
  const a = document.createElement('a')
  a.href = url; a.download = filename; a.click()
  URL.revokeObjectURL(url)
}
