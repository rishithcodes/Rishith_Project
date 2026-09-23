# Expense Tracker — Run Instructions & Troubleshooting

## Step-by-Step Run Instructions (Windows)

---

### STEP 1: Set Up MySQL Database

1. Open **MySQL Workbench**
2. Connect to your local MySQL server (localhost, root/root)
3. Click **File → Open SQL Script**
4. Open the file: `PP/schema.sql`
5. Click the **⚡ lightning bolt (Execute)** button
6. You should see:
   - Database `expense_tracker` created
   - Tables `categories` and `expenses` created
   - 7 categories inserted (Food, Travel, etc.)
7. Verify by running:
   ```sql
   USE expense_tracker;
   SELECT * FROM categories;
   ```
   You should see 7 rows.

---

### STEP 2: Run the Spring Boot Backend

1. Open **VS Code**
2. Open the folder: `PP/backend`
3. Open a **new Terminal** in VS Code (Terminal → New Terminal)
4. Make sure Java 17 is installed:
   ```
   java -version
   ```
   Should show: `openjdk 17...`
5. Run the Spring Boot app:
   ```
   mvnw.cmd spring-boot:run
   ```
   > **Note:** The first time this runs, Maven will download all dependencies.
   > This may take 2–5 minutes. Wait for it.

6. When you see this in the terminal, the backend is ready:
   ```
   Started ExpenseTrackerApplication in X.XXX seconds
   ```
7. Test it: Open your browser and go to:
   ```
   http://localhost:8080/api/categories
   ```
   You should see JSON like:
   ```json
   [{"id":1,"name":"Food"}, {"id":2,"name":"Travel"}, ...]
   ```

> **Keep this terminal open!** Do not close it.

---

### STEP 3: Run the React Frontend

1. Open a **second terminal** in VS Code (click the + button in terminal)
2. Go to the frontend folder:
   ```
   cd PP/frontend
   ```
3. Install Node.js dependencies (first time only):
   ```
   npm install
   ```
4. Start the React development server:
   ```
   npm run dev
   ```
5. You should see:
   ```
   VITE v5.x.x  ready in XXX ms
   ➜  Local:   http://localhost:5173/
   ```
6. Open your browser and go to:
   ```
   http://localhost:5173
   ```
   The Expense Tracker UI should appear!

---

### STEP 4: Use the Application

1. **Add Expense:** Fill the form → Click "Add Expense"
2. **View:** See all expenses in the table below
3. **Edit:** Click "✏️ Edit" on any row → update fields → Click "Update Expense"
4. **Delete:** Click "🗑️ Delete" → Confirm the popup → Expense is removed
5. **Total:** Green banner at top shows the total of all expenses

---

## Folder Structure

```
PP/
├── schema.sql                          ← Run this in MySQL Workbench first
├── VIVA_NOTES.md                       ← Study guide for viva
├── README.md                           ← This file
│
├── backend/                            ← Spring Boot project
│   ├── pom.xml                         ← Maven config (dependencies)
│   └── src/main/
│       ├── java/com/expensetracker/
│       │   ├── ExpenseTrackerApplication.java   ← Main entry point
│       │   ├── config/
│       │   │   └── CorsConfig.java              ← CORS settings
│       │   ├── entity/
│       │   │   ├── Category.java                ← DB table mapping
│       │   │   └── Expense.java                 ← DB table mapping
│       │   ├── repository/
│       │   │   ├── CategoryRepository.java      ← DB operations
│       │   │   └── ExpenseRepository.java       ← DB operations
│       │   ├── service/
│       │   │   ├── CategoryService.java         ← Business logic
│       │   │   └── ExpenseService.java          ← Business logic
│       │   └── controller/
│       │       ├── CategoryController.java      ← REST endpoints
│       │       └── ExpenseController.java       ← REST endpoints
│       └── resources/
│           └── application.properties           ← DB connection config
│
└── frontend/                           ← React + Vite project
    ├── package.json                    ← npm dependencies
    ├── vite.config.js                  ← Vite settings
    ├── index.html                      ← HTML entry point
    └── src/
        ├── main.jsx                    ← React entry point
        ├── App.jsx                     ← Root component (state management)
        ├── index.css                   ← All CSS styles
        ├── api/
        │   └── expenseApi.js           ← All Axios API calls
        └── components/
            ├── ExpenseForm.jsx         ← Add/Edit form
            └── ExpenseList.jsx         ← Table of all expenses
```

---

## Troubleshooting

### ❌ Problem: CORS Error in Browser Console
**Symptom:** `Access to XMLHttpRequest at 'http://localhost:8080/api/...' from origin 'http://localhost:5173' has been blocked by CORS policy`

**Fix:**
- Make sure the backend is running (check terminal showing "Started ExpenseTrackerApplication")
- Check that `CorsConfig.java` exists in `com.expensetracker.config`
- Restart the backend after any changes to CorsConfig

---

### ❌ Problem: MySQL Connection Error
**Symptom:** Backend fails to start with `Communications link failure` or `Access denied for user 'root'@'localhost'`

**Fix:**
1. Make sure MySQL server is running (check MySQL Workbench)
2. Open `backend/src/main/resources/application.properties`
3. Verify these lines:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=root
   ```
4. Make sure the `expense_tracker` database exists:
   ```sql
   SHOW DATABASES;
   ```
   If not visible, re-run `schema.sql`

---

### ❌ Problem: Port 8080 Already In Use
**Symptom:** `Web server failed to start. Port 8080 was already in use.`

**Fix (Windows):**
1. Open Command Prompt and run:
   ```
   netstat -ano | findstr :8080
   ```
2. Note the PID number at the end
3. Kill it:
   ```
   taskkill /PID <pid_number> /F
   ```
4. Re-run `mvnw.cmd spring-boot:run`

---

### ❌ Problem: Port 5173 Already In Use
**Symptom:** Vite says port 5173 is in use

**Fix:**
- Press Ctrl+C in the frontend terminal and run `npm run dev` again
- Or: Kill the process using Task Manager

---

### ❌ Problem: `mvnw.cmd` not found
**Symptom:** `'mvnw.cmd' is not recognized`

**Fix:**
1. Download Maven wrapper files or use Maven directly:
   ```
   mvn spring-boot:run
   ```
2. Make sure Maven is installed: `mvn -version`

---

### ❌ Problem: `npm install` fails
**Symptom:** Errors during `npm install`

**Fix:**
1. Make sure you are in the `frontend` folder: `cd PP/frontend`
2. Check Node.js is installed: `node -v` (should show v18+)
3. Delete `node_modules` folder and retry:
   ```
   rd /s /q node_modules
   npm install
   ```
