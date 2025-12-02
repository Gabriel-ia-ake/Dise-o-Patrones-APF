package com.inventario.model;

public class ProductoBuilder {
    
    // Atributos del builder
    private String codigo;
    private String nombre;
    private TipoTela tipoTela;
    private String color;
    private double precio;
    private int stockActual;
    private int stockMinimo;

    public ProductoBuilder() {
        this.stockActual = 0;
        this.stockMinimo = 5;
        this.tipoTela = TipoTela.ALGODON;
        this.color = "Sin especificar";
    }


    public ProductoBuilder conCodigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public ProductoBuilder conNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public ProductoBuilder conTipoTela(TipoTela tipoTela) {
        this.tipoTela = tipoTela;
        return this;
    }

    public ProductoBuilder conColor(String color) {
        this.color = color;
        return this;
    }

    public ProductoBuilder conPrecio(double precio) {
        this.precio = precio;
        return this;
    }

    public ProductoBuilder conStockActual(int stockActual) {
        this.stockActual = stockActual;
        return this;
    }

    public ProductoBuilder conStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
        return this;
    }

    public Producto build() {
        validar();
        return new Producto(codigo, nombre, tipoTela, color, precio, stockActual, stockMinimo);
    }

    private void validar() {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new IllegalStateException("El código es obligatorio");
        }
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalStateException("El nombre es obligatorio");
        }
        if (tipoTela == null) {
            throw new IllegalStateException("El tipo de tela es obligatorio");
        }
        if (precio <= 0) {
            throw new IllegalStateException("El precio debe ser mayor a 0");
        }
        if (stockActual < 0) {
            throw new IllegalStateException("El stock actual no puede ser negativo");
        }
        if (stockMinimo < 0) {
            throw new IllegalStateException("El stock mínimo no puede ser negativo");
        }
    }

    public static ProductoBuilder desde(Producto producto) {
        return new ProductoBuilder()
                .conCodigo(producto.getCodigo())
                .conNombre(producto.getNombre())
                .conTipoTela(producto.getTipoTela())
                .conColor(producto.getColor())
                .conPrecio(producto.getPrecio())
                .conStockActual(producto.getStockActual())
                .conStockMinimo(producto.getStockMinimo());
    }
    
    public ProductoBuilder reset() {
        this.codigo = null;
        this.nombre = null;
        this.tipoTela = TipoTela.ALGODON;
        this.color = "Sin especificar";
        this.precio = 0;
        this.stockActual = 0;
        this.stockMinimo = 5;
        return this;
    }
}