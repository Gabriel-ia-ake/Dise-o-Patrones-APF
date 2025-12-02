package com.inventario.observer;

import com.inventario.model.Producto;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificadorConsola implements IObservador {
    
    private static final DateTimeFormatter FORMATO_FECHA = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public void onProductoAgregado(Producto producto) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println(" _______________________________________________________________ ");
        System.out.println("   PRODUCTO AGREGADO - " + timestamp);
        System.out.println("   Código: " + producto.getCodigo());
        System.out.println("   Nombre: " + producto.getNombre());
        System.out.println("   Tipo: " + producto.getTipoTela());
        System.out.println("   Precio: S/ " + String.format("%.2f", producto.getPrecio()));
        System.out.println(" _______________________________________________________________ ");
    }

    @Override
    public void onProductoActualizado(Producto producto) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println(" _______________________________________________________________ ");
        System.out.println("   PRODUCTO ACTUALIZADO - " + timestamp);
        System.out.println("   Código: " + producto.getCodigo());
        System.out.println("   Nombre: " + producto.getNombre());
        System.out.println("   Stock: " + producto.getStockActual() + 
                         " (Mínimo: " + producto.getStockMinimo() + ")");
        System.out.println(" _______________________________________________________________ ");
    }

    @Override
    public void onProductoEliminado(Producto producto) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println(" _______________________________________________________________ ");
        System.out.println(" ️  PRODUCTO ELIMINADO - " + timestamp);
        System.out.println("   Código: " + producto.getCodigo());
        System.out.println("   Nombre: " + producto.getNombre());
        System.out.println(" _______________________________________________________________ ");
    }

    @Override
    public void onStockBajo(Producto producto) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println(" _______________________________________________________________ ");
        System.out.println(" ️  ALERTA: STOCK BAJO - " + timestamp);
        System.out.println("   Código: " + producto.getCodigo());
        System.out.println("   Nombre: " + producto.getNombre());
        System.out.println("   Stock actual: " + producto.getStockActual());
        System.out.println("   Stock mínimo: " + producto.getStockMinimo());
        System.out.println("    ACCIÓN REQUERIDA: Solicitar reabastecimiento");
        System.out.println(" _______________________________________________________________ ");
    }

    @Override
    public void onStockCritico(Producto producto) {
        String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
        System.out.println(" _______________________________________________________________ ");
        System.out.println("   CRÍTICO: SIN STOCK - " + timestamp);
        System.out.println("   Código: " + producto.getCodigo());
        System.out.println("   Nombre: " + producto.getNombre());
        System.out.println("   Stock actual: " + producto.getStockActual());
        System.out.println("    URGENTE: Reponer inmediatamente");
        System.out.println(" _______________________________________________________________ ");
    }
}