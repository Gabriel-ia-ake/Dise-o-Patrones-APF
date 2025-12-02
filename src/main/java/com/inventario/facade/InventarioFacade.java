package com.inventario.facade;

import com.inventario.model.Producto;
import com.inventario.model.ProductoBuilder;
import com.inventario.model.TipoTela;
import com.inventario.observer.IObservador;
import com.inventario.observer.NotificadorConsola;
import com.inventario.observer.NotificadorLog;
import com.inventario.repository.IProductoRepository;
import com.inventario.repository.ProductoRepositoryImpl;
import com.inventario.service.InventarioService;
import com.inventario.service.ProductoService;
import com.inventario.strategy.IValidacionStrategy;
import com.inventario.strategy.ValidacionBasicaStrategy;
import com.inventario.strategy.ValidacionEstrictaStrategy;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InventarioFacade {
    
    private final ProductoService productoService;
    private final InventarioService inventarioService;
    private final IProductoRepository repository;
    
    public InventarioFacade() {
        this.repository = new ProductoRepositoryImpl();
        
        IValidacionStrategy estrategia = new ValidacionBasicaStrategy();
        this.productoService = new ProductoService(repository, estrategia);
        this.inventarioService = new InventarioService(repository);
        this.productoService.agregarObservador(new NotificadorConsola());
        this.productoService.agregarObservador(new NotificadorLog());
    }

    public InventarioFacade(IProductoRepository repository, IValidacionStrategy estrategia) {
        this.repository = repository;
        this.productoService = new ProductoService(repository, estrategia);
        this.inventarioService = new InventarioService(repository);
    }

    public Producto crearProductoSimple(String codigo, String nombre, TipoTela tipoTela,
                                       String color, double precio, int stockInicial) {
        Producto producto = new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(tipoTela)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(stockInicial)
                .build();
        
        return productoService.crearProducto(producto);
    }

    public boolean registrarEntrada(String codigoProducto, int cantidad) {
        try {
            Optional<Producto> productoOpt = productoService.buscarPorCodigo(codigoProducto);
            
            if (productoOpt.isEmpty()) {
                System.err.println("Producto no encontrado: " + codigoProducto);
                return false;
            }
            
            Producto producto = productoOpt.get();
            productoService.incrementarStock(producto.getId(), cantidad);
            
            System.out.println(" Entrada registrada: " + cantidad + " unidades de " + 
                             producto.getNombre());
            return true;
            
        } catch (Exception e) {
            System.err.println("Error al registrar entrada: " + e.getMessage());
            return false;
        }
    }

    public boolean registrarSalida(String codigoProducto, int cantidad) {
        try {
            Optional<Producto> productoOpt = productoService.buscarPorCodigo(codigoProducto);
            
            if (productoOpt.isEmpty()) {
                System.err.println("Producto no encontrado: " + codigoProducto);
                return false;
            }
            
            Producto producto = productoOpt.get();
            productoService.decrementarStock(producto.getId(), cantidad);
            
            System.out.println(" Salida registrada: " + cantidad + " unidades de " + 
                             producto.getNombre());
            return true;
            
        } catch (IllegalStateException e) {
            System.err.println("X " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error al registrar salida: " + e.getMessage());
            return false;
        }
    }

    public Optional<Producto> buscarProducto(String codigo) {
        return productoService.buscarPorCodigo(codigo);
    }

    public List<Producto> listarTodosLosProductos() {
        return productoService.obtenerTodos();
    }

    public List<Producto> listarProductosPorTipo(TipoTela tipo) {
        return productoService.buscarPorTipo(tipo);
    }

   
    public List<Producto> listarProductosStockBajo() {
        return productoService.obtenerProductosConStockBajo();
    }

    public List<Producto> buscarProductosPorNombre(String nombre) {
        return productoService.buscarPorNombre(nombre);
    }

    public Map<String, Object> obtenerResumenInventario() {
        return inventarioService.obtenerResumenInventario();
    }

   
    public Map<TipoTela, Map<String, Object>> obtenerEstadisticasPorTipo() {
        return inventarioService.obtenerEstadisticasPorTipo();
    }

    public void mostrarReporteInventario() {
        String reporte = inventarioService.generarReporteTexto();
        System.out.println(reporte);
    }

    public List<Producto> obtenerTopProductosMasValiosos(int cantidad) {
        return inventarioService.obtenerProductosMasValiosos(cantidad);
    }

    public void activarValidacionEstricta() {
        System.out.println(" Validación estricta activada");
    }

    public void agregarObservador(IObservador observador) {
        productoService.agregarObservador(observador);
    }

    public void mostrarInformacionSistema() {
        System.out.println(" _______________________________________________________________ ");
        System.out.println("  SISTEMA DE INVENTARIO TEXTIL - v1.0.0");
        System.out.println(" _______________________________________________________________ ");
        System.out.println("  Patrones implementados:");
        System.out.println("  - Builder (creación de productos)");
        System.out.println("  - Factory Method (productos por tipo)");
        System.out.println("  - Strategy (validaciones)");
        System.out.println("  - Observer (notificaciones)");
        System.out.println("  - Facade (interfaz simplificada)");
        System.out.println("  - Repository (acceso a datos)");
        System.out.println(" _______________________________________________________________ ");
    }
}