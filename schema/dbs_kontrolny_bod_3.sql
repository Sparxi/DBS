CREATE DATABASE  dbs;

CREATE TABLE students (
  ID      SERIAL  NOT NULL,
  name    VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email_address VARCHAR(255) NOT NULL,
  phone_number INTEGER,
  PRIMARY KEY(ID)
);

CREATE TABLE teachers (
  ID  SERIAL NOT NULL,
  name VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  email_address VARCHAR(255) NOT NULL,
  phone_number INTEGER,
  PRIMARY KEY(ID)
);

CREATE TABLE classes (
  ID  SERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  teacher_id SERIAL,
  PRIMARY KEY(ID),
  FOREIGN KEY(teacher_id) REFERENCES teachers
);

CREATE TABLE class_in_time (
  ID SERIAL NOT NULL,
  class_id SERIAL NOT NULL ,
  teacher_id SERIAL,
  date date,
  time VARCHAR(30),
  room VARCHAR(50),
  PRIMARY KEY (ID),
  FOREIGN KEY (class_id) REFERENCES classes,
  FOREIGN KEY (teacher_id) REFERENCES teachers
);

CREATE TABLE attendance (
  ID SERIAL NOT NULL,
  student_id SERIAL NOT NULL,
  class_in_time_id SERIAL NOT NULL,
  FOREIGN KEY (student_id) REFERENCES students,
  FOREIGN KEY (class_in_time_id) REFERENCES class_in_time
);

CREATE TABLE problems (
  ID SERIAL  NOT NULL,
  type_id  SMALLINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  content text NOT NULL,
  result real NOT NULL,
  max_points smallint,
  PRIMARY KEY(ID),
  FOREIGN KEY (type_id) REFERENCES problem_types
);

CREATE TABLE problems_students (
  ID SERIAL NOT NULL,
  student_id integer NOT NULL,
  problem_id integer NOT NULL,
  points smallint NOT NULL,
  PRIMARY KEY(ID),
  FOREIGN KEY (student_id) REFERENCES students,
  FOREIGN KEY (problem_id) REFERENCES problems,
  UNIQUE(student_id, problem_id)
);


CREATE TABLE problem_types (
  ID      smallserial  NOT NULL,
  name	  varchar(50) NOT NULL,
  PRIMARY KEY(ID)
);

