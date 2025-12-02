package com.inventario.service;

import com.inventario.model.Producto;
import com.inventario.model.TipoTela;
import com.inventario.observer.IObservador;
import com.inventario.repository.IProductoRepository;
import com.inventario.strategy.IValidacionStrategy;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoService {
    
    private final IProductoRepository repository;
    private final IValidacionStrategy validacionStrategy;
    private final List<IObservador> observadores;
    public ProductoService(IProductoRepository repository, IValidacionStrategy validacionStrategy) {
        this.repository = repository;
        this.validacionStrategy = validacionStrategy;
        this.observadores = new ArrayList<>();
    }

    public void agregarObservador(IObservador observador) {
        if (observador != null && !observadores.contains(observador)) {
            observadores.add(observador);
        }
    }

    public void removerObservador(IObservador observador) {
        observadores.remove(observador);
    }

    private void notificarProductoAgregado(Producto producto) {
        observadores.forEach(obs -> obs.onProductoAgregado(producto));
    }

    private void notificarProductoActualizado(Producto producto) {
        observadores.forEach(obs -> obs.onProductoActualizado(producto));
        
        if (producto.getStockActual() == 0) {
            observadores.forEach(obs -> obs.onStockCritico(producto));
        } else if (producto.tieneStockBajo()) {
            observadores.forEach(obs -> obs.onStockBajo(producto));
        }
    }

    private void notificarProductoEliminado(Producto producto) {
        observadores.forEach(obs -> obs.onProductoEliminado(producto));
    }

    public Producto crearProducto(Producto producto) {
        validacionStrategy.validar(producto);
        
        Producto productoGuardado = repository.guardar(producto);
        
        notificarProductoAgregado(productoGuardado);
        
        return productoGuardado;
    }

    public Producto actualizarProducto(Producto producto) {
        validacionStrategy.validar(producto);
        
        Producto productoActualizado = repository.actualizar(producto);
        
        notificarProductoActualizado(productoActualizado);
        
        return productoActualizado;
    }

    public boolean eliminarProducto(Long id) {
        Optional<Producto> producto = repository.buscarPorId(id);
        
        if (producto.isPresent()) {
            boolean eliminado = repository.eliminar(id);
            if (eliminado) {
                notificarProductoEliminado(producto.get());
            }
            return eliminado;
        }
        
        return false;
    }

    public Optional<Producto> buscarPorId(Long id) {
        return repository.buscarPorId(id);
    }

    public Optional<Producto> buscarPorCodigo(String codigo) {
        return repository.buscarPorCodigo(codigo);
    }

    public List<Producto> obtenerTodos() {
        return repository.obtenerTodos();
    }


    public List<Producto> buscarPorTipo(TipoTela tipoTela) {
        return repository.buscarPorTipo(tipoTela);
    }


    public List<Producto> obtenerProductosConStockBajo() {
        return repository.obtenerProductosConStockBajo();
    }

    public List<Producto> buscarPorNombre(String nombre) {
        return repository.buscarPorNombre(nombre);
    }

    public Producto incrementarStock(Long id, int cantidad) {
        Optional<Producto> productoOpt = repository.buscarPorId(id);
        
        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        
        Producto producto = productoOpt.get();
        producto.incrementarStock(cantidad);
        
        return actualizarProducto(producto);
    }

    public Producto decrementarStock(Long id, int cantidad) {
        Optional<Producto> productoOpt = repository.buscarPorId(id);
        
        if (productoOpt.isEmpty()) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + id);
        }
        
        Producto producto = productoOpt.get();
        producto.decrementarStock(cantidad);
        
        return actualizarProducto(producto);
    }

    public boolean existePorCodigo(String codigo) {
        return repository.existePorCodigo(codigo);
    }
}