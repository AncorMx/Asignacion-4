package com.mycompany.demoendianess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class Producto {

    // Regex: exactamente 3 letras mayúsculas seguidas de exactamente 10 dígitos
    private static final Pattern PATRON_CLAVE = Pattern.compile("^[A-Z]{3}\\d{10}$");

    // Regex fecha DD/MM/AAAA (validación de formato; rango lógico se verifica aparte)
    private static final Pattern PATRON_FECHA = Pattern.compile(
        "^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$");

    private String    clave;
    private String    nombre;
    private double    precio;
    private int       cantidad;
    private MarcaEnum marca;
    private String    fecha;      // formato DD/MM/AAAA
    private String    categoria;

    public Producto() {
        this.categoria = "General";
    }

    public Producto(String clave, String nombre, double precio, int cantidad,
                    MarcaEnum marca, String fecha, String categoria) {
        setClave(clave);
        setNombre(nombre);
        setPrecio(precio);
        setCantidad(cantidad);
        setMarca(marca);
        setFecha(fecha);
        setCategoria(categoria);
    }

    // ── CLAVE ────────────────────────────────────────────────────────────────
    /** 3 letras mayúsculas + 10 dígitos (longitud total = 13). */
    public String getClave() { return clave; }

    public void setClave(String clave) {
        if (clave == null || !PATRON_CLAVE.matcher(clave.trim()).matches()) {
            throw new IllegalArgumentException(
                "Clave inválida: debe tener exactamente 3 letras mayúsculas "
                + "seguidas de 10 dígitos (ej. ABC1234567890).");
        }
        this.clave = clave.trim();
    }

    // ── NOMBRE ───────────────────────────────────────────────────────────────
    /** No vacío, máximo 30 caracteres. */
    public String getNombre() { return nombre; }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío.");
        }
        if (nombre.trim().length() > 30) {
            throw new IllegalArgumentException(
                "El nombre no puede superar los 30 caracteres (actual: "
                + nombre.trim().length() + ").");
        }
        this.nombre = nombre.trim();
    }

    // ── PRECIO ───────────────────────────────────────────────────────────────
    /** Mayor a 0, menor a 10 000, exactamente 2 decimales. */
    public double getPrecio() { return precio; }

    public void setPrecio(double precio) {
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a 0.");
        }
        if (precio >= 10000) {
            throw new IllegalArgumentException("El precio debe ser menor a 10,000.");
        }
        // Redondear a 2 decimales
        BigDecimal bd = BigDecimal.valueOf(precio).setScale(2, RoundingMode.HALF_UP);
        this.precio = bd.doubleValue();
    }

    // ── CANTIDAD ─────────────────────────────────────────────────────────────
    /** Mayor a 0 y menor a 100 000. */
    public int getCantidad() { return cantidad; }

    public void setCantidad(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a 0.");
        }
        if (cantidad >= 100000) {
            throw new IllegalArgumentException("La cantidad debe ser menor a 100,000.");
        }
        this.cantidad = cantidad;
    }

    // ── MARCA ────────────────────────────────────────────────────────────────
    /** Debe ser uno de los valores del enumerador MarcaEnum. */
    public MarcaEnum getMarca() { return marca; }

    public void setMarca(MarcaEnum marca) {
        if (marca == null) {
            throw new IllegalArgumentException("La marca no puede ser nula.");
        }
        this.marca = marca;
    }

    // ── FECHA ────────────────────────────────────────────────────────────────
    /** Formato DD/MM/AAAA. */
    public String getFecha() { return fecha; }

    public void setFecha(String fecha) {
        if (fecha == null || !PATRON_FECHA.matcher(fecha.trim()).matches()) {
            throw new IllegalArgumentException(
                "Fecha inválida: debe tener formato DD/MM/AAAA (ej. 29/10/2026).");
        }
        this.fecha = fecha.trim();
    }

    // ── CATEGORÍA ────────────────────────────────────────────────────────────
    public String getCategoria() { return categoria; }

    public void setCategoria(String categoria) {
        if (categoria == null || categoria.trim().isEmpty()) {
            this.categoria = "General";
        } else {
            this.categoria = categoria.trim();
        }
    }

    // ── toString ─────────────────────────────────────────────────────────────
    @Override
    public String toString() {
        return "Producto{" +
               "clave='"    + clave    + '\'' +
               ", nombre='" + nombre   + '\'' +
               ", precio="  + String.format("%.2f", precio) +
               ", cantidad=" + cantidad +
               ", marca='"  + marca    + '\'' +
               ", fecha='"  + fecha    + '\'' +
               ", categoria='" + categoria + '\'' +
               '}';
    }
}
