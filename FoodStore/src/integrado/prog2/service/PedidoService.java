package integrado.prog2.service;

import integrado.prog2.entities.Pedido;
import integrado.prog2.entities.Producto;
import integrado.prog2.entities.Usuario;

public class PedidoService {

    public Pedido crearPedido(Usuario usuario) {

        if(usuario == null) {
            throw new RuntimeException("Debe seleccionar un usuario.");
        }

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);

        return pedido;
    }

    public void agregarProducto(
            Pedido pedido,
            Producto producto,
            int cantidad) {

        if(cantidad <= 0) {
            throw new RuntimeException("La cantidad debe ser mayor a cero.");
        }

        pedido.addDetallePedido(
                cantidad,
                producto.getPrecio(),
                producto
        );
    }
}