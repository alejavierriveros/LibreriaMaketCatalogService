package com.example.libreriaMarketCatalogService.service;

import org.springframework.stereotype.Service;
import com.example.libreriaMarketCatalogService.repository.ProductoRepository;
import com.example.libreriaMarketCatalogService.repository.ProveedorRepository;
import com.example.libreriaMarketCatalogService.model.Producto;
import com.example.libreriaMarketCatalogService.model.Proveedor;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repository;
    private final ProveedorRepository proveedorRepository;

    public ProductoService(ProductoRepository repository, ProveedorRepository proveedorRepository) {
        this.repository = repository;
        this.proveedorRepository = proveedorRepository;
    }

    // listar todos los productos
    public List<Producto> listar() {
        return repository.findAll();
    }

    // buscar producto por ID
    public Producto obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    // crear producto
    public Producto guardar(Producto producto) {

        // validar duplicado por ISBN
        if (repository.findByIsbn(producto.getIsbn()).isPresent()) {
            throw new RuntimeException("Ya existe un producto con ese ISBN");
        }

        // validar proveedor
        if (producto.getProveedor() == null || producto.getProveedor().getId() == null) {
            throw new RuntimeException("Debe asignar un proveedor");
        }

        Proveedor proveedor = proveedorRepository.findById(producto.getProveedor().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no existe"));

        producto.setProveedor(proveedor);

        return repository.save(producto);
    }

    // actualizar producto
    public Producto actualizar(Long id, Producto producto) {

        Producto existente = obtenerPorId(id);

        // validar proveedor
        Proveedor proveedor = proveedorRepository.findById(producto.getProveedor().getId())
                .orElseThrow(() -> new RuntimeException("Proveedor no existe"));

        existente.setTitulo(producto.getTitulo());
        existente.setAutor(producto.getAutor());
        existente.setEditorial(producto.getEditorial());
        existente.setCategoria(producto.getCategoria());
        existente.setAnioPublicacion(producto.getAnioPublicacion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setIsbn(producto.getIsbn());
        existente.setDescripcion(producto.getDescripcion());
        existente.setProveedor(proveedor);

        return repository.save(existente);
    }

    // eliminar producto
    public String eliminar(Long id) {
        Producto producto = obtenerPorId(id);
        repository.delete(producto);
        return "Producto eliminado";
    }

    // comprar producto (resta stock)
    public Producto comprar(Long id, int cantidad) {

        Producto producto = obtenerPorId(id);

        if (cantidad <= 0) {
            throw new RuntimeException("Cantidad inválida");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("No hay suficiente stock");
        }

        producto.setStock(producto.getStock() - cantidad);

        return repository.save(producto);
    }
}
