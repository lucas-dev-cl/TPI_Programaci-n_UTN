package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    public ProductoDAO(){}

    public Long crearProducto(Producto producto) {
        String sql = "INSERT INTO productos " +
                "(nombre, descripcion, precio, stock, imagen, disponible, categoria_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            Long productoId;

            try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, producto.getNombre());
                ps.setString(2, producto.getDescripcion());
                ps.setDouble(3, producto.getPrecio());
                ps.setInt(4, producto.getStock());
                ps.setString(5, producto.getImagen());
                ps.setBoolean(6, producto.isDisponible());
                ps.setLong(7, producto.getCategoriaId());

                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        productoId = rs.getLong(1);
                    } else {
                        throw new SQLException("No se generó un id para el producto");
                    }
                }
            }

            conn.commit();
            return productoId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DataAccessException("Error al hacer rollback al crear producto", ex);
                }
            }
            throw new DataAccessException("Error al crear el producto", e);

        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    throw new DataAccessException("Error al cerrar la conexión", e);
                }
            }
        }
    }

    public Producto buscarPorId(Long id) {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen, disponible, categoria_id " +
                "FROM productos WHERE id = ? AND eliminado = false";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearProducto(rs);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar el producto con id " + id, e);
        }

        return null; // no se encontró
    }

    public List<Producto> listarTodos() {
        String sql = "SELECT id, nombre, descripcion, precio, stock, imagen, disponible, categoria_id " +
                "FROM productos WHERE eliminado = false";

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearProducto(rs));
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar los productos", e);
        }

        return productos;
    }

    public List<Producto> listarPorCategoria(Long categoriaId) {
        String sql = "SELECT p.id, p.nombre, p.descripcion, p.precio, p.stock, p.imagen, " +
                "p.disponible, p.categoria_id, c.nombre AS categoria_nombre " +
                "FROM productos p " +
                "JOIN categoria c ON p.categoria_id = c.id " +
                "WHERE p.eliminado = false AND p.categoria_id = ?";

        List<Producto> productos = new ArrayList<>();

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, categoriaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    productos.add(mapearProducto(rs));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar productos de la categoría " + categoriaId, e);
        }

        return productos;
    }

    public void eliminarProducto(Long id) {
        String sql = "UPDATE productos SET eliminado = true WHERE id = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new DataAccessException("No se encontró el producto con id " + id + " para eliminar");
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar el producto con id " + id, e);
        }
    }

    public void actualizarProducto(Producto producto) {
        String sql = "UPDATE productos SET precio = ?, stock = ?, categoria_id = ? " +
                "WHERE id = ? AND eliminado = false";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, producto.getPrecio());
            ps.setInt(2, producto.getStock());
            ps.setLong(3, producto.getCategoriaId());
            ps.setLong(4, producto.getId());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas == 0) {
                throw new DataAccessException(
                        "No se pudo actualizar: el producto con id " + producto.getId() + " ya no existe o fue eliminado"
                );
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar el producto con id " + producto.getId(), e);
        }
    }

    // MÉTODOS PRIVADOS

    private Producto mapearProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();
        producto.setId(rs.getLong("id"));
        producto.setNombre(rs.getString("nombre"));
        producto.setDescripcion(rs.getString("descripcion"));
        producto.setPrecio(rs.getDouble("precio"));
        producto.setStock(rs.getInt("stock"));
        producto.setImagen(rs.getString("imagen"));
        producto.setDisponible(rs.getBoolean("disponible"));
        producto.setCategoriaId(rs.getLong("categoria_id"));
        return producto;
    }
}
