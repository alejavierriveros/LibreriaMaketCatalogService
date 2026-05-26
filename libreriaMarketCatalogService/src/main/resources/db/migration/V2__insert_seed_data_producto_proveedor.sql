-- 1. Insertar proveedores semilla
INSERT INTO proveedores (nombre, contacto) VALUES
                                               ('Proveedor Test', 'contacto@test.cl'),
                                               ('Distribuidora Libros', 'ventas@libros.cl');

-- 2. Insertar productos semilla (Con ISBN de 17 caracteres para pasar el filtro de tu @Size)
INSERT INTO productos (
    titulo, autor, editorial, categoria, anio_publicacion, precio, isbn, descripcion, proveedor_id
) VALUES (
             'Clean Code',
             'Robert Martin',
             'Prentice Hall',
             'Programación',
             2008,
             15000.0,
             '978-0-13-235088-4', -- ISBN corregido a 17 caracteres (Formato de 5 bloques con guiones)
             'Libro clásico de buenas prácticas de programación.',
             1
         );