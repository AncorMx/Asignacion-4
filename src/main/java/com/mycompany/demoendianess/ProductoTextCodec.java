package com.mycompany.demoendianess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class ProductoTextCodec {

    /**
     * Serializa el producto en una línea delimitada por '|'.
     * Orden: clave|nombre|precio|cantidad|marca|fecha|categoria
     */
    public static String encode(Producto p) {
        return p.getClave()               + "|" +
               p.getNombre()              + "|" +
               String.format("%.2f", p.getPrecio()) + "|" +
               p.getCantidad()            + "|" +
               p.getMarca().name()        + "|" +   // nombre del enum
               p.getFecha()               + "|" +   // DD/MM/AAAA
               p.getCategoria();
    }

    /**
     * Deserializa una línea en un objeto Producto.
     * Compatible con archivos viejos (6 campos sin categoría).
     */
    public static Producto decode(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Línea vacía o nula al leer el archivo.");
        }
        String[] parts = line.split("\\|", -1);

        String clave    = parts[0];
        String nombre   = parts[1];
        double precio   = Double.parseDouble(parts[2]);
        int    cantidad = Integer.parseInt(parts[3]);

        // Marca: puede venir como enum name o como string legacy
        MarcaEnum marca = MarcaEnum.fromString(parts[4]);

        // Fecha: puede venir como DD/MM/AAAA (nuevo) o como timestamp long (viejo)
        String fecha = parseOConvertirFecha(parts[5]);

        // Categoría opcional
        String categoria = "General";
        if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
            categoria = parts[6].trim();
        }

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }

    /**
     * Si el valor ya tiene formato DD/MM/AAAA lo devuelve tal cual.
     * Si es un timestamp (long), lo convierte.
     */
    private static String parseOConvertirFecha(String valor) {
        valor = valor.trim();
        // Verificar si ya viene en formato DD/MM/AAAA
        if (valor.matches("^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/(\\d{4})$")) {
            return valor;
        }
        // Intentar interpretar como timestamp legacy (long) → convertir
        try {
            long ts = Long.parseLong(valor);
            java.util.Date d = new java.util.Date(ts);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            return sdf.format(d);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                "Formato de fecha no reconocido: \"" + valor + "\"");
        }
    }

    public static void guardar(Producto p, File file) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(
                new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write(encode(p));
            bw.newLine();
        }
    }

    public static Producto leer(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(
                new FileReader(file, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            return decode(line);
        }
    }
}
