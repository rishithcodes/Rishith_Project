function ConfirmDialog({ message, onConfirm, onCancel }) {
  return (
    <div className="dialog-overlay" role="dialog" aria-modal="true" aria-label="Confirm action">
      <div className="dialog-box">
        <p>{message}</p>
        <div className="dialog-buttons">
          <button className="btn-danger" onClick={onConfirm} autoFocus>Yes, Delete</button>
          <button className="btn-secondary" onClick={onCancel}>Cancel</button>
        </div>
      </div>
    </div>
  )
}
export default ConfirmDialog
