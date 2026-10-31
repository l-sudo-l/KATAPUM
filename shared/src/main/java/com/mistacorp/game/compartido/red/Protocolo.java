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

    public static String numero(float valor) {
        return String.valueOf(Math.round(Math.max(0f, valor) * 100f) / 100f);
    }
}
