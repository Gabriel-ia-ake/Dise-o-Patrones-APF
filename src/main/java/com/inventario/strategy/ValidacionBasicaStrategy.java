package com.inventario.strategy;

import com.inventario.model.Producto;

public class ValidacionBasicaStrategy implements IValidacionStrategy {

    @Override
    public boolean validar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser null");
        }
        
        if (producto.getCodigo() == null || producto.getCodigo().trim().isEmpty()) {
            throw new IllegalArgumentException("El código del producto es obligatorio");
        }
        
        if (producto.getCodigo().length() < 3 || producto.getCodigo().length() > 20) {
            throw new IllegalArgumentException(
                "El código debe tener entre 3 y 20 caracteres"
            );
        }
        
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto es obligatorio");
        }
        
        if (producto.getNombre().length() < 3) {
            throw new IllegalArgumentException(
                "El nombre debe tener al menos 3 caracteres"
            );
        }
        
        if (producto.getPrecio() <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0");
        }
        
        if (producto.getTipoTela() == null) {
            throw new IllegalArgumentException("El tipo de tela es obligatorio");
        }
        
        return true;
    }

    @Override
    public String getNombreEstrategia() {
        return "Validación Básica";
    }

    @Override
    public String getDescripcionReglas() {
        return "Valida campos obligatorios: código (3-20 chars), nombre (min 3 chars), " +
               "precio (>0) y tipo de tela";
    }
}