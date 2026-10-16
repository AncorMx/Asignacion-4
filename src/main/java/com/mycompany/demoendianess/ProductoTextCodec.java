package com.mycompany.demoendianess;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class ProductoTextCodec {

    public static String encode(Producto p) {
        return p.getClave() + "|" +
                p.getNombre() + "|" +
                p.getPrecio() + "|" +
                p.getCantidad() + "|" +
                p.getMarca() + "|" +
                p.getFecha() + "|" +
                p.getCategoria();
    }

    public static Producto decode(String line) {
        String[] parts = line.split("\\|", -1);
        String clave = parts[0];
        String nombre = parts[1];
        double precio = Double.parseDouble(parts[2]);
        int cantidad = Integer.parseInt(parts[3]);
        String marca = parts[4];
        long fecha = Long.parseLong(parts[5]);

        // Si el archivo viene en formato viejo con 6 campos
        String categoria = "General";
        if (parts.length >= 7 && !parts[6].trim().isEmpty()) {
            categoria = parts[6].trim();
        }

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }

    public static void guardar(Producto p, File file) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            bw.write(encode(p));
            bw.newLine();
        }
    }

    public static Producto leer(File file) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            return decode(line);
        }
    }
}
