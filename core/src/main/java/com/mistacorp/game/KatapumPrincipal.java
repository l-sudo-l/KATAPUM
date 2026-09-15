package com.mistacorp.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mistacorp.game.pantallas.PantallaMenu;

public class KatapumPrincipal extends Game {

    private SpriteBatch lote;
    private BitmapFont fuente;
    private RecursosGraficos recursos;
    private RecursosSonido sonidos;

    @Override
    public void create() {
        lote = new SpriteBatch();
        fuente = new BitmapFont();
        fuente.getData().setScale(1.2f);
        recursos = new RecursosGraficos();
        sonidos = new RecursosSonido();

        setScreen(new PantallaMenu(this));
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
        lote.dispose();
        fuente.dispose();
        recursos.dispose();
        sonidos.dispose();
    }
}
