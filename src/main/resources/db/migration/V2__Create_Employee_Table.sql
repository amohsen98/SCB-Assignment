CREATE TABLE employees (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    salary DOUBLE NOT NULL,
    hire_date DATE NOT NULL,
    department_id BIGINT NOT NULL,
    FOREIGN KEY (department_id) REFERENCES departments(id)
);

-- Insert some initial employees for testing with hashed passwords (BCrypt hash of 'password')
INSERT INTO employees (name, email, password, role, salary, hire_date, department_id) 
VALUES ('Ahmed Moshen', 'Ahmed@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN', 50000.0, '2023-01-15', 1);

INSERT INTO employees (name, email, password, role, salary, hire_date, department_id) 
VALUES ('Ali Taha', 'AlyTaha@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'ADMIN', 60000.0, '2022-11-20', 2);

INSERT INTO employees (name, email, password, role, salary, hire_date, department_id) 
VALUES ('Mariam Mohamed', 'MariamMohamed@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', 55000.0, '2023-03-10', 3);

INSERT INTO employees (name, email, password, role, salary, hire_date, department_id) 
VALUES ('Manar Elsayed', 'MElsayed@test.com', '$2a$10$dXJ3SW6G7P50lGmMkkmwe.20cQQubK3.HZWzG3YB1tlRy.fqvM/BG', 'USER', 65000.0, '2022-09-05', 4);
