package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    public void crear(Pedido pedido) {
        String sql = "INSERT INTO pedido(fecha, estado, total, forma_pago, usuario_id, eliminado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(pedido.getFecha()));
            ps.setString(2, pedido.getEstado().name());
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getFormaPago() != null ? pedido.getFormaPago().name() : null);
            ps.setLong(5, pedido.getUsuario().getId());
            ps.setBoolean(6, pedido.isEliminado());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    pedido.setId(rs.getLong(1));
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al crear pedido", e);
        }
    }

    public List<Pedido> listar() {
        List<Pedido> lista = new ArrayList<>();

        String sql = """
                SELECT p.*, u.nombre, u.apellido, u.mail, u.celular, u.contrasenia, u.rol
                FROM pedido p
                INNER JOIN usuario u ON p.usuario_id = u.id
                WHERE p.eliminado = false
                """;

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Pedido pedido = mapearPedido(rs);
                lista.add(pedido);
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al listar pedidos", e);
        }

        return lista;
    }

    public Pedido buscarPorId(Long id) {
        String sql = """
                SELECT p.*, u.nombre, u.apellido, u.mail, u.celular, u.contrasenia, u.rol
                FROM pedido p
                INNER JOIN usuario u ON p.usuario_id = u.id
                WHERE p.id = ? AND p.eliminado = false
                """;

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPedido(rs);
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar pedido", e);
        }

        return null;
    }

    public void actualizarEstadoYFormaPago(Pedido pedido) {
        String sql = "UPDATE pedido SET estado = ?, forma_pago = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, pedido.getEstado().name());
            ps.setString(2, pedido.getFormaPago().name());
            ps.setLong(3, pedido.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar pedido", e);
        }
    }

    public void actualizarTotal(Long pedidoId, Double total) {
        String sql = "UPDATE pedido SET total = ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDouble(1, total);
            ps.setLong(2, pedidoId);

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al actualizar total del pedido", e);
        }
    }

    public void eliminarLogico(Long id) {
        String sql = "UPDATE pedido SET eliminado = true WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al eliminar pedido", e);
        }
    }

    private Pedido mapearPedido(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();

        usuario.setId(rs.getLong("usuario_id"));
        usuario.setNombre(rs.getString("nombre"));
        usuario.setApellido(rs.getString("apellido"));
        usuario.setMail(rs.getString("mail"));
        usuario.setCelular(rs.getString("celular"));
        usuario.setContrasenia(rs.getString("contrasenia"));
        usuario.setRol(integrado.prog2.enums.Rol.valueOf(rs.getString("rol")));

        Pedido pedido = new Pedido();

        pedido.setId(rs.getLong("id"));
        pedido.setFecha(rs.getDate("fecha").toLocalDate());
        pedido.setEstado(Estado.valueOf(rs.getString("estado")));
        pedido.setTotal(rs.getDouble("total"));

        String formaPago = rs.getString("forma_pago");
        if (formaPago != null) {
            pedido.setFormaPago(FormaPago.valueOf(formaPago));
        }

        pedido.setUsuario(usuario);
        pedido.setEliminado(rs.getBoolean("eliminado"));

        return pedido;
    }
}