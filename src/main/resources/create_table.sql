CREATE TABLE users (
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (username)
);

CREATE TABLE user_roles (
    user_role_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    username VARCHAR(50) NOT NULL,
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_role_id),
    FOREIGN KEY (username) REFERENCES users(username)
);

CREATE TABLE courses (
    courseId VARCHAR(50) NOT NULL,
    courseName VARCHAR(50) NOT NULL,
    courseDescription VARCHAR(500) NOT NULL,
    PRIMARY KEY (courseId)
);

CREATE TABLE courses_lecturers (
    course_lecturer_record_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    courseId VARCHAR(50) NOT NULL,
    course_lecturer VARCHAR(50) NOT NULL,
    PRIMARY KEY (course_lecturer_record_id),
    FOREIGN KEY (courseId) REFERENCES courses(courseId),
    FOREIGN KEY (course_lecturer) REFERENCES users(username)
);

INSERT INTO users VALUES ('ken', '123');
INSERT INTO user_roles(username, role) VALUES ('ken', 'ROLE_STUDENT');

INSERT INTO users VALUES ('oliver', '123');
INSERT INTO user_roles(username, role) VALUES ('oliver', 'ROLE_TEACHER');

INSERT INTO users VALUES ('admin', '123');
INSERT INTO user_roles(username, role) VALUES ('admin', 'ROLE_ADMIN');

SELECT * FROM users;
SELECT * FROM user_roles;