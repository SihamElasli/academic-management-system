CREATE TABLE students (
                          id UUID PRIMARY KEY,
                          student_number VARCHAR(50) NOT NULL UNIQUE,
                          first_name VARCHAR(255) NOT NULL,
                          last_name VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL
);