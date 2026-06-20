/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.service;
import integrado.prog2.dao.CategoriaDAO;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.EntityNotFoundException;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author Lila
 */
public class FoodStoreService {
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public void registrarCategoria(String nombre, String descripcion) throws Exception {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new Exception("El nombre no puede estar vacio.");
        }
        List<Categoria> actuales = categoriaDAO.listar();
        for (Categoria c : actuales) {
            if (c.getNombre().equalsIgnoreCase(nombre)) {
                throw new Exception("Error: Ya existe una categoria activa con ese nombre.");
            }
        }
        Categoria nueva = new Categoria(null, nombre, descripcion);
        categoriaDAO.crear(nueva);
    }

    public List<Categoria> obtenerCategoriasActivas() throws SQLException {
        return categoriaDAO.listar();
    }

    public void darBajaLogicaCategoria(Long id) throws SQLException, EntityNotFoundException {
        Categoria encontrada = categoriaDAO.buscarPorId(id);
        if (encontrada == null) {
            throw new EntityNotFoundException("La categoria con ID " + id + " no existe o ya fue eliminada.");
        }
        categoriaDAO.eliminarLogico(id);
    }
    public void actualizarCategoria(Long id, String nuevoNombre, String nuevaDescripcion) throws SQLException, EntityNotFoundException {
    // Busca la categoria en la base de datos
    Categoria categoriaExistente = categoriaDAO.buscarPorId(id);
   
    if (categoriaExistente == null) {
        throw new EntityNotFoundException("La categoría con ID " + id + " no existe.");
    }

    categoriaExistente.setNombre(nuevoNombre);
    categoriaExistente.setDescripcion(nuevaDescripcion);
    
    categoriaDAO.actualizar(categoriaExistente);
    }
        
}


