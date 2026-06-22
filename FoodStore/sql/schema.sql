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

CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    mail VARCHAR(150) NOT NULL,
    celular VARCHAR(20),
    contrasenia VARCHAR(255) NOT NULL,
    rol VARCHAR(50) NOT NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    estado VARCHAR(50) NOT NULL,
    total DOUBLE NOT NULL,
    forma_pago VARCHAR(50),
    usuario_id BIGINT NOT NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    subtotal DOUBLE NOT NULL,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    eliminado BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id),
    FOREIGN KEY (producto_id) REFERENCES productos(id)
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

INSERT INTO pedido (fecha, estado, total, forma_pago, usuario_id) VALUES
('2025-06-01', 'CONFIRMADO', 1500.00, 'EFECTIVO', 1),
('2025-06-03', 'PENDIENTE', 2300.50, 'TARJETA', 2),
('2025-06-05', 'TERMINADO', 800.00, 'TRANSFERENCIA', 1),
('2025-06-10', 'CANCELADO', 450.00, 'EFECTIVO', 3),
('2025-06-15', 'PENDIENTE', 3200.00, 'TARJETA', 2);

-- DETALLES DE PEDIDO
INSERT INTO detalle_pedido (cantidad, subtotal, pedido_id, producto_id) VALUES
(2, 600.00, 1, 1),
(1, 900.00, 1, 3),
(3, 1200.00, 2, 2),
(1, 1100.50, 2, 5),
(2, 800.00, 3, 4),
(1, 450.00, 4, 1),
(4, 2000.00, 5, 3),
(2, 1200.00, 5, 2);