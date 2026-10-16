# Asignación: Modificaciones Demo Endianess

**Materia:** Sistemas Distribuidos  
**Alumno:** Ángel Cortez (ID: 252229)

## Descripción
Práctica sobre serialización y deserialización de datos en formato de texto y binario, aplicando compatibilidad hacia atrás mediante el manejo de formatos y tamaños de bytes fijos al agregar el campo `categoria`.

## Clases del proyecto
- `Producto.java`: Contiene los atributos del producto (`clave`, `nombre`, `precio`, `cantidad`, `marca`, `fecha`, `categoria`). Si el campo `categoria` se deja en blanco o nulo, se asigna automáticamente el valor `"General"`.
- `ProductoTextCodec.java`: Serializa y deserializa el producto en formato de texto delimitado por pipes (`|`). Si el registro contiene 6 campos (archivo viejo), asigna la categoría `"General"`; si contiene 7 campos (archivo nuevo), lee la categoría correspondiente.
- `ProductoBinaryCodec.java`: Serializa y deserializa en binario utilizando `ByteBuffer` (Big Endian). Para registros nuevos utiliza una longitud fija de 101 bytes (agregando 15 bytes al final para la categoría). Para archivos viejos de 86 bytes, asigna automáticamente `"General"`.
- `App.java`: Interfaz de consola para capturar los datos del producto, guardar y leer archivos tanto en texto como en binario, y verificar la lectura de archivos con el formato anterior.

## Formato de datos
- **Texto (`producto.txt`):**  
  `Clave|Nombre|Precio|Cantidad|Marca|Fecha|Categoria`
- **Binario (`producto.bin`):**  
  - Clave: 6 bytes
  - Nombre: 30 bytes
  - Precio: 8 bytes (`double`)
  - Cantidad: 4 bytes (`int`)
  - Marca: 30 bytes
  - Fecha: 8 bytes (`long`)
  - Categoria: 15 bytes  
  **Total:** 101 bytes (archivo nuevo) / 86 bytes (archivo viejo sin categoría).

## Compilación y ejecución
Compilar:
```bash
javac -d target/classes src/main/java/com/mycompany/demoendianess/*.java
```

Ejecutar:
```bash
java -cp target/classes com.mycompany.demoendianess.App
```
