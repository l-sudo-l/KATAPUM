package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.red.HiloBusqueda;
import com.mistacorp.game.red.OyenteBusqueda;
import com.mistacorp.game.red.PartidaEncontrada;

import java.net.SocketException;

public class PantallaPartidasLan extends PantallaBase implements OyenteBusqueda {

    private static final int MAXIMO_FILAS = 6;
    private static final float ALTO_FILA = 44f;
    private static final float TAMANO_ICONO = 16f;
    private static final long PING_BUENO = 50;
    private static final long PING_REGULAR = 150;
    private static final Color COLOR_FILA_RESALTADA = new Color(0.85f, 0.62f, 0.12f, 0.35f);

    private final Rectangle PANEL_LISTA;
    private final Rectangle BOTON_ACTUALIZAR;
    private final BotonMouse BOTON_CREAR;
    private final BotonMouse BOTON_IP;
    private final BotonMouse BOTON_VOLVER;
    private final Array<PartidaEncontrada> PARTIDAS = new Array<>();

    private HiloBusqueda busqueda = null;
    private int filaResaltada = -1;
    private String mensajeError = "";

    public PantallaPartidasLan(KatapumPrincipal juego) {
        super(juego);
        PANEL_LISTA = new Rectangle(60f, 140f, 840f, 310f);
        BOTON_ACTUALIZAR = new Rectangle(PANEL_LISTA.x + PANEL_LISTA.width - 44f, PANEL_LISTA.y + PANEL_LISTA.height + 8f, 36f, 36f);
        BOTON_CREAR = new BotonMouse(60f, 50f, 260f, 60f, "CREAR PARTIDA");
        BOTON_IP = new BotonMouse(350f, 50f, 260f, 60f, "CONECTAR POR IP");
        BOTON_VOLVER = new BotonMouse(640f, 50f, 260f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    JUEGO.setScreen(new PantallaMenu(JUEGO));
                    return true;
                }
                return false;
            }
        });
        try {
            busqueda = new HiloBusqueda(this);
            busqueda.start();
        } catch (SocketException e) {
            mensajeError = "No se pudo buscar partidas en la red";
        }
    }

    @Override
    public void hide() {
        detenerBusqueda();
    }

    @Override
    public void dispose() {
        detenerBusqueda();
    }

    private void detenerBusqueda() {
        if (busqueda != null) {
            busqueda.detener();
            busqueda = null;
        }
    }

    @Override
    public void partidaEncontrada(PartidaEncontrada nueva) {
        for (int i = PARTIDAS.size - 1; i >= 0; i--) {
            PartidaEncontrada existente = PARTIDAS.get(i);
            boolean mismaSala = existente.obtenerNombreSala().equals(nueva.obtenerNombreSala());
            if (existente.obtenerIp().equals(nueva.obtenerIp())) {
                PARTIDAS.removeIndex(i);
            } else if (mismaSala && nueva.esLocal() && !existente.esLocal()) {
                return;
            } else if (mismaSala && existente.esLocal() && !nueva.esLocal()) {
                PARTIDAS.removeIndex(i);
            }
        }
        PARTIDAS.add(nueva);
    }

    private void quitarPartidasViejas() {
        long limite = System.currentTimeMillis() - ConfiguracionRed.MILISEGUNDOS_DESCONEXION;
        for (int i = PARTIDAS.size - 1; i >= 0; i--) {
            if (PARTIDAS.get(i).obtenerMomentoRecibida() < limite) {
                PARTIDAS.removeIndex(i);
            }
        }
    }

    private void actualizarLista() {
        PARTIDAS.clear();
        if (busqueda != null) {
            busqueda.buscarAhora();
        }
    }

    private Rectangle limitesDeFila(int indice) {
        float y = PANEL_LISTA.y + PANEL_LISTA.height - 50f - (indice + 1) * ALTO_FILA;
        return new Rectangle(PANEL_LISTA.x + 8f, y, PANEL_LISTA.width - 16f, ALTO_FILA - 4f);
    }

    @Override
    public void render(float delta) {
        quitarPartidasViejas();
        manejarMouse();

        SpriteBatch lote = iniciarDibujo();
        RecursosGraficos recursos = JUEGO.obtenerRecursos();
        Texture pixel = recursos.obtenerTexturaPixel();

        dibujarCentrado("PARTIDAS EN LA RED LOCAL", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 40);
        dibujarPanel(PANEL_LISTA);
        lote.draw(recursos.obtenerTexturaIconoActualizar(), BOTON_ACTUALIZAR.x, BOTON_ACTUALIZAR.y,
            BOTON_ACTUALIZAR.width, BOTON_ACTUALIZAR.height);

        float yTitulos = PANEL_LISTA.y + PANEL_LISTA.height - 20f;
        dibujarTexto("SALA", PANEL_LISTA.x + 50f, yTitulos);
        dibujarTexto("MAPA", PANEL_LISTA.x + 330f, yTitulos);
        dibujarTexto("JUGADORES", PANEL_LISTA.x + 470f, yTitulos);
        dibujarTexto("IP", PANEL_LISTA.x + 620f, yTitulos);

        if (PARTIDAS.size == 0) {
            dibujarCentrado("Buscando partidas...", CENTRO_X, PANEL_LISTA.y + PANEL_LISTA.height / 2f);
        }
        for (int i = 0; i < PARTIDAS.size && i < MAXIMO_FILAS; i++) {
            dibujarFila(lote, recursos, pixel, i, PARTIDAS.get(i));
        }

        BOTON_CREAR.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_IP.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_VOLVER.dibujar(lote, pixel, JUEGO.obtenerFuente());
        dibujarCentrado(mensajeError, CENTRO_X, 130f);
        lote.end();
    }

    private void dibujarFila(SpriteBatch lote, RecursosGraficos recursos, Texture pixel, int indice, PartidaEncontrada partida) {
        Rectangle fila = limitesDeFila(indice);
        if (indice == filaResaltada && !partida.estaLlena()) {
            lote.setColor(COLOR_FILA_RESALTADA);
            lote.draw(pixel, fila.x, fila.y, fila.width, fila.height);
            lote.setColor(Color.WHITE);
        }
        float yIcono = fila.y + fila.height / 2f - TAMANO_ICONO / 2f;
        float yTexto = fila.y + fila.height / 2f + 7f;
        lote.draw(regionDeSenal(recursos, partida.obtenerPing()), fila.x + 12f, yIcono, TAMANO_ICONO, TAMANO_ICONO);
        dibujarTexto(partida.obtenerNombreSala(), PANEL_LISTA.x + 50f, yTexto);
        dibujarTexto(partida.obtenerMapa().obtenerNombreVisible(), PANEL_LISTA.x + 330f, yTexto);
        dibujarTexto(partida.obtenerJugadores() + "/" + partida.obtenerMaximoJugadores(), PANEL_LISTA.x + 470f, yTexto);
        if (partida.estaLlena()) {
            lote.draw(recursos.obtenerTexturaIconoSalaLlena(), PANEL_LISTA.x + 520f, yIcono, TAMANO_ICONO, TAMANO_ICONO);
        }
        dibujarTexto(partida.obtenerIp(), PANEL_LISTA.x + 620f, yTexto);
    }

    private TextureRegion regionDeSenal(RecursosGraficos recursos, long ping) {
        TextureRegion[] cuadros = recursos.obtenerFramesIconoSenal();
        if (ping < PING_BUENO) {
            return cuadros[2];
        }
        if (ping < PING_REGULAR) {
            return cuadros[1];
        }
        return cuadros[0];
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_CREAR.actualizarResaltado(mouse.x, mouse.y);
        BOTON_IP.actualizarResaltado(mouse.x, mouse.y);
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);
        filaResaltada = -1;
        for (int i = 0; i < PARTIDAS.size && i < MAXIMO_FILAS; i++) {
            if (limitesDeFila(i).contains(mouse.x, mouse.y)) {
                filaResaltada = i;
            }
        }

        if (!Gdx.input.justTouched()) {
            return;
        }
        if (filaResaltada >= 0) {
            unirseA(PARTIDAS.get(filaResaltada));
        } else if (BOTON_ACTUALIZAR.contains(mouse.x, mouse.y)) {
            actualizarLista();
        } else if (BOTON_CREAR.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaCrearPartida(JUEGO));
        } else if (BOTON_IP.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaConectar(JUEGO));
        } else if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
            JUEGO.setScreen(new PantallaMenu(JUEGO));
        }
    }

    private void unirseA(PartidaEncontrada partida) {
        if (partida.estaLlena()) {
            mensajeError = "Esa sala esta llena";
            return;
        }
        JUEGO.setScreen(new PantallaConectar(JUEGO, partida.obtenerIp(), true));
    }
}
