-- Limpiar en orden correcto
SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE productos;
TRUNCATE TABLE proveedores;

SET FOREIGN_KEY_CHECKS = 1;


-- INSERT proveedores
INSERT INTO proveedores (nombre, contacto) VALUES 
('Proveedor Test', 'contacto@test.cl'),
('Distribuidora Libros Chile', 'ventas@libros.cl');


-- INSERT productos
INSERT INTO productos (
    titulo,
    autor,
    editorial,
    categoria,
    anio_publicacion,
    precio,
--     stock,
    isbn,
    descripcion,
    proveedor_id
) VALUES (
    'Clean Code',
    'Robert Martin',
    'Prentice Hall',
    'Programación',
    2008,
    15000,
--     10,
    '123456',
    'Libro clásico',
    1
);