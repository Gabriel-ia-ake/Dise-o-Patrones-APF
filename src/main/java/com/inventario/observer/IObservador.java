package com.inventario.observer;

import com.inventario.model.Producto;

public interface IObservador {
    void onProductoAgregado(Producto producto);
    void onProductoActualizado(Producto producto);
    void onProductoEliminado(Producto producto);
    void onStockBajo(Producto producto);
    void onStockCritico(Producto producto);
}