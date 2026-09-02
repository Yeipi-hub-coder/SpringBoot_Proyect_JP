USE logitrack_db;

INSERT INTO bodega (nombre, ubicacion, capacidad, encargado)
VALUES
('Bodega Principal', 'Bucaramanga', 1000, 'Carlos Gómez'),
('Bodega Norte', 'Barrancabermeja', 500, 'Laura Martínez'),
('Bodega Sur', 'Floridablanca', 750, 'Andrés Rodríguez'),
('Bodega Centro', 'Girón', 600, 'María López');

INSERT INTO producto (nombre, categoria, precio)
VALUES
('Laptop Lenovo IdeaPad 3', 'Computadores', 2450000.00),
('Mouse Logitech M185', 'Periféricos', 55000.00),
('Teclado Logitech K120', 'Periféricos', 75000.00),
('Monitor Samsung 24"', 'Monitores', 680000.00),
('Impresora Epson EcoTank L3250', 'Impresoras', 950000.00),
('Disco SSD Kingston 480GB', 'Almacenamiento', 210000.00),
('Memoria RAM Kingston 8GB', 'Componentes', 115000.00),
('Webcam Logitech C920', 'Periféricos', 320000.00);

INSERT INTO inventario (bodega_id, producto_id, stock)
VALUES
(1, 1, 20),
(1, 2, 80),
(1, 3, 60),
(1, 4, 25),
(2, 1, 10),
(2, 5, 15),
(2, 6, 30),
(2, 7, 40),
(3, 2, 50),
(3, 4, 20),
(3, 8, 12),
(4, 3, 35),
(4, 5, 10),
(4, 6, 25);


DELIMITER $$
CREATE TRIGGER tr_usuario_insert
AFTER INSERT ON usuario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        @usuario_actual,
        'USUARIO',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', email=', NEW.email,
            ', rol=', NEW.rol
        )
    );
END $$


CREATE TRIGGER tr_usuario_update
AFTER UPDATE ON usuario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'UPDATE',
        @usuario_actual,
        'USUARIO',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', email=', OLD.email,
            ', rol=', OLD.rol
        ),
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', email=', NEW.email,
            ', rol=', NEW.rol
        )
    );
END $$


CREATE TRIGGER tr_usuario_delete
AFTER DELETE ON usuario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'DELETE',
        @usuario_actual,
        'USUARIO',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', email=', OLD.email,
            ', rol=', OLD.rol
        ),
        NULL
    );
END $$





CREATE TRIGGER tr_bodega_insert
AFTER INSERT ON bodega
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        @usuario_actual,
        'BODEGA',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', ubicacion=', NEW.ubicacion,
            ', capacidad=', NEW.capacidad,
            ', encargado=', NEW.encargado
        )
    );
END $$


CREATE TRIGGER tr_bodega_update
AFTER UPDATE ON bodega
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'UPDATE',
        @usuario_actual,
        'BODEGA',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', ubicacion=', OLD.ubicacion,
            ', capacidad=', OLD.capacidad,
            ', encargado=', OLD.encargado
        ),
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', ubicacion=', NEW.ubicacion,
            ', capacidad=', NEW.capacidad,
            ', encargado=', NEW.encargado
        )
    );
END $$


CREATE TRIGGER tr_bodega_delete
AFTER DELETE ON bodega
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'DELETE',
        @usuario_actual,
        'BODEGA',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', ubicacion=', OLD.ubicacion,
            ', capacidad=', OLD.capacidad,
            ', encargado=', OLD.encargado
        ),
        NULL
    );
END $$




CREATE TRIGGER tr_producto_insert
AFTER INSERT ON producto
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        @usuario_actual,
        'PRODUCTO',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', categoria=', NEW.categoria,
            ', precio=', NEW.precio
        )
    );
END $$


CREATE TRIGGER tr_producto_update
AFTER UPDATE ON producto
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'UPDATE',
        @usuario_actual,
        'PRODUCTO',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', categoria=', OLD.categoria,
            ', precio=', OLD.precio
        ),
        CONCAT(
            'id=', NEW.id,
            ', nombre=', NEW.nombre,
            ', categoria=', NEW.categoria,
            ', precio=', NEW.precio
        )
    );
END $$


CREATE TRIGGER tr_producto_delete
AFTER DELETE ON producto
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'DELETE',
        @usuario_actual,
        'PRODUCTO',
        CONCAT(
            'id=', OLD.id,
            ', nombre=', OLD.nombre,
            ', categoria=', OLD.categoria,
            ', precio=', OLD.precio
        ),
        NULL
    );
END $$



CREATE TRIGGER tr_inventario_insert
AFTER INSERT ON inventario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        @usuario_actual,
        'INVENTARIO',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', bodega_id=', NEW.bodega_id,
            ', producto_id=', NEW.producto_id,
            ', stock=', NEW.stock
        )
    );
END $$


CREATE TRIGGER tr_inventario_update
AFTER UPDATE ON inventario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'UPDATE',
        @usuario_actual,
        'INVENTARIO',
        CONCAT(
            'id=', OLD.id,
            ', bodega_id=', OLD.bodega_id,
            ', producto_id=', OLD.producto_id,
            ', stock=', OLD.stock
        ),
        CONCAT(
            'id=', NEW.id,
            ', bodega_id=', NEW.bodega_id,
            ', producto_id=', NEW.producto_id,
            ', stock=', NEW.stock
        )
    );
END $$


CREATE TRIGGER tr_inventario_delete
AFTER DELETE ON inventario
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'DELETE',
        @usuario_actual,
        'INVENTARIO',
        CONCAT(
            'id=', OLD.id,
            ', bodega_id=', OLD.bodega_id,
            ', producto_id=', OLD.producto_id,
            ', stock=', OLD.stock
        ),
        NULL
    );
END $$





CREATE TRIGGER tr_movimiento_insert
AFTER INSERT ON movimiento
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        COALESCE(@usuario_actual, NEW.usuario_id),
        'MOVIMIENTO',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', fecha=', NEW.fecha,
            ', tipo=', NEW.tipo,
            ', usuario_id=', NEW.usuario_id,
            ', bodega_origen_id=', IFNULL(NEW.bodega_origen_id, 'NULL'),
            ', bodega_destino_id=', IFNULL(NEW.bodega_destino_id, 'NULL')
        )
    );
END $$



CREATE TRIGGER tr_movimiento_detalle_insert
AFTER INSERT ON movimiento_detalle
FOR EACH ROW
BEGIN
    INSERT INTO auditoria (
        operacion,
        usuario_id,
        entidad,
        valores_anteriores,
        valores_nuevos
    )
    VALUES (
        'INSERT',
        @usuario_actual,
        'MOVIMIENTO_DETALLE',
        NULL,
        CONCAT(
            'id=', NEW.id,
            ', movimiento_id=', NEW.movimiento_id,
            ', producto_id=', NEW.producto_id,
            ', cantidad=', NEW.cantidad
        )
    );
END $$

DELIMITER ;

