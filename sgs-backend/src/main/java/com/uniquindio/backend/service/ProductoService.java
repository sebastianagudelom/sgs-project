package com.uniquindio.backend.service;

import com.uniquindio.backend.dto.ProductoRequest;
import com.uniquindio.backend.dto.ProductoResponse;
import com.uniquindio.backend.model.Categoria;
import com.uniquindio.backend.model.Producto;
import com.uniquindio.backend.repository.CategoriaRepository;
import com.uniquindio.backend.repository.DetallePedidoRepository;
import com.uniquindio.backend.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final InventarioService inventarioService;

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarTodos() {
        return productoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarActivos() {
        return productoRepository.findByActivoTrue().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toResponse(producto);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> buscarPorNombre(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarPorCategoria(Long categoriaId) {
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto producto = Producto.builder()
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .precio(request.precio())
                .stock(request.stock())
                .stockMinimo(normalizarStockMinimo(request.stockMinimo()))
                .imagenUrl(request.imagenUrl())
                .activo(true)
                .categoria(categoria)
                .build();

        producto = productoRepository.save(producto);
        inventarioService.sincronizarProducto(producto);
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setNombre(request.nombre());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setStock(request.stock());
        producto.setStockMinimo(normalizarStockMinimo(request.stockMinimo()));
        producto.setImagenUrl(request.imagenUrl());
        producto.setCategoria(categoria);

        producto = productoRepository.save(producto);
        inventarioService.sincronizarProducto(producto);
        return toResponse(producto);
    }

    @Transactional
    public ProductoResponse toggleActivo(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        producto.setActivo(!producto.isActivo());
        producto = productoRepository.save(producto);
        inventarioService.sincronizarProducto(producto);
        return toResponse(producto);
    }

    @Transactional
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (detallePedidoRepository.existsByProductoId(id)) {
            throw new RuntimeException("No se puede eliminar el producto porque tiene pedidos asociados. Puedes desactivarlo en su lugar.");
        }

        productoRepository.delete(producto);
    }

    private ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getStockMinimo() != null ? producto.getStockMinimo() : 5,
                producto.getImagenUrl(),
                producto.isActivo(),
                producto.getCategoria().getId(),
                producto.getCategoria().getNombre(),
                producto.getFechaCreacion()
        );
    }

    private int normalizarStockMinimo(Integer stockMinimo) {
        return stockMinimo != null && stockMinimo > 0 ? stockMinimo : 5;
    }
}
