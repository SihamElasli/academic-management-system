CREATE TABLE courses (
                         id UUID PRIMARY KEY,

                         course_code VARCHAR(50) NOT NULL UNIQUE,

                         title VARCHAR(150) NOT NULL,

                         description TEXT,

                         credits INTEGER NOT NULL CHECK (credits > 0),

                         capacity INTEGER NOT NULL CHECK (capacity > 0),

                         teacher_id UUID NOT NULL,

                         active BOOLEAN NOT NULL DEFAULT TRUE,

                         CONSTRAINT fk_course_teacher
                             FOREIGN KEY (teacher_id)
                                 REFERENCES teachers(id)
                                 ON DELETE RESTRICT
);

CREATE INDEX idx_courses_teacher_id
    ON courses(teacher_id);