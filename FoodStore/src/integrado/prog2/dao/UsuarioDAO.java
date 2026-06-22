package integrado.prog2.dao;

import integrado.prog2.config.ConexionDB;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public Long crear(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        String sql = "INSERT INTO usuario(nombre, apellido, mail, celular, contrasenia, rol, eliminado) VALUES(?,?,?,?,?,?,?)";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, nombre);
            ps.setString(2, apellido);
            ps.setString(3, mail);
            ps.setString(4, celular);
            ps.setString(5, contrasenia);
            ps.setString(6, rol.name());
            ps.setBoolean(7, false);

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al crear usuario: " + e.getMessage());
        }

        return null;
    }

    public List<Usuario> listar() {

        List<Usuario> lista = new ArrayList<>();

        String sql =
        "SELECT * FROM usuario WHERE eliminado = false";

        try(Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()) {

                Usuario u = new Usuario();

                u.setId(rs.getLong("id"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido(rs.getString("apellido"));
                u.setMail(rs.getString("mail"));
                u.setCelular(rs.getString("celular"));
                u.setContrasenia(rs.getString("contrasenia"));
                u.setRol(Rol.valueOf(rs.getString("rol")));
                u.setEliminado(rs.getBoolean("eliminado"));

                lista.add(u);
            }

        } catch(SQLException e) {
            throw new DataAccessException("Error al listar usuarios", e);
        }

        return lista;
    }

    public Usuario buscarPorId(Long id) {

        String sql =
        "SELECT * FROM usuario WHERE id = ? AND eliminado = false";

        try(Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try(ResultSet rs = ps.executeQuery()) {

                if(rs.next()) {

                    Usuario u = new Usuario();

                    u.setId(rs.getLong("id"));
                    u.setNombre(rs.getString("nombre"));
                    u.setApellido(rs.getString("apellido"));
                    u.setMail(rs.getString("mail"));
                    u.setCelular(rs.getString("celular"));
                    u.setContrasenia(rs.getString("contrasenia"));
                    u.setRol(Rol.valueOf(rs.getString("rol")));

                    return u;
                }
            }

        } catch(SQLException e) {
            throw new DataAccessException("Error al buscar usuario", e);
        }

        return null;
    }

    public void actualizar(Usuario usuario) {

        String sql =
        "UPDATE usuario SET nombre=?, apellido=?, mail=?, celular=?, contrasenia=?, rol=? WHERE id=? AND eliminado = false";

        try(Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombre());
            ps.setString(2, usuario.getApellido());
            ps.setString(3, usuario.getMail());
            ps.setString(4, usuario.getCelular());
            ps.setString(5, usuario.getContrasenia());
            ps.setString(6, usuario.getRol().name());
            ps.setLong(7, usuario.getId());

            ps.executeUpdate();

        } catch(SQLException e) {
            throw new DataAccessException("Error al actualizar usuario", e);
        }
    }

    public void eliminarLogico(Long id) {

        String sql =
        "UPDATE usuario SET eliminado = true WHERE id = ?";

        try(Connection con = ConexionDB.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            ps.executeUpdate();

        } catch(SQLException e) {
            throw new DataAccessException("Error al eliminar usuario", e);
        }
    }

    public boolean existeMail(String mail) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE mail = ? AND eliminado = FALSE";

        try (Connection con = ConexionDB.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, mail);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al verificar mail: " + e.getMessage());
        }

        return false;
    }
}