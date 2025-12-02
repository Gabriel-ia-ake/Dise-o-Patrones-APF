package com.inventario.model;

public enum TipoTela {
    ALGODON("Algodón", "Material natural y transpirable"),
    POLIESTER("Poliéster", "Material sintético resistente"),
    DENIM("Denim", "Tejido de algodón resistente para jeans"),
    SEDA("Seda", "Material natural de lujo y suave"),
    LINO("Lino", "Material natural fresco ideal para verano"),
    LYCRA("Lycra", "Material elástico para ropa deportiva"),
    LANA("Lana", "Material natural cálido para invierno"),
    NYLON("Nylon", "Material sintético resistente y ligero");

    private final String nombre;
    private final String descripcion;

    TipoTela(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
    
    public static TipoTela fromString(String texto) {
        for (TipoTela tipo : TipoTela.values()) {
            if (tipo.nombre.equalsIgnoreCase(texto) || 
                tipo.name().equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de tela no válido: " + texto);
    }
}