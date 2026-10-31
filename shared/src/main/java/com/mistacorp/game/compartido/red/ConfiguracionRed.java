package com.mistacorp.game.compartido.red;

public final class ConfiguracionRed {

    private ConfiguracionRed() {
    }

    public static final int PUERTO_SERVIDOR = 5555;
    public static final int TAMANO_BUFER = 4096;
    public static final String SEPARADOR = "-";
    public static final int LARGO_MAXIMO_NOMBRE_SALA = 16;
    public static final String NOMBRE_SALA_POR_DEFECTO = "Sala KATAPUM";

    public static final float ENVIOS_POR_SEGUNDO = 30f;
    public static final long MILISEGUNDOS_DESCONEXION = 5000;
    public static final long MILISEGUNDOS_REINTENTO_CONEXION = 1000;
    public static final int MILISEGUNDOS_ESPERA_RECEPCION = 200;
    public static final int MILISEGUNDOS_PASO_SIMULACION = 16;
}
