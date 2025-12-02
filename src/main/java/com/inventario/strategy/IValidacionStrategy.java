package com.inventario.strategy;

import com.inventario.model.Producto;

public interface IValidacionStrategy { 
    boolean validar(Producto producto);
    String getNombreEstrategia();
    String getDescripcionReglas();
}