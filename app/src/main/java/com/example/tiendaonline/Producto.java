package com.example.tiendaonline;

public class Producto {

    private String nombre;
    private String descripcion;
    private double precio;
    private int imagenResId;

    public Producto(String nombre, String descripcion, double precio, int imagenResId) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenResId = imagenResId;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public int getImagenResId() {
        return imagenResId;
    }
}
