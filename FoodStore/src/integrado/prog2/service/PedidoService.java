package integrado.prog2.service;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.dao.DetallePedidoDAO;
import integrado.prog2.dao.PedidoDAO;
import integrado.prog2.dao.UsuarioDAO;
import integrado.prog2.entities.DetallePedido;
import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.exception.DataAccessException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final DetallePedidoDAO detallePedidoDAO;
    private final UsuarioDAO usuarioDAO;

    public PedidoService(PedidoDAO pedidoDAO, DetallePedidoDAO detallePedidoDAO, UsuarioDAO usuarioDAO) {
        this.pedidoDAO = pedidoDAO;
        this.detallePedidoDAO = detallePedidoDAO;
        this.usuarioDAO = usuarioDAO;
    }

    public List<Pedido> listarPedidos() {
        return pedidoDAO.listar();
    }

    public Pedido buscarPedidoPorId(Long id) {
        Pedido pedido = pedidoDAO.buscarPorId(id);

        if (pedido == null) {
            throw new RuntimeException("No existe un pedido activo con ID: " + id);
        }

        List<DetallePedido> detalles = detallePedidoDAO.listarPorPedido(id);
        pedido.setDetalles(detalles);

        return pedido;
    }

    public Pedido iniciarPedido(Long usuarioId, FormaPago formaPago) {
        Usuario usuario = usuarioDAO.buscarPorId(usuarioId);

        if (usuario == null) {
            throw new RuntimeException("El usuario no existe o fue eliminado.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFormaPago(formaPago);
        pedido.setEstado(Estado.PENDIENTE);
        pedido.setTotal(0.0);

        pedidoDAO.crear(pedido);

        return pedido;
    }

    public void agregarDetalleAPedido(Long pedidoId, Long productoId, int cantidad) {
        if (cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero.");
        }

        Pedido pedido = pedidoDAO.buscarPorId(pedidoId);

        if (pedido == null) {
            throw new RuntimeException("El pedido no existe o fue eliminado.");
        }

        Producto producto = buscarProductoPorId(productoId);

        if (producto == null) {
            throw new RuntimeException("El producto no existe o fue eliminado.");
        }

        if (!producto.isDisponible()) {
            throw new RuntimeException("El producto no está disponible.");
        }

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente. Stock actual: " + producto.getStock());
        }

        pedido.setDetalles(detallePedidoDAO.listarPorPedido(pedidoId));
        pedido.addDetallePedido(cantidad, producto.getPrecio(), producto);

        DetallePedido nuevoDetalle = pedido.getDetalles().get(pedido.getDetalles().size() - 1);

        detallePedidoDAO.crear(pedidoId, nuevoDetalle);
        descontarStock(productoId, cantidad);

        pedido.calcularTotal();
        pedidoDAO.actualizarTotal(pedidoId, pedido.getTotal());
    }

    public void actualizarEstadoYFormaPago(Long pedidoId, Estado estado, FormaPago formaPago) {
        Pedido pedido = pedidoDAO.buscarPorId(pedidoId);

        if (pedido == null) {
            throw new RuntimeException("El pedido no existe o fue eliminado.");
        }

        pedido.setEstado(estado);
        pedido.setFormaPago(formaPago);

        pedidoDAO.actualizarEstadoYFormaPago(pedido);
    }

    public void eliminarPedido(Long pedidoId) {
        Pedido pedido = pedidoDAO.buscarPorId(pedidoId);

        if (pedido == null) {
            throw new RuntimeException("El pedido no existe o ya fue eliminado.");
        }

        detallePedidoDAO.eliminarLogicoPorPedido(pedidoId);
        pedidoDAO.eliminarLogico(pedidoId);
    }

    private Producto buscarProductoPorId(Long id) {
        String sql = "SELECT * FROM productos WHERE id = ? AND eliminado = false";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Producto(
                            rs.getLong("id"),
                            rs.getString("nombre"),
                            rs.getString("descripcion"),
                            rs.getDouble("precio"),
                            rs.getInt("stock"),
                            rs.getString("imagen"),
                            rs.getBoolean("disponible"),
                            rs.getBoolean("eliminado"),
                            rs.getLong("categoria_id")
                    );
                }
            }

        } catch (SQLException e) {
            throw new DataAccessException("Error al buscar producto", e);
        }

        return null;
    }

    private void descontarStock(Long productoId, int cantidad) {
        String sql = "UPDATE productos SET stock = stock - ? WHERE id = ?";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, cantidad);
            ps.setLong(2, productoId);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new DataAccessException("Error al descontar stock del producto", e);
        }
    }
}