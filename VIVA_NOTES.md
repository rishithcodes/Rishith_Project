# VIVA NOTES — Expense Tracker Project
### For 2nd Year B.Tech Students | Simple Language Guide

---

## 1. Project Title and Objective

**Title:** Expense Tracker Web Application

**Objective:**
To build a full-stack web application that helps users record, view, edit, and delete their daily expenses. Each expense is grouped under a category (like Food, Travel, etc.), and the app shows the total amount spent.

---

## 2. Problem Statement / Purpose

**Problem:**
People often forget where they spent their money. Maintaining a manual diary or Excel sheet is slow and inconvenient.

**Purpose:**
This app provides a simple digital solution where users can:
- Quickly add an expense with its title, amount, date, and category
- View all expenses in a table
- Edit or delete any expense
- See the total money spent at a glance

**Real-World Use Case:**
A college student can use this to track monthly spending on food, transport, books, and entertainment.

---

## 3. Features and Functionalities

| Feature | Description |
|---|---|
| Add Expense | Fill the form with title, amount, date, category, and optional description |
| View All Expenses | All expenses shown in a table with Sr. No., title, amount, date, category |
| Edit Expense | Click Edit on any row → form pre-fills with existing data → update |
| Delete Expense | Click Delete → confirmation popup → expense removed from DB |
| Category Dropdown | Categories (Food, Travel, etc.) loaded from the database |
| Total Amount | Sum of all expense amounts shown at the top in ₹ |

---

## 4. Technologies Used (and Why)

| Technology | Purpose | Why Chosen |
|---|---|---|
| **React.js** | Frontend UI | Popular, component-based, easy to learn |
| **Vite** | Build tool for React | Fast development server, simple setup |
| **Axios** | HTTP client | Easier than fetch(), cleaner syntax |
| **Plain CSS** | Styling | Simple, no libraries needed, easy to explain |
| **Java 17** | Backend language | Strongly typed, industry standard |
| **Spring Boot 3** | REST API framework | Reduces boilerplate, auto-configures everything |
| **Spring Data JPA** | Database ORM | No raw SQL needed, maps Java classes to DB tables |
| **MySQL** | Database | Relational DB, free, widely used in industry |
| **Maven** | Build tool for Java | Manages dependencies (like npm for Java) |

---

## 5. Architecture / Workflow

```
USER
  |
  |  (Opens browser)
  v
REACT FRONTEND (port 5173)
  |
  |  HTTP Request (via Axios)
  |  e.g. POST http://localhost:8080/api/expenses
  v
SPRING BOOT BACKEND (port 8080)
  |
  +--> Controller  (receives HTTP request, validates input)
  |
  +--> Service     (business logic: create/update/delete)
  |
  +--> Repository  (talks to database using JPA)
  |
  v
MySQL DATABASE (port 3306)
  |
  |  Returns data
  v
SPRING BOOT  -->  JSON Response  -->  REACT  -->  Updates UI
```

**Flow Example (Adding an Expense):**
1. User fills the form and clicks "Add Expense"
2. React calls `POST /api/expenses` via Axios
3. `ExpenseController` receives the request
4. `ExpenseService` links the category, creates the expense object
5. `ExpenseRepository.save()` inserts a row into MySQL
6. Backend returns the saved expense as JSON
7. React refreshes the expense list automatically

---

## 6. Database Structure

### Table: `categories`
| Column | Type | Description |
|---|---|---|
| `id` | INT, PK, AUTO_INCREMENT | Unique ID |
| `name` | VARCHAR(100), UNIQUE | Category name (Food, Travel…) |

### Table: `expenses`
| Column | Type | Description |
|---|---|---|
| `id` | INT, PK, AUTO_INCREMENT | Unique ID |
| `title` | VARCHAR(255), NOT NULL | Expense title |
| `amount` | DECIMAL(10,2), NOT NULL | Amount in rupees |
| `expense_date` | DATE, NOT NULL | Date of expense |
| `description` | VARCHAR(255) | Optional notes |
| `category_id` | INT, FK → categories.id | Which category |

### Relationship:
- **Many expenses → One category**
- This is a **Many-to-One** relationship
- `category_id` in `expenses` is a Foreign Key pointing to `categories.id`

---

## 7. Frontend Implementation

**Technology:** React.js (using Vite)

**Components:**

### `main.jsx`
- Entry point. Mounts `<App />` into the HTML `#root` div.

### `App.jsx` (Root Component)
- Holds the global state: list of expenses, which expense is being edited
- Calls `fetchExpenses()` on page load using `useEffect`
- Passes data and callbacks to child components

### `ExpenseForm.jsx`
- Handles both **Add** and **Edit** modes
- Uses `useState` for each field (title, amount, date, etc.)
- Loads categories from backend using `useEffect` on mount
- When `expenseToEdit` prop is set → pre-fills fields (Edit mode)
- On submit → calls `createExpense()` or `updateExpense()` from API file

### `ExpenseList.jsx`
- Receives `expenses` array as props from App
- Renders a `<table>` with all expenses
- Calculates total using `.reduce()`
- Edit button → calls `onEdit(expense)` → parent sets `expenseToEdit`
- Delete button → `window.confirm()` popup → calls `deleteExpense(id)`

### `expenseApi.js`
- Centralized file for all Axios HTTP calls
- Functions: `fetchExpenses`, `fetchCategories`, `createExpense`, `updateExpense`, `deleteExpense`
- Base URL: `http://localhost:8080/api`

**State Management:** Only `useState` and `useEffect` — no Redux needed.

---

## 8. Backend Implementation

**Technology:** Spring Boot 3, Java 17, Spring Data JPA

**Package Structure:** `com.expensetracker`

### `ExpenseTrackerApplication.java`
- Main class with `@SpringBootApplication`
- Starts the embedded Tomcat server on port 8080

### `CorsConfig.java`
- Allows React (port 5173) to call backend (port 8080)
- Without CORS, the browser blocks cross-origin requests

### Entities (Model Layer)
- **`Category.java`** → Maps to `categories` table
- **`Expense.java`** → Maps to `expenses` table
- Uses JPA annotations: `@Entity`, `@Table`, `@Column`, `@ManyToOne`, `@JoinColumn`

### Repositories (Data Layer)
- **`CategoryRepository`** → extends `JpaRepository<Category, Integer>`
- **`ExpenseRepository`** → extends `JpaRepository<Expense, Integer>`
- Spring generates all SQL automatically (no need to write queries)

### Services (Business Logic Layer)
- **`CategoryService`** → `getAllCategories()`
- **`ExpenseService`** → `getAllExpenses()`, `getExpenseById()`, `createExpense()`, `updateExpense()`, `deleteExpense()`

### Controllers (API Layer)
- **`CategoryController`** → `GET /api/categories`
- **`ExpenseController`** → `GET`, `POST`, `PUT`, `DELETE` for `/api/expenses`

---

## 9. Key Modules Developed

| Module | Files | Purpose |
|---|---|---|
| CORS Config | `CorsConfig.java` | Allows browser to call backend |
| Category Module | Entity + Repo + Service + Controller | Manage expense categories |
| Expense Module | Entity + Repo + Service + Controller | Full CRUD for expenses |
| API Module | `expenseApi.js` | All Axios calls in one place |
| UI Components | `ExpenseForm.jsx`, `ExpenseList.jsx`, `App.jsx` | Frontend interface |
| Styling | `index.css` | All visual design |

---

## 10. Demo Steps (Viva Script)

> **"I will now demonstrate the working project."**

### Step 1: Show the UI
- "This is the Expense Tracker homepage. At the top we have the Add Expense form, and below that is the expense table."

### Step 2: Add an Expense
- Fill: Title = "Pizza Dinner", Amount = 350, Date = today, Category = Food, Description = "Dinner with friends"
- Click **Add Expense**
- "The expense is now added. You can see it in the table. The total has also updated to ₹350."

### Step 3: Add another Expense
- Add: "Uber Cab", Amount = 120, Category = Travel
- "Now total shows ₹470."

### Step 4: Edit an Expense
- Click **Edit** on "Uber Cab"
- Change amount to 150
- Click **Update Expense**
- "The record is updated. Total is now ₹500."

### Step 5: Delete an Expense
- Click **Delete** on "Pizza Dinner"
- A confirm popup appears: "Are you sure?"
- Click OK
- "The expense is deleted. Total is now ₹150."

### Step 6: Show the Database (MySQL Workbench)
- Run: `SELECT * FROM expenses;`
- Show remaining record in the database

---

## 11. Challenges Faced and How They Were Solved

| Challenge | Solution |
|---|---|
| CORS Error | Added `CorsConfig.java` to allow React (port 5173) to call Spring Boot (port 8080) |
| MySQL Connection Error | Set correct URL, username, password in `application.properties` |
| Category FK constraint | Fetched category by ID in Service layer before saving expense |
| Edit form pre-fill | Used `useEffect` with `expenseToEdit` dependency to populate form fields |
| Date format mismatch | Used `LocalDate` in Java with `type="date"` in HTML input (both use YYYY-MM-DD) |
| Port conflict | Used different ports: React on 5173, Spring Boot on 8080, MySQL on 3306 |

---

## 12. Future Scope

| Feature | Description |
|---|---|
| **User Login** | Add Spring Security + JWT so multiple users can have their own expense lists |
| **Monthly Reports** | Filter expenses by month, show monthly total |
| **Charts/Graphs** | Use Chart.js to show a pie chart of spending by category |
| **Export to Excel** | Add a button to download expenses as an Excel/CSV file |
| **Budget Alerts** | Set a monthly budget limit and alert when exceeded |
| **Search & Filter** | Search by title, filter by category or date range |
| **Mobile App** | Use React Native to build a mobile version |
| **Cloud Deployment** | Deploy to AWS/Heroku so it's accessible from anywhere |

---

## 13. Ten Likely Viva Questions with Short Answers

**Q1. What is the purpose of this project?**
> To track daily expenses by recording title, amount, date, and category. Users can add, view, edit, and delete expenses.

**Q2. What is REST API?**
> REST API is a way for the frontend and backend to communicate using standard HTTP methods: GET (read), POST (create), PUT (update), DELETE (remove). Data is exchanged as JSON.

**Q3. What is Spring Boot?**
> Spring Boot is a Java framework that makes it easy to build REST APIs. It auto-configures most things, so you write less code.

**Q4. What is JPA / Hibernate?**
> JPA (Java Persistence API) is a way to map Java classes (called Entities) to database tables. Hibernate is the implementation that generates SQL automatically. We don't need to write SQL — we just call methods like `findAll()`, `save()`, `deleteById()`.

**Q5. What is the difference between `@Controller` and `@RestController`?**
> `@Controller` is used for web pages. `@RestController` automatically converts Java objects to JSON. We use `@RestController` because our backend returns JSON data to the React frontend.

**Q6. What is CORS? Why did you configure it?**
> CORS (Cross-Origin Resource Sharing) is a browser security rule that blocks requests from one domain to another. React runs on port 5173 and Spring Boot on port 8080 — these are different origins. We added `CorsConfig.java` to tell the browser it's safe to allow these requests.

**Q7. What is Axios? Why use it instead of fetch?**
> Axios is a JavaScript library for making HTTP requests. It's simpler than `fetch()` because it automatically converts JSON, handles errors more cleanly, and has a cleaner syntax.

**Q8. What is the Many-to-One relationship in your project?**
> Many expenses can belong to ONE category. For example, 10 different food expenses all belong to the "Food" category. In the database, `category_id` in the `expenses` table is a foreign key pointing to the `categories` table.

**Q9. What is `useState` and `useEffect` in React?**
> `useState` is a React hook used to store data that can change (like the list of expenses). `useEffect` is used to run code at certain times — like when the page first loads (we use it to fetch expenses from the backend).

**Q10. What would you improve if you had more time?**
> I would add user authentication (login/signup), monthly spending charts using Chart.js, a search/filter feature, and the ability to export expenses to Excel.

---

*Good luck with your viva! 🎓*
