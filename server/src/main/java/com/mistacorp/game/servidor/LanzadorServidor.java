package com.mistacorp.game.servidor;

import com.mistacorp.game.compartido.red.ConfiguracionRed;

import java.net.SocketException;

public class LanzadorServidor {

    public static void main(String[] args) {
        try {
            HiloServidor servidor = new HiloServidor(ConfiguracionRed.PUERTO_SERVIDOR);
            servidor.start();
            System.out.println("Servidor KATAPUM escuchando en el puerto " + ConfiguracionRed.PUERTO_SERVIDOR);
        } catch (SocketException e) {
            System.out.println("No se pudo abrir el puerto " + ConfiguracionRed.PUERTO_SERVIDOR + ": " + e.getMessage());
        }
    }
}
