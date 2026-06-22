/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;
import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.dao.UsuarioDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.CategoriaException;
import integrado.prog2.exception.CategoriaNotFoundException;
import java.util.List;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;

public class FoodStoreService {
    private final CategoriaDAO categoriaDAO;
    private final UsuarioDAO usuarioDAO;

    public FoodStoreService(CategoriaDAO categoriaDAO,UsuarioDAO usuarioDAO) {
        this.categoriaDAO = categoriaDAO;
        this.usuarioDAO = usuarioDAO;

    }
    public void registrarUsuario(String nombre, String apellido,String mail,String celular, String contrasenia,Rol rol) {

    if(mail == null || mail.trim().isEmpty()) {
        throw new RuntimeException("El mail es obligatorio");
    }

    List<Usuario> usuarios = usuarioDAO.listar();

    for(Usuario u : usuarios) {

        if(u.getMail().equalsIgnoreCase(mail)) {

            throw new RuntimeException("Ya existe un usuario con ese mail");
        }
    }

    Usuario nuevo = new Usuario(null,nombre,apellido,mail,celular, contrasenia,rol
    );

    usuarioDAO.crear(nuevo);
}

    public void registrarCategoria(String nombre, String descripcion){
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CategoriaException("El nombre no puede estar vacio.");
        }
        List<Categoria> actuales = categoriaDAO.listar();
        for (Categoria c : actuales) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                throw new CategoriaException("Error: Ya existe una categoria activa con ese nombre.");
            }
        }
        Categoria nueva = new Categoria(null, nombre, descripcion);
        categoriaDAO.crear(nueva);
    }
    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioDAO.listar();
}
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaDAO.listar();
    }

    public void darBajaLogicaCategoria(Long id) {
        Categoria encontrada = categoriaDAO.buscarPorId(id);
        if (encontrada == null) {
            throw new CategoriaNotFoundException("La categoria con ID " + id + " no existe o ya fue eliminada.");
        }
        categoriaDAO.eliminarLogico(id);
    }
    public void actualizarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) {
    // Busca la categoria en la base de datos
    Categoria categoriaExistente = categoriaDAO.buscarPorId(id);
   
    if (categoriaExistente == null) {
        throw new CategoriaNotFoundException("La categoría con ID " + id + " no existe.");
    }

    categoriaExistente.setNombre(nuevoNombre);
    categoriaExistente.setDescripcion(nuevaDescripcion);
    
    categoriaDAO.actualizar(categoriaExistente);
    }
        
}


