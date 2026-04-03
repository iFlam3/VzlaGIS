package com.model;

public class Parroquia {
    private Long id;
    private String nombre;
    private Long municipioId;

    public Parroquia() {}

    public Parroquia(Long id, String nombre, Long municipioId) {
        this.id = id;
        this.nombre = nombre;
        this.municipioId = municipioId;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Long getMunicipioId() { return municipioId; }
    public void setMunicipioId(Long municipioId) { this.municipioId = municipioId; }
}