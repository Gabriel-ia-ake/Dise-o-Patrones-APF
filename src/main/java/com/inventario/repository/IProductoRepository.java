package com.inventario.repository;

import com.inventario.model.Producto;
import com.inventario.model.TipoTela;
import java.util.List;
import java.util.Optional;

public interface IProductoRepository {
    
    Producto guardar(Producto producto);
    Producto actualizar(Producto producto);
    boolean eliminar(Long id);
    Optional<Producto> buscarPorId(Long id);
    Optional<Producto> buscarPorCodigo(String codigo);
    List<Producto> obtenerTodos();
    List<Producto> buscarPorTipo(TipoTela tipoTela);
    List<Producto> obtenerProductosConStockBajo();
    List<Producto> buscarPorNombre(String nombre);
    List<Producto> buscarPorColor(String color);
    long contarProductos();
    long contarProductosConStockBajo();
    boolean existePorCodigo(String codigo);
    double calcularValorTotalInventario();
}