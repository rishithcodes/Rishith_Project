import api from './axiosConfig'
export const fetchRecurring = () => api.get('/recurring')
export const createRecurring = (data) => api.post('/recurring', data)
export const toggleRecurring = (id) => api.patch(`/recurring/${id}/toggle`)
export const deleteRecurring = (id) => api.delete(`/recurring/${id}`)
