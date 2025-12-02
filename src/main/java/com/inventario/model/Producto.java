package com.inventario.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Producto {
    
    // Atributos
    private Long id;
    private String codigo;
    private String nombre;
    private TipoTela tipoTela;
    private String color;
    private double precio;
    private int stockActual;
    private int stockMinimo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    private boolean activo;

    public Producto() {
        this.fechaCreacion = LocalDateTime.now();
        this.fechaActualizacion = LocalDateTime.now();
        this.activo = true;
    }

    public Producto(String codigo, String nombre, TipoTela tipoTela, 
                   String color, double precio, int stockActual, int stockMinimo) {
        this();
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipoTela = tipoTela;
        this.color = color;
        this.precio = precio;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public TipoTela getTipoTela() {
        return tipoTela;
    }

    public void setTipoTela(TipoTela tipoTela) {
        this.tipoTela = tipoTela;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0) {
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        if (stockActual < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
        this.stockActual = stockActual;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        if (stockMinimo < 0) {
            throw new IllegalArgumentException("El stock mínimo no puede ser negativo");
        }
        this.stockMinimo = stockMinimo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public boolean tieneStockBajo() {
        return stockActual <= stockMinimo;
    }

    public void incrementarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        this.stockActual += cantidad;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void decrementarStock(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad debe ser positiva");
        }
        if (this.stockActual < cantidad) {
            throw new IllegalStateException(
                String.format("Stock insuficiente. Disponible: %d, Solicitado: %d", 
                             stockActual, cantidad)
            );
        }
        this.stockActual -= cantidad;
        this.fechaActualizacion = LocalDateTime.now();
    }

    public double getValorInventario() {
        return precio * stockActual;
    }
    
    public String getEstadoStock() {
        if (stockActual == 0) {
            return "CRÍTICO";
        } else if (tieneStockBajo()) {
            return "BAJO";
        } else {
            return "NORMAL";
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Producto producto = (Producto) o;
        return Objects.equals(codigo, producto.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return String.format("Producto{id=%d, codigo='%s', nombre='%s', tipoTela=%s, " +
                           "color='%s', precio=%.2f, stockActual=%d, stockMinimo=%d, " +
                           "estado='%s', activo=%b}",
                id, codigo, nombre, tipoTela, color, precio, 
                stockActual, stockMinimo, getEstadoStock(), activo);
    }
}