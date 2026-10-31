package com.mistacorp.game.compartido.red;

public final class Protocolo {

    private Protocolo() {
    }

    public static String armar(TipoMensaje tipo, String... datos) {
        StringBuilder mensaje = new StringBuilder(tipo.name());
        for (String dato : datos) {
            mensaje.append(ConfiguracionRed.SEPARADOR).append(dato);
        }
        return mensaje.toString();
    }

    public static String[] separar(String mensaje) {
        return mensaje.trim().split(ConfiguracionRed.SEPARADOR);
    }

    public static TipoMensaje obtenerTipo(String[] partes) {
        try {
            return TipoMensaje.valueOf(partes[0]);
        } catch (IllegalArgumentException e) {
            return null;
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    public static String limpiarNombreSala(String nombre) {
        StringBuilder limpio = new StringBuilder();
        for (int i = 0; i < nombre.length() && limpio.length() < ConfiguracionRed.LARGO_MAXIMO_NOMBRE_SALA; i++) {
            char caracter = nombre.charAt(i);
            if (Character.isLetterOrDigit(caracter) || caracter == ' ') {
                limpio.append(caracter);
            }
        }
        String resultado = limpio.toString().trim();
        if (resultado.isEmpty()) {
            return ConfiguracionRed.NOMBRE_SALA_POR_DEFECTO;
        }
        return resultado;
    }

    public static String entero(float valor) {
        return String.valueOf(Math.max(0, Math.round(valor)));
    }

    public static String numero(float valor) {
        return String.valueOf(Math.round(Math.max(0f, valor) * 100f) / 100f);
    }
}
