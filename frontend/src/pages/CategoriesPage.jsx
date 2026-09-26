import { useState, useEffect } from 'react'
import { fetchCategories, createCategory, updateCategory, deleteCategory } from '../api/categoryApi'
import ErrorMessage from '../components/ErrorMessage'
import LoadingSpinner from '../components/LoadingSpinner'

function CategoriesPage() {
  const [categories, setCategories] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [newName, setNewName] = useState('')
  const [editId, setEditId] = useState(null)
  const [editName, setEditName] = useState('')
  const [saving, setSaving] = useState(false)
  const [deleteError, setDeleteError] = useState('')

  const load = async () => {
    setLoading(true); setError(null)
    try { const r = await fetchCategories(); setCategories(r.data) }
    catch { setError('Failed to load categories') }
    finally { setLoading(false) }
  }
  useEffect(() => { load() }, [])

  const handleCreate = async (e) => {
    e.preventDefault(); if (!newName.trim()) return
    setSaving(true)
    try { await createCategory({ name: newName.trim() }); setNewName(''); load() }
    catch (err) { setError(err.response?.data?.message || 'Failed to create category') }
    finally { setSaving(false) }
  }

  const handleRename = async (id) => {
    if (!editName.trim()) return
    setSaving(true)
    try { await updateCategory(id, { name: editName.trim() }); setEditId(null); load() }
    catch (err) { setError(err.response?.data?.message || 'Failed to rename category') }
    finally { setSaving(false) }
  }

  const handleDelete = async (id, name) => {
    setDeleteError('')
    if (!confirm(`Delete category "${name}"?`)) return
    try { await deleteCategory(id); load() }
    catch (err) { setDeleteError(err.response?.data?.message || 'Cannot delete this category') }
  }

  return (
    <div className="page">
      <div className="page-header"><h1>🏷️ Categories</h1></div>

      <div className="form-card">
        <h3>Add New Category</h3>
        <form onSubmit={handleCreate} style={{ display: 'flex', gap: '12px', alignItems: 'flex-end' }}>
          <div className="form-group" style={{ flex: 1, marginBottom: 0 }}>
            <label htmlFor="new-cat">Category Name</label>
            <input id="new-cat" type="text" value={newName} onChange={e => setNewName(e.target.value)}
              placeholder="e.g. Fitness" required maxLength={100} />
          </div>
          <button type="submit" className="btn-primary" disabled={saving}>Add</button>
        </form>
      </div>

      {deleteError && <ErrorMessage message={deleteError} />}
      {error && <ErrorMessage message={error} onRetry={load} />}
      {loading && <LoadingSpinner />}

      {!loading && (
        <div className="list-card">
          <h3>Your Categories ({categories.length})</h3>
          {categories.length === 0 ? (
            <p className="empty-state">No categories yet.</p>
          ) : (
            <ul className="category-list">
              {categories.map(cat => (
                <li key={cat.id} className="category-item">
                  {editId === cat.id ? (
                    <div className="edit-inline">
                      <input type="text" value={editName} onChange={e => setEditName(e.target.value)}
                        autoFocus onKeyDown={e => e.key === 'Enter' && handleRename(cat.id)} maxLength={100} />
                      <button className="btn-primary" onClick={() => handleRename(cat.id)} disabled={saving}>Save</button>
                      <button className="btn-secondary" onClick={() => setEditId(null)}>Cancel</button>
                    </div>
                  ) : (
                    <div className="category-row">
                      <span className="category-badge">{cat.name}</span>
                      <div className="action-buttons">
                        <button className="btn-edit" onClick={() => { setEditId(cat.id); setEditName(cat.name) }}>✏️ Rename</button>
                        <button className="btn-delete" onClick={() => handleDelete(cat.id, cat.name)}>🗑️ Delete</button>
                      </div>
                    </div>
                  )}
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
    </div>
  )
}
export default CategoriesPage
