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

CREATE TABLE problem_types (
  ID      smallserial  NOT NULL,
  name	  varchar(50) NOT NULL,
  PRIMARY KEY(ID)
);

INSERT INTO problem_types (id, name) VALUES (0, 'Zlomky');
INSERT INTO problem_types (name) VALUES ('Slovne ulohy');
INSERT INTO problem_types (name) VALUES ('Nasobenie');
INSERT INTO problem_types (name) VALUES ('Linearne rovnice');
INSERT INTO problem_types (name) VALUES ('Nerovnice');
INSERT INTO problem_types (name) VALUES ('Scitovanie a odcitovanie');
INSERT INTO problem_types (name) VALUES ('Priama a nepriama umernost');
INSERT INTO problem_types (name) VALUES ('Funkcie');

INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z0', '1', 0, 1);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z1', '2', 0, 2);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z2', '3', 0, 3);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z3', '4', 0, 4);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z4', '5', 0, 5);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z5', '6', 0, 6);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z6', '7', 0, 7);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z7', '8', 0, 8);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z8', '9', 0, 9);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z9', '10', 0, 10);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z10', '11', 0, 11);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z11', '12', 0, 12);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z12', '13', 0, 13);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z13', '14', 0, 14);
INSERT INTO problems (type_id, name, content, result, max_points) VALUES (1, 'Z14', '15', 0, 15);

SELECT * FROM problem_types;

DROP TABLE problems;




