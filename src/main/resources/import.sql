-- PERMISSIONS
INSERT INTO permissions (name) VALUES ('READ_USER');
INSERT INTO permissions (name) VALUES ('CREATE_USER');
INSERT INTO permissions (name) VALUES ('UPDATE_USER');
INSERT INTO permissions (name) VALUES ('DELETE_USER');
INSERT INTO permissions (name) VALUES ('READ_ADMIN');
INSERT INTO permissions (name) VALUES ('FULL_ACCESS');

-- ROLES
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- ROLE_PERMISSIONS
INSERT INTO roles_permissions (role_id, permission_id) VALUES (1, 1); -- ROLE_USER → READ_USER

-- ROLE_ADMIN → todos los permisos
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 1);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 2);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 3);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 4);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 5);
INSERT INTO roles_permissions (role_id, permission_id) VALUES (2, 6);

-- USERS
-- Contraseñas bcrypt: "userpass" y "adminpass" (por seguridad, cámbialas en producción)
INSERT INTO users (username, password, enabled) VALUES ('user1', '$2a$10$abcdefghijklmnopqrstuvwxysabcdefghijklmno', TRUE); -- dummy
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$zyxwvutsrqponmlkjihgfedcbaqwertyuiopasdfgh', TRUE); -- dummy

-- USERS_ROLES
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1); -- user1 → ROLE_USER
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2); -- admin → ROLE_ADMIN
