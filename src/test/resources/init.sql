DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS roles;
CREATE TABLE IF NOT EXISTS roles (id SERIAL PRIMARY KEY, role TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS users (id SERIAL PRIMARY KEY, name TEXT NOT NULL, role_id INT, CONSTRAINT role_id FOREIGN KEY (role_id) REFERENCES roles(id));

INSERT INTO roles (role) VALUES ('ADMIN_ROLE');
INSERT INTO roles (role) VALUES ('MEMBER_ROLE');
INSERT INTO roles (role) VALUES ('SUPER_ROLE');
INSERT INTO roles (role) VALUES ('GIGA_ROLE');

INSERT INTO users (name, role_id) VALUES ('admin', 1);
INSERT INTO users (name, role_id) VALUES ('member', 2);
INSERT INTO users (name, role_id) VALUES ('Kotoka Torahime', 2);
INSERT INTO users (name, role_id) VALUES ('Zaion Lanza', 2);
INSERT INTO users (name, role_id) VALUES ('Layla Alstromeria', 2);
INSERT INTO users (name, role_id) VALUES ('Finana Ryuugu', 2);
INSERT INTO users (name, role_id) VALUES ('Mori Calliope', 2);
INSERT INTO users (name, role_id) VALUES ('Miori Celesta', 4);
INSERT INTO users (name, role_id) VALUES ('Elira Pendora', 2);
INSERT INTO users (name, role_id) VALUES ('Maria Marionette', 2);



