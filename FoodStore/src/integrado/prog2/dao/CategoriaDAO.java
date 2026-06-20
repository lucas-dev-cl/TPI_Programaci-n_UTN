/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package integrado.prog2.dao;
import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Lila
 */
public class CategoriaDAO {

    public void crear(Categoria categoria) throws SQLException {
        String sql = "INSERT INTO categoria (nombre, descripcion, eliminado) VALUES (?, ?, ?)";
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
        }
    }

    public List<Categoria> listar() throws SQLException {
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
        }
        return lista;
    }

    public Categoria buscarPorId(Long id) throws SQLException {
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
        }
        return null;
    }

    public void eliminarLogico(Long id) throws SQLException {
        String sql = "UPDATE categoria SET eliminado = true WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            
            ps.setLong(1, id);
            ps.executeUpdate();
        }
    }
    public void actualizar(Categoria categoria) throws SQLException {
    // 1. Modificamos el SQL para cambiar 'nombre' y 'descripcion' usando parámetros (?)
    String sql = "UPDATE categoria SET nombre = ?, descripcion = ? WHERE id = ?";
    
        try (Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

           ps.setString(1, categoria.getNombre());       
           ps.setString(2, categoria.getDescripcion());  
           ps.setLong(3, categoria.getId());             
          
           ps.executeUpdate();
        }
    }
 }