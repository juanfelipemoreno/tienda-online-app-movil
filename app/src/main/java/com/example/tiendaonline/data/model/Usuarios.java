package com.example.tiendaonline.data.model;

public class Usuarios {
    private int id;
    private String identificacion;
    private String nombre;
    private String password;
    private String imagenUrl;

    public Usuarios() {
    }
    public Usuarios(int id, String identificacion, String nombre,String password,String imagenUrl) {
        this.password = password;
        this.id = id;
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.imagenUrl = imagenUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
