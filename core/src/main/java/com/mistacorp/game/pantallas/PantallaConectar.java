package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.TipoMensaje;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.red.HiloCliente;
import com.mistacorp.game.red.OyenteRed;

import java.net.SocketException;
import java.net.UnknownHostException;

public class PantallaConectar extends ScreenAdapter implements OyenteRed {

    private static final int LARGO_MAXIMO_IP = 15;
    private static final Color COLOR_CAMPO = new Color(0.16f, 0.16f, 0.22f, 1f);
    private static final float GROSOR_BORDE = 3f;

    private final KatapumPrincipal JUEGO;
    private final OrthographicCamera CAMARA = new OrthographicCamera();
    private final Viewport VENTANA_GRAFICA = new FitViewport(ConfiguracionPartida.ANCHO_MUNDO, ConfiguracionPartida.ALTO_MUNDO, CAMARA);

    private final Rectangle CAMPO_IP;
    private final BotonMouse BOTON_CONECTAR;
    private final BotonMouse BOTON_VOLVER;
    private final StringBuilder IP = new StringBuilder("127.0.0.1");

    private EstadoConexion estado = EstadoConexion.ESCRIBIENDO_IP;
    private HiloCliente cliente = null;
    private int numeroJugador = 0;
    private String mensajeError = "";
    private float tiempoCursor = 0f;

    public PantallaConectar(KatapumPrincipal juego) {
        this.JUEGO = juego;
        float centroX = ConfiguracionPartida.ANCHO_MUNDO / 2f;
        CAMPO_IP = new Rectangle(centroX - 150f, 300f, 300f, 50f);
        BOTON_CONECTAR = new BotonMouse(centroX - 150f, 210f, 300f, 60f, "CONECTAR");
        BOTON_VOLVER = new BotonMouse(centroX - 150f, 120f, 300f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char caracter) {
                escribir(caracter);
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    conectar();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    volverAlMenu();
                    return true;
                }
                return false;
            }
        });
    }

    private void escribir(char caracter) {
        if (estado != EstadoConexion.ESCRIBIENDO_IP && estado != EstadoConexion.ERROR && estado != EstadoConexion.SALA_LLENA) {
            return;
        }
        if (caracter == '\b') {
            if (IP.length() > 0) {
                IP.deleteCharAt(IP.length() - 1);
            }
        } else if ((Character.isDigit(caracter) || caracter == '.') && IP.length() < LARGO_MAXIMO_IP) {
            IP.append(caracter);
        }
    }

    private void conectar() {
        if (cliente != null) {
            return;
        }
        try {
            cliente = new HiloCliente(IP.toString(), ConfiguracionRed.PUERTO_SERVIDOR, this);
            cliente.start();
            estado = EstadoConexion.CONECTANDO;
        } catch (UnknownHostException e) {
            mostrarError("IP invalida");
        } catch (SocketException e) {
            mostrarError("No se pudo abrir la red");
        }
    }

    private void mostrarError(String mensaje) {
        estado = EstadoConexion.ERROR;
        mensajeError = mensaje;
    }

    @Override
    public void mensajeRecibido(TipoMensaje tipo, String[] partes) {
        switch (tipo) {
            case ASIGNAR:
                numeroJugador = Integer.parseInt(partes[1]);
                if (estado == EstadoConexion.CONECTANDO) {
                    estado = EstadoConexion.ESPERANDO_RIVAL;
                }
                break;
            case EMPEZAR:
                estado = EstadoConexion.SALA_COMPLETA;
                break;
            case SALA_LLENA:
                detenerCliente();
                estado = EstadoConexion.SALA_LLENA;
                break;
            default:
                break;
        }
    }

    private void detenerCliente() {
        if (cliente != null) {
            cliente.detener();
            cliente = null;
        }
    }

    private void volverAlMenu() {
        detenerCliente();
        JUEGO.setScreen(new PantallaMenu(JUEGO));
    }

    @Override
    public void dispose() {
        detenerCliente();
    }

    @Override
    public void resize(int ancho, int alto) {
        VENTANA_GRAFICA.update(ancho, alto, true);
    }

    @Override
    public void render(float delta) {
        tiempoCursor += delta;
        manejarMouse();

        ScreenUtils.clear(0.08f, 0.08f, 0.1f, 1f);
        VENTANA_GRAFICA.apply();
        CAMARA.update();
        SpriteBatch lote = JUEGO.obtenerLote();
        BitmapFont fuente = JUEGO.obtenerFuente();
        Texture pixel = JUEGO.obtenerRecursos().obtenerTexturaPixel();
        lote.setProjectionMatrix(CAMARA.combined);
        lote.begin();

        dibujarCentrado(lote, fuente, "PARTIDA LAN", ConfiguracionPartida.ALTO_MUNDO - 70);
        dibujarCentrado(lote, fuente, "IP del servidor:", CAMPO_IP.y + CAMPO_IP.height + 30);
        dibujarCampoIp(lote, fuente, pixel);
        BOTON_CONECTAR.dibujar(lote, pixel, fuente);
        BOTON_VOLVER.dibujar(lote, pixel, fuente);
        dibujarCentrado(lote, fuente, textoDeEstado(), 70);

        lote.end();
    }

    private void dibujarCampoIp(SpriteBatch lote, BitmapFont fuente, Texture pixel) {
        lote.setColor(Color.WHITE);
        lote.draw(pixel, CAMPO_IP.x, CAMPO_IP.y, CAMPO_IP.width, CAMPO_IP.height);
        lote.setColor(COLOR_CAMPO);
        lote.draw(pixel, CAMPO_IP.x + GROSOR_BORDE, CAMPO_IP.y + GROSOR_BORDE,
            CAMPO_IP.width - GROSOR_BORDE * 2, CAMPO_IP.height - GROSOR_BORDE * 2);
        lote.setColor(Color.WHITE);

        boolean mostrarCursor = cliente == null && ((int) (tiempoCursor * 2)) % 2 == 0;
        String texto = IP.toString() + (mostrarCursor ? "_" : "");
        GlyphLayout layout = new GlyphLayout(fuente, texto);
        fuente.draw(lote, texto, CAMPO_IP.x + 14f, CAMPO_IP.y + CAMPO_IP.height / 2f + layout.height / 2f);
    }

    private String textoDeEstado() {
        switch (estado) {
            case CONECTANDO:
                return "Conectando a " + IP + "...";
            case ESPERANDO_RIVAL:
                return "Sos el Jugador " + numeroJugador + ". Esperando al otro jugador...";
            case SALA_COMPLETA:
                return "Sala completa. Sos el Jugador " + numeroJugador + ".";
            case SALA_LLENA:
                return "La sala esta llena.";
            case ERROR:
                return mensajeError;
            default:
                return "Escribi la IP y presiona CONECTAR (ENTER)";
        }
    }

    private void dibujarCentrado(SpriteBatch lote, BitmapFont fuente, String texto, float y) {
        GlyphLayout layout = new GlyphLayout(fuente, texto);
        fuente.draw(lote, texto, ConfiguracionPartida.ANCHO_MUNDO / 2f - layout.width / 2f, y);
    }

    private void manejarMouse() {
        Vector2 mouseMundo = new Vector2(Gdx.input.getX(), Gdx.input.getY());
        VENTANA_GRAFICA.unproject(mouseMundo);

        BOTON_CONECTAR.actualizarResaltado(mouseMundo.x, mouseMundo.y);
        BOTON_VOLVER.actualizarResaltado(mouseMundo.x, mouseMundo.y);

        if (Gdx.input.justTouched()) {
            if (BOTON_CONECTAR.contienePunto(mouseMundo.x, mouseMundo.y)) {
                conectar();
            } else if (BOTON_VOLVER.contienePunto(mouseMundo.x, mouseMundo.y)) {
                volverAlMenu();
            }
        }
    }
}
