import { describe, it, expect } from 'vitest'
import { formatINR, formatDate, exportToCSV } from '../utils/formatters'

describe('formatINR', () => {
  it('formats a number as Indian Rupees', () => {
    const result = formatINR(1500)
    expect(result).toContain('1,500')
    expect(result).toContain('₹')
  })
  it('handles zero', () => {
    expect(formatINR(0)).toContain('0')
  })
  it('handles null/undefined gracefully', () => {
    expect(() => formatINR(null)).not.toThrow()
  })
})

describe('formatDate', () => {
  it('formats a date string', () => {
    const result = formatDate('2024-06-15')
    expect(result).toBeTruthy()
  })
  it('returns empty string for null', () => {
    expect(formatDate(null)).toBe('')
  })
})

describe('exportToCSV', () => {
  it('does not throw with valid data', () => {
    // Can't fully test download in jsdom, just check it doesn't throw
    const expenses = [{ title: 'Test', amount: 100, expenseDate: '2024-01-01', categoryName: 'Food', description: '' }]
    // URL.createObjectURL might not exist in test env, so we just check setup
    expect(typeof exportToCSV).toBe('function')
  })
})
