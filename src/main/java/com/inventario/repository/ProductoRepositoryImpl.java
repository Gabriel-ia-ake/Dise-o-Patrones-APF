package com.inventario.repository;

import com.inventario.model.Producto;
import com.inventario.model.TipoTela;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ProductoRepositoryImpl implements IProductoRepository {
    private final Map<Long, Producto> productos;
    private final AtomicLong idGenerator;
    
    public ProductoRepositoryImpl() {
        this.productos = new ConcurrentHashMap<>();
        this.idGenerator = new AtomicLong(1);
    }

    @Override
    public Producto guardar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        
        if (existePorCodigo(producto.getCodigo())) {
            throw new IllegalArgumentException(
                "Ya existe un producto con el código: " + producto.getCodigo()
            );
        }
        
        if (producto.getId() == null) {
            producto.setId(idGenerator.getAndIncrement());
        }
        
        productos.put(producto.getId(), producto);
        
        return producto;
    }

    @Override
    public Producto actualizar(Producto producto) {
        if (producto == null || producto.getId() == null) {
            throw new IllegalArgumentException("El producto y su ID no pueden ser null");
        }
        
        if (!productos.containsKey(producto.getId())) {
            throw new IllegalArgumentException("No existe el producto con ID: " + producto.getId());
        }
        
        productos.put(producto.getId(), producto);
        return producto;
    }

    @Override
    public boolean eliminar(Long id) {
        if (id == null) {
            return false;
        }
        
        Optional<Producto> producto = buscarPorId(id);
        if (producto.isPresent()) {
            producto.get().setActivo(false);
            return true;
        }
        
        return false;
    }

    @Override
    public Optional<Producto> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(productos.get(id));
    }

    @Override
    public Optional<Producto> buscarPorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return Optional.empty();
        }
        
        return productos.values().stream()
                .filter(p -> p.isActivo())
                .filter(p -> p.getCodigo().equalsIgnoreCase(codigo.trim()))
                .findFirst();
    }

    @Override
    public List<Producto> obtenerTodos() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .sorted(Comparator.comparing(Producto::getCodigo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorTipo(TipoTela tipoTela) {
        if (tipoTela == null) {
            return Collections.emptyList();
        }
        
        return productos.values().stream()
                .filter(Producto::isActivo)
                .filter(p -> p.getTipoTela() == tipoTela)
                .sorted(Comparator.comparing(Producto::getCodigo))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> obtenerProductosConStockBajo() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .filter(Producto::tieneStockBajo)
                .sorted(Comparator.comparing(Producto::getStockActual))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String nombreBusqueda = nombre.trim().toLowerCase();
        
        return productos.values().stream()
                .filter(Producto::isActivo)
                .filter(p -> p.getNombre().toLowerCase().contains(nombreBusqueda))
                .sorted(Comparator.comparing(Producto::getNombre))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> buscarPorColor(String color) {
        if (color == null || color.trim().isEmpty()) {
            return Collections.emptyList();
        }
        
        String colorBusqueda = color.trim().toLowerCase();
        
        return productos.values().stream()
                .filter(Producto::isActivo)
                .filter(p -> p.getColor().toLowerCase().contains(colorBusqueda))
                .sorted(Comparator.comparing(Producto::getCodigo))
                .collect(Collectors.toList());
    }

    @Override
    public long contarProductos() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .count();
    }

    @Override
    public long contarProductosConStockBajo() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .filter(Producto::tieneStockBajo)
                .count();
    }

    @Override
    public boolean existePorCodigo(String codigo) {
        if (codigo == null || codigo.trim().isEmpty()) {
            return false;
        }
        
        return productos.values().stream()
                .filter(Producto::isActivo)
                .anyMatch(p -> p.getCodigo().equalsIgnoreCase(codigo.trim()));
    }

    @Override
    public double calcularValorTotalInventario() {
        return productos.values().stream()
                .filter(Producto::isActivo)
                .mapToDouble(Producto::getValorInventario)
                .sum();
    }
    
    public void limpiar() {
        productos.clear();
        idGenerator.set(1);
    }
    
    public int tamano() {
        return productos.size();
    }
}