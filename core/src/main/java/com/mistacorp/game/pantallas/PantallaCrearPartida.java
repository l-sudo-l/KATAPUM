package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.interfaz.CampoTexto;
import com.mistacorp.game.interfaz.TipoCampoTexto;

public class PantallaCrearPartida extends PantallaBase {

    private static final float TAMANO_FLECHA = 32f;
    private static final float X_MARCO = 560f;
    private static final float Y_MARCO = 250f;

    private final CampoTexto CAMPO_NOMBRE;
    private final Rectangle FLECHA_MENOS_JUGADORES;
    private final Rectangle FLECHA_MAS_JUGADORES;
    private final Rectangle FLECHA_MAPA_ANTERIOR;
    private final Rectangle FLECHA_MAPA_SIGUIENTE;
    private final BotonMouse BOTON_CREAR;
    private final BotonMouse BOTON_VOLVER;

    private int cantidadJugadores = ConfiguracionRed.MAXIMO_JUGADORES;
    private int indiceMapa = 0;
    private String mensajeError = "";

    public PantallaCrearPartida(KatapumPrincipal juego) {
        super(juego);
        CAMPO_NOMBRE = new CampoTexto(80f, 360f, 380f, 50f, ConfiguracionRed.NOMBRE_SALA_POR_DEFECTO,
            ConfiguracionRed.LARGO_MAXIMO_NOMBRE_SALA, TipoCampoTexto.NOMBRE);
        FLECHA_MENOS_JUGADORES = new Rectangle(80f, 250f, TAMANO_FLECHA, TAMANO_FLECHA);
        FLECHA_MAS_JUGADORES = new Rectangle(220f, 250f, TAMANO_FLECHA, TAMANO_FLECHA);
        FLECHA_MAPA_ANTERIOR = new Rectangle(X_MARCO, Y_MARCO - 60f, TAMANO_FLECHA, TAMANO_FLECHA);
        FLECHA_MAPA_SIGUIENTE = new Rectangle(X_MARCO + 248f - TAMANO_FLECHA, Y_MARCO - 60f, TAMANO_FLECHA, TAMANO_FLECHA);
        BOTON_CREAR = new BotonMouse(80f, 50f, 380f, 60f, "CREAR Y ESPERAR");
        BOTON_VOLVER = new BotonMouse(500f, 50f, 380f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char caracter) {
                CAMPO_NOMBRE.escribir(caracter);
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    crear();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    JUEGO.setScreen(new PantallaPartidasLan(JUEGO));
                    return true;
                }
                return false;
            }
        });
    }

    private void crear() {
        TipoMapa mapa = TipoMapa.values()[indiceMapa];
        String error = JUEGO.iniciarServidorLocal(CAMPO_NOMBRE.obtenerTexto(), mapa);
        if (error != null) {
            mensajeError = error;
            return;
        }
        JUEGO.setScreen(new PantallaConectar(JUEGO, "127.0.0.1", true));
    }

    private void cambiarJugadores(int cambio) {
        cantidadJugadores = Math.max(ConfiguracionRed.MINIMO_JUGADORES,
            Math.min(ConfiguracionRed.MAXIMO_JUGADORES, cantidadJugadores + cambio));
    }

    private void cambiarMapa(int cambio) {
        int cantidad = TipoMapa.values().length;
        indiceMapa = (indiceMapa + cambio + cantidad) % cantidad;
    }

    @Override
    public void render(float delta) {
        CAMPO_NOMBRE.actualizar(delta);
        manejarMouse();

        SpriteBatch lote = iniciarDibujo();
        RecursosGraficos recursos = JUEGO.obtenerRecursos();
        Texture pixel = recursos.obtenerTexturaPixel();

        dibujarCentrado("CREAR PARTIDA", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 50);

        dibujarTexto("Nombre de la sala:", 80f, 440f);
        CAMPO_NOMBRE.dibujar(lote, pixel, JUEGO.obtenerFuente(), true);

        dibujarTexto("Jugadores (incluido vos):", 80f, 320f);
        lote.draw(recursos.obtenerTexturaFlechaIzquierda(), FLECHA_MENOS_JUGADORES.x, FLECHA_MENOS_JUGADORES.y, TAMANO_FLECHA, TAMANO_FLECHA);
        dibujarCentrado(String.valueOf(cantidadJugadores), 166f, FLECHA_MENOS_JUGADORES.y + 22f);
        lote.draw(recursos.obtenerTexturaFlechaDerecha(), FLECHA_MAS_JUGADORES.x, FLECHA_MAS_JUGADORES.y, TAMANO_FLECHA, TAMANO_FLECHA);
        dibujarTexto("Maximo " + ConfiguracionRed.MAXIMO_JUGADORES + " jugadores", 80f, 230f);

        TipoMapa mapa = TipoMapa.values()[indiceMapa];
        dibujarTexto("Mapa:", X_MARCO, Y_MARCO + 175f);
        lote.draw(recursos.obtenerTexturaMarcoMapaSeleccionado(), X_MARCO, Y_MARCO);
        lote.draw(recursos.obtenerTexturaMiniaturaBosque(), X_MARCO + 4f, Y_MARCO + 4f);
        lote.draw(recursos.obtenerTexturaFlechaIzquierda(), FLECHA_MAPA_ANTERIOR.x, FLECHA_MAPA_ANTERIOR.y, TAMANO_FLECHA, TAMANO_FLECHA);
        dibujarCentrado(mapa.obtenerNombreVisible(), X_MARCO + 124f, FLECHA_MAPA_ANTERIOR.y + 22f);
        lote.draw(recursos.obtenerTexturaFlechaDerecha(), FLECHA_MAPA_SIGUIENTE.x, FLECHA_MAPA_SIGUIENTE.y, TAMANO_FLECHA, TAMANO_FLECHA);

        BOTON_CREAR.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_VOLVER.dibujar(lote, pixel, JUEGO.obtenerFuente());
        dibujarCentrado(mensajeError, CENTRO_X, 150f);
        lote.end();
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_CREAR.actualizarResaltado(mouse.x, mouse.y);
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);

        if (!Gdx.input.justTouched()) {
            return;
        }
        if (BOTON_CREAR.contienePunto(mouse.x, mouse.y)) {
            crear();
        } else if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaPartidasLan(JUEGO));
        } else if (FLECHA_MENOS_JUGADORES.contains(mouse.x, mouse.y)) {
            cambiarJugadores(-1);
        } else if (FLECHA_MAS_JUGADORES.contains(mouse.x, mouse.y)) {
            cambiarJugadores(1);
        } else if (FLECHA_MAPA_ANTERIOR.contains(mouse.x, mouse.y)) {
            cambiarMapa(-1);
        } else if (FLECHA_MAPA_SIGUIENTE.contains(mouse.x, mouse.y)) {
            cambiarMapa(1);
        }
    }
}
