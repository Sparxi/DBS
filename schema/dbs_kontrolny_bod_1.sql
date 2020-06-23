CREATE DATABASE  dbs;

CREATE TABLE students (
  ID      SERIAL  NOT NULL,
  name    VARCHAR(50) NOT NULL,
  password VARCHAR(30) NOT NULL,
  email_address VARCHAR(50) NOT NULL,
  phone_number INTEGER,
  PRIMARY KEY(ID)
);

CREATE TABLE teachers (
  ID  SERIAL NOT NULL,
  name VARCHAR(50) NOT NULL,
  password VARCHAR(30) NOT NULL,
  email_address VARCHAR(50) NOT NULL,
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

