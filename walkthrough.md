# Walkthrough - Staff Management System

I have successfully built the complete, production-ready Staff Management System. Below is a summary of the components and how to use the system.

## Project Structure

The project follows a standard Spring Boot 3 layers architecture:
- **`com.staffms.model.entity`**: JPA Entities for all database tables.
- **`com.staffms.repository`**: Data access layer.
- **`com.staffms.service`**: Business logic layer.
- **`com.staffms.controller`**: Web controllers handling role-based requests.
- **`com.staffms.config`**: Security and MVC configuration.
- **`com.staffms.util`**: Enums, Data seeder, and helper classes.

## Key Features Implemented

### 1. Authentication & Security
- Role-based Access Control (RBAC) using Spring Security.
- Custom login page and success handler for role-based redirection.
- Permissions:
  - `SYSTEM_ADMIN`: Full access to everything including logs.
  - `HR_MANAGER`: Staff profiles, payroll, leave approval.
  - `DEPT_MANAGER`: Team view, performance evaluations.
  - `STAFF`: Personal profile, attendance, leave submissions.
  - `IT_SUPPORT`: Support ticket management.

### 2. Modules
- **Attendance**: Staff can check in/out daily via their dashboard.
- **Staff Profiles**: HR can manage detailed staff information linked to user accounts.
- **Leave Management**: Workflow for staff to request and HR/Managers to approve leaves.
- **Payroll**: Monthly generation based on base salary, allowances, and deductions.
- **Support Tickets**: Ticketing system for various categories (IT, HR, etc.).
- **Performance**: Rating and comment system for evaluations.
- **System Logs**: Auditing of all administrative actions.

## How to Run

1. **Database Setup**:
   - You can instantly start the database using Docker by running `docker-compose up -d` in the project root.
   - If using a manual MySQL installation (like XAMPP), ensure the MySQL Server is running on port `3306` with user `root` and password `root`. The configuration is handled in `application.properties`.
   - The provided [schema.sql](file:///d:/Y4S1/SE2030%20-%20Software%20Engineering/Senu%20Lanka%20Auto%20Park%20System/schema.sql) is automatically executed by the Docker container or can be utilized manually.

2. **Seed Data**:
   - On the first run, the system will automatically seed default accounts:
     - **Admin**: `admin / admin123`
     - **HR**: `hr_manager / hr123`
     - **Staff**: `staff / staff123`

3. **Build & Run (Using IntelliJ IDEA)**:
   - Open this directory in **IntelliJ IDEA**.
   - Make sure you configure your project settings to use **Java 17** (`File` > `Project Structure` > `SDK`).
   - Allow IntelliJ to sync the Maven dependencies automatically.
   - Select and run `StaffManagementSystemApplication` via the green Run button.
   - Once started, access the application at `http://localhost:8080`.

## UI/UX Highlights
- **Responsive Sidebar**: Navigation changes dynamically based on the logged-in user's role.
- **Clean Dashboards**: Summary cards for key stats (Total Users, Active Tickets, etc.).
- **Data Tables**: Sorted and searchable tables for large data lists.
- **Modern Design**: Powered by Bootstrap 5 and Inter font for a premium feel.
