-- ============================================================
-- Scholarship & Financial Aid Management Platform
-- Database Schema  (MySQL 8+ / Railway)
-- Target database: railway  (set in datasource URL)
-- ============================================================

-- ── Users ────────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS users (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name          VARCHAR(50)  NOT NULL,
    last_name           VARCHAR(50)  NOT NULL,
    email               VARCHAR(100) NOT NULL UNIQUE,
    password            VARCHAR(255) NOT NULL,
    phone               VARCHAR(20),
    address             TEXT,
    institution         VARCHAR(200),
    major               VARCHAR(100),
    gpa                 DOUBLE,
    bio                 TEXT,
    role                ENUM('STUDENT','ADMIN') NOT NULL DEFAULT 'STUDENT',
    status              ENUM('ACTIVE','SUSPENDED','DELETED') NOT NULL DEFAULT 'ACTIVE',
    reset_token         VARCHAR(255),
    reset_token_expiry  DATETIME,
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email     (email),
    INDEX idx_role      (role),
    INDEX idx_status    (status)
) ENGINE=InnoDB;

-- ── Scholarships ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS scholarships (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    title               VARCHAR(255) NOT NULL,
    provider            VARCHAR(255) NOT NULL,
    category            VARCHAR(50),
    amount              DECIMAL(12,2),
    deadline            DATE,
    description         TEXT,
    eligibility         TEXT,
    gpa_requirement     DOUBLE,
    seats               INT,
    website             VARCHAR(500),
    renewable           BOOLEAN NOT NULL DEFAULT FALSE,
    renewal_criteria    VARCHAR(500),
    status              ENUM('ACTIVE','INACTIVE','DRAFT','EXPIRED') NOT NULL DEFAULT 'ACTIVE',
    created_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_status    (status),
    INDEX idx_category  (category),
    INDEX idx_deadline  (deadline)
) ENGINE=InnoDB;

-- ── Applications ─────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS applications (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id          BIGINT NOT NULL,
    scholarship_id      BIGINT NOT NULL,
    status              ENUM('SUBMITTED','UNDER_REVIEW','DOCUMENTS_REQUIRED','APPROVED','REJECTED','WITHDRAWN')
                            NOT NULL DEFAULT 'SUBMITTED',
    full_name           VARCHAR(150),
    date_of_birth       VARCHAR(20),
    phone               VARCHAR(20),
    address             TEXT,
    city                VARCHAR(100),
    state               VARCHAR(100),
    zip_code            VARCHAR(20),
    institution         VARCHAR(200),
    major               VARCHAR(100),
    gpa                 DOUBLE,
    graduation_year     INT,
    current_year        VARCHAR(50),
    statement           TEXT,
    financial_need      TEXT,
    achievements        TEXT,
    admin_notes         TEXT,
    reviewed_by         VARCHAR(100),
    reviewed_at         DATETIME,
    applied_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uq_student_scholarship (student_id, scholarship_id),
    CONSTRAINT fk_app_student     FOREIGN KEY (student_id)     REFERENCES users(id)        ON DELETE CASCADE,
    CONSTRAINT fk_app_scholarship FOREIGN KEY (scholarship_id) REFERENCES scholarships(id) ON DELETE CASCADE,
    INDEX idx_status     (status),
    INDEX idx_student    (student_id),
    INDEX idx_scholarship(scholarship_id)
) ENGINE=InnoDB;

-- ── Saved Scholarships ───────────────────────────────────────
CREATE TABLE IF NOT EXISTS saved_scholarships (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    scholarship_id  BIGINT NOT NULL,
    saved_at        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uq_user_scholarship (user_id, scholarship_id),
    CONSTRAINT fk_saved_user         FOREIGN KEY (user_id)        REFERENCES users(id)        ON DELETE CASCADE,
    CONSTRAINT fk_saved_scholarship  FOREIGN KEY (scholarship_id) REFERENCES scholarships(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ── Financial Aids ───────────────────────────────────────────
CREATE TABLE IF NOT EXISTS financial_aids (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    title           VARCHAR(255) NOT NULL,
    category        VARCHAR(50)  NOT NULL,
    amount          DECIMAL(12,2),
    max_applicants  INT,
    deadline        DATE,
    description     TEXT,
    status          ENUM('ACTIVE','INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at      DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status)
) ENGINE=InnoDB;

-- ── Documents ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS documents (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id  BIGINT NOT NULL,
    file_name       VARCHAR(255) NOT NULL,
    file_path       VARCHAR(500) NOT NULL,
    file_type       VARCHAR(100),
    file_size       BIGINT,
    document_type   ENUM('TRANSCRIPT','RECOMMENDATION_LETTER','ID_PROOF',
                         'FINANCIAL_STATEMENT','PERSONAL_STATEMENT',
                         'ACHIEVEMENT_CERTIFICATE','OTHER') DEFAULT 'OTHER',
    uploaded_at     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_doc_application FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ── Notifications ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS notifications (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id     BIGINT NOT NULL,
    message     VARCHAR(1000) NOT NULL,
    type        ENUM('INFO','SUCCESS','WARNING','ERROR') DEFAULT 'INFO',
    is_read     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_notif_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_read (user_id, is_read)
) ENGINE=InnoDB;

-- ============================================================
-- Seed Data
-- ============================================================

-- Default admin  (password: admin123)
INSERT IGNORE INTO users (first_name, last_name, email, password, role, status) VALUES
('Admin', 'User', 'admin@scholar.com',
 '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LeAiZ9Q6nvmkrSbxK', 'ADMIN', 'ACTIVE');

-- Default student  (password: student123)
INSERT IGNORE INTO users (first_name, last_name, email, password, role, status) VALUES
('Student', 'Demo', 'student@scholar.com',
 '$2a$12$4yExmOr.3oYDVpL/DcNSbeEhJCbKH7QT0GE5n7P3PNLygFELv0LWC', 'STUDENT', 'ACTIVE');

-- Sample Scholarships
INSERT IGNORE INTO scholarships (title, provider, category, amount, deadline, description, gpa_requirement, seats, renewable, status) VALUES
('National Merit Scholarship Program', 'National Merit Scholarship Corp', 'STEM', 8000.00, '2025-06-30',
 'For academically talented students who demonstrate exceptional ability and potential.', 3.5, 200, TRUE, 'ACTIVE'),
('STEM Excellence Award', 'Tech Foundation USA', 'STEM', 12000.00, '2025-05-15',
 'Supporting the next generation of science and technology leaders.', 3.7, 100, FALSE, 'ACTIVE'),
('Creative Arts Grant', 'National Arts Council', 'Arts', 5000.00, '2025-07-20',
 'Empowering creative minds to pursue artistic careers.', 3.0, 150, FALSE, 'ACTIVE'),
('Future Business Leaders Fund', 'Chamber of Commerce', 'Business', 10000.00, '2025-08-01',
 'Investing in future business leaders who show entrepreneurial spirit.', NULL, 75, TRUE, 'ACTIVE'),
('Healthcare Heroes Scholarship', 'National Health Foundation', 'Healthcare', 15000.00, '2025-05-31',
 'For aspiring healthcare professionals committed to patient care.', 3.6, 50, TRUE, 'ACTIVE'),
('Engineering Innovation Award', 'Society of Engineers', 'Engineering', 20000.00, '2025-09-15',
 'Recognizing exceptional engineering students who develop innovative solutions.', 3.8, 25, FALSE, 'ACTIVE');

-- Sample Financial Aids
INSERT IGNORE INTO financial_aids (title, category, amount, max_applicants, deadline, description, status) VALUES
('Emergency Financial Aid', 'Emergency', 2000.00, 100, '2025-12-31',
 'For students facing unexpected financial hardship.', 'ACTIVE'),
('Work-Study Program', 'Work-Study', 5000.00, 200, '2025-08-01',
 'Combines part-time employment with financial assistance.', 'ACTIVE'),
('Federal Pell Grant', 'Grant', 7500.00, 500, '2025-06-30',
 'Need-based federal aid for undergraduate students.', 'ACTIVE');
