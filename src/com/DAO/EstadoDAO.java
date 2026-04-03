package com.DAO;

import com.config.DatabaseConnection;
import com.model.Estado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class EstadoDAO implements CrudDAO<Estado, Integer> {

    @Override
    public boolean create(Estado estado) {
        String sql = "INSERT INTO estados (nombre) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.getNombre());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar estado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Estado findById(Integer id) {
        String sql = "SELECT id, nombre FROM estados WHERE id = ?";
        Estado estado = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    estado = new Estado(rs.getInt("id"), rs.getString("nombre"));
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al buscar estado por ID: " + e.getMessage());
        }

        return estado;
    }

    @Override
    public List<Estado> findAll() {
        List<Estado> estados = new ArrayList<>();
        String sql = "SELECT id, nombre FROM estados ORDER BY nombre ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                estados.add(new Estado(rs.getInt("id"), rs.getString("nombre")));
            }

        } catch (SQLException e) {
            System.err.println("Error al listar estados: " + e.getMessage());
        }

        return estados;
    }

    @Override
    public boolean update(Estado estado) {
        String sql = "UPDATE estados SET nombre = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, estado.getNombre());
            stmt.setInt(2, estado.getId());

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar estado: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM estados WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar estado: " + e.getMessage());
            return false;
        }
    }
}