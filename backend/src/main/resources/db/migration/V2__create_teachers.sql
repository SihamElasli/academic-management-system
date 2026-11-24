CREATE TABLE teachers (
                          id UUID PRIMARY KEY,
                          teacher_number VARCHAR(50) NOT NULL UNIQUE,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          specialization VARCHAR(150) NOT NULL
);