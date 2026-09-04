CREATE TABLE bodegaExamen(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	ubicacion VARCHAR(200) NOT NULL
)

CREATE TABLE productoExamen(
	id INT AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	stock INT NOT NULL,
	bodega INT NOT NULL,
	FOREIGN KEY(bodega) REFERENCES bodegaExamen(id)
)

INSERT INTO bodegaExamen (nombre,ubicacion) VALUES
('Bodega Principal', 'Calle Principal'),
('Bodega Sur', 'Carrera norte con calle sur');

INSERT INTO productoExamen (nombre, stock, bodega) VALUES
('Gaseosa', 60, 1),
('Pan', 100, 1),
('Maní', 20, 2);
