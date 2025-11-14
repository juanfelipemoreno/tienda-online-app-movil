package com.example.tiendaonline.data.model;

public class Productos {
    private int id;
    private String nombre;
    private double precio;
    private String descripcion;
    private String imagenUrl;

    public Productos() {
    }

    public Productos(int id, String nombre, double precio, String descripcion,String imagenUrl) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    public Productos(String nombre, double precio, String descripcion,String imagenUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.descripcion = descripcion;
        this.imagenUrl = imagenUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }
}
