package com.mistacorp.game.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mistacorp.game.ControlVolumen;
import com.mistacorp.game.KatapumPrincipal;
import com.mistacorp.game.RecursosGraficos;
import com.mistacorp.game.compartido.red.ConfiguracionRed;
import com.mistacorp.game.compartido.red.Protocolo;
import com.mistacorp.game.compartido.red.TipoMensaje;
import com.mistacorp.game.red.HiloCliente;
import com.mistacorp.game.red.OyenteRed;

public class PantallaFinPartidaRed extends PantallaBase implements OyenteRed {

    private static final Color COLOR_RESALTADO = new Color(1f, 1f, 1f, 0.25f);
    private static final Color COLOR_PEDIDO = new Color(0.85f, 0.62f, 0.12f, 0.45f);
    private static final float ANCHO_BANNER = 420f;
    private static final float ALTO_BANNER = 110f;
    private static final float Y_BANNER = 320f;

    private final HiloCliente CLIENTE;
    private final int NUMERO_JUGADOR;
    private final Texture TEXTURA_BANNER;
    private final Rectangle LIMITES_BOTON_REVANCHA;
    private final Rectangle LIMITES_BOTON_SALIR;

    private boolean pidoRevancha = false;
    private boolean rivalPideRevancha = false;
    private boolean rivalSeFue;
    private boolean clienteEntregado = false;
    private float tiempoDesdeAviso = ConfiguracionRed.SEGUNDOS_ENTRE_AVISOS_REVANCHA;
    private float tiempoSinRespuesta = 0f;
    private float tiempo = 0f;

    public PantallaFinPartidaRed(KatapumPrincipal juego, HiloCliente cliente, int numeroJugador, int numeroGanador, boolean rivalSeFue) {
        super(juego);
        this.CLIENTE = cliente;
        this.NUMERO_JUGADOR = numeroJugador;
        this.rivalSeFue = rivalSeFue;
        RecursosGraficos recursos = juego.obtenerRecursos();
        this.TEXTURA_BANNER = numeroGanador == 1 ? recursos.obtenerTexturaGanadorJugador1() : recursos.obtenerTexturaGanadorJugador2();

        float anchoBoton = 220f;
        float altoBoton = 60f;
        float separacion = 40f;
        float xBotones = CENTRO_X - (anchoBoton * 2 + separacion) / 2f;
        LIMITES_BOTON_REVANCHA = new Rectangle(xBotones, 130f, anchoBoton, altoBoton);
        LIMITES_BOTON_SALIR = new Rectangle(xBotones + anchoBoton + separacion, 130f, anchoBoton, altoBoton);
        CLIENTE.establecerOyente(this);
    }

    @Override
    public void show() {
        JUEGO.obtenerSonidos().obtenerSonidoGanar().play(ControlVolumen.obtenerVolumenEfectivo());
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    pedirRevancha();
                    return true;
                }
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
        tiempoSinRespuesta = 0f;
        switch (tipo) {
            case ESTADO_REVANCHA:
                rivalPideRevancha = "1".equals(partes[NUMERO_JUGADOR == 1 ? 2 : 1]);
                break;
            case RIVAL_SALIO:
                rivalSeFue = true;
                rivalPideRevancha = false;
                break;
            case EMPEZAR:
                clienteEntregado = true;
                JUEGO.setScreen(new PantallaJuegoRed(JUEGO, CLIENTE, NUMERO_JUGADOR));
                break;
            default:
                break;
        }
    }

    private void pedirRevancha() {
        pidoRevancha = true;
        tiempoDesdeAviso = ConfiguracionRed.SEGUNDOS_ENTRE_AVISOS_REVANCHA;
    }

    private void salir() {
        clienteEntregado = true;
        CLIENTE.detener();
        JUEGO.setScreen(new PantallaPartidasLan(JUEGO));
    }

    private void avisarAlServidor(float delta) {
        tiempoDesdeAviso += delta;
        if (tiempoDesdeAviso < ConfiguracionRed.SEGUNDOS_ENTRE_AVISOS_REVANCHA) {
            return;
        }
        tiempoDesdeAviso = 0f;
        CLIENTE.enviar(Protocolo.armar(TipoMensaje.REVANCHA, pidoRevancha ? "1" : "0"));
    }

    @Override
    public void render(float delta) {
        tiempo += delta;
        avisarAlServidor(delta);
        tiempoSinRespuesta += delta;
        if (tiempoSinRespuesta * 1000f > ConfiguracionRed.MILISEGUNDOS_DESCONEXION) {
            clienteEntregado = true;
            CLIENTE.detener();
            JUEGO.setScreen(new PantallaConectar(JUEGO, "Se perdio la conexion con el servidor"));
            return;
        }

        Vector2 mouse = obtenerMouseMundo();
        boolean sobreRevancha = LIMITES_BOTON_REVANCHA.contains(mouse.x, mouse.y);
        boolean sobreSalir = LIMITES_BOTON_SALIR.contains(mouse.x, mouse.y);
        if (Gdx.input.justTouched()) {
            if (sobreRevancha) {
                pedirRevancha();
            } else if (sobreSalir) {
                salir();
                return;
            }
        }

        SpriteBatch lote = iniciarDibujo();
        RecursosGraficos recursos = JUEGO.obtenerRecursos();
        Texture pixel = recursos.obtenerTexturaPixel();

        lote.draw(TEXTURA_BANNER, CENTRO_X - ANCHO_BANNER / 2f, Y_BANNER, ANCHO_BANNER, ALTO_BANNER);
        dibujarCentrado(textoDeRevancha(), CENTRO_X, Y_BANNER - 40f);

        lote.draw(recursos.obtenerTexturaBotonVolverJugar(), LIMITES_BOTON_REVANCHA.x, LIMITES_BOTON_REVANCHA.y,
            LIMITES_BOTON_REVANCHA.width, LIMITES_BOTON_REVANCHA.height);
        lote.draw(recursos.obtenerTexturaBotonVolverMenu(), LIMITES_BOTON_SALIR.x, LIMITES_BOTON_SALIR.y,
            LIMITES_BOTON_SALIR.width, LIMITES_BOTON_SALIR.height);

        boolean destacarRevancha = pidoRevancha || (rivalPideRevancha && ((int) (tiempo * 3)) % 2 == 0);
        if (destacarRevancha) {
            resaltar(lote, pixel, LIMITES_BOTON_REVANCHA, COLOR_PEDIDO);
        } else if (sobreRevancha) {
            resaltar(lote, pixel, LIMITES_BOTON_REVANCHA, COLOR_RESALTADO);
        }
        if (sobreSalir) {
            resaltar(lote, pixel, LIMITES_BOTON_SALIR, COLOR_RESALTADO);
        }

        dibujarCentrado("ENTER: revancha", LIMITES_BOTON_REVANCHA.x + LIMITES_BOTON_REVANCHA.width / 2f, LIMITES_BOTON_REVANCHA.y - 12f);
        dibujarCentrado("ESC: salir", LIMITES_BOTON_SALIR.x + LIMITES_BOTON_SALIR.width / 2f, LIMITES_BOTON_SALIR.y - 12f);
        lote.end();
    }

    private String textoDeRevancha() {
        if (rivalSeFue) {
            return pidoRevancha ? "El rival se fue. Esperando a otro jugador..." : "El rival se fue de la sala";
        }
        if (pidoRevancha && rivalPideRevancha) {
            return "Revancha aceptada, empezando...";
        }
        if (pidoRevancha) {
            return "Pediste la revancha. Esperando al rival...";
        }
        if (rivalPideRevancha) {
            return "El rival quiere la revancha! VOLVER A JUGAR para aceptar";
        }
        return "Revancha? Toca VOLVER A JUGAR";
    }

    private void resaltar(SpriteBatch lote, Texture pixel, Rectangle limites, Color color) {
        lote.setColor(color);
        lote.draw(pixel, limites.x, limites.y, limites.width, limites.height);
        lote.setColor(Color.WHITE);
    }

    @Override
    public void dispose() {
        if (!clienteEntregado) {
            CLIENTE.detener();
        }
    }
}
