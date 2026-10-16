package com.mycompany.demoendianess;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ProductoBinaryCodec {

    public static final int CLAVE_LEN = 6;
    public static final int NOMBRE_LEN = 30;
    public static final int PRECIO_LEN = 8;
    public static final int CANTIDAD_LEN = 4;
    public static final int MARCA_LEN = 30;
    public static final int FECHA_LEN = 8;
    public static final int CATEGORIA_LEN = 15;

    public static final int OLD_LEN = 86;
    public static final int NEW_LEN = 101;

    public static byte[] fixedBytes(String text, int len) {
        byte[] dest = new byte[len];
        if (text != null) {
            byte[] src = text.getBytes(StandardCharsets.UTF_8);
            int copyLen = Math.min(src.length, len);
            System.arraycopy(src, 0, dest, 0, copyLen);
        }
        return dest;
    }

    public static String readFixedString(ByteBuffer bb, int len) {
        byte[] bytes = new byte[len];
        bb.get(bytes);
        int actualLen = 0;
        while (actualLen < len && bytes[actualLen] != 0) {
            actualLen++;
        }
        return new String(bytes, 0, actualLen, StandardCharsets.UTF_8).trim();
    }

    public static byte[] encode(Producto p) {
        ByteBuffer bb = ByteBuffer.allocate(NEW_LEN);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(fixedBytes(p.getClave(), CLAVE_LEN));
        bb.put(fixedBytes(p.getNombre(), NOMBRE_LEN));
        bb.putDouble(p.getPrecio());
        bb.putInt(p.getCantidad());
        bb.put(fixedBytes(p.getMarca(), MARCA_LEN));
        bb.putLong(p.getFecha());
        bb.put(fixedBytes(p.getCategoria(), CATEGORIA_LEN));

        return bb.array();
    }

    public static Producto decode(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        bb.order(ByteOrder.BIG_ENDIAN);

        String clave = readFixedString(bb, CLAVE_LEN);
        String nombre = readFixedString(bb, NOMBRE_LEN);
        double precio = bb.getDouble();
        int cantidad = bb.getInt();
        String marca = readFixedString(bb, MARCA_LEN);
        long fecha = bb.getLong();

        // Compatibilidad: si mide 86 bytes es archivo viejo y se asigna "General"
        String categoria = "General";
        if (bytes.length >= NEW_LEN) {
            categoria = readFixedString(bb, CATEGORIA_LEN);
            if (categoria.isEmpty()) {
                categoria = "General";
            }
        }

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }

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
