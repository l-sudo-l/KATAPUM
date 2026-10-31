package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.compartido.ConfiguracionPartida;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.TipoMensaje;
import com.mistacorp.game.interfaz.BotonMouse;
import com.mistacorp.game.interfaz.CampoTexto;
import com.mistacorp.game.interfaz.TipoCampoTexto;
import com.mistacorp.game.red.HiloCliente;
import com.mistacorp.game.red.OyenteRed;

import java.net.SocketException;
import java.net.UnknownHostException;

public class PantallaConectar extends PantallaBase implements OyenteRed {

    private static final int LARGO_MAXIMO_IP = 15;
    private static final float TAMANO_ANIMACION = 32f;

    private final CampoTexto CAMPO_IP;
    private final BotonMouse BOTON_CONECTAR;
    private final BotonMouse BOTON_VOLVER;
    private final boolean CONECTAR_AL_MOSTRAR;

    private EstadoConexion estado = EstadoConexion.ESCRIBIENDO_IP;
    private HiloCliente cliente = null;
    private String mensajeError = "";
    private float tiempo = 0f;

    public PantallaConectar(KatapumPrincipal juego) {
        this(juego, "127.0.0.1", false);
    }

    public PantallaConectar(KatapumPrincipal juego, String aviso) {
        this(juego, "127.0.0.1", false);
        mostrarError(aviso);
    }

    public PantallaConectar(KatapumPrincipal juego, String ip, boolean conectarAlMostrar) {
        super(juego);
        this.CONECTAR_AL_MOSTRAR = conectarAlMostrar;
        CAMPO_IP = new CampoTexto(CENTRO_X - 150f, 300f, 300f, 50f, ip, LARGO_MAXIMO_IP, TipoCampoTexto.DIRECCION_IP);
        BOTON_CONECTAR = new BotonMouse(CENTRO_X - 150f, 210f, 300f, 60f, "CONECTAR");
        BOTON_VOLVER = new BotonMouse(CENTRO_X - 150f, 120f, 300f, 60f, "VOLVER");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char caracter) {
                if (puedeEscribir()) {
                    CAMPO_IP.escribir(caracter);
                }
                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    conectar();
                    return true;
                }
                if (keycode == Input.Keys.ESCAPE) {
                    volver();
                    return true;
                }
                return false;
            }
        });
        if (CONECTAR_AL_MOSTRAR) {
            conectar();
        }
    }

    private boolean puedeEscribir() {
        return cliente == null;
    }

    private void conectar() {
        if (cliente != null) {
            return;
        }
        try {
            cliente = new HiloCliente(CAMPO_IP.obtenerTexto(), ConfiguracionRed.PUERTO_SERVIDOR, this);
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
        if (cliente == null) {
            return;
        }
        if (tipo == TipoMensaje.ASIGNAR) {
            int numeroJugador = Integer.parseInt(partes[1]);
            String nombreSala = partes[2];
            TipoMapa mapa = TipoMapa.valueOf(partes[3]);
            HiloCliente clienteEnSala = cliente;
            cliente = null;
            JUEGO.setScreen(new PantallaSala(JUEGO, clienteEnSala, numeroJugador, nombreSala, mapa));
        } else if (tipo == TipoMensaje.SALA_LLENA) {
            detenerCliente();
            estado = EstadoConexion.SALA_LLENA;
        }
    }

    private void detenerCliente() {
        if (cliente != null) {
            cliente.detener();
            cliente = null;
        }
    }

    private void volver() {
        detenerCliente();
        JUEGO.setScreen(new PantallaMenu(JUEGO));
    }

    @Override
    public void dispose() {
        detenerCliente();
    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        CAMPO_IP.actualizar(delta);
        manejarMouse();

        SpriteBatch lote = iniciarDibujo();
        Texture pixel = JUEGO.obtenerRecursos().obtenerTexturaPixel();

        dibujarCentrado("CONECTAR POR IP", CENTRO_X, ConfiguracionPartida.ALTO_MUNDO - 70);
        dibujarCentrado("IP del servidor:", CENTRO_X, CAMPO_IP.obtenerLimites().y + CAMPO_IP.obtenerLimites().height + 30);
        CAMPO_IP.dibujar(lote, pixel, JUEGO.obtenerFuente(), puedeEscribir());
        BOTON_CONECTAR.dibujar(lote, pixel, JUEGO.obtenerFuente());
        BOTON_VOLVER.dibujar(lote, pixel, JUEGO.obtenerFuente());
        dibujarCentrado(textoDeEstado(), CENTRO_X, 70);

        if (estado == EstadoConexion.CONECTANDO) {
            TextureRegion cuadro = JUEGO.obtenerRecursos().obtenerAnimacionConectando().getKeyFrame(tiempo, true);
            lote.draw(cuadro, CENTRO_X - TAMANO_ANIMACION / 2f, 80f, TAMANO_ANIMACION, TAMANO_ANIMACION);
        }
        lote.end();
    }

    private String textoDeEstado() {
        switch (estado) {
            case CONECTANDO:
                return "Conectando a " + CAMPO_IP.obtenerTexto() + "...";
            case SALA_LLENA:
                return "La sala esta llena.";
            case ERROR:
                return mensajeError;
            default:
                return "Escribi la IP y presiona CONECTAR (ENTER)";
        }
    }

    private void manejarMouse() {
        Vector2 mouse = obtenerMouseMundo();
        BOTON_CONECTAR.actualizarResaltado(mouse.x, mouse.y);
        BOTON_VOLVER.actualizarResaltado(mouse.x, mouse.y);

        if (Gdx.input.justTouched()) {
            if (BOTON_CONECTAR.contienePunto(mouse.x, mouse.y)) {
                conectar();
            } else if (BOTON_VOLVER.contienePunto(mouse.x, mouse.y)) {
                volver();
            }
        }
    }
}
