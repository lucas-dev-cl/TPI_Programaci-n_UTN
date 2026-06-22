/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;
import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;
import integrado.prog2.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lila
 */
public class CategoriaDAO {

    public void crear(Categoria categoria) {
    String sql = "INSERT INTO categoria (nombre, descripcion, eliminado) VALUES (?, ?, ?)";

    // Dejamos el try-with-resources para que cierre la conexión y el ps automáticamente
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        ps.setString(1, categoria.getNombre());
        ps.setString(2, categoria.getDescripcion());
        ps.setBoolean(3, categoria.isEliminado());
        ps.executeUpdate();

        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                categoria.setId(rs.getLong(1));
            }
        }

    } catch (SQLException e) {
        // ATREPAS LA EXCEPCIÓN DE JDBC Y LANZÁS LA DE TU NEGOCIO
        // Le pasamos un lindo mensaje explicativo y le adjuntamos el 'e' original para no perder el rastro del error real
        throw new DataAccessException("Error al intentar persistir la categoría en la base de datos", e);
    }
}

    public List<Categoria> listar() {
    List<Categoria> lista = new ArrayList<>();
    String sql = "SELECT * FROM categoria WHERE eliminado = false";
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
            Categoria cat = new Categoria();
            cat.setId(rs.getLong("id"));
            cat.setNombre(rs.getString("nombre"));
            cat.setDescripcion(rs.getString("descripcion"));
            cat.setEliminado(rs.getBoolean("eliminado"));
            lista.add(cat);
        }
    } catch (SQLException e) {
        throw new DataAccessException("Error al listar categorías", e);
    }
    return lista;
}

    public Categoria buscarPorId(Long id) {
    String sql = "SELECT * FROM categoria WHERE id = ? AND eliminado = false";

    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setLong(1, id);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Categoria cat = new Categoria();
                cat.setId(rs.getLong("id"));
                cat.setNombre(rs.getString("nombre"));
                cat.setDescripcion(rs.getString("descripcion"));
                cat.setEliminado(rs.getBoolean("eliminado"));
                return cat;
            }
        }

    } catch (SQLException e) {
        throw new DataAccessException("Error al intentar buscar la categoría con ID: " + id, e);
    }

    return null; // si no encontró ninguna fila, el flujo llega acá de forma limpia
}

    public void eliminarLogico(Long id) {
    String sql = "UPDATE categoria SET eliminado = true WHERE id = ?";
    try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setLong(1, id);
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Error al eliminar lógicamente la categoría con id: " + id, e);
    }
}

    public void actualizar(Categoria categoria) {
    String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, categoria.getNombre());
        ps.setString(2, categoria.getDescripcion());
        ps.setLong(3, categoria.getId());
        ps.executeUpdate();
    } catch (SQLException e) {
        throw new DataAccessException("Error al actualizar la categoría con id: " + categoria.getId(), e);
    }
}
}