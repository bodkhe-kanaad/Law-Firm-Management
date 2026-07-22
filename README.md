# Legal Case Management System
## Law Firm Database Project

A JavaFX desktop application for managing law firm data including court cases, clients, lawyers, invoices, hearings, and evidence. Backed by an Oracle SQL database hosted on UBC's CS department servers.

---

## Tech Stack

- **Language:** Java 21
- **GUI Framework:** JavaFX 21
- **Database:** Oracle SQL (UBC CS department server)
- **Build Tool:** Maven

---

## Prerequisites

Before running the app you need:

1. **Java 21** installed on your machine
2. **Maven** installed on your machine
3. **UBC CS account (CWL)** with access to the Oracle database
4. An active **SSH tunnel** to the UBC database server (see setup below)

---

## Database Setup

### Step 1 — Update database credentials

Open `src/main/java/backend/db/DatabaseConnection.java` and update the credentials to match your own Oracle account:

```java
private static final String USERNAME = "ora_YOUR_CWL";
private static final String PASSWORD = "aYOUR_STUDENT_NUMBER";
```

---

### Step 2 — Upload setup.sql to the server

Run this from your **local machine** (not inside SSH). Navigate to the project root first:

```bash
cd path/to/team_90-4
scp src/main/java/backend/db/setup.sql YOUR_CWL@remote.students.cs.ubc.ca:~/setup.sql
```

---

### Step 3 — SSH into the UBC remote server and load the data

Open a terminal and connect to the UBC CS remote server:

```bash
ssh YOUR_CWL@remote.students.cs.ubc.ca
```

Find your home directory path (you will need this below):

```bash
echo ~
```

Connect to the Oracle database:

```bash
sqlplus ora_YOUR_CWL/aYOUR_STUDENT_NUMBER@dbhost.students.cs.ubc.ca:1522/stu
```

At the `SQL>` prompt, run setup.sql using the path from `echo ~` above:

```sql
@/home/FIRST_LETTER/YOUR_CWL/setup.sql
```

For example, if your CWL is `mchoubey`: `@/home/m/mchoubey/setup.sql`

Verify the data loaded correctly:

```sql
SELECT COUNT(*) FROM Court_Case;
```

This should return `12`.

Type `exit` to leave sqlplus when done.

---

### Step 4 — Open the SSH tunnel (required to run the app)

The app runs on your **local machine** but connects to the UBC database through an SSH tunnel. Open a terminal and run:

```bash
ssh -l YOUR_CWL -L localhost:1522:dbhost.students.cs.ubc.ca:1522 remote.students.cs.ubc.ca
```

Enter your CWL password when prompted. **Keep this terminal open the entire time you are using the app.** Closing it will cut the database connection.

---

## Running the App

Open a second terminal (keep the SSH tunnel terminal open), navigate to the project root, and run:

```bash
cd path/to/team_90-4
mvn clean javafx:run
```

The application window will open after a few seconds.

---

## Application Features

The app has 5 modules accessible from the landing screen:

### 1. Court Case Management
Full CRUD interface for the `Court_Case` table.
- View all cases in a live table
- Insert a new case (Case ID, Type, Client ID, Lawyer ID)
- Update an existing case (click a row to pre-fill the Case ID)
- Delete a case by ID

### 2. Case Search (Selection)
Dynamic query builder for filtering court cases.
- Add multiple filter conditions using AND / OR
- Supported attributes: `case_id`, `case_type`, `client_id`, `lawyer_id`, `is_successful`, `closing_date`
- Supported operators: `=`, `<`, `>`, `<=`, `>=`, `<>`, `LIKE`, `IS NULL`, `IS NOT NULL`
- User-friendly guided examples shown for each attribute

### 3. Invoice View (Projection)
Choose which columns of the `Invoice` table to display.
- Check/uncheck columns: `invoice_id`, `amount`, `case_id`, `status_code`
- Reorder selected columns with Up/Down buttons
- Results update on demand

### 4. Case-Lawyer Lookup (Join)
Joins `Court_Case`, `Client`, and `Lawyer` to show case details with names.
- Select a case type from a dropdown (`Family`, `Criminal`, `Corporate`) to avoid case-sensitivity errors
- Returns: Case ID, Type, Opening Date, Closing Date, Client Name, Lawyer Name

### 5. Lawyer Workload Analytics
Six pre-built analytical queries over lawyer and case data:
- **Total Hours per Lawyer** — GROUP BY aggregation, ordered by hours descending
- **Lawyers on Multiple Cases** — HAVING clause, lawyers with more than one distinct case
- **Lawyers Above Average Hours** — Nested aggregation, lawyers at or above the average total hours
- **Lawyers with Work Logged on Every Assigned Case** — Relational division using `MINUS` inside `NOT EXISTS`
- **Hearing Schedule** — Join across Court_Case, Hearing, and CourtHouse showing all hearings with courthouse location
- **Evidence per Case** — Join across Evidence and Presented showing all evidence items with the hearing they were presented at

---

## Project Structure

```
src/
└── main/
    ├── java/backend/
    │   ├── LawFirmApplication.java       # Entry point
    │   ├── controller/                   # JavaFX screen controllers
    │   ├── repository/                   # SQL queries (JDBC)
    │   ├── model/                        # Data classes
    │   └── db/
    │       ├── DatabaseConnection.java   # Oracle JDBC connection
    │       └── setup.sql                 # Schema + seed data
    └── resources/                        # FXML layout files
```

---

## Troubleshooting

**App opens but tables are empty**
- The SSH tunnel is not running. Open it in a separate terminal (see Step 2 above).

**`mvn clean javafx:run` fails with a build error**
- Make sure you have Java 21 and Maven installed. Run `java -version` and `mvn -version` to verify.

**`ORA-12162` error in sqlplus**
- You need to specify the full connection string: `sqlplus ora_CWL/aSTUDENTNUM@dbhost.students.cs.ubc.ca:1522/stu`

**Data missing after setup.sql**
- Run `COMMIT;` in sqlplus after pasting the setup file. Oracle does not auto-commit DDL inserts in all configurations.
