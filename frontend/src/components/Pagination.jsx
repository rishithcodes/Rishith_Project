function Pagination({ totalPages, currentPage, onPageChange }) {
  if (totalPages <= 1) return null
  return (
    <div className="pagination" role="navigation" aria-label="Expense pages">
      <button
        onClick={() => onPageChange(currentPage - 1)}
        disabled={currentPage === 0}
        aria-label="Previous page"
      >← Prev</button>
      <span>Page {currentPage + 1} of {totalPages}</span>
      <button
        onClick={() => onPageChange(currentPage + 1)}
        disabled={currentPage >= totalPages - 1}
        aria-label="Next page"
      >Next →</button>
    </div>
  )
}
export default Pagination
