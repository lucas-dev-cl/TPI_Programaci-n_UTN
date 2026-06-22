package integrado.prog2.service;

import integrado.prog2.dao.UsuarioDAO;
import integrado.prog2.entities.Usuario;
import integrado.prog2.enums.Rol;
import integrado.prog2.exception.MailDuplicadoException;
import integrado.prog2.exception.UsuarioNotFoundException;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Long registrarUsuario(String nombre, String apellido, String mail, String celular, String contrasenia, Rol rol) {
        validarMail(mail);

        if (usuarioDAO.existeMail(mail)) {
            throw new MailDuplicadoException("Ya existe un usuario con ese mail.");
        }

        return usuarioDAO.crear(nombre, apellido, mail, celular, contrasenia, rol);
    }

    public List<Usuario> obtenerUsuariosActivos() {
        return usuarioDAO.listar();
    }

    // HU-USR-03 - Editar usuario
    public void actualizarUsuario(Long id, String nuevoNombre, String nuevoApellido, String nuevoMail, String nuevoCelular) {
        Usuario existente = usuarioDAO.buscarPorId(id);

        if (existente == null || existente.isEliminado()) {
            throw new UsuarioNotFoundException("El usuario con ID " + id + " no existe o fue eliminado.");
        }

        if (!nuevoMail.equalsIgnoreCase(existente.getMail())) {
            validarMail(nuevoMail);
            if (usuarioDAO.existeMail(nuevoMail)) {
                throw new MailDuplicadoException("Ya existe un usuario con ese mail.");
            }
        }

        existente.setNombre(nuevoNombre);
        existente.setApellido(nuevoApellido);
        existente.setMail(nuevoMail);
        existente.setCelular(nuevoCelular);

        usuarioDAO.actualizar(existente);
    }

    // HU-USR-04 - Baja lógica
    public void darBajaLogicaUsuario(Long id) {
        Usuario existente = usuarioDAO.buscarPorId(id);

        if (existente == null || existente.isEliminado()) {
            throw new RuntimeException("El usuario con ID " + id + " no existe o ya fue eliminado.");
        }

        usuarioDAO.eliminarLogico(id);
    }

    private void validarMail(String mail) {
        if (mail == null || mail.isBlank()) {
            throw new IllegalArgumentException("El mail no puede estar vacío");
        }
        if (!mail.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("Formato de mail inválido");
        }
    }
}
