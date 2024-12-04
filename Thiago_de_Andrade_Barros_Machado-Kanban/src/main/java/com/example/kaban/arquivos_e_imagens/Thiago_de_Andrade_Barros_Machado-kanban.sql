CREATE DATABASE IF NOT EXISTS kanban;
USE kanban;

CREATE TABLE usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE task (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    description TEXT,
    sector VARCHAR(255),
    priority ENUM('BAIXA', 'MEDIA', 'ALTA'),
    created_at DATETIME NOT NULL,
    status ENUM('A_FAZER', 'FAZENDO', 'PRONTO'),
    FOREIGN KEY (user_id) REFERENCES usuario(id) ON DELETE CASCADE
);
