CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    salary DOUBLE NOT NULL,
    hire_date DATE NOT NULL,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Insert some initial employees for testing
INSERT INTO employees (name, email, salary, hire_date, department_id) 
VALUES ('Ahmed Moshen', 'Ahmed@test.com', 50000.0, '2023-01-15', 1);

INSERT INTO employees (name, email, salary, hire_date, department_id) 
VALUES ('Ali Taha', 'AlyTaha@test.com', 60000.0, '2022-11-20', 2);

INSERT INTO employees (name, email, salary, hire_date, department_id) 
VALUES ('Mariam Mohamed', 'MariamMohamed@test.com', 55000.0, '2023-03-10', 3);

INSERT INTO employees (name, email, salary, hire_date, department_id) 
VALUES ('Manar Elsayed', 'MElsayed@test.com', 65000.0, '2022-09-05', 4);