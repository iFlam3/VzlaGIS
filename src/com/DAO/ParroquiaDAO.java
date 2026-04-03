package com.DAO;

import com.config.DatabaseConnection;
import com.model.Parroquia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParroquiaDAO implements CrudDAO<Parroquia, Long> {

    @Override
    public boolean create(Parroquia parroquia) {
        String sql = "INSERT INTO parroquias (nombre, municipio_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, parroquia.getNombre());
            stmt.setLong(2, parroquia.getMunicipioId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar parroquia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Parroquia findById(Long id) {
        String sql = "SELECT id, nombre, municipio_id FROM parroquias WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Parroquia(rs.getLong("id"), rs.getString("nombre"), rs.getLong("municipio_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar parroquia: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Parroquia> findAll() {
        List<Parroquia> parroquias = new ArrayList<>();
        String sql = "SELECT id, nombre, municipio_id FROM parroquias ORDER BY nombre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                parroquias.add(new Parroquia(rs.getLong("id"), rs.getString("nombre"), rs.getLong("municipio_id")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar parroquias: " + e.getMessage());
        }
        return parroquias;
    }

    @Override
    public boolean update(Parroquia parroquia) {
        String sql = "UPDATE parroquias SET nombre = ?, municipio_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, parroquia.getNombre());
            stmt.setLong(2, parroquia.getMunicipioId());
            stmt.setLong(3, parroquia.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar parroquia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM parroquias WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar parroquia: " + e.getMessage());
            return false;
        }
    }
}