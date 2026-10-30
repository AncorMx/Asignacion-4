package com.mycompany.demoendianess;

import java.io.File;
import java.util.Scanner;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static Producto producto = null;

    public static void main(String[] args) {
        int opcion = 0;

        while (opcion != 11) {
            System.out.println("\n--- MENU - ASIGNACION 5: XML + XSD ---");
            System.out.println("1. Capturar producto");
            System.out.println("2. Guardar en texto");
            System.out.println("3. Leer de texto");
            System.out.println("4. Guardar en binario");
            System.out.println("5. Leer de binario");
            System.out.println("6. Guardar en XML");
            System.out.println("7. Leer y validar XML contra XSD");
            System.out.println("8. Validar archivo XML existente contra producto.xsd");
            System.out.println("9. Leer archivo viejo (texto)");
            System.out.println("10. Leer archivo viejo (binario)");
            System.out.println("11. Salir");
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
                        if (producto == null) {
                            System.out.println("Primero capture un producto.");
                        } else {
                            ProductoXmlCodec.guardar(producto, new File("producto.xml"));
                            System.out.println("Producto guardado con exito en producto.xml");
                        }
                        break;
                    case 7:
                        File fXml = new File("producto.xml");
                        File fXsd = new File("producto.xsd");
                        if (!fXml.exists()) {
                            System.out.println("No existe el archivo producto.xml");
                        } else if (!fXsd.exists()) {
                            System.out.println("No existe el archivo producto.xsd");
                        } else {
                            Producto px = ProductoXmlCodec.leer(fXml, fXsd);
                            System.out.println("[VALIDACION EXITOSA] El archivo cumple con el esquema XSD.");
                            System.out.println("Producto leido desde XML:");
                            System.out.println(px);
                        }
                        break;
                    case 8:
                        System.out.print("Ruta del archivo XML a validar (por defecto: producto.xml): ");
                        String pathXml = scanner.nextLine().trim();
                        if (pathXml.isEmpty()) pathXml = "producto.xml";
                        File archXml = new File(pathXml);
                        File archXsd = new File("producto.xsd");
                        if (!archXml.exists()) {
                            System.out.println("No se encontro el archivo: " + pathXml);
                        } else if (!archXsd.exists()) {
                            System.out.println("No existe el esquema producto.xsd en el directorio raiz.");
                        } else {
                            ProductoXmlCodec.validarContraXSD(archXml, archXsd);
                            System.out.println("[VALIDACION EXITOSA] " + pathXml + " es valido segun producto.xsd.");
                        }
                        break;
                    case 9:
                        File fViejoTxt = new File("producto_viejo.txt");
                        if (!fViejoTxt.exists()) {
                            System.out.println("No existe el archivo producto_viejo.txt");
                        } else {
                            Producto pvt = ProductoTextCodec.leer(fViejoTxt);
                            System.out.println("Producto viejo leido (texto):");
                            System.out.println(pvt);
                        }
                        break;
                    case 10:
                        File fViejoBin = new File("producto_viejo.bin");
                        if (!fViejoBin.exists()) {
                            System.out.println("No existe el archivo producto_viejo.bin");
                        } else {
                            Producto pvb = ProductoBinaryCodec.leer(fViejoBin);
                            System.out.println("Producto viejo leido (binario):");
                            System.out.println(pvb);
                        }
                        break;
                    case 11:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("[Error] " + e.getMessage());
            }
        }
    }

    /** Captura un producto con validaciones en cada campo. */
    public static Producto capturarProducto() {

        // ── CLAVE ─────────────────────────────────────────────────────────────
        String clave = null;
        while (clave == null) {
            System.out.print("Clave (3 letras mayusculas + 10 digitos, ej. ABC1234567890): ");
            String input = scanner.nextLine().trim();
            try {
                Producto tmp = new Producto();
                tmp.setClave(input);
                clave = input;
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── NOMBRE ────────────────────────────────────────────────────────────
        String nombre = null;
        while (nombre == null) {
            System.out.print("Nombre (1-30 caracteres): ");
            String input = scanner.nextLine().trim();
            try {
                Producto tmp = new Producto();
                tmp.setNombre(input);
                nombre = input;
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── PRECIO ────────────────────────────────────────────────────────────
        double precio = -1;
        while (precio < 0) {
            System.out.print("Precio (> 0 y < 10000, 2 decimales, ej. 1234.56): ");
            try {
                double val = Double.parseDouble(scanner.nextLine().trim());
                Producto tmp = new Producto();
                tmp.setPrecio(val);
                precio = val;
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Ingrese un numero valido.");
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── CANTIDAD ──────────────────────────────────────────────────────────
        int cantidad = -1;
        while (cantidad < 0) {
            System.out.print("Cantidad (> 0 y < 100000): ");
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                Producto tmp = new Producto();
                tmp.setCantidad(val);
                cantidad = val;
            } catch (NumberFormatException e) {
                System.out.println("  [Error] Ingrese un numero entero valido.");
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── MARCA ─────────────────────────────────────────────────────────────
        MarcaEnum marca = null;
        while (marca == null) {
            System.out.println("Marcas disponibles: " + MarcaEnum.listaMarcas());
            System.out.print("Marca: ");
            String input = scanner.nextLine().trim();
            try {
                marca = MarcaEnum.fromString(input);
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── FECHA ─────────────────────────────────────────────────────────────
        String fecha = null;
        while (fecha == null) {
            System.out.print("Fecha (DD/MM/AAAA): ");
            String input = scanner.nextLine().trim();
            try {
                Producto tmp = new Producto();
                tmp.setFecha(input);
                fecha = input;
            } catch (IllegalArgumentException e) {
                System.out.println("  [Error] " + e.getMessage());
            }
        }

        // ── CATEGORÍA ─────────────────────────────────────────────────────────
        System.out.print("Categoria (ej. Filtros, Frenos, Aceites) [Enter = General]: ");
        String categoria = scanner.nextLine().trim();

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }
}
