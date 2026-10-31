package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.ConfiguracionPartida;

public class PantallaFinPartida extends ScreenAdapter {

    private static final Color COLOR_RESALTADO = new Color(1f, 1f, 1f, 0.25f);

    private final KatapumPrincipal JUEGO;
    private final Texture TEXTURA_BANNER;
    private final boolean ES_PARTIDA_RED;
    private final String AVISO;

    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO, CAMARA);

    private final Rectangle LIMITES_BOTON_JUGAR;
    private final Rectangle LIMITES_BOTON_MENU;
    private boolean botonJugarResaltado = false;
    private boolean botonMenuResaltado = false;

    public PantallaFinPartida(KatapumPrincipal juego, String nombreGanador) {
        this(juego, nombreGanador, false, "");
    }

    public PantallaFinPartida(KatapumPrincipal juego, String nombreGanador, boolean esPartidaRed, String aviso) {
        this.JUEGO = juego;
        this.ES_PARTIDA_RED = esPartidaRed;
        this.AVISO = aviso;

        RecursosGraficos recursos = juego.obtenerRecursos();
        this.TEXTURA_BANNER = "Jugador 1".equals(nombreGanador)
            ? recursos.obtenerTexturaGanadorJugador1()
            : recursos.obtenerTexturaGanadorJugador2();

        float anchoBoton = 220f;
        float altoBoton = 60f;
        float separacion = 40f;
        float xBotones = ConfiguracionPartida.ANCHO_MUNDO / 2f - (anchoBoton * 2 + separacion) / 2f;
        float yBotones = 180f;

        LIMITES_BOTON_JUGAR = new Rectangle(xBotones, yBotones, anchoBoton, altoBoton);
        LIMITES_BOTON_MENU = new Rectangle(xBotones + anchoBoton + separacion, yBotones, anchoBoton, altoBoton);
    }

    @Override
    public void show() {
        JUEGO.obtenerSonidos().obtenerSonidoGanar().play(ControlVolumen.obtenerVolumenEfectivo());

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    volverAJugar();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    JUEGO.setScreen(new PantallaMenu(JUEGO));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
    }

    @Override
    public void render(float delta) {
        manejarMouse();

        ScreenUtils.clear(0.05f, 0.05f, 0.08f, 1f);

        VENTANA_GRAFICA.apply();
        CAMARA.update();
        JUEGO.obtenerLote().setProjectionMatrix(CAMARA.combined);
        JUEGO.obtenerLote().begin();

        float anchoBanner = 420f;
        float altoBanner = 110f;
        float xBanner = ConfiguracionPartida.ANCHO_MUNDO / 2f - anchoBanner / 2f;
        float yBanner = 300f;
        JUEGO.obtenerLote().draw(TEXTURA_BANNER, xBanner, yBanner, anchoBanner, altoBanner);

        if (!AVISO.isEmpty()) {
            GlyphLayout layoutAviso = new GlyphLayout(JUEGO.obtenerFuente(), AVISO);
            JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), AVISO,
                ConfiguracionPartida.ANCHO_MUNDO / 2f - layoutAviso.width / 2f, yBanner - 20f);
        }

        RecursosGraficos recursos = JUEGO.obtenerRecursos();
        Texture pixel = recursos.obtenerTexturaPixel();

        JUEGO.obtenerLote().draw(recursos.obtenerTexturaBotonVolverJugar(),
            LIMITES_BOTON_JUGAR.x, LIMITES_BOTON_JUGAR.y, LIMITES_BOTON_JUGAR.width, LIMITES_BOTON_JUGAR.height);
        JUEGO.obtenerLote().draw(recursos.obtenerTexturaBotonVolverMenu(),
            LIMITES_BOTON_MENU.x, LIMITES_BOTON_MENU.y, LIMITES_BOTON_MENU.width, LIMITES_BOTON_MENU.height);

        if (botonJugarResaltado) {
            JUEGO.obtenerLote().setColor(COLOR_RESALTADO);
            JUEGO.obtenerLote().draw(pixel, LIMITES_BOTON_JUGAR.x, LIMITES_BOTON_JUGAR.y, LIMITES_BOTON_JUGAR.width, LIMITES_BOTON_JUGAR.height);
            JUEGO.obtenerLote().setColor(Color.WHITE);
        }
        if (botonMenuResaltado) {
            JUEGO.obtenerLote().setColor(COLOR_RESALTADO);
            JUEGO.obtenerLote().draw(pixel, LIMITES_BOTON_MENU.x, LIMITES_BOTON_MENU.y, LIMITES_BOTON_MENU.width, LIMITES_BOTON_MENU.height);
            JUEGO.obtenerLote().setColor(Color.WHITE);
        }

        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), "ENTER", LIMITES_BOTON_JUGAR.x + 80, LIMITES_BOTON_JUGAR.y - 12);
        JUEGO.obtenerFuente().draw(JUEGO.obtenerLote(), "ESC", LIMITES_BOTON_MENU.x + 90, LIMITES_BOTON_MENU.y - 12);

        JUEGO.obtenerLote().end();
    }

    private void volverAJugar() {
        if (ES_PARTIDA_RED) {
            JUEGO.setScreen(new PantallaConectar(JUEGO));
        } else {
            JUEGO.setScreen(new PantallaJuego(JUEGO));
        }
    }

    private void manejarMouse() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        VENTANA_GRAFICA.unproject(mouseMundo);

        botonJugarResaltado = LIMITES_BOTON_JUGAR.contains(mouseMundo.x, mouseMundo.y);
        botonMenuResaltado = LIMITES_BOTON_MENU.contains(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            if (botonJugarResaltado) {
                volverAJugar();
            } else if (botonMenuResaltado) {
                JUEGO.setScreen(new PantallaMenu(JUEGO));
            }
        }
    }
}
