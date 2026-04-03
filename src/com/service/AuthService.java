package com.service;

import com.DAO.UsuarioDAO;
import com.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService {

    private final UsuarioDAO usuarioDAO;

    public AuthService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario login(String username, String password) {
        Usuario usuario = usuarioDAO.findByUsername(username);

        if (usuario == null || !usuario.getActivo()) {
            return null;
        }

        if (BCrypt.checkpw(password, usuario.getPasswordHash())) {
            return usuario; // Login exitoso
        }

        return null; // Contraseña incorrecta
    }

    public boolean registrarUsuario(String username, String passwordPlana, Integer rolId) {

        if (usuarioDAO.findByUsername(username) != null) {
            System.err.println("El nombre de usuario ya está en uso.");
            return false;
        }

        String hashPassword = BCrypt.hashpw(passwordPlana, BCrypt.gensalt());

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(username);
        nuevoUsuario.setPasswordHash(hashPassword);
        nuevoUsuario.setRolId(rolId);
        nuevoUsuario.setActivo(true); // Activo por defecto

        return usuarioDAO.create(nuevoUsuario);
    }
}