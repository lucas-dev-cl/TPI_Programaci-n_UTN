package integrado.prog2.menus;

import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.dao.ProductoDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.entities.Producto;
import integrado.prog2.exception.CategoriaNoValidaException;
import integrado.prog2.exception.DataAccessException;
import integrado.prog2.exception.DatosInvalidosException;
import integrado.prog2.exception.ProductoNoEncontradoException;
import integrado.prog2.service.FoodStoreService;
import integrado.prog2.service.ProductoService;

import java.util.List;
import java.util.Scanner;

public class ProductoMenu {

    public ProductoMenu(){

    }

    private final Scanner sc = new Scanner(System.in);

    private ProductoDAO productoDAO = new ProductoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private FoodStoreService foodStoreService = new FoodStoreService(categoriaDAO);

    private final ProductoService productoService = new ProductoService(productoDAO, categoriaDAO);

    /**
     * Mostramos las opciones del producto y con los case activamos cada funcion correspondiente
     */
    public void mostrar() {
        boolean volver = false;

        while (!volver) {
            mostrarOpciones();
            int opcion = leerOpcion();

            switch (opcion) {
                case 1 -> listar();
                case 2 -> crear();
                case 3 -> editar();
                case 4 -> eliminar();
                case 0 -> volver = true;
                default -> System.out.println("Opción fuera de rango. Intente de nuevo.");
            }
        }
    }

    /**
     * Opciones que verá el usuario
     */
    private void mostrarOpciones() {
        System.out.println("\n--- PRODUCTOS ---");
        System.out.println("1. Listar");
        System.out.println("2. Crear");
        System.out.println("3. Editar");
        System.out.println("4. Eliminar");
        System.out.println("0. Volver");
        System.out.print("Seleccione: ");
    }

    private void listar() {
        System.out.println("\n¿Cómo desea listar los productos?");
        System.out.println("1. Todos los productos");
        System.out.println("2. Filtrar por categoría");
        System.out.print("Seleccione: ");

        int opcion = leerOpcion();

        List<Producto> productos;

        switch (opcion) {
            case 1 -> productos = productoService.listarProductos();
            case 2 -> {
                mostrarCategoriasDisponibles();

                Long categoriaId = leerLong("Ingrese el ID de la categoría: ");
                if (categoriaId == null) return;

                try {
                    productos = productoService.listarProductosPorCategoria(categoriaId);
                } catch (CategoriaNoValidaException e) {
                    System.out.println("No se pudo listar: " + e.getMessage());
                    return;
                }
            }
            default -> {
                System.out.println("Opción no válida");
                return;
            }
        }

        mostrarProductos(productos);
    }

    private void mostrarCategoriasDisponibles() {
        List<Categoria> categorias = foodStoreService.obtenerCategoriasActivas();

        if (categorias.isEmpty()) {
            System.out.println("No hay categorías registradas");
            return;
        }

        System.out.println("\n--- CATEGORÍAS DISPONIBLES ---");
        for (Categoria c : categorias) {
            System.out.println(c.getId() + " - " + c.getNombre());
        }
    }

    private void mostrarProductos(List<Producto> productos) {
        if (productos.isEmpty()) {
            System.out.println("No hay productos para mostrar");
            return;
        }

        System.out.println("\n--- PRODUCTOS ---");
        for (Producto p : productos) {
            System.out.println(p.getId() + " - " + p.getNombre() + " - $" + p.getPrecio() +
                    " - stock: " + p.getStock() + " - ID Categoria: " + p.getCategoriaId());
        }
    }

    private void crear() {
        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Descripción: ");
        String descripcion = sc.nextLine();

        Double precio = leerDouble("Precio: ");
        if (precio == null) return;

        Integer stock = leerInt("Stock: ");
        if (stock == null) return;

        System.out.print("Imagen (ruta o url): ");
        String imagen = sc.nextLine();

        System.out.print("¿Disponible? (s/n): ");
        boolean disponible = sc.nextLine().equalsIgnoreCase("s");

        Long categoriaId = leerLong("ID de categoría: ");
        if (categoriaId == null) return;

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStock(stock);
        producto.setImagen(imagen);
        producto.setDisponible(disponible);
        producto.setCategoriaId(categoriaId);

        try {
            Long id = productoService.crearProducto(producto);
            System.out.println("Producto creado correctamente. ID generado: " + id);
        } catch (DatosInvalidosException | CategoriaNoValidaException e) {
            System.out.println("No se pudo crear el producto: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Ocurrió un error al guardar el producto. Intente más tarde.");
        }
    }

    private void editar() {
        listar(); // mostramos antes, según la pauta de "facilitar pruebas"

        Long id = leerLong("ID del producto a editar: ");
        if (id == null) return;

        System.out.print("Nuevo precio (Enter para no modificar): ");
        Double nuevoPrecio = leerDoubleOpcional(sc.nextLine());

        System.out.print("Nuevo stock (Enter para no modificar): ");
        Integer nuevoStock = leerIntOpcional(sc.nextLine());

        System.out.print("Nueva categoría id (Enter para no modificar): ");
        Long nuevaCategoriaId = leerLongOpcional(sc.nextLine());

        foodstore.dto.ActualizarProductoDTO datos = new foodstore.dto.ActualizarProductoDTO(id, nuevoPrecio, nuevoStock, nuevaCategoriaId);

        try {
            productoService.editarProducto(datos);
            System.out.println("Producto id " + id + " actualizado correctamente");
        } catch (ProductoNoEncontradoException | CategoriaNoValidaException |
                 DatosInvalidosException e) {
            System.out.println("No se pudo actualizar: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Ocurrió un error al guardar los cambios. Intente más tarde.");
        }
    }

    private void eliminar() {
        listar();

        Long id = leerLong("ID del producto a eliminar: ");
        if (id == null) return;

        System.out.print("¿Está seguro que desea eliminar el producto " + id + "? (s/n): ");
        String confirmacion = sc.nextLine();

        if (!confirmacion.equalsIgnoreCase("s")) {
            System.out.println("Operación cancelada");
            return;
        }

        try {
            productoService.eliminarProducto(id);
            System.out.println("Producto id " + id + " eliminado correctamente");
        } catch (ProductoNoEncontradoException e) {
            System.out.println("No se pudo eliminar: " + e.getMessage());
        } catch (DataAccessException e) {
            System.out.println("Ocurrió un error al eliminar el producto. Intente más tarde.");
        }
    }

    // --- helpers de lectura con validación ---

    private int leerOpcion() {
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private Double leerDouble(String mensaje) {
        System.out.print(mensaje);
        try {
            return Double.parseDouble(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido");
            return null;
        }
    }

    private Integer leerInt(String mensaje) {
        System.out.print(mensaje);
        try {
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido");
            return null;
        }
    }

    private Long leerLong(String mensaje) {
        System.out.print(mensaje);
        try {
            return Long.parseLong(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Valor numérico inválido");
            return null;
        }
    }

    private Double leerDoubleOpcional(String input) {
        if (input.isBlank()) return null;
        try {
            return Double.parseDouble(input);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, se ignora el cambio de precio");
            return null;
        }
    }

    private Integer leerIntOpcional(String input) {
        if (input.isBlank()) return null;
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, se ignora el cambio de stock");
            return null;
        }
    }

    private Long leerLongOpcional(String input) {
        if (input.isBlank()) return null;
        try {
            return Long.parseLong(input);
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido, se ignora el cambio de categoría");
            return null;
        }
    }
}
