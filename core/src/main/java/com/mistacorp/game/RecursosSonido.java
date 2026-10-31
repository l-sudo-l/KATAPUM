package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.mistacorp.game.compartido.TipoMapa;

public class RecursosSonido {

    private final Sound SONIDO_DISPARO;
    private final Sound SONIDO_IMPACTO;
    private final Sound SONIDO_MUERTE;
    private final Sound SONIDO_GANAR;
    private final Music MUSICA_MENU;
    private final Music MUSICA_BOSQUE;

    public RecursosSonido() {
        SONIDO_DISPARO = Gdx.audio.newSound(Gdx.files.internal("sonidos/disparo.wav"));
        SONIDO_IMPACTO = Gdx.audio.newSound(Gdx.files.internal("sonidos/impacto.wav"));
        SONIDO_MUERTE = Gdx.audio.newSound(Gdx.files.internal("sonidos/muerte.wav"));
        SONIDO_GANAR = Gdx.audio.newSound(Gdx.files.internal("sonidos/ganar.wav"));

        MUSICA_MENU = Gdx.audio.newMusic(Gdx.files.internal("musica/menu_juego.wav"));
        MUSICA_BOSQUE = Gdx.audio.newMusic(Gdx.files.internal("musica/musica_bosque.wav"));
        MUSICA_MENU.setLooping(true);
        MUSICA_BOSQUE.setLooping(true);
    }

    public Sound obtenerSonidoDisparo() {
        return SONIDO_DISPARO;
    }

    public Sound obtenerSonidoImpacto() {
        return SONIDO_IMPACTO;
    }

    public Sound obtenerSonidoMuerte() {
        return SONIDO_MUERTE;
    }

    public Sound obtenerSonidoGanar() {
        return SONIDO_GANAR;
    }

    public Music obtenerMusicaMenu() {
        return MUSICA_MENU;
    }

    public Music obtenerMusicaPartida(TipoMapa mapa) {
        return MUSICA_BOSQUE;
    }

    public void dispose() {
        SONIDO_DISPARO.dispose();
        SONIDO_IMPACTO.dispose();
        SONIDO_MUERTE.dispose();
        SONIDO_GANAR.dispose();
        MUSICA_MENU.dispose();
        MUSICA_BOSQUE.dispose();
    }
}
