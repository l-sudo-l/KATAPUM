package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class RecursosSonido {

    private final Sound sonidoDisparo;
    private final Sound sonidoImpacto;
    private final Sound sonidoMuerte;
    private final Sound sonidoGanar;
    private final Music musicaMenu;
    private final Music musicaJuego;

    public RecursosSonido() {
        sonidoDisparo = Gdx.audio.newSound(Gdx.files.internal("disparo.wav"));
        sonidoImpacto = Gdx.audio.newSound(Gdx.files.internal("impacto.wav"));
        sonidoMuerte = Gdx.audio.newSound(Gdx.files.internal("muerte.wav"));
        sonidoGanar = Gdx.audio.newSound(Gdx.files.internal("ganar.wav"));

        musicaMenu = Gdx.audio.newMusic(Gdx.files.internal("menu_juego.wav"));
        musicaJuego = Gdx.audio.newMusic(Gdx.files.internal("musica_juego.wav"));
        musicaMenu.setLooping(true);
        musicaJuego.setLooping(true);
    }

    public Sound obtenerSonidoDisparo() {
        return sonidoDisparo;
    }

    public Sound obtenerSonidoImpacto() {
        return sonidoImpacto;
    }

    public Sound obtenerSonidoMuerte() {
        return sonidoMuerte;
    }

    public Sound obtenerSonidoGanar() {
        return sonidoGanar;
    }

    public Music obtenerMusicaMenu() {
        return musicaMenu;
    }

    public Music obtenerMusicaJuego() {
        return musicaJuego;
    }

    public void dispose() {
        sonidoDisparo.dispose();
        sonidoImpacto.dispose();
        sonidoMuerte.dispose();
        sonidoGanar.dispose();
        musicaMenu.dispose();
        musicaJuego.dispose();
    }
}
