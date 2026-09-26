import api from './axiosConfig'
export const fetchBudget = (month, year) => api.get('/budget', { params: { month, year } })
export const saveBudget = (data) => api.post('/budget', data)
export const saveCategoryBudget = (data) => api.post('/budget/category', data)
