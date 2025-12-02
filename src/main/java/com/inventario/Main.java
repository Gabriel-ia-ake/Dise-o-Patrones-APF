package com.inventario;

import com.inventario.facade.InventarioFacade;
import com.inventario.factory.ProductoFactory;
import com.inventario.model.Producto;
import com.inventario.model.TipoTela;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    
    private static final InventarioFacade facade = new InventarioFacade();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        mostrarBanner();
        inicializarDatosDemo();
        
        boolean continuar = true;
        
        while (continuar) {
            try {
                mostrarMenu();
                int opcion = leerOpcion();
                
                switch (opcion) {
                    case 1 -> listarTodosLosProductos();
                    case 2 -> buscarProductoPorCodigo();
                    case 3 -> agregarNuevoProducto();
                    case 4 -> registrarEntradaStock();
                    case 5 -> registrarSalidaStock();
                    case 6 -> listarProductosStockBajo();
                    case 7 -> mostrarResumenInventario();
                    case 8 -> mostrarEstadisticasPorTipo();
                    case 9 -> buscarProductosPorNombre();
                    case 10 -> listarProductosPorTipo();
                    case 0 -> {
                        System.out.println("\nGracias por usar el Sistema de Inventario Textil");
                        System.out.println("   Desarrollado con patrones SOLID, GoF y GRASP\n");
                        continuar = false;
                    }
                    default -> System.out.println("Opción inválida. Intente nuevamente.");
                }
                
                if (continuar && opcion != 0) {
                    System.out.println("\nPresione ENTER para continuar...");
                    scanner.nextLine();
                }
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }

    private static void mostrarBanner() {
        System.out.println(" _______________________________________________________________ ");
        System.out.println("|                                                               |");
        System.out.println("|        SISTEMA DE INVENTARIO TEXTIL - VERSION 1.0.0           |");
        System.out.println("|                                                               |");
        System.out.println("|  Desarrollado con patrones de diseño:                         |");
        System.out.println("|  ✓ SOLID (SRP, OCP, LSP, ISP, DIP)                            |");
        System.out.println("|  ✓ GoF (Builder, Factory, Strategy, Observer, Facade)         |");
        System.out.println("|  ✓ GRASP (Controller, Expert, Low Coupling, High Cohesion)    |");
        System.out.println("|                                                               |");
        System.out.println("|  Equipo de Desarrollo:                                        |");
        System.out.println("|  • ALOR SALAS, GABRIEL ALESSANDRO                             |");
        System.out.println(" _______________________________________________________________ ");
        System.out.println();
    }

    private static void mostrarMenu() {
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|                        MENÚ PRINCIPAL                         |");
        System.out.println(" _______________________________________________________________ ");
        System.out.println("|  1. Listar todos los productos                               |");
        System.out.println("|  2. Buscar producto por código                               |");
        System.out.println("|  3. Agregar nuevo producto                                   |");
        System.out.println("|  4. Registrar entrada de stock                               |");
        System.out.println("|  5. Registrar salida de stock                                |");
        System.out.println("|  6. Ver productos con stock bajo                             |");
        System.out.println("|  7. Ver resumen del inventario                               |");
        System.out.println("|  8. Ver estadísticas por tipo de tela                        |");
        System.out.println("|  9. Buscar productos por nombre                              |");
        System.out.println("|  10. Listar productos por tipo de tela                       |");
        System.out.println("|  0. Salir                                                    |");
        System.out.println(" _______________________________________________________________ ");
        System.out.print("\nSeleccione una opción: ");
    }

    private static int leerOpcion() {
        try {
            int opcion = Integer.parseInt(scanner.nextLine());
            return opcion;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void inicializarDatosDemo() {
        System.out.println("Inicializando productos de demostración...\n");
                Producto[] productosDemo = ProductoFactory.crearProductosDemostracion();
        
        for (Producto producto : productosDemo) {
            producto.setStockActual(50);
            facade.crearProductoSimple(
                producto.getCodigo(),
                producto.getNombre(),
                producto.getTipoTela(),
                producto.getColor(),
                producto.getPrecio(),
                producto.getStockActual()
            );
        }
        
        // un producto con stock bajo para demostrar alertas
        facade.crearProductoSimple("TEL006", "Lycra Deportiva", TipoTela.LYCRA, 
                                  "Negro", 28.00, 3); // Stock bajo
        
        System.out.println("✅ Productos de demostración creados\n");
    }

    private static void listarTodosLosProductos() {
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|                    LISTADO DE PRODUCTOS                       |");
        System.out.println(" _______________________________________________________________ \n");
        
        List<Producto> productos = facade.listarTodosLosProductos();
        
        if (productos.isEmpty()) {
            System.out.println("  No hay productos registrados.");
            return;
        }
        
        System.out.printf("%-10s %-30s %-15s %-12s %-10s %s\n", 
                         "CÓDIGO", "NOMBRE", "TIPO", "COLOR", "PRECIO", "STOCK");
        System.out.println("─".repeat(90));
        
        for (Producto p : productos) {
            System.out.printf("%-10s %-30s %-15s %-12s S/ %7.2f %d/%d %s\n",
                p.getCodigo(),
                p.getNombre().length() > 30 ? p.getNombre().substring(0, 27) + "..." : p.getNombre(),
                p.getTipoTela(),
                p.getColor(),
                p.getPrecio(),
                p.getStockActual(),
                p.getStockMinimo(),
                p.tieneStockBajo() ? "⚠️" : "✓"
            );
        }
        
        System.out.println("\nTotal: " + productos.size() + " productos");
    }

    private static void buscarProductoPorCodigo() {
        System.out.print("\nIngrese el código del producto: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        Optional<Producto> productoOpt = facade.buscarProducto(codigo);
        
        if (productoOpt.isEmpty()) {
            System.out.println("Producto no encontrado con código: " + codigo);
            return;
        }
        
        Producto p = productoOpt.get();
        
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|                   INFORMACIÓN DEL PRODUCTO                    |");
        System.out.println(" _______________________________________________________________ ");
        System.out.println("  Código:         " + p.getCodigo());
        System.out.println("  Nombre:         " + p.getNombre());
        System.out.println("  Tipo de tela:   " + p.getTipoTela());
        System.out.println("  Color:          " + p.getColor());
        System.out.println("  Precio:         S/ " + String.format("%.2f", p.getPrecio()));
        System.out.println("  Stock actual:   " + p.getStockActual());
        System.out.println("  Stock mínimo:   " + p.getStockMinimo());
        System.out.println("  Estado:         " + p.getEstadoStock());
        System.out.println("  Valor inv.:     S/ " + String.format("%.2f", p.getValorInventario()));
        System.out.println("  Fecha creación: " + p.getFechaCreacion());
    }

    private static void agregarNuevoProducto() {
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|                    AGREGAR NUEVO PRODUCTO                     |");
        System.out.println(" _______________________________________________________________ \n");
        
        try {
            System.out.print("Código (ej: TEL007): ");
            String codigo = scanner.nextLine().trim().toUpperCase();
            
            System.out.print("Nombre: ");
            String nombre = scanner.nextLine().trim();
            
            System.out.println("\nTipos de tela disponibles:");
            TipoTela[] tipos = TipoTela.values();
            for (int i = 0; i < tipos.length; i++) {
                System.out.printf("  %d. %s\n", i + 1, tipos[i]);
            }
            System.out.print("Seleccione tipo (1-" + tipos.length + "): ");
            int tipoIdx = Integer.parseInt(scanner.nextLine()) - 1;
            TipoTela tipo = tipos[tipoIdx];
            
            System.out.print("Color: ");
            String color = scanner.nextLine().trim();
            
            System.out.print("Precio: S/ ");
            double precio = Double.parseDouble(scanner.nextLine());
            
            System.out.print("Stock inicial: ");
            int stock = Integer.parseInt(scanner.nextLine());
            
            facade.crearProductoSimple(codigo, nombre, tipo, color, precio, stock);
            
            System.out.println("\n Producto agregado exitosamente!");
            
        } catch (Exception e) {
            System.out.println("Error al agregar producto: " + e.getMessage());
        }
    }

    private static void registrarEntradaStock() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Cantidad de entrada: ");
        int cantidad = Integer.parseInt(scanner.nextLine());
        
        facade.registrarEntrada(codigo, cantidad);
    }

    private static void registrarSalidaStock() {
        System.out.print("\nCódigo del producto: ");
        String codigo = scanner.nextLine().trim().toUpperCase();
        
        System.out.print("Cantidad de salida: ");
        int cantidad = Integer.parseInt(scanner.nextLine());
        
        facade.registrarSalida(codigo, cantidad);
    }

    private static void listarProductosStockBajo() {
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|                PRODUCTOS CON STOCK BAJO                       |");
        System.out.println(" _______________________________________________________________ \n");
        
        List<Producto> productos = facade.listarProductosStockBajo();
        
        if (productos.isEmpty()) {
            System.out.println(" No hay productos con stock bajo. ¡Todo en orden!");
            return;
        }
        
        System.out.printf("%-10s %-30s %-10s %-10s %s\n", 
                         "CÓDIGO", "NOMBRE", "STOCK", "MÍNIMO", "ESTADO");
        System.out.println("─".repeat(70));
        
        for (Producto p : productos) {
            String estado = p.getStockActual() == 0 ? "CRÍTICO" : " BAJO";
            System.out.printf("%-10s %-30s %-10d %-10d %s\n",
                p.getCodigo(),
                p.getNombre().length() > 30 ? p.getNombre().substring(0, 27) + "..." : p.getNombre(),
                p.getStockActual(),
                p.getStockMinimo(),
                estado
            );
        }
        
        System.out.println("\nTotal: " + productos.size() + " productos requieren atención");
    }

    private static void mostrarResumenInventario() {
        facade.mostrarReporteInventario();
    }

    private static void mostrarEstadisticasPorTipo() {
        System.out.println("\n _______________________________________________________________ ");
        System.out.println("|              ESTADÍSTICAS POR TIPO DE TELA                    |");
        System.out.println(" _______________________________________________________________ \n");
        
        Map<TipoTela, Map<String, Object>> estadisticas = facade.obtenerEstadisticasPorTipo();
        
        if (estadisticas.isEmpty()) {
            System.out.println("  No hay estadísticas disponibles.");
            return;
        }
        
        System.out.printf("%-15s %-10s %-12s %-15s %-15s\n",
                         "TIPO", "CANTIDAD", "STOCK TOT.", "VALOR TOTAL", "PRECIO PROM.");
        System.out.println("─".repeat(75));
        
        estadisticas.forEach((tipo, stats) -> {
            System.out.printf("%-15s %-10d %-12d S/ %11.2f S/ %11.2f\n",
                tipo,
                stats.get("cantidad"),
                stats.get("stockTotal"),
                (Double) stats.get("valorTotal"),
                (Double) stats.get("precioPromedio")
            );
        });
    }

    private static void buscarProductosPorNombre() {
        System.out.print("\nIngrese el nombre a buscar: ");
        String nombre = scanner.nextLine().trim();
        
        List<Producto> productos = facade.buscarProductosPorNombre(nombre);
        
        if (productos.isEmpty()) {
            System.out.println("No se encontraron productos con el nombre: " + nombre);
            return;
        }
        
        System.out.println("\nProductos encontrados: " + productos.size());
        System.out.printf("%-10s %-30s %-15s %-10s\n", "CÓDIGO", "NOMBRE", "TIPO", "STOCK");
        System.out.println("─".repeat(70));
        
        for (Producto p : productos) {
            System.out.printf("%-10s %-30s %-15s %d\n",
                p.getCodigo(),
                p.getNombre(),
                p.getTipoTela(),
                p.getStockActual()
            );
        }
    }

    private static void listarProductosPorTipo() {
        System.out.println("\nTipos de tela disponibles:");
        TipoTela[] tipos = TipoTela.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.printf("  %d. %s\n", i + 1, tipos[i]);
        }
        
        System.out.print("Seleccione tipo (1-" + tipos.length + "): ");
        int tipoIdx = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (tipoIdx < 0 || tipoIdx >= tipos.length) {
            System.out.println("Opción inválida");
            return;
        }
        
        TipoTela tipo = tipos[tipoIdx];
        List<Producto> productos = facade.listarProductosPorTipo(tipo);
        
        if (productos.isEmpty()) {
            System.out.println("No hay productos del tipo: " + tipo);
            return;
        }
        
        System.out.println("\nProductos de tipo " + tipo + ": " + productos.size());
        System.out.printf("%-10s %-30s %-12s %-10s\n", "CÓDIGO", "NOMBRE", "COLOR", "STOCK");
        System.out.println("─".repeat(70));
        
        for (Producto p : productos) {
            System.out.printf("%-10s %-30s %-12s %d\n",
                p.getCodigo(),
                p.getNombre(),
                p.getColor(),
                p.getStockActual()
            );
        }
    }
}