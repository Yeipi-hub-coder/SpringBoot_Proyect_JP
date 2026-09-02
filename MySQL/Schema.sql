CREATE DATABASE IF NOT EXISTS logitrack_db;

USE logitrack_db;

drop table if exists auditoria;
drop table if exists movimiento_detalle;
drop table if exists movimiento;
drop table if exists inventario;
drop table if exists producto;
drop table if exists bodega;
drop table if exists usuario;

CREATE TABLE usuario(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol ENUM('ADMIN', 'EMPLEADO') NOT NULL
);

CREATE TABLE bodega(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200) NOT NULL,
    capacidad INT NOT NULL,
    encargado VARCHAR(100) NOT NULL
);

CREATE TABLE producto(
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    categoria VARCHAR(100) NOT NULL,
    precio DECIMAL(12,2) NOT NULL
);

CREATE TABLE inventario(
    id INT AUTO_INCREMENT PRIMARY KEY,
    bodega_id INT NOT NULL,
    producto_id INT NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_inventario_bodega FOREIGN KEY (bodega_id) REFERENCES bodega(id),
    CONSTRAINT fk_inventario_producto FOREIGN KEY (producto_id) REFERENCES producto(id),
    CONSTRAINT uk_bodega_producto UNIQUE (bodega_id, producto_id)
);

CREATE TABLE movimiento(
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    tipo ENUM('ENTRADA', 'SALIDA', 'TRANSFERENCIA') NOT NULL,
    usuario_id INT NOT NULL,
    bodega_origen_id INT NULL,
    bodega_destino_id INT NULL,
    CONSTRAINT fk_movimiento_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_movimiento_bodega_origen FOREIGN KEY (bodega_origen_id) REFERENCES bodega(id),
    CONSTRAINT fk_movimiento_bodega_destino FOREIGN KEY (bodega_destino_id) REFERENCES bodega(id)
);

CREATE TABLE movimiento_detalle(
    id INT AUTO_INCREMENT PRIMARY KEY,
    movimiento_id INT NOT NULL,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL,
    CONSTRAINT fk_detalle_movimiento FOREIGN KEY (movimiento_id) REFERENCES movimiento(id) ON DELETE CASCADE,
    CONSTRAINT fk_detalle_producto FOREIGN KEY (producto_id) REFERENCES producto(id)
);

CREATE TABLE auditoria(
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    operacion ENUM('INSERT', 'UPDATE', 'DELETE') NOT NULL,
    usuario_id INT NULL,
    entidad VARCHAR(100) NOT NULL,
    valores_anteriores TEXT NULL,
    valores_nuevos TEXT NULL,
    CONSTRAINT fk_auditoria_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE SET NULL
);