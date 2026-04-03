package com.DAO;

import com.config.DatabaseConnection;
import com.model.Rol;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolDAO implements CrudDAO<Rol, Integer> {

    @Override
    public boolean create(Rol rol) {
        String sql = "INSERT INTO roles (nombre, descripcion) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar rol: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Rol findById(Integer id) {
        String sql = "SELECT id, nombre, descripcion FROM roles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Rol(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar rol: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Rol> findAll() {
        List<Rol> roles = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM roles";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                roles.add(new Rol(rs.getInt("id"), rs.getString("nombre"), rs.getString("descripcion")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar roles: " + e.getMessage());
        }
        return roles;
    }

    @Override
    public boolean update(Rol rol) {
        String sql = "UPDATE roles SET nombre = ?, descripcion = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rol.getNombre());
            stmt.setString(2, rol.getDescripcion());
            stmt.setInt(3, rol.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar rol: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM roles WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar rol: " + e.getMessage());
            return false;
        }
    }
}