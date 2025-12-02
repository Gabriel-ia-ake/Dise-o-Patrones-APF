package com.inventario.strategy;

import com.inventario.model.Producto;

public class ValidacionEstrictaStrategy implements IValidacionStrategy {
    
    private final IValidacionStrategy validacionBasica = new ValidacionBasicaStrategy();

    @Override
    public boolean validar(Producto producto) {
        validacionBasica.validar(producto);

        if (!producto.getCodigo().matches("^[A-Z0-9]+$")) {
            throw new IllegalArgumentException(
                "El código debe contener solo letras mayúsculas y números (sin espacios ni caracteres especiales)"
            );
        }
        
        if (producto.getColor() == null || producto.getColor().trim().isEmpty()) {
            throw new IllegalArgumentException("El color es obligatorio en validación estricta");
        }
        
        if (producto.getPrecio() > 1000) {
            throw new IllegalArgumentException(
                "El precio parece excesivo. Verifique que sea correcto (máximo: S/ 1000)"
            );
        }
        
        if (producto.getPrecio() < 1) {
            throw new IllegalArgumentException(
                "El precio parece muy bajo. Mínimo: S/ 1.00"
            );
        }
        
        if (producto.getStockMinimo() < 1) {
            throw new IllegalArgumentException(
                "El stock mínimo debe ser al menos 1 en validación estricta"
            );
        }
        
        if (producto.getStockActual() > 10000) {
            throw new IllegalArgumentException(
                "El stock actual parece excesivo. Verifique el valor (máximo: 10,000)"
            );
        }
        
        if (producto.getStockActual() > 0 && producto.getStockMinimo() > producto.getStockActual() * 2) {
            throw new IllegalArgumentException(
                "El stock mínimo no puede ser más del doble del stock actual"
            );
        }
        
        return true;
    }

    @Override
    public String getNombreEstrategia() {
        return "Validación Estricta";
    }

    @Override
    public String getDescripcionReglas() {
        return "Validación rigurosa: incluye validación básica + código alfanumérico " +
               "mayúsculas, color obligatorio, rangos de precio (S/ 1-1000), " +
               "stock mínimo ≥1, stock actual ≤10000, coherencia stock actual/mínimo";
    }
}