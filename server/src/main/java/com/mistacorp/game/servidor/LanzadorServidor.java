package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;

import java.net.SocketException;

public class LanzadorServidor {

    public static void main(String[] args) {
        String nombreSala = args.length > 0 ? String.join(" ", args) : ConfiguracionRed.NOMBRE_SALA_POR_DEFECTO;
        try {
            HiloServidor servidor = new HiloServidor(ConfiguracionRed.PUERTO_SERVIDOR, nombreSala, TipoMapa.BOSQUE);
            servidor.start();
            System.out.println("Servidor KATAPUM \"" + servidor.obtenerNombreSala() + "\" escuchando en el puerto " + ConfiguracionRed.PUERTO_SERVIDOR);
        } catch (SocketException e) {
            System.out.println("No se pudo abrir el puerto " + ConfiguracionRed.PUERTO_SERVIDOR + ": " + e.getMessage());
        }
    }
}
