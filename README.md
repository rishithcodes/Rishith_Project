# FinTrack — Personal Finance Application

A full-stack personal finance tracker built with React + Vite (frontend) and Java Spring Boot (backend), backed by MySQL.

## Features

- **Authentication**: Register/login with JWT tokens, bcrypt password hashing
- **Expense CRUD**: Add, edit, delete, view with full data isolation per user
- **Search & Filter**: Search by title/description, filter by category and date range
- **Quick Date Filters**: Today, This Week, This Month, This Year
- **Sorting**: Newest, oldest, highest, lowest, A-Z title
- **Pagination**: 10 expenses per page
- **Dashboard**: Summary cards, monthly trend chart (Recharts), category pie chart, recent transactions
- **Budget Management**: Set monthly income, overall budget, per-category budgets with progress bars
- **Budget Alerts**: Warnings at 80%, 100%, and above-budget levels
- **Recurring Expenses**: Daily/weekly/monthly/yearly auto-generation (scheduled daily at midnight)
- **Category Management**: Create, rename, delete custom categories (safe deletion check)
- **CSV Export**: Export filtered expenses to CSV
- **Dark Mode**: Toggle with localStorage persistence
- **Responsive**: Mobile-first card layout + desktop table view
- **Accessibility**: ARIA labels, focus states, semantic HTML

## Tech Stack

| Layer | Technology |
|---|---|
| Frontend | React 18, Vite, Axios, Recharts, React Router v6 |
| Backend | Java 17, Spring Boot 3.2.5, Spring Security, JJWT |
| Database | MySQL 8+, Flyway migrations |
| Testing | Vitest (frontend), JUnit 5 / Spring Boot Test (backend) |

---

## Setup Instructions

### Prerequisites
- Java 17+
- Node.js 18+
- MySQL 8+
- Maven 3.8+

### Step 1: Database Setup

Open MySQL Workbench and run:
```sql
CREATE DATABASE IF NOT EXISTS expense_tracker;
```
**Do NOT run schema.sql anymore** — Flyway manages all schema creation automatically on startup.

> **Existing database**: If you already have the old `expense_tracker` database with data, drop it first:
> ```sql
> DROP DATABASE expense_tracker;
> CREATE DATABASE expense_tracker;
> ```
> Flyway will recreate all tables cleanly.

### Step 2: Backend Environment Variables (Optional)

The backend uses these environment variables with safe defaults:

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/expense_tracker?...` | MySQL URL |
| `DB_USER` | `root` | MySQL username |
| `DB_PASS` | `root` | MySQL password |
| `JWT_SECRET` | (built-in default) | JWT signing secret |
| `PORT` | `8080` | Server port |

To override, set environment variables before running, or edit `application.properties` directly.

### Step 3: Run the Backend

```bash
cd PP/backend
mvn spring-boot:run
```

First run downloads dependencies (~2 min). Flyway will automatically create all 6 tables.

✅ Ready when you see: `Started ExpenseTrackerApplication in X.XXX seconds`

### Step 4: Frontend Environment

The frontend is pre-configured. `.env.local` contains:
```
VITE_API_URL=http://localhost:8080/api
```

### Step 5: Run the Frontend

```bash
cd PP/frontend
npm install     # first time only
npm run dev
```

Open: **http://localhost:5173**

---

## First Use

1. Go to `http://localhost:5173`
2. Click **Register** → create your account
3. You'll be automatically logged in with 7 default categories (Food, Travel, Shopping, Bills, Entertainment, Health, Other)
4. Navigate using the top navbar: Dashboard → Expenses → Budget → Recurring → Categories

---

## API Documentation

All protected endpoints require: `Authorization: Bearer <token>`

### Auth (Public)
| Method | Endpoint | Body | Response |
|---|---|---|---|
| POST | `/api/auth/register` | `{name, email, password}` | `{token, name, email}` |
| POST | `/api/auth/login` | `{email, password}` | `{token, name, email}` |

### Expenses (Protected)
| Method | Endpoint | Notes |
|---|---|---|
| GET | `/api/expenses` | Params: `search`, `categoryId`, `startDate`, `endDate`, `sortBy`, `page`, `size` |
| GET | `/api/expenses/{id}` | |
| POST | `/api/expenses` | Body: `{title, amount, expenseDate, categoryId, description}` |
| PUT | `/api/expenses/{id}` | Same body as POST |
| DELETE | `/api/expenses/{id}` | |

**sortBy values**: `newest` (default), `oldest`, `highest`, `lowest`, `title`

### Categories (Protected)
| Method | Endpoint | Body |
|---|---|---|
| GET | `/api/categories` | |
| POST | `/api/categories` | `{name}` |
| PUT | `/api/categories/{id}` | `{name}` |
| DELETE | `/api/categories/{id}` | Fails if expenses exist |

### Dashboard (Protected)
| Method | Endpoint | Notes |
|---|---|---|
| GET | `/api/dashboard` | Param: `year` (default: current year) |

Returns: `{todayTotal, monthTotal, yearTotal, topCategory, recentExpenses, monthlyTrend, categoryBreakdown}`

### Budget (Protected)
| Method | Endpoint | Body |
|---|---|---|
| GET | `/api/budget` | Params: `month`, `year` |
| POST | `/api/budget` | `{month, year, income, overallBudget}` |
| POST | `/api/budget/category` | `{categoryId, month, year, budgetAmount}` |

### Recurring (Protected)
| Method | Endpoint | Notes |
|---|---|---|
| GET | `/api/recurring` | |
| POST | `/api/recurring` | `{title, amount, frequency, startDate, categoryId, description}` |
| PATCH | `/api/recurring/{id}/toggle` | Pause/resume |
| DELETE | `/api/recurring/{id}` | |

**frequency values**: `DAILY`, `WEEKLY`, `MONTHLY`, `YEARLY`

---

## Running Tests

### Backend Tests (H2 in-memory, no MySQL needed)
```bash
cd backend
mvn test
```
Tests: context loads, register user, create/update/delete/filter expenses.

### Frontend Tests
```bash
cd frontend
npm test
```
Tests: INR formatting, date formatting, CSV export utility.

---

## Project Structure

```
PP/
├── .gitignore
├── schema.sql                    ← Legacy (no longer needed — Flyway handles schema)
├── README.md
├── VIVA_NOTES.md
│
├── backend/
│   ├── pom.xml                   ← Spring Boot + Security + JWT + Flyway
│   └── src/
│       ├── main/
│       │   ├── java/com/expensetracker/
│       │   │   ├── ExpenseTrackerApplication.java
│       │   │   ├── config/
│       │   │   │   ├── CorsConfig.java
│       │   │   │   └── SecurityConfig.java
│       │   │   ├── controller/
│       │   │   │   ├── AuthController.java
│       │   │   │   ├── CategoryController.java
│       │   │   │   ├── ExpenseController.java
│       │   │   │   ├── DashboardController.java
│       │   │   │   ├── BudgetController.java
│       │   │   │   └── RecurringExpenseController.java
│       │   │   ├── dto/           ← 15 DTOs (request/response separation)
│       │   │   ├── entity/        ← 6 JPA entities
│       │   │   ├── exception/     ← GlobalExceptionHandler, custom exceptions
│       │   │   ├── repository/    ← 6 Spring Data repositories
│       │   │   ├── security/      ← JwtUtil, JwtAuthFilter, UserDetailsServiceImpl
│       │   │   └── service/       ← 6 service classes
│       │   └── resources/
│       │       ├── application.properties
│       │       └── db/migration/
│       │           ├── V1__init_schema.sql
│       │           └── V2__seed_default_categories.sql
│       └── test/
│           ├── java/com/expensetracker/
│           │   └── ExpenseTrackerApplicationTests.java
│           └── resources/
│               └── application-test.properties  ← H2 in-memory config
│
└── frontend/
    ├── .env.local                ← VITE_API_URL (not committed)
    ├── package.json
    ├── vite.config.js
    ├── index.html
    └── src/
        ├── App.jsx               ← Router + Auth guard
        ├── main.jsx
        ├── index.css             ← Complete design system with dark mode
        ├── api/
        │   ├── axiosConfig.js    ← JWT interceptor + 401 handler
        │   ├── authApi.js
        │   ├── expenseApi.js
        │   ├── categoryApi.js
        │   ├── dashboardApi.js
        │   ├── budgetApi.js
        │   └── recurringApi.js
        ├── context/
        │   ├── AuthContext.jsx   ← Login/logout state
        │   └── ThemeContext.jsx  ← Dark mode
        ├── hooks/
        │   └── useExpenses.js    ← Filter/pagination/loading state
        ├── utils/
        │   └── formatters.js     ← INR format, CSV export, date utils
        ├── components/
        │   ├── Navbar.jsx
        │   ├── ExpenseForm.jsx
        │   ├── ExpenseList.jsx
        │   ├── ExpenseFilters.jsx
        │   ├── Pagination.jsx
        │   ├── SummaryCards.jsx
        │   ├── TrendChart.jsx
        │   ├── CategoryChart.jsx
        │   ├── RecentTransactions.jsx
        │   ├── BudgetPanel.jsx
        │   ├── LoadingSpinner.jsx
        │   ├── ErrorMessage.jsx
        │   └── ConfirmDialog.jsx
        ├── pages/
        │   ├── LoginPage.jsx
        │   ├── RegisterPage.jsx
        │   ├── DashboardPage.jsx
        │   ├── ExpensesPage.jsx
        │   ├── BudgetPage.jsx
        │   ├── RecurringPage.jsx
        │   └── CategoriesPage.jsx
        └── __tests__/
            └── formatters.test.js
```

---

## Troubleshooting

### ❌ Flyway migration fails on startup
**Symptom**: `Migration checksum mismatch` or `already applied`
**Fix**: Drop and recreate the database:
```sql
DROP DATABASE expense_tracker;
CREATE DATABASE expense_tracker;
```
Then restart the backend.

### ❌ CORS Error
**Symptom**: Browser blocks API calls
**Fix**: Ensure React runs on port 5173 (not 3000). CorsConfig.java allows only `http://localhost:5173`.

### ❌ 401 Unauthorized after login
**Symptom**: API calls fail with 401 right after logging in
**Fix**: Clear localStorage and log in again. Token may be from old format.

### ❌ JWT Secret too short
**Symptom**: `WeakKeyException` on startup
**Fix**: Set `JWT_SECRET` env var to a string of at least 32 characters.

### ❌ Port 8080 in use
```bash
# macOS / Linux
lsof -ti:8080 | xargs kill -9
```

### ❌ MySQL connection refused
Ensure MySQL is running: open MySQL Workbench and check the connection.

---

## Features Deliberately Deferred

| Feature | Reason |
|---|---|
| **Receipt image uploads** | Requires disk/S3 storage configuration — adds infrastructure complexity inappropriate for student project |
| **PDF report generation** | Requires iText/JasperReports — significant dependency for marginal gain |
| **Push notifications** | Requires WebSocket or FCM — out of scope |
| **OAuth/Social login** | Adds complexity; JWT email/password sufficient for this scope |
| **Rate limiting** | Recommended for production but not required for student project |
