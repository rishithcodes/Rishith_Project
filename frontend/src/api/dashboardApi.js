import api from './axiosConfig'
export const fetchDashboard = (year) => api.get('/dashboard', { params: { year } })
