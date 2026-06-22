package integrado.prog2.service;

import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.dao.ProductoDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.CategoriaNoValidaException;
import integrado.prog2.exception.DatosInvalidosException;
import integrado.prog2.exception.ProductoNoEncontradoException;

import java.util.List;

public class ProductoService {

    private ProductoDAO productoDAO;
    private CategoriaDAO categoriaDAO;

    public ProductoService(ProductoDAO productoDAO, CategoriaDAO categoriaDAO){
        this.productoDAO = productoDAO;
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Obtenemos todos los productos disponibles de la BD
     * @return retornamos la lista de productos
     */
    public List<Producto> listarProductos() {
        return productoDAO.listarTodos();
    }

    /**
     * Método para crear un producto utilizando DAO para la BD
     * @param producto Obtenemos el objeto ya creado desde el menú
     * @return retornamos el ID del objeto creado
     */
    public Long crearProducto(Producto producto){
        validarDatos(producto);
        validarCategoria(producto.getCategoriaId());

        return productoDAO.crearProducto(producto);
    }

    /**
     * Método para editar el producto utilizando DAO para la BD
     * @param dto actualizamos el producto usando un dto
     */
    public void editarProducto(foodstore.dto.ActualizarProductoDTO dto) {
        Producto producto = productoDAO.buscarPorId(dto.getId());

        if (producto == null) {
            throw new ProductoNoEncontradoException(
                    "No existe o fue eliminado el producto con id " + dto.getId()
            );
        }

        aplicarPrecio(producto, dto.getPrecio());
        aplicarStock(producto, dto.getStock());
        aplicarCategoria(producto, dto.getCategoriaId());

        productoDAO.actualizarProducto(producto);
    }

    /**
     * Eliminamos un producto utilizando DAO para la BD
     * @param id eliminamos un producto especificando el ID
     */
    public void eliminarProducto(Long id) {
        Producto producto = productoDAO.buscarPorId(id);
        if (producto == null) {
            throw new ProductoNoEncontradoException("No existe o ya fue eliminado el producto con id " + id);
        }

        productoDAO.eliminarProducto(id);
    }

    // ================= MÉTODOS PRIVADOS =================

    /**
     * Validamos los datos que obtenemos del objeto, si son incorrectos lanzamos la excepción de negocio
     * @param producto Obtenemos el objeto que le pasamos al servicio
     */
    private void validarDatos(Producto producto) {
        if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
            throw new DatosInvalidosException("El nombre no puede estar vacío");
        }
        if (producto.getPrecio() < 0) {
            throw new DatosInvalidosException("El precio no puede ser negativo");
        }
        if (producto.getStock() < 0) {
            throw new DatosInvalidosException("El stock no puede ser negativo");
        }
    }

    /**
     * Validamos la categoría asignada al producto, si no existe entonces está eliminada
     * @param categoriaId Verificamos con el ID de la categoría
     */
    private void validarCategoria(Long categoriaId) {
        Categoria categoria = categoriaDAO.buscarPorId(categoriaId);
        if (categoria == null) {
            throw new CategoriaNoValidaException("La categoría con id " + categoriaId + " no existe o fue eliminada");
        }
    }

    /**
     * Aplicamos el nuevo precio al producto
     * @param producto producto que obtenemos con el DAO
     * @param nuevoPrecio nuevo precio que obtenemos del DTO
     */
    private void aplicarPrecio(Producto producto, Double nuevoPrecio) {
        if (nuevoPrecio == null) {
            return; // no se quiere modificar este campo
        }
        validarPrecio(nuevoPrecio);
        producto.setPrecio(nuevoPrecio);
    }

    /**
     * Aplicamos el nuevo stock al producto
     * @param producto producto que obtenemos del DAO
     * @param nuevoStock stock que obtenemos del DTO
     */
    private void aplicarStock(Producto producto, Integer nuevoStock) {
        if (nuevoStock == null) {
            return;
        }
        validarStock(nuevoStock);
        producto.setStock(nuevoStock);
    }

    /**
     * Aplicamos la nueva categoría
     * @param producto producto que obtenemos del DAO
     * @param nuevaCategoriaId categoría que obtenemos del DTO
     */
    private void aplicarCategoria(Producto producto, Long nuevaCategoriaId) {
        if (nuevaCategoriaId == null) {
            return;
        }
        validarCategoria(nuevaCategoriaId);
        producto.setCategoriaId(nuevaCategoriaId);
    }

    private void validarPrecio(double precio) {
        if (precio < 0) {
            throw new DatosInvalidosException("El precio no puede ser negativo");
        }
    }

    private void validarStock(int stock) {
        if (stock < 0) {
            throw new DatosInvalidosException("El stock no puede ser negativo");
        }
    }
}
