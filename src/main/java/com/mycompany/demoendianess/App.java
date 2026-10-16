package com.mycompany.demoendianess;

import java.io.File;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static Producto producto = null;

    public static void main(String[] args) {
        int opcion = 0;

        while (opcion != 8) {
            System.out.println("\n--- MENU ---");
            System.out.println("1. Capturar producto");
            System.out.println("2. Guardar en texto");
            System.out.println("3. Leer de texto");
            System.out.println("4. Guardar en binario");
            System.out.println("5. Leer de binario");
            System.out.println("6. Leer archivo viejo (texto)");
            System.out.println("7. Leer archivo viejo (binario)");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                opcion = 0;
            }

            try {
                switch (opcion) {
                    case 1:
                        producto = capturarProducto();
                        System.out.println("Producto capturado:");
                        System.out.println(producto);
                        break;
                    case 2:
                        if (producto == null) {
                            System.out.println("Primero capture un producto.");
                        } else {
                            ProductoTextCodec.guardar(producto, new File("producto.txt"));
                            System.out.println("Producto guardado en producto.txt");
                        }
                        break;
                    case 3:
                        File fTexto = new File("producto.txt");
                        if (!fTexto.exists()) {
                            System.out.println("No existe el archivo producto.txt");
                        } else {
                            Producto pt = ProductoTextCodec.leer(fTexto);
                            System.out.println("Producto leido desde texto:");
                            System.out.println(pt);
                        }
                        break;
                    case 4:
                        if (producto == null) {
                            System.out.println("Primero capture un producto.");
                        } else {
                            ProductoBinaryCodec.guardar(producto, new File("producto.bin"));
                            System.out.println("Producto guardado en producto.bin");
                        }
                        break;
                    case 5:
                        File fBin = new File("producto.bin");
                        if (!fBin.exists()) {
                            System.out.println("No existe el archivo producto.bin");
                        } else {
                            Producto pb = ProductoBinaryCodec.leer(fBin);
                            System.out.println("Producto leido desde binario:");
                            System.out.println(pb);
                        }
                        break;
                    case 6:
                        File fViejoTxt = new File("producto_viejo.txt");
                        if (!fViejoTxt.exists()) {
                            System.out.println("No existe el archivo producto_viejo.txt");
                        } else {
                            Producto pvt = ProductoTextCodec.leer(fViejoTxt);
                            System.out.println("Producto viejo leido (texto):");
                            System.out.println(pvt);
                        }
                        break;
                    case 7:
                        File fViejoBin = new File("producto_viejo.bin");
                        if (!fViejoBin.exists()) {
                            System.out.println("No existe el archivo producto_viejo.bin");
                        } else {
                            Producto pvb = ProductoBinaryCodec.leer(fViejoBin);
                            System.out.println("Producto viejo leido (binario):");
                            System.out.println(pvb);
                        }
                        break;
                    case 8:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static Producto capturarProducto() {
        System.out.print("Clave: ");
        String clave = scanner.nextLine().trim();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();

        System.out.print("Precio: ");
        double precio = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Cantidad: ");
        int cantidad = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Marca: ");
        String marca = scanner.nextLine().trim();

        long fecha = System.currentTimeMillis();

        System.out.print("Categoria (ej. Filtros, Frenos, Aceites): ");
        String categoria = scanner.nextLine().trim();

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }
}
