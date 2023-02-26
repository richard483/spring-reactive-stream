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
INSERT INTO users (name, role_id) VALUES ('super', 3);
INSERT INTO users (name, role_id) VALUES ('giga', 4);
INSERT INTO users (name, role_id) VALUES ('andi', 2);
INSERT INTO users (name, role_id) VALUES ('budi', 2);
INSERT INTO users (name, role_id) VALUES ('caca', 2);
INSERT INTO users (name, role_id) VALUES ('didi', 2);
INSERT INTO users (name, role_id) VALUES ('efi', 2);
INSERT INTO users (name, role_id) VALUES ('fifi', 2);
INSERT INTO users (name, role_id) VALUES ('gigi', 2);
INSERT INTO users (name, role_id) VALUES ('haha', 2);
INSERT INTO users (name, role_id) VALUES ('ihi', 2);
INSERT INTO users (name, role_id) VALUES ('jiji', 2);
INSERT INTO users (name, role_id) VALUES ('kiki', 2);
INSERT INTO users (name, role_id) VALUES ('lili', 2);
INSERT INTO users (name, role_id) VALUES ('mimi', 2);
INSERT INTO users (name, role_id) VALUES ('nini', 2);
INSERT INTO users (name, role_id) VALUES ('ono', 2);
INSERT INTO users (name, role_id) VALUES ('papa', 2);
INSERT INTO users (name, role_id) VALUES ('qiqi', 2);
INSERT INTO users (name, role_id) VALUES ('riri', 2);
INSERT INTO users (name, role_id) VALUES ('sisi', 2);
INSERT INTO users (name, role_id) VALUES ('titi', 2);
INSERT INTO users (name, role_id) VALUES ('uwi', 2);
INSERT INTO users (name, role_id) VALUES ('vivi', 2);
INSERT INTO users (name, role_id) VALUES ('wawa', 2);
INSERT INTO users (name, role_id) VALUES ('xixi', 2);
INSERT INTO users (name, role_id) VALUES ('yaya', 2);
INSERT INTO users (name, role_id) VALUES ('zizi', 2);



