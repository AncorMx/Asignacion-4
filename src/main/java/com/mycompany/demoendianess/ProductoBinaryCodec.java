package com.mycompany.demoendianess;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ProductoBinaryCodec {

    // ── Longitudes de campos en bytes ─────────────────────────────────────────
    public static final int CLAVE_LEN     = 13;  // 3 letras + 10 dígitos
    public static final int NOMBRE_LEN    = 30;
    public static final int PRECIO_LEN    =  8;  // double
    public static final int CANTIDAD_LEN  =  4;  // int
    public static final int MARCA_LEN     = 15;  // nombre del enum (máx ~12 chars)
    public static final int FECHA_LEN     = 10;  // "DD/MM/AAAA"
    public static final int CATEGORIA_LEN = 15;

    /** Tamaño del registro nuevo (en bytes). */
    public static final int NEW_LEN =
        CLAVE_LEN + NOMBRE_LEN + PRECIO_LEN + CANTIDAD_LEN +
        MARCA_LEN + FECHA_LEN + CATEGORIA_LEN;
    // 13 + 30 + 8 + 4 + 15 + 10 + 15 = 95

    /** Tamaño del registro viejo (antes de agregar categoría y cambios de campo). */
    public static final int OLD_LEN = 86;

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Rellena o trunca una cadena hasta exactamente {@code len} bytes UTF-8. */
    public static byte[] fixedBytes(String text, int len) {
        byte[] dest = new byte[len];
        if (text != null) {
            byte[] src = text.getBytes(StandardCharsets.UTF_8);
            int copyLen = Math.min(src.length, len);
            System.arraycopy(src, 0, dest, 0, copyLen);
        }
        return dest;
    }

    /** Lee exactamente {@code len} bytes del buffer y los convierte a String (sin padding de ceros). */
    public static String readFixedString(ByteBuffer bb, int len) {
        byte[] bytes = new byte[len];
        bb.get(bytes);
        int actualLen = 0;
        while (actualLen < len && bytes[actualLen] != 0) {
            actualLen++;
        }
        return new String(bytes, 0, actualLen, StandardCharsets.UTF_8).trim();
    }

    // ── Codificación / Decodificación ─────────────────────────────────────────

    public static byte[] encode(Producto p) {
        ByteBuffer bb = ByteBuffer.allocate(NEW_LEN);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(fixedBytes(p.getClave(),          CLAVE_LEN));
        bb.put(fixedBytes(p.getNombre(),          NOMBRE_LEN));
        bb.putDouble(p.getPrecio());
        bb.putInt(p.getCantidad());
        bb.put(fixedBytes(p.getMarca().name(),    MARCA_LEN));
        bb.put(fixedBytes(p.getFecha(),           FECHA_LEN));
        bb.put(fixedBytes(p.getCategoria(),       CATEGORIA_LEN));

        return bb.array();
    }

    public static Producto decode(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);

        // ── Compatibilidad con archivos viejos (OLD_LEN = 86 bytes) ──────────
        // Los archivos viejos tenían: CLAVE=6, NOMBRE=30, PRECIO=8, CANTIDAD=4,
        // MARCA=30, FECHA=8 (long). Se mapean lo mejor posible.
        if (bytes.length <= OLD_LEN) {
            String claveVieja    = readFixedString(bb, 6);
            String nombreViejo   = readFixedString(bb, 30);
            double precioViejo   = bb.getDouble();
            int    cantidadVieja = bb.getInt();
            String marcaVieja    = readFixedString(bb, 30);
            long   tsViejo       = bb.getLong();

            // Convertir timestamp a DD/MM/AAAA
            java.util.Date d = new java.util.Date(tsViejo);
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
            String fechaStr = sdf.format(d);

            // Intentar parsear marca; si falla usar BOSCH como fallback
            MarcaEnum marcaEnum;
            try {
                marcaEnum = MarcaEnum.fromString(marcaVieja);
            } catch (IllegalArgumentException ex) {
                marcaEnum = MarcaEnum.BOSCH;
            }

            return new Producto(claveVieja, nombreViejo, precioViejo,
                                cantidadVieja, marcaEnum, fechaStr, "General");
        }

        // ── Registro nuevo ────────────────────────────────────────────────────
        String clave    = readFixedString(bb, CLAVE_LEN);
        String nombre   = readFixedString(bb, NOMBRE_LEN);
        double precio   = bb.getDouble();
        int    cantidad = bb.getInt();
        String marcaStr = readFixedString(bb, MARCA_LEN);
        String fecha    = readFixedString(bb, FECHA_LEN);
        String categoria = readFixedString(bb, CATEGORIA_LEN);

        MarcaEnum marca;
        try {
            marca = MarcaEnum.fromString(marcaStr);
        } catch (IllegalArgumentException ex) {
            marca = MarcaEnum.BOSCH;
        }

        if (categoria.isEmpty()) categoria = "General";

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }

    // ── E/S de archivos ───────────────────────────────────────────────────────

    public static void guardar(Producto p, File file) throws Exception {
        byte[] bytes = encode(p);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        }
    }

    public static Producto leer(File file) throws Exception {
        byte[] bytes = Files.readAllBytes(file.toPath());
        return decode(bytes);
    }
}
