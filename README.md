# Asignación 5: XML + XSD

**Materia:** Sistemas Distribuidos  
**Alumno:** Ángel Cortez (ID: 252229)

## Descripción
Proyecto para la captura, lectura y validación de productos mediante un esquema **XSD** (`producto.xsd`) y formato **XML** (`producto.xml`), manteniendo compatibilidad con archivos de texto y binarios.

## Restricciones del Esquema (XSD)
- **Clave:** 3 letras mayúsculas seguidas de 10 dígitos (ej. `ABC1234567890`).
- **Nombre:** No vacío, máximo 30 caracteres.
- **Precio:** Mayor a 0, menor a 10000 y con 2 decimales.
- **Cantidad:** Entero mayor a 0 y menor a 100000.
- **Marca:** Una de 10 marcas (`BOSCH`, `TOYOTA`, `HONDA`, `FORD`, `CHEVROLET`, `NISSAN`, `VOLKSWAGEN`, `SAMSUNG`, `LG`, `SONY`).
- **Fecha:** Formato `DD/MM/AAAA`.
- **Categoría:** Opcional (por defecto `"General"`).

## Archivos Principales
- `producto.xsd`: Esquema de validación XSD con las restricciones requeridas.
- `producto.xml`: Archivo XML con datos de ejemplo válidos.
- `src/main/java/com/mycompany/demoendianess/`:
  - `Producto.java`: Modelo con validaciones en setters y atributos.
  - `MarcaEnum.java`: Enumerador con las 10 marcas permitidas.
  - `ProductoXmlCodec.java`: Serialización y validación XML contra `producto.xsd`.
  - `ProductoTextCodec.java`: Manejo de archivo de texto delimitado (`|`).
  - `ProductoBinaryCodec.java`: Manejo de archivo binario con tamaño fijo.
  - `App.java`: Menú de consola para probar todas las operaciones.

## Compilación y Ejecución
```bash
# Compilar
javac -d target/classes src/main/java/com/mycompany/demoendianess/*.java

# Ejecutar
java -cp target/classes com.mycompany.demoendianess.App
```
