CREATE TABLE daily_summary (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    summary_date DATE NOT NULL,
    department_id BIGINT NOT NULL,
    employee_count INT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);