package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.Arrays;

public class LanzadorServidor {

    private static final String COMANDO_SALIR = "salir";

    public static void main(String[] args) {
        TipoMapa mapa = TipoMapa.BOSQUE;
        int primerPalabraNombre = 0;
        if (args.length > 0 && buscarMapa(args[0]) != null) {
            mapa = buscarMapa(args[0]);
            primerPalabraNombre = 1;
        }
        String nombreSala = ConfiguracionRed.NOMBRE_SALA_POR_DEFECTO;
        if (args.length > primerPalabraNombre) {
            nombreSala = String.join(" ", Arrays.copyOfRange(args, primerPalabraNombre, args.length));
        }
        final HiloServidor servidor;
        try {
            servidor = new HiloServidor(ConfiguracionRed.PUERTO_SERVIDOR, nombreSala, mapa);
        } catch (SocketException e) {
            System.out.println("No se pudo abrir el puerto " + ConfiguracionRed.PUERTO_SERVIDOR + ": " + e.getMessage());
            return;
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                servidor.detener();
            }
        });
        servidor.start();
        System.out.println("Servidor KATAPUM \"" + servidor.obtenerNombreSala() + "\" escuchando en el puerto " + ConfiguracionRed.PUERTO_SERVIDOR
            + " con el mapa " + mapa.obtenerNombreVisible());
        System.out.println("Escribi \"" + COMANDO_SALIR + "\" y ENTER (o Ctrl+C) para cerrarlo");
        if (esperarComandoSalir()) {
            servidor.detener();
        }
        esperarFin(servidor);
    }

    private static TipoMapa buscarMapa(String texto) {
        for (TipoMapa mapa : TipoMapa.values()) {
            if (mapa.name().equalsIgnoreCase(texto)) {
                return mapa;
            }
        }
        return null;
    }

    private static boolean esperarComandoSalir() {
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        try {
            String linea = lector.readLine();
            while (linea != null) {
                if (COMANDO_SALIR.equalsIgnoreCase(linea.trim())) {
                    return true;
                }
                linea = lector.readLine();
            }
        } catch (IOException e) {
            System.out.println("No se puede leer la consola: " + e.getMessage());
        }
        return false;
    }

    private static void esperarFin(HiloServidor servidor) {
        try {
            servidor.join();
        } catch (InterruptedException e) {
            return;
        }
        System.out.println("Servidor detenido");
    }
}
