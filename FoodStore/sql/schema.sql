-- schema.sql

CREATE DATABASE IF NOT EXISTS pedidos_db;
USE pedidos_db;

CREATE TABLE categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE productos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    precio DOUBLE NOT NULL,
    stock INT NOT NULL,
    imagen VARCHAR(255),
    disponible BOOLEAN NOT NULL DEFAULT TRUE,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    categoria_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

-- Datos de prueba: 2 categorías
INSERT INTO categoria (nombre, descripcion) VALUES ('Bebidas', 'Bebidas frías y calientes');
INSERT INTO categoria (nombre, descripcion) VALUES ('Comidas', 'Platos principales');

-- Datos de prueba: 4 productos (2 usan la categoría 1, 2 usan la categoría 2)
INSERT INTO productos (nombre, descripcion, precio, stock, imagen, disponible, categoria_id)
VALUES ('Coca-Cola 500ml', 'Bebida gaseosa', 1200.00, 50, 'coca.png', true, 1);

INSERT INTO productos (nombre, descripcion, precio, stock, imagen, disponible, categoria_id)
VALUES ('Agua mineral 500ml', 'Agua sin gas', 800.00, 80, 'agua.png', true, 1);

INSERT INTO productos (nombre, descripcion, precio, stock, imagen, disponible, categoria_id)
VALUES ('Hamburguesa clásica', 'Carne, lechuga, tomate y queso', 4500.00, 20, 'hamburguesa.png', true, 2);

INSERT INTO productos (nombre, descripcion, precio, stock, imagen, disponible, categoria_id)
VALUES ('Papas fritas', 'Porción individual', 2200.00, 35, 'papas.png', true, 2);