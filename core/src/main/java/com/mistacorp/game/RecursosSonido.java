package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class RecursosSonido {

    private final Sound SONIDO_DISPARO;
    private final Sound SONIDO_IMPACTO;
    private final Sound SONIDO_MUERTE;
    private final Sound SONIDO_GANAR;
    private final Music MUSICA_MENU;
    private final Music MUSICA_JUEGO;

    public RecursosSonido() {
        SONIDO_DISPARO = Gdx.audio.newSound(Gdx.files.internal("disparo.wav"));
        SONIDO_IMPACTO = Gdx.audio.newSound(Gdx.files.internal("impacto.wav"));
        SONIDO_MUERTE = Gdx.audio.newSound(Gdx.files.internal("muerte.wav"));
        SONIDO_GANAR = Gdx.audio.newSound(Gdx.files.internal("ganar.wav"));

        MUSICA_MENU = Gdx.audio.newMusic(Gdx.files.internal("menu_juego.wav"));
        MUSICA_JUEGO = Gdx.audio.newMusic(Gdx.files.internal("musica_juego.wav"));
        MUSICA_MENU.setLooping(true);
        MUSICA_JUEGO.setLooping(true);
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

    public Music obtenerMusicaJuego() {
        return MUSICA_JUEGO;
    }

    public void dispose() {
        SONIDO_DISPARO.dispose();
        SONIDO_IMPACTO.dispose();
        SONIDO_MUERTE.dispose();
        SONIDO_GANAR.dispose();
        MUSICA_MENU.dispose();
        MUSICA_JUEGO.dispose();
    }
}
