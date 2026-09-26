function ErrorMessage({ message, onRetry }) {
  return (
    <div className="error-banner" role="alert">
      <span>⚠️ {message}</span>
      {onRetry && (
        <button onClick={onRetry} className="btn-retry">Try Again</button>
      )}
    </div>
  )
}
export default ErrorMessage
