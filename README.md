# 💰 FinTrack – Personal Expense Tracker

FinTrack is a full-stack personal finance management web application designed to help users manage their daily expenses, budgets, categories, and recurring transactions from a single dashboard.

The application provides secure user authentication, expense management, budget tracking, category-wise analysis, recurring expense management, and an interactive financial dashboard.

---

## 🚀 Features

### 🔐 User Authentication
- User registration and login
- JWT-based authentication
- Secure password handling
- Protected API endpoints
- Session-based frontend authentication state

### 💸 Expense Management
- Add new expenses
- Edit existing expenses
- Delete expenses
- View expense history
- Search and filter expenses
- Pagination for expense records

### 📊 Dashboard
- Monthly financial summary
- Total expenses
- Budget overview
- Recent transactions
- Category-wise expense visualization
- Spending trends and charts

### 💰 Budget Management
- Create monthly budgets
- Track budget usage
- Monitor remaining budget
- Category-based budgeting
- Budget progress visualization

### 🏷️ Category Management
- Create and manage expense categories
- Assign expenses to categories
- Category-wise expense analysis
- Visual category distribution

### 🔄 Recurring Expenses
- Add recurring expenses
- Manage recurring transactions
- Track recurring financial commitments

### 🎨 User Interface
- Responsive React-based interface
- Clean dashboard
- Interactive charts
- Navigation between different modules
- Loading and error states
- Confirmation dialogs

---

## 🛠️ Technology Stack

### Frontend
- React.js
- Vite
- JavaScript
- HTML5
- CSS3
- Axios
- Charting components

### Backend
- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- REST APIs
- JWT Authentication
- Maven

### Database
- MySQL
- Hibernate / JPA
- Flyway Database Migration

### Development Tools
- Visual Studio Code
- Git
- GitHub
- Postman
- MySQL

---

## 🏗️ Project Architecture

The project follows a full-stack client-server architecture.

```text
                    ┌─────────────────────┐
                    │       User          │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │   React Frontend   │
                    │      + Vite         │
                    └──────────┬──────────┘
                               │
                         REST API / Axios
                               │
                               ▼
                    ┌─────────────────────┐
                    │   Spring Boot API   │
                    │                     │
                    │ Controllers         │
                    │ Services            │
                    │ Repositories        │
                    │ Security / JWT      │
                    └──────────┬──────────┘
                               │
                               ▼
                    ┌─────────────────────┐
                    │       MySQL         │
                    │      Database       │
                    └─────────────────────┘
