package com.DAO;

import com.config.DatabaseConnection;
import com.model.Municipio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MunicipioDAO implements CrudDAO<Municipio, Long> {

    @Override
    public boolean create(Municipio municipio) {
        String sql = "INSERT INTO municipios (nombre, estado_id) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, municipio.getNombre());
            stmt.setInt(2, municipio.getEstadoId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar municipio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Municipio findById(Long id) {
        String sql = "SELECT id, nombre, estado_id FROM municipios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Municipio(rs.getLong("id"), rs.getString("nombre"), rs.getInt("estado_id"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar municipio: " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Municipio> findAll() {
        List<Municipio> municipios = new ArrayList<>();
        String sql = "SELECT id, nombre, estado_id FROM municipios ORDER BY nombre ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                municipios.add(new Municipio(rs.getLong("id"), rs.getString("nombre"), rs.getInt("estado_id")));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar municipios: " + e.getMessage());
        }
        return municipios;
    }

    @Override
    public boolean update(Municipio municipio) {
        String sql = "UPDATE municipios SET nombre = ?, estado_id = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, municipio.getNombre());
            stmt.setInt(2, municipio.getEstadoId());
            stmt.setLong(3, municipio.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar municipio: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Long id) {
        String sql = "DELETE FROM municipios WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar municipio: " + e.getMessage());
            return false;
        }
    }
}