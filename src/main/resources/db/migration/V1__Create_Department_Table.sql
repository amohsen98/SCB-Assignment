CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- Insert some initial departments for testing
INSERT INTO departments (name) VALUES ('HR');
INSERT INTO departments (name) VALUES ('IT');
INSERT INTO departments (name) VALUES ('Finance');
INSERT INTO departments (name) VALUES ('Marketing');