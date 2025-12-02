package com.inventario.observer;

import com.inventario.model.Producto;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificadorLog implements IObservador {
    
    private static final String ARCHIVO_LOG = "inventario.log";
    private static final DateTimeFormatter FORMATO_FECHA = 
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    private void escribirLog(String mensaje) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ARCHIVO_LOG, true))) {
            String timestamp = LocalDateTime.now().format(FORMATO_FECHA);
            writer.println("[" + timestamp + "] " + mensaje);
        } catch (IOException e) {
            System.err.println("Error al escribir en log: " + e.getMessage());
        }
    }

    @Override
    public void onProductoAgregado(Producto producto) {
        escribirLog(String.format("PRODUCTO_AGREGADO | Código: %s | Nombre: %s | Tipo: %s | Precio: %.2f",
                producto.getCodigo(), producto.getNombre(), 
                producto.getTipoTela(), producto.getPrecio()));
    }

    @Override
    public void onProductoActualizado(Producto producto) {
        escribirLog(String.format("PRODUCTO_ACTUALIZADO | Código: %s | Stock: %d/%d",
                producto.getCodigo(), producto.getStockActual(), producto.getStockMinimo()));
    }

    @Override
    public void onProductoEliminado(Producto producto) {
        escribirLog(String.format("PRODUCTO_ELIMINADO | Código: %s | Nombre: %s",
                producto.getCodigo(), producto.getNombre()));
    }

    @Override
    public void onStockBajo(Producto producto) {
        escribirLog(String.format("ALERTA_STOCK_BAJO | Código: %s | Stock: %d | Mínimo: %d",
                producto.getCodigo(), producto.getStockActual(), producto.getStockMinimo()));
    }

    @Override
    public void onStockCritico(Producto producto) {
        escribirLog(String.format("CRITICO_SIN_STOCK | Código: %s | Nombre: %s | Stock: %d",
                producto.getCodigo(), producto.getNombre(), producto.getStockActual()));
    }
}