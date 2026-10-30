package com.mycompany.demoendianess;

public enum MarcaEnum {
    BOSCH,
    TOYOTA,
    HONDA,
    FORD,
    CHEVROLET,
    NISSAN,
    VOLKSWAGEN,
    SAMSUNG,
    LG,
    SONY;

    /**
     * Busca una marca por nombre ignorando mayúsculas/minúsculas.
     * Lanza IllegalArgumentException si no se encuentra.
     */
    public static MarcaEnum fromString(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("La marca no puede ser vacía.");
        }
        for (MarcaEnum m : values()) {
            if (m.name().equalsIgnoreCase(value.trim())) {
                return m;
            }
        }
        throw new IllegalArgumentException(
            "Marca inválida: \"" + value + "\". Opciones: " + listaMarcas());
    }

    /** Devuelve todas las marcas como cadena separada por comas. */
    public static String listaMarcas() {
        StringBuilder sb = new StringBuilder();
        MarcaEnum[] vals = values();
        for (int i = 0; i < vals.length; i++) {
            sb.append(vals[i].name());
            if (i < vals.length - 1) sb.append(", ");
        }
        return sb.toString();
    }
}

