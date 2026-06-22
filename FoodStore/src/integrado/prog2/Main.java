package integrado.prog2;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.dao.DetallePedidoDAO;
import integrado.prog2.dao.PedidoDAO;
import integrado.prog2.dao.UsuarioDAO;
import integrado.prog2.enums.Estado;
import integrado.prog2.enums.FormaPago;
import integrado.prog2.enums.Rol;
import integrado.prog2.menus.ProductoMenu;
import integrado.prog2.service.FoodStoreService;
import integrado.prog2.service.PedidoService;
import integrado.prog2.service.UsuarioService;

import java.util.Scanner;

public class Main {
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private static final Scanner leer = new Scanner(System.in);
    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final PedidoDAO pedidoDAO = new PedidoDAO();
    private static final DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
    private static final PedidoService pedidoService = new PedidoService(pedidoDAO, detallePedidoDAO, usuarioDAO);
    private static final FoodStoreService servicio = new FoodStoreService(categoriaDAO);
    private static final UsuarioService usuarioService = new UsuarioService(usuarioDAO);

    private static final ProductoMenu productoMenu = new ProductoMenu();

    public static void main(String[] args) {
        int opcion;
        // PRUEBA DE CONEXION PROVISORIA
        try {
            System.out.println("Intentando conectar a pedidos_db...");
            java.sql.Connection testCon = ConexionDB.getConexion();
            if (testCon != null && !testCon.isClosed()) {
                System.out.println("==================================================================");
                System.out.println("¡EXITO TOTAL! Tu cOdigo de NetBeans se conectó perfectamente a MySQL.");
                System.out.println("==================================================================");
                testCon.close();
            }
        } catch (java.sql.SQLException e) {
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
            System.out.println("ERROR DE CONEXIÓN EN EL MAIN: " + e.getMessage());
            System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
        }
        // FIN DE LA PRUEBA
        do {
            System.out.println("\n=== FOOD STORE - MENU PRINCIPAL ===");
            System.out.println("1. GESTION DE CATEGORIAS ");
            System.out.println("2. GESTION DE PRODUCTOS ");
            System.out.println("3. GESTION DE USUARIOS ");
            System.out.println("4. GESTION DE PEDIDOS ");
            System.out.println("0. SALIR");
            System.out.print("Seleccione una opcion: ");
            opcion = Integer.parseInt(leer.nextLine());

            switch (opcion) {
                case 1 -> menuCategorias();
                case 2 -> productoMenu.mostrar();
                case 3 -> menuUsuarios();
                case 4 -> menuPedidos();
                case 0 -> System.out.println("Saliendo del sistema...");
            }
        } while (opcion != 0);
    }

    private static void menuCategorias() {
        int op;

        do {
            System.out.println("\n--- SUB MENU GESTION DE CATEGORIAS ---");
            System.out.println("1. Listar todas las Categorias Activas");
            System.out.println("2. Crear nueva Categoria");
            System.out.println("3. Eliminar Categoria ");
            System.out.println("4. Actualizar Categoria ");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            try {
                op = Integer.parseInt(leer.nextLine());

                switch (op) {
                    case 1 -> {
                        var listaActivas = servicio.obtenerCategoriasActivas();
                        if (listaActivas.isEmpty()) {
                            System.out.println("No se encontraron categorias registradas.");
                        } else {
                            System.out.println("\n--- CATEGORIAS ENCONTRADAS ---");
                            listaActivas.forEach(System.out::println);
                        }
                    }

                    case 2 -> {
                        System.out.print("Ingrese nombre de la categoria: ");
                        String nom = leer.nextLine();
                        System.out.print("Ingrese descripcion de la categoria: ");
                        String desc = leer.nextLine();
                        servicio.registrarCategoria(nom, desc);
                        System.out.println("¡Categoria guardada exitosamente en MySQL!");
                    }

                    case 3 -> {
                        System.out.print("Ingrese el ID de la categoria a dar de baja: ");
                        Long id = Long.parseLong(leer.nextLine());
                        servicio.darBajaLogicaCategoria(id);
                        System.out.println("¡Baja logica aplicada con exito!");
                    }

                    case 4 -> {
                        System.out.print("Ingrese el ID de la categoria para actualizarla: ");
                        Long id = Long.parseLong(leer.nextLine());

                        System.out.print("Ingrese el nuevo nombre: ");
                        String nuevoNombre = leer.nextLine();

                        System.out.print("Ingrese la nueva descripcion: ");
                        String nuevaDescripcion = leer.nextLine();

                        servicio.actualizarCategoria(id, nuevoNombre, nuevaDescripcion);
                        System.out.println("Actualizacion exitosa!");
                    }

                    case 0 -> {
                        System.out.println("Regresando al menu principal...");
                    }

                    default -> {
                        System.out.println("Opcion no valida. Intente de nuevo.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: ¡Debe ingresar un numero valido!");
                op = -1;

            } catch (Exception e) {
                System.out.println("Error de Persistencia: " + e.getMessage());
                op = -1;
            }

        } while (op != 0);
    }

    private static void menuUsuarios() {
        int op;

        do {
            System.out.println("\n--- GESTION DE USUARIOS ---");
            System.out.println("1. Listar usuarios");
            System.out.println("2. Crear usuario");
            System.out.println("3. Modificar usuario");
            System.out.println("4. Eliminar usuario");
            System.out.println("0. Volver");
            System.out.print("Seleccione una opcion: ");

            try {
                op = Integer.parseInt(leer.nextLine());

                switch (op) {
                    case 1 -> {
                        var usuarios = usuarioService.obtenerUsuariosActivos();
                        if (usuarios.isEmpty()) {
                            System.out.println("No hay usuarios registrados.");
                        } else {
                            System.out.println("\n--- USUARIOS ENCONTRADOS ---");
                            usuarios.forEach(System.out::println);
                        }
                    }

                    case 2 -> {
                        System.out.print("Nombre: ");
                        String nombre = leer.nextLine();

                        System.out.print("Apellido: ");
                        String apellido = leer.nextLine();

                        System.out.print("Mail: ");
                        String mail = leer.nextLine();

                        System.out.print("Celular: ");
                        String celular = leer.nextLine();

                        System.out.print("Contraseña: ");
                        String pass = leer.nextLine();

                        usuarioService.registrarUsuario(nombre, apellido, mail, celular, pass, Rol.USUARIO);
                        System.out.println("Usuario creado exitosamente.");
                    }

                    case 3 -> {
                        System.out.print("Ingrese el ID del usuario a modificar: ");
                        Long id = Long.parseLong(leer.nextLine());

                        System.out.print("Nuevo nombre: ");
                        String nombre = leer.nextLine();

                        System.out.print("Nuevo apellido: ");
                        String apellido = leer.nextLine();

                        System.out.print("Nuevo mail: ");
                        String mail = leer.nextLine();

                        System.out.print("Nuevo celular: ");
                        String celular = leer.nextLine();

                        usuarioService.actualizarUsuario(id, nombre, apellido, mail, celular);
                        System.out.println("Usuario actualizado exitosamente.");
                    }

                    case 4 -> {
                        System.out.print("Ingrese el ID del usuario a eliminar: ");
                        Long id = Long.parseLong(leer.nextLine());

                        System.out.print("¿Confirma eliminar el usuario? S/N: ");
                        String confirma = leer.nextLine();

                        if (confirma.equalsIgnoreCase("S")) {
                            usuarioService.darBajaLogicaUsuario(id);
                            System.out.println("Usuario eliminado logicamente.");
                        } else {
                            System.out.println("Operacion cancelada.");
                        }
                    }

                    case 0 -> System.out.println("Regresando al menu principal...");

                    default -> System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero valido.");
                op = -1;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                op = -1;
            }

        } while (op != 0);
    }

    private static void menuPedidos() {
        int op;

        do {
            System.out.println("\n--- SUB MENU GESTION DE PEDIDOS ---");
            System.out.println("1. Listar pedidos");
            System.out.println("2. Crear pedido");
            System.out.println("3. Agregar detalle a pedido");
            System.out.println("4. Cambiar estado y forma de pago");
            System.out.println("5. Eliminar pedido");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            try {
                op = Integer.parseInt(leer.nextLine());

                switch (op) {
                    case 1 -> {
                        var pedidos = pedidoService.listarPedidos();

                        if (pedidos.isEmpty()) {
                            System.out.println("No hay pedidos cargados.");
                        } else {
                            System.out.println("\n--- PEDIDOS ENCONTRADOS ---");
                            pedidos.forEach(System.out::println);
                        }
                    }

                    case 2 -> {
                        System.out.print("Ingrese ID del usuario: ");
                        Long usuarioId = Long.parseLong(leer.nextLine());

                        FormaPago formaPago = seleccionarFormaPago();

                        var pedido = pedidoService.iniciarPedido(usuarioId, formaPago);

                        System.out.println("Pedido creado exitosamente con ID: " + pedido.getId());
                    }

                    case 3 -> {
                        System.out.print("Ingrese ID del pedido: ");
                        Long pedidoId = Long.parseLong(leer.nextLine());

                        System.out.print("Ingrese ID del producto: ");
                        Long productoId = Long.parseLong(leer.nextLine());

                        System.out.print("Ingrese cantidad: ");
                        int cantidad = Integer.parseInt(leer.nextLine());

                        pedidoService.agregarDetalleAPedido(pedidoId, productoId, cantidad);

                        System.out.println("Detalle agregado correctamente. Stock y total actualizados.");
                    }

                    case 4 -> {
                        System.out.print("Ingrese ID del pedido: ");
                        Long pedidoId = Long.parseLong(leer.nextLine());

                        Estado estado = seleccionarEstado();
                        FormaPago formaPago = seleccionarFormaPago();

                        pedidoService.actualizarEstadoYFormaPago(pedidoId, estado, formaPago);

                        System.out.println("Pedido actualizado correctamente.");
                    }

                    case 5 -> {
                        System.out.print("Ingrese ID del pedido a eliminar: ");
                        Long pedidoId = Long.parseLong(leer.nextLine());

                        System.out.print("¿Confirma eliminar el pedido? S/N: ");
                        String confirma = leer.nextLine();

                        if (confirma.equalsIgnoreCase("S")) {
                            pedidoService.eliminarPedido(pedidoId);
                            System.out.println("Pedido eliminado logicamente.");
                        } else {
                            System.out.println("Operacion cancelada.");
                        }
                    }

                    case 0 -> System.out.println("Regresando al menu principal...");

                    default -> System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un numero valido.");
                op = -1;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                op = -1;
            }

        } while (op != 0);
    }

    private static Estado seleccionarEstado() {
        System.out.println("\nSeleccione estado:");
        System.out.println("1. PENDIENTE");
        System.out.println("2. CONFIRMADO");
        System.out.println("3. TERMINADO");
        System.out.println("4. CANCELADO");
        System.out.print("Opcion: ");

        int opcion = Integer.parseInt(leer.nextLine());

        return switch (opcion) {
            case 1 -> Estado.PENDIENTE;
            case 2 -> Estado.CONFIRMADO;
            case 3 -> Estado.TERMINADO;
            case 4 -> Estado.CANCELADO;
            default -> throw new RuntimeException("Estado invalido.");
        };
    }

    private static FormaPago seleccionarFormaPago() {
        System.out.println("\nSeleccione forma de pago:");
        System.out.println("1. TARJETA");
        System.out.println("2. TRANSFERENCIA");
        System.out.println("3. EFECTIVO");
        System.out.print("Opcion: ");

        int opcion = Integer.parseInt(leer.nextLine());

        return switch (opcion) {
            case 1 -> FormaPago.TARJETA;
            case 2 -> FormaPago.TRANSFERENCIA;
            case 3 -> FormaPago.EFECTIVO;
            default -> throw new RuntimeException("Forma de pago invalida.");
        };
    }
}