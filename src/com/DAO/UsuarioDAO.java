package com.DAO;

import com.config.DatabaseConnection;
import com.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO implements CrudDAO<Usuario, Long> {

    @Override
    public boolean create(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password_hash, rol_id, activo) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setInt(3, usuario.getRolId());
            stmt.setBoolean(4, usuario.getActivo() != null ? usuario.getActivo() : true);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Usuario findById(Long id) {
        String sql = "SELECT id, username, password_hash, rol_id, activo, fecha_creacion FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Usuario> findAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, password_hash, rol_id, activo, fecha_creacion FROM usuarios";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                usuarios.add(mapResultSetToUsuario(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar usuarios: " + e.getMessage());
        }
        return usuarios;
    }

    @Override
    public boolean update(Usuario usuario) {
        String sql = "UPDATE usuarios SET username = ?, password_hash = ?, rol_id = ?, activo = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getUsername());
            stmt.setString(2, usuario.getPasswordHash());
            stmt.setInt(3, usuario.getRolId());
            stmt.setBoolean(4, usuario.getActivo());
            stmt.setLong(5, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }

    public Usuario findByUsername(String username) {
        String sql = "SELECT id, username, password_hash, rol_id, activo, fecha_creacion FROM usuarios WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUsuario(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario por username: " + e.getMessage());
        }
        return null;
    }

    // Método para evitar duplicar
    private Usuario mapResultSetToUsuario(ResultSet rs) throws SQLException {
        return new Usuario(
                rs.getLong("id"),
                rs.getString("username"),
                rs.getString("password_hash"),
                rs.getInt("rol_id"),
                rs.getBoolean("activo"),
                rs.getTimestamp("fecha_creacion")
        );
    }
}