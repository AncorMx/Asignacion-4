package com.mycompany.demoendianess;

public class Producto {

    private String clave;
    private String nombre;
    private double precio;
    private int cantidad;
    private String marca;
    private long fecha;
    private String categoria;

    public Producto() {
        this.categoria = "General";
    }

    public Producto(String clave, String nombre, double precio, int cantidad, String marca, long fecha, String categoria) {
        this.clave = clave;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.marca = marca;
        this.fecha = fecha;
        setCategoria(categoria);
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
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

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            this.categoria = "General";
        } else {
            this.categoria = categoria.trim();
        }
    }

    @Override
    public String toString() {
        return "Producto{" +
                "clave='" + clave + '\'' +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", cantidad=" + cantidad +
                ", marca='" + marca + '\'' +
                ", fecha=" + fecha +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
