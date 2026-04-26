You are a senior full-stack Java developer. Build a complete, production-ready
Web-Based Staff Management System using the following tech stack and requirements.

=============================================================
TECH STACK
=============================================================

- Backend: Java (Spring Boot 3.x)
- Frontend: Thymeleaf (server-side templating) + Bootstrap 5
- Database: MySQL 8.x
- ORM: Spring Data JPA + Hibernate
- Security: Spring Security (role-based access control)
- Build: Maven
- Auth: Session-based login with BCrypt password hashing

=============================================================
DATABASE SCHEMA — Create these tables in MySQL
=============================================================

1. users (user_id, username, password_hash, email, role, is_active, created_at)
2. staff_profiles (profile_id, user_id FK, full_name, nic, phone, address, department_id FK,
   job_title, employment_type, date_joined, salary_base)
3. departments (dept_id, dept_name, manager_id FK)
4. attendance (attendance_id, user_id FK, date, check_in, check_out, status)
5. leave_requests(leave_id, user_id FK, leave_type, start_date, end_date, reason,
   status[PENDING/APPROVED/REJECTED], reviewed_by FK, reviewed_at)
6. payroll (payroll_id, user_id FK, month, year, basic_salary, allowances,
   deductions, net_salary, generated_at)
7. performance (eval_id, user_id FK, evaluated_by FK, period, rating[1-5],
   comments, created_at)
8. support_tickets(ticket_id, raised_by FK, category[HR/IT/PAYROLL/OTHER],
   subject, description, status[OPEN/IN_PROGRESS/ESCALATED/RESOLVED],
   assigned_to FK, created_at, updated_at)
9. system_logs (log_id, user_id FK, action, entity, entity_id, timestamp, ip_address)

=============================================================
USER ROLES & ACCESS CONTROL
=============================================================
Roles: SYSTEM_ADMIN, HR_MANAGER, DEPT_MANAGER, STAFF, IT_SUPPORT, ORG_MANAGEMENT

Role Permissions:

- SYSTEM_ADMIN → Full access: user CRUD, roles, system config, all logs
- HR_MANAGER → Staff profiles, leave approval, payroll, attendance reports
- DEPT_MANAGER → View team attendance, approve team leave, performance evals
- STAFF → View own profile, submit leave, view attendance, raise tickets
- IT_SUPPORT → View/manage/escalate IT tickets, access system diagnostics
- ORG_MANAGEMENT → Read-only analytics: payroll summaries, performance, attendance trends

=============================================================
MODULES TO BUILD (with full CRUD)
=============================================================

MODULE 1 — Authentication & User Management

- Login / Logout with Spring Security
- Role-based redirect after login (each role → its own dashboard)
- Admin: Create, Read, Update, Delete users; assign roles
- Password reset flow

MODULE 2 — Staff Profile Management (HR_MANAGER, SYSTEM_ADMIN)

- Add new staff (linked to user account)
- View, edit, delete staff profiles
- Filter by department, employment type

MODULE 3 — Attendance Management

- Staff: Daily check-in / check-out
- HR/Admin: View attendance history per employee
- Generate attendance reports (by department, date range)
- Export to CSV

MODULE 4 — Leave Management

- Staff: Submit leave request form (type, dates, reason)
- HR_MANAGER / DEPT_MANAGER: Approve or Reject with comments
- Staff: View own leave history and status
- Leave balance tracking per employee

MODULE 5 — Payroll Management (HR_MANAGER)

- Add/update salary records per staff
- Generate monthly payroll (auto-calculate net = basic + allowances - deductions)
- View payroll history per employee
- Generate payroll report (monthly summary)

MODULE 6 — Performance Management (DEPT_MANAGER)

- Submit performance evaluation for team members
- Rating 1-5 with written comments
- View historical evaluations per employee
- Department performance summary

MODULE 7 — Ticket / Support Management

- Staff: Submit support ticket (category, subject, description)
- IT_SUPPORT / HR_MANAGER: View open tickets, update status, escalate
- Status flow: OPEN → IN_PROGRESS → RESOLVED (or ESCALATED)
- Assign ticket to a specific support officer

MODULE 8 — Reports & Analytics (HR_MANAGER, ORG_MANAGEMENT, SYSTEM_ADMIN)

- Attendance trend report (by dept, by month)
- Leave summary report
- Payroll cost report (monthly/annual)
- Performance analytics (avg ratings by dept)
- Staff count and department breakdown

MODULE 9 — System Configuration & Logs (SYSTEM_ADMIN)

- Configure system settings (org name, leave types, dept list)
- View full system activity logs (who did what, when)
- Access diagnostics page (DB status, active sessions)

=============================================================
UI/UX REQUIREMENTS
=============================================================

- Responsive Bootstrap 5 layout
- Sidebar navigation (different menu per role)
- Dashboard home page per role showing key stats/cards:
  - Admin: total users, active tickets, system health
  - HR: pending leaves, today's attendance, payroll due
  - Staff: my attendance today, leave balance, open tickets
  - Manager: team attendance %, pending evaluations
- Data tables with search, sort, and pagination (use DataTables.js)
- Flash messages for success/error feedback
- Confirmation dialogs before delete actions
- Form validation (client-side + server-side)

=============================================================
PROJECT STRUCTURE (Spring Boot)
=============================================================
src/
└─ main/
├─ java/com/staffms/
│ ├─ config/ (SecurityConfig, WebMvcConfig)
│ ├─ controller/ (one per module)
│ ├─ service/ (one per module)
│ ├─ repository/ (JPA Repositories)
│ ├─ model/entity/ (JPA Entities)
│ ├─ model/dto/ (Data Transfer Objects)
│ └─ util/ (helpers, enums)
└─ resources/
├─ templates/ (Thymeleaf HTML per module)
├─ static/ (CSS, JS, images)
└─ application.properties

=============================================================
application.properties CONFIGURATION
=============================================================
spring.datasource.url=jdbc:mysql://localhost:3306/staff_mgmt_db
spring.datasource.username=root
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.thymeleaf.cache=false
server.port=8080

=============================================================
SEED DATA — Insert on first run
=============================================================

- 1 default SYSTEM_ADMIN user: admin / admin123
- 3 departments: Engineering, HR, Operations
- 5 sample staff members across departments
- Sample leave types: Annual, Sick, Casual, Maternity

=============================================================
DELIVERABLES — Generate in this order:
=============================================================

1. SQL schema script (schema.sql)
2. pom.xml with all dependencies
3. application.properties
4. All Entity classes with JPA annotations
5. All Repository interfaces
6. All Service classes with business logic
7. All Controller classes with request mappings
8. SecurityConfig with role-based URL protection
9. All Thymeleaf HTML templates (with Bootstrap 5)
10. Data seeder / CommandLineRunner for initial data

Build each module completely with no placeholders.
Every form must submit, every button must work, every
role must see only what they are permitted to see.
