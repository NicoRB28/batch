DROP TABLE IF EXISTS people;
DROP TABLE IF EXISTS users;

CREATE TABLE people (
    person_id INTEGER NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

CREATE TABLE users(
    user_id INTEGER NOT NULL PRIMARY KEY,
    first_name VARCHAR(20),
    last_name VARCHAR(20)
);

INSERT INTO users (user_id, first_name, last_name) VALUES (1,'Juan', 'Perez');
INSERT INTO users (user_id, first_name, last_name) VALUES (2,'Marcos', 'Bataglia');
INSERT INTO users (user_id, first_name, last_name) VALUES (3,'Lorena', 'Jacinto');
INSERT INTO users (user_id, first_name, last_name) VALUES (4,'Rodolfo', 'Berissa');
INSERT INTO users (user_id, first_name, last_name) VALUES (5,'Cholo', 'Coco');
INSERT INTO users (user_id, first_name, last_name) VALUES (6,'Aldo', 'Ceca');
INSERT INTO users (user_id, first_name, last_name) VALUES (7,'Fernanda', 'Rojas');
INSERT INTO users (user_id, first_name, last_name) VALUES (8, 'Julio', 'Anto');
INSERT INTO users (user_id, first_name, last_name) VALUES (9, 'Julia', 'Menendez');
INSERT INTO users (user_id, first_name, last_name) VALUES (10, 'Melina', 'Alo');
INSERT INTO users (user_id, first_name, last_name) VALUES (11,'Mateo', 'Zole');
INSERT INTO users (user_id, first_name, last_name) VALUES (12,'Ricardo', 'Trujo');
INSERT INTO users (user_id, first_name, last_name) VALUES (13, 'Joel', 'Tribiano');
INSERT INTO users (user_id, first_name, last_name) VALUES (14, 'Charlotte', 'Bong');
INSERT INTO users (user_id, first_name, last_name) VALUES (15, 'Chananandler', 'Bong');
INSERT INTO users (user_id, first_name, last_name) VALUES (16, 'Julieta', 'Venegas');
INSERT INTO users (user_id, first_name, last_name) VALUES (17, 'Marcelo', 'Tinelli');
INSERT INTO users (user_id, first_name, last_name) VALUES (18, 'Joaquin', 'Pajarin');
INSERT INTO users (user_id, first_name, last_name) VALUES (19, 'Belen', 'Frances');
INSERT INTO users (user_id, first_name, last_name) VALUES (20, 'Romina', 'Chulengo');
INSERT INTO users (user_id, first_name, last_name) VALUES (21, 'Son', 'Goku');
INSERT INTO users (user_id, first_name, last_name) VALUES (22, 'Chakie', 'Chan');
INSERT INTO users (user_id, first_name, last_name) VALUES (23, 'Bruce', 'Lee');
INSERT INTO users (user_id, first_name, last_name) VALUES (24, 'Martin', 'Lorenloren');
INSERT INTO users (user_id, first_name, last_name) VALUES (25, 'Donatto', 'Cuchina');
INSERT INTO users (user_id, first_name, last_name) VALUES (26, 'Elmo', 'Ron');
INSERT INTO users (user_id, first_name, last_name) VALUES (27, 'Eltar', 'Tamudo');
INSERT INTO users (user_id, first_name, last_name) VALUES (28, 'Eno', 'Miro');
INSERT INTO users (user_id, first_name, last_name) VALUES (29, 'Anakin', 'Sky');
INSERT INTO users (user_id, first_name, last_name) VALUES (30, 'Pepe', 'Wallker');
INSERT INTO users (user_id, first_name, last_name) VALUES (31, 'Joa', 'Vera');
INSERT INTO users (user_id, first_name, last_name) VALUES (32,'Felipe', 'Ortega');
INSERT INTO users (user_id, first_name, last_name) VALUES (33, 'Marcelo', 'Riva');
INSERT INTO users (user_id, first_name, last_name) VALUES (34, 'Karol', 'Ge');
INSERT INTO users (user_id, first_name, last_name) VALUES (35, 'Ele', 'Granola');
INSERT INTO users (user_id, first_name, last_name) VALUES (36, 'Roberto', 'Prece');
INSERT INTO users (user_id, first_name, last_name) VALUES (37, 'Miguel', 'Nouno');
INSERT INTO users (user_id, first_name, last_name) VALUES (38, 'Javier', 'Dosto');
INSERT INTO users (user_id, first_name, last_name) VALUES (39, 'Dimitri', 'Kara');
INSERT INTO users (user_id, first_name, last_name) VALUES (40, 'Alexei', 'Kara');
INSERT INTO users (user_id, first_name, last_name) VALUES (41, 'Ivan', 'Kara');
INSERT INTO users (user_id, first_name, last_name) VALUES (42, 'Pablo', 'Kara');
