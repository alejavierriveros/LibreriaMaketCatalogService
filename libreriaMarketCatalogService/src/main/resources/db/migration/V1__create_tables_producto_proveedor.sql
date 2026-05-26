-- 1. Crear tabla proveedores primero (Lado "Uno")
CREATE TABLE proveedores (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             nombre VARCHAR(20) NOT NULL, -- Respetando el @Size(max = 20)
                             contacto VARCHAR(255) NOT NULL
);

-- 2. Crear tabla productos (Lado "Muchos")
CREATE TABLE productos (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           titulo VARCHAR(100) NOT NULL,      -- Respetando el @Length(max = 100)
                           autor VARCHAR(100) NOT NULL,       -- Respetando el @Length(max = 100)
                           editorial VARCHAR(100) NOT NULL,   -- Respetando el @Length(max = 100)
                           categoria VARCHAR(20) NOT NULL,    -- Respetando el @Length(max = 20)
                           anio_publicacion INT NOT NULL,     -- Mapeo de anio_publicacion
                           precio DOUBLE NOT NULL,
                           isbn VARCHAR(17) NOT NULL,         -- Forzado a 17 por tu restricción @Size(min=17, max=17)
                           descripcion VARCHAR(300) NULL,     -- Respetando el @Size(max = 300)
                           proveedor_id BIGINT NULL,

    -- Restricción de Clave Foránea hacia proveedores
                           CONSTRAINT fk_productos_proveedores FOREIGN KEY (proveedor_id)
                               REFERENCES proveedores(id) ON DELETE SET NULL
);