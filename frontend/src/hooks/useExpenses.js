import { useState, useEffect, useCallback } from 'react'
import { fetchExpenses } from '../api/expenseApi'

export function useExpenses() {
  const [data, setData] = useState({ content: [], totalPages: 0, totalElements: 0, filteredTotal: 0 })
  const [filters, setFilters] = useState({ search: '', categoryId: '', startDate: '', endDate: '', sortBy: 'newest', page: 0, size: 10 })
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  const load = useCallback(async () => {
    setLoading(true)
    setError(null)
    try {
      const params = {}
      if (filters.search) params.search = filters.search
      if (filters.categoryId) params.categoryId = filters.categoryId
      if (filters.startDate) params.startDate = filters.startDate
      if (filters.endDate) params.endDate = filters.endDate
      params.sortBy = filters.sortBy
      params.page = filters.page
      params.size = filters.size
      const res = await fetchExpenses(params)
      setData(res.data)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to load expenses')
    } finally {
      setLoading(false)
    }
  }, [filters])

  useEffect(() => { load() }, [load])

  const updateFilter = (key, value) => setFilters(f => ({ ...f, [key]: value, page: key !== 'page' ? 0 : value }))
  const resetFilters = () => setFilters({ search: '', categoryId: '', startDate: '', endDate: '', sortBy: 'newest', page: 0, size: 10 })

  return { data, filters, loading, error, updateFilter, resetFilters, reload: load }
}
