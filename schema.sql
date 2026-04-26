-- Database Schema for Staff Management System

CREATE DATABASE IF NOT EXISTS staff_mgmt_db;
USE staff_mgmt_db;

-- 1. users table
CREATE TABLE IF NOT EXISTS users (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('SYSTEM_ADMIN', 'HR_MANAGER', 'DEPT_MANAGER', 'STAFF', 'IT_SUPPORT', 'ORG_MANAGEMENT') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. departments table (needed for staff_profiles FK)
CREATE TABLE IF NOT EXISTS departments (
    dept_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
    manager_id BIGINT,
    FOREIGN KEY (manager_id) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 2. staff_profiles table
CREATE TABLE IF NOT EXISTS staff_profiles (
    profile_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    nic VARCHAR(20) NOT NULL UNIQUE,
    phone VARCHAR(20),
    address TEXT,
    department_id BIGINT,
    job_title VARCHAR(100),
    employment_type ENUM('FULL_TIME', 'PART_TIME', 'CONTRACT', 'INTERN'),
    date_joined DATE,
    salary_base DECIMAL(10, 2),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES departments(dept_id) ON DELETE SET NULL
);

-- 4. attendance table
CREATE TABLE IF NOT EXISTS attendance (
    attendance_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    date DATE NOT NULL,
    check_in TIME,
    check_out TIME,
    status ENUM('PRESENT', 'ABSENT', 'LATE', 'ON_LEAVE') DEFAULT 'PRESENT',
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 5. leave_requests table
CREATE TABLE IF NOT EXISTS leave_requests (
    leave_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    leave_type ENUM('ANNUAL', 'SICK', 'CASUAL', 'MATERNITY') NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by BIGINT,
    reviewed_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (reviewed_by) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 6. payroll table
CREATE TABLE IF NOT EXISTS payroll (
    payroll_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    month INT NOT NULL,
    year INT NOT NULL,
    basic_salary DECIMAL(10, 2) NOT NULL,
    allowances DECIMAL(10, 2) DEFAULT 0,
    deductions DECIMAL(10, 2) DEFAULT 0,
    net_salary DECIMAL(10, 2) NOT NULL,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 7. performance table
CREATE TABLE IF NOT EXISTS performance (
    eval_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    evaluated_by BIGINT NOT NULL,
    period VARCHAR(50) NOT NULL,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (evaluated_by) REFERENCES users(user_id) ON DELETE CASCADE
);

-- 8. support_tickets table
CREATE TABLE IF NOT EXISTS support_tickets (
    ticket_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    raised_by BIGINT NOT NULL,
    category ENUM('HR', 'IT', 'PAYROLL', 'OTHER') NOT NULL,
    subject VARCHAR(255) NOT NULL,
    description TEXT,
    status ENUM('OPEN', 'IN_PROGRESS', 'ESCALATED', 'RESOLVED') DEFAULT 'OPEN',
    assigned_to BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (raised_by) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_to) REFERENCES users(user_id) ON DELETE SET NULL
);

-- 9. system_logs table
CREATE TABLE IF NOT EXISTS system_logs (
    log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    action VARCHAR(255) NOT NULL,
    entity VARCHAR(50),
    entity_id BIGINT,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);
