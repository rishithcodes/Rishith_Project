import { NavLink, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { useTheme } from '../context/ThemeContext'
import { useState } from 'react'

function Navbar() {
  const { user, logoutUser } = useAuth()
  const { dark, toggleTheme } = useTheme()
  const navigate = useNavigate()
  const [menuOpen, setMenuOpen] = useState(false)

  const handleLogout = () => {
    logoutUser()
    navigate('/login')
  }

  const navLinks = [
    { to: '/dashboard', label: '📊 Dashboard' },
    { to: '/expenses', label: '💸 Expenses' },
    { to: '/budget', label: '🎯 Budget' },
    { to: '/recurring', label: '🔄 Recurring' },
    { to: '/categories', label: '🏷️ Categories' },
  ]

  return (
    <nav className="navbar" role="navigation" aria-label="Main navigation">
      <div className="nav-brand">
        <span className="brand-icon">💰</span>
        <span className="brand-name">FinTrack</span>
      </div>

      <button
        className="nav-hamburger"
        aria-label="Toggle menu"
        aria-expanded={menuOpen}
        onClick={() => setMenuOpen(m => !m)}
      >
        {menuOpen ? '✕' : '☰'}
      </button>

      <div className={`nav-links ${menuOpen ? 'open' : ''}`}>
        {navLinks.map(link => (
          <NavLink
            key={link.to}
            to={link.to}
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
            onClick={() => setMenuOpen(false)}
          >
            {link.label}
          </NavLink>
        ))}
      </div>

      <div className="nav-actions">
        <button
          className="btn-icon"
          onClick={toggleTheme}
          aria-label={dark ? 'Switch to light mode' : 'Switch to dark mode'}
          title={dark ? 'Light mode' : 'Dark mode'}
        >
          {dark ? '☀️' : '🌙'}
        </button>
        <span className="nav-user">👤 {user?.name}</span>
        <button className="btn-logout" onClick={handleLogout} aria-label="Logout">
          Logout
        </button>
      </div>
    </nav>
  )
}

export default Navbar
