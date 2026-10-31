package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ConfiguracionJuego;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.TipoMensaje;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.red.HiloCliente;
import com.mistacorp.game.red.OyenteRed;

public class PantallaSala extends PantallaBase implements OyenteRed {

    private static final float TAMANO_TANQUE = 40f;
    private static final float TAMANO_ICONO = 16f;
    private static final float TAMANO_ESPERANDO = 32f;

    private final HiloCliente CLIENTE;
    private final int NUMERO_JUGADOR;
    private final String NOMBRE_SALA;
    private final TipoMapa MAPA;
    private final Rectangle LUGAR_JUGADOR1;
    private final Rectangle LUGAR_JUGADOR2;
    private final BotonMouse BOTON_SALIR;

    private boolean salaCompleta = false;
    private boolean clienteEntregado = false;
    private float tiempo = 0f;
    private float tiempoDesdeCompleta = 0f;

    public PantallaSala(KatapumPrincipal juego, HiloCliente cliente, int numeroJugador, String nombreSala, TipoMapa mapa) {
        super(juego);
        this.CLIENTE = cliente;
        this.NUMERO_JUGADOR = numeroJugador;
        this.NOMBRE_SALA = nombreSala;
        this.MAPA = mapa;
        LUGAR_JUGADOR1 = new Rectangle(80f, 300f, 360f, 70f);
        LUGAR_JUGADOR2 = new Rectangle(80f, 210f, 360f, 70f);
        BOTON_SALIR = new BotonMouse(80f, 60f, 360f, 60f, "SALIR DE LA SALA");
        CLIENTE.establecerOyente(this);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    salir();
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void mensajeRecibido(TipoMensaje tipo, String[] partes) {
        if (tipo == TipoMensaje.EMPEZAR) {
            salaCompleta = true;
        }
    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        if (salaCompleta) {
            tiempoDesdeCompleta += delta;
            if (tiempoDesdeCompleta >= ConfiguracionJuego.RETRASO_FIN_PARTIDA) {
                clienteEntregado = true;
                JUEGO.setScreen(new PantallaJuegoRed(JUEGO, CLIENTE, NUMERO_JUGADOR));
                return;
            }
        }
        manejarMouse();

        SpriteBatch lote = iniciarDibujo();
        RecursosGraficos recursos = JUEGO.obtenerRecursos();

        dibujarCentrado("SALA: " + NOMBRE_SALA, CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 50);
        dibujarLugar(recursos, LUGAR_JUGADOR1, 1, true, recursos.obtenerTexturaTanqueVerde());
        dibujarLugar(recursos, LUGAR_JUGADOR2, 2, salaCompleta, recursos.obtenerTexturaTanqueAzul());
        dibujarMapa(lote, recursos);
        BOTON_SALIR.dibujar(lote, recursos.obtenerTexturaPixel(), JUEGO.obtenerFuente());

        if (salaCompleta) {
            dibujarCentrado("Sala completa. La partida empieza...", CENTRO_X, 170f);
        } else {
            TextureRegion cuadro = recursos.obtenerAnimacionEsperando().getKeyFrame(tiempo, true);
            lote.draw(cuadro, LUGAR_JUGADOR2.x + LUGAR_JUGADOR2.width - TAMANO_ESPERANDO - 16f,
                LUGAR_JUGADOR2.y + LUGAR_JUGADOR2.height / 2f - TAMANO_ESPERANDO / 2f, TAMANO_ESPERANDO, TAMANO_ESPERANDO);
            dibujarCentrado("Esperando al segundo jugador...", CENTRO_X, 170f);
        }
        lote.end();
    }

    private void dibujarLugar(RecursosGraficos recursos, Rectangle lugar, int numero, boolean ocupado, Texture tanque) {
        SpriteBatch lote = JUEGO.obtenerLote();
        dibujarPanel(lugar);
        float centroY = lugar.y + lugar.height / 2f;
        float xImagen = lugar.x + 16f;
        if (ocupado) {
            lote.draw(tanque, xImagen, centroY - TAMANO_TANQUE / 2f, TAMANO_TANQUE, TAMANO_TANQUE);
        } else {
            lote.draw(recursos.obtenerTexturaSlotVacio(), xImagen, centroY - TAMANO_TANQUE / 2f, TAMANO_TANQUE, TAMANO_TANQUE);
        }

        String nombre = ocupado ? "Jugador " + numero : "Libre";
        if (numero == NUMERO_JUGADOR) {
            nombre = nombre + " (vos)";
        }
        dibujarTexto(nombre, xImagen + TAMANO_TANQUE + 16f, centroY + 8f);

        Texture estado = ocupado ? recursos.obtenerTexturaIconoListo() : recursos.obtenerTexturaIconoNoListo();
        lote.draw(estado, xImagen + TAMANO_TANQUE + 16f, centroY - 26f, TAMANO_ICONO, TAMANO_ICONO);
        if (numero == 1) {
            lote.draw(recursos.obtenerTexturaIconoHost(), xImagen + TAMANO_TANQUE + 40f, centroY - 26f, TAMANO_ICONO, TAMANO_ICONO);
        }
    }

    private void dibujarMapa(SpriteBatch lote, RecursosGraficos recursos) {
        float xMarco = 520f;
        float yMarco = 220f;
        Texture marco = recursos.obtenerTexturaMarcoMapaSeleccionado();
        lote.draw(marco, xMarco, yMarco, marco.getWidth(), marco.getHeight());
        lote.draw(recursos.obtenerTexturaMiniaturaBosque(), xMarco + 4f, yMarco + 4f);
        dibujarCentrado("Mapa: " + MAPA.obtenerNombreVisible(), xMarco + marco.getWidth() / 2f, yMarco - 15f);
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_SALIR.actualizarResaltado(mouse.x, mouse.y);
        if (Gdx.input.justTouched() && BOTON_SALIR.contienePunto(mouse.x, mouse.y)) {
            salir();
        }
    }

    private void salir() {
        clienteEntregado = true;
        CLIENTE.detener();
        JUEGO.setScreen(new PantallaMenu(JUEGO));
    }

    @Override
    public void dispose() {
        if (!clienteEntregado) {
            CLIENTE.detener();
        }
    }
}
