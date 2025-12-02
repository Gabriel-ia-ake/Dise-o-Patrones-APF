package com.inventario.service;

import com.inventario.model.Producto;
import com.inventario.model.TipoTela;
import com.inventario.repository.IProductoRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InventarioService {
    
    private final IProductoRepository repository;

    public InventarioService(IProductoRepository repository) {
        this.repository = repository;
    }

    public Map<String, Object> obtenerResumenInventario() {
        Map<String, Object> resumen = new HashMap<>();
        
        long totalProductos = repository.contarProductos();
        long productosStockBajo = repository.contarProductosConStockBajo();
        double valorTotal = repository.calcularValorTotalInventario();
        List<Producto> productosConStockBajo = repository.obtenerProductosConStockBajo();
        
        resumen.put("totalProductos", totalProductos);
        resumen.put("productosStockBajo", productosStockBajo);
        resumen.put("valorTotalInventario", valorTotal);
        resumen.put("porcentajeStockBajo", 
                    totalProductos > 0 ? (productosStockBajo * 100.0 / totalProductos) : 0);
        resumen.put("listaProductosStockBajo", productosConStockBajo);
        
        return resumen;
    }

    public Map<TipoTela, Map<String, Object>> obtenerEstadisticasPorTipo() {
        Map<TipoTela, Map<String, Object>> estadisticas = new HashMap<>();
        
        for (TipoTela tipo : TipoTela.values()) {
            List<Producto> productosTipo = repository.buscarPorTipo(tipo);
            
            if (!productosTipo.isEmpty()) {
                Map<String, Object> stats = new HashMap<>();
                stats.put("cantidad", productosTipo.size());
                stats.put("stockTotal", calcularStockTotal(productosTipo));
                stats.put("valorTotal", calcularValorTotal(productosTipo));
                stats.put("precioPromedio", calcularPrecioPromedio(productosTipo));
                
                estadisticas.put(tipo, stats);
            }
        }
        
        return estadisticas;
    }

    private int calcularStockTotal(List<Producto> productos) {
        return productos.stream()
                .mapToInt(Producto::getStockActual)
                .sum();
    }

    private double calcularValorTotal(List<Producto> productos) {
        return productos.stream()
                .mapToDouble(Producto::getValorInventario)
                .sum();
    }

    private double calcularPrecioPromedio(List<Producto> productos) {
        return productos.stream()
                .mapToDouble(Producto::getPrecio)
                .average()
                .orElse(0.0);
    }


    public List<Producto> obtenerProductosMasValiosos(int top) {
        return repository.obtenerTodos().stream()
                .sorted((p1, p2) -> Double.compare(p2.getValorInventario(), p1.getValorInventario()))
                .limit(top)
                .toList();
    }


    public List<Producto> obtenerProductosSinStock() {
        return repository.obtenerTodos().stream()
                .filter(p -> p.getStockActual() == 0)
                .toList();
    }

    public double calcularPorcentajeCumplimientoStock() {
        long total = repository.contarProductos();
        long cumplimiento = repository.obtenerTodos().stream()
                .filter(p -> p.getStockActual() > p.getStockMinimo())
                .count();
        
        return total > 0 ? (cumplimiento * 100.0 / total) : 0;
    }


    public String generarReporteTexto() {
        StringBuilder reporte = new StringBuilder();
        Map<String, Object> resumen = obtenerResumenInventario();
        
        reporte.append(" _______________________________________________________________ \n");
        reporte.append("           REPORTE DE INVENTARIO TEXTIL           \n");
        reporte.append(" _______________________________________________________________ \n\n");
        
        reporte.append(String.format("Total de productos: %d\n", resumen.get("totalProductos")));
        reporte.append(String.format("Productos con stock bajo: %d\n", resumen.get("productosStockBajo")));
        reporte.append(String.format("Valor total del inventario: S/ %.2f\n", resumen.get("valorTotalInventario")));
        reporte.append(String.format("Porcentaje stock bajo: %.2f%%\n\n", resumen.get("porcentajeStockBajo")));
        
        @SuppressWarnings("unchecked")
        List<Producto> stockBajo = (List<Producto>) resumen.get("listaProductosStockBajo");
        
        if (!stockBajo.isEmpty()) {
            reporte.append("PRODUCTOS CON STOCK BAJO:\n");
            reporte.append(" _______________________________________________________________ \n");
            for (Producto p : stockBajo) {
                reporte.append(String.format("  %s - %s (Stock: %d/%d)\n",
                        p.getCodigo(), p.getNombre(), 
                        p.getStockActual(), p.getStockMinimo()));
            }
        }
        
        reporte.append("\n _______________________________________________________________ \n");
        
        return reporte.toString();
    }
}