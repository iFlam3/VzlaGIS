package com.model;

import java.sql.Timestamp;

public class Usuario {
    private Long id;
    private String username;
    private String passwordHash;
    private Integer rolId;
    private Boolean activo;
    private Timestamp fechaCreacion;

    public Usuario() {}

    public Usuario(Long id, String username, String passwordHash, Integer rolId, Boolean activo, Timestamp fechaCreacion) {
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rolId = rolId;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }
    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }
    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
}