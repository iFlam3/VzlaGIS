package com.model;

public class Municipio {
    private Long id;
    private String nombre;
    private Integer estadoId;

    public Municipio() {}

    public Municipio(Long id, String nombre, Integer estadoId) {
        this.id = id;
        this.nombre = nombre;
        this.estadoId = estadoId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getEstadoId() { return estadoId; }
    public void setEstadoId(Integer estadoId) { this.estadoId = estadoId; }
}