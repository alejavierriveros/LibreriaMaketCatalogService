-- Insertar el producto que forzará el ID 2 en la tabla auto-incremental
INSERT INTO productos (
    titulo,
    autor,
    editorial,
    categoria,
    anio_publicacion,
    precio,
    isbn,
    descripcion,
    proveedor_id
) VALUES (
             'Design Patterns',
             'Erich Gamma',
             'Addison-Wesley',
             'Programación',
             1994,
             25000.0,
             '978-0-20-163361-0', -- ISBN válido de exactamente 17 caracteres
             'Libro clásico de patrones de diseño orientado a objetos.',
             1 -- Asociado al proveedor_id 1 que ya existe gracias a la V2
         );