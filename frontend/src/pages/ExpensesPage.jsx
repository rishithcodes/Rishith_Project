import { useState } from 'react'
import ExpenseForm from '../components/ExpenseForm'
import ExpenseList from '../components/ExpenseList'
import ExpenseFilters from '../components/ExpenseFilters'
import Pagination from '../components/Pagination'
import { useExpenses } from '../hooks/useExpenses'

function ExpensesPage() {
  const [expenseToEdit, setExpenseToEdit] = useState(null)
  const { data, filters, loading, error, updateFilter, resetFilters, reload } = useExpenses()

  const handleEdit = (expense) => {
    setExpenseToEdit(expense)
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }

  const handleSuccess = () => {
    reload()
    setExpenseToEdit(null)
  }

  return (
    <div className="page">
      <div className="page-header">
        <h1>💸 Expenses</h1>
      </div>

      <ExpenseForm
        expenseToEdit={expenseToEdit}
        onSuccess={handleSuccess}
        onCancelEdit={() => setExpenseToEdit(null)}
      />

      <ExpenseFilters
        filters={filters}
        onFilterChange={updateFilter}
        onReset={resetFilters}
      />

      <ExpenseList
        data={data}
        loading={loading}
        error={error}
        onEdit={handleEdit}
        onDeleted={reload}
        onRetry={reload}
        filters={filters}
      />

      <Pagination
        totalPages={data.totalPages}
        currentPage={data.pageNumber}
        onPageChange={(p) => updateFilter('page', p)}
      />
    </div>
  )
}
export default ExpensesPage
