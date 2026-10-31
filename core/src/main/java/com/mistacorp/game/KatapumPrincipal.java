package com.mistacorp.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.pantallas.PantallaMenu;
import com.mistacorp.game.servidor.HiloServidor;

import java.net.SocketException;

public class KatapumPrincipal extends Game {

    private SpriteBatch lote;
    private BitmapFont fuente;
    private RecursosGraficos recursos;
    private RecursosSonido sonidos;
    private HiloServidor servidorLocal = null;

    @Override
    public void create() {
        lote = new SpriteBatch();
        fuente = new BitmapFont();
        fuente.getData().setScale(1.2f);
        recursos = new RecursosGraficos();
        sonidos = new RecursosSonido();

        setScreen(new PantallaMenu(this));
    }

    public String iniciarServidorLocal(String nombreSala, TipoMapa mapa) {
        detenerServidorLocal();
        try {
            servidorLocal = new HiloServidor(ConfiguracionRed.PUERTO_SERVIDOR, nombreSala, mapa);
            servidorLocal.setDaemon(true);
            servidorLocal.start();
            return null;
        } catch (SocketException e) {
            return "No se pudo abrir el puerto " + ConfiguracionRed.PUERTO_SERVIDOR + " (ya hay un servidor abierto?)";
        }
    }

    public void detenerServidorLocal() {
        if (servidorLocal != null) {
            servidorLocal.detener();
            servidorLocal = null;
        }
    }

    public SpriteBatch obtenerLote() {
        return lote;
    }

    public BitmapFont obtenerFuente() {
        return fuente;
    }

    public RecursosGraficos obtenerRecursos() {
        return recursos;
    }

    public RecursosSonido obtenerSonidos() {
        return sonidos;
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        detenerServidorLocal();
        lote.dispose();
        fuente.dispose();
        recursos.dispose();
        sonidos.dispose();
    }
}
