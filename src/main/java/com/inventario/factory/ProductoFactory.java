package com.inventario.factory;

import com.inventario.model.Producto;
import com.inventario.model.ProductoBuilder;
import com.inventario.model.TipoTela;

public class ProductoFactory {

    public static Producto crearProducto(String codigo, String nombre, TipoTela tipoTela,
                                        String color, double precio, int stock) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(tipoTela)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(stock)
                .conStockMinimo(calcularStockMinimo(tipoTela))
                .build();
    }

    public static Producto crearAlgodon(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.ALGODON)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(10)
                .build();
    }

    public static Producto crearPoliester(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.POLIESTER)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(15)
                .build();
    }

    public static Producto crearDenim(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.DENIM)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(12)
                .build();
    }

    public static Producto crearSeda(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.SEDA)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(5)
                .build();
    }

    public static Producto crearLino(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.LINO)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(8)
                .build();
    }

    public static Producto crearLycra(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.LYCRA)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(15)
                .build();
    }

    public static Producto crearLana(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.LANA)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(10)
                .build();
    }

    public static Producto crearNylon(String codigo, String nombre, String color, double precio) {
        return new ProductoBuilder()
                .conCodigo(codigo)
                .conNombre(nombre)
                .conTipoTela(TipoTela.NYLON)
                .conColor(color)
                .conPrecio(precio)
                .conStockActual(0)
                .conStockMinimo(10)
                .build();
    }

    public static Producto crearPorTipo(TipoTela tipo, String codigo, String nombre, 
                                       String color, double precio) {
        return switch (tipo) {
            case ALGODON -> crearAlgodon(codigo, nombre, color, precio);
            case POLIESTER -> crearPoliester(codigo, nombre, color, precio);
            case DENIM -> crearDenim(codigo, nombre, color, precio);
            case SEDA -> crearSeda(codigo, nombre, color, precio);
            case LINO -> crearLino(codigo, nombre, color, precio);
            case LYCRA -> crearLycra(codigo, nombre, color, precio);
            case LANA -> crearLana(codigo, nombre, color, precio);
            case NYLON -> crearNylon(codigo, nombre, color, precio);
        };
    }

    private static int calcularStockMinimo(TipoTela tipoTela) {
        return switch (tipoTela) {
            case SEDA -> 5;
            case LINO -> 8;
            case ALGODON, LANA, NYLON -> 10;
            case DENIM -> 12;
            case POLIESTER, LYCRA -> 15;
        };
    }
    
    public static Producto[] crearProductosDemostracion() {
        return new Producto[] {
            crearAlgodon("ALG001", "Algodón Blanco Premium", "Blanco", 25.50),
            crearPoliester("POL001", "Poliéster Negro Básico", "Negro", 18.75),
            crearDenim("DEN001", "Denim Azul Clásico", "Azul", 35.00),
            crearSeda("SED001", "Seda Rosa Natural", "Rosa", 85.00),
            crearLino("LIN001", "Lino Beige Verano", "Beige", 42.50)
        };
    }
}