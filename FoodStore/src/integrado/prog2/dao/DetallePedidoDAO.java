package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetallePedidoDAO {

    public void crear(Long pedidoId, DetallePedido detalle) {
        String sql = "INSERT INTO detalle_pedido(cantidad, subtotal, pedido_id, producto_id, eliminado) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, detalle.getCantidad());
            ps.setDouble(2, detalle.getSubtotal());
            ps.setLong(3, pedidoId);
            ps.setLong(4, detalle.getProducto().getId());
            ps.setBoolean(5, detalle.isEliminado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    detalle.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al crear detalle de pedido", e);
        }
    }

    public List<DetallePedido> listarPorPedido(Long pedidoId) {
        List<DetallePedido> lista = new ArrayList<>();

        String sql = """
                SELECT d.*, pr.nombre, pr.descripcion, pr.precio, pr.stock, pr.imagen, pr.disponible, pr.categoria_id
                FROM detalle_pedido d
                INNER JOIN productos pr ON d.producto_id = pr.id
                WHERE d.pedido_id = ? AND d.eliminado = false
                """;

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, pedidoId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto producto = new Producto(
                            rs.getLong("producto_id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getString("imagen"),
                            rs.getBoolean("disponible"),
                            false,
                            rs.getLong("categoria_id")
                    );

                    DetallePedido detalle = new DetallePedido();

                    detalle.setId(rs.getLong("id"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setSubtotal(rs.getDouble("subtotal"));
                    detalle.setProducto(producto);
                    detalle.setEliminado(rs.getBoolean("eliminado"));

                    lista.add(detalle);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar detalles del pedido", e);
        }

        return lista;
    }

    public void eliminarLogicoPorPedido(Long pedidoId) {
        String sql = "UPDATE detalle_pedido SET eliminado = true WHERE pedido_id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, pedidoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar detalles del pedido", e);
        }
    }
}