package com.mycompany.demoendianess;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ProductoXmlCodec {

    /**
     * Valida un archivo XML contra un esquema XSD dado.
     * Lanza una excepción SAXException si no cumple con las restricciones del esquema.
     */
    public static void validarContraXSD(File xmlFile, File xsdFile) throws Exception {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(xsdFile);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(xmlFile));
    }

    /**
     * Serializa un objeto Producto a formato XML.
     */
    public static String encode(Producto p) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
        sb.append("<producto xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        sb.append("          xsi:noNamespaceSchemaLocation=\"producto.xsd\">\n");
        sb.append("    <clave>").append(p.getClave()).append("</clave>\n");
        sb.append("    <nombre>").append(p.getNombre()).append("</nombre>\n");
        sb.append("    <precio>").append(String.format(java.util.Locale.US, "%.2f", p.getPrecio())).append("</precio>\n");
        sb.append("    <cantidad>").append(p.getCantidad()).append("</cantidad>\n");
        sb.append("    <marca>").append(p.getMarca().name()).append("</marca>\n");
        sb.append("    <fecha>").append(p.getFecha()).append("</fecha>\n");
        sb.append("    <categoria>").append(p.getCategoria()).append("</categoria>\n");
        sb.append("</producto>\n");
        return sb.toString();
    }

    /**
     * Guarda el producto en un archivo XML.
     */
    public static void guardar(Producto p, File file) throws Exception {
        try (FileWriter writer = new FileWriter(file, StandardCharsets.UTF_8)) {
            writer.write(encode(p));
        }
    }

    /**
     * Lee un archivo XML, lo valida contra el esquema XSD si se proporciona,
     * y reconstruye el objeto Producto.
     */
    public static Producto leer(File xmlFile, File xsdFile) throws Exception {
        if (xsdFile != null && xsdFile.exists()) {
            validarContraXSD(xmlFile, xsdFile);
        }

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        String clave = getElementValue(doc, "clave");
        String nombre = getElementValue(doc, "nombre");
        double precio = Double.parseDouble(getElementValue(doc, "precio"));
        int cantidad = Integer.parseInt(getElementValue(doc, "cantidad"));
        MarcaEnum marca = MarcaEnum.fromString(getElementValue(doc, "marca"));
        String fecha = getElementValue(doc, "fecha");

        String categoria = "General";
        NodeList catList = doc.getElementsByTagName("categoria");
        if (catList != null && catList.getLength() > 0 && catList.item(0).getTextContent() != null) {
            String catVal = catList.item(0).getTextContent().trim();
            if (!catVal.isEmpty()) {
                categoria = catVal;
            }
        }

        return new Producto(clave, nombre, precio, cantidad, marca, fecha, categoria);
    }

    private static String getElementValue(Document doc, String tagName) {
        NodeList list = doc.getElementsByTagName(tagName);
        if (list == null || list.getLength() == 0) {
            throw new IllegalArgumentException("Etiqueta faltante en XML: <" + tagName + ">");
        }
        return list.item(0).getTextContent().trim();
    }
}

