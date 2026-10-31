package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RecursosGraficos {

    private final Texture TEXTURA_PIXEL;

    private final Texture TEXTURA_TANQUE_VERDE;
    private final Texture TEXTURA_TANQUE_AZUL;
    private final Texture TEXTURA_TANQUE_VERDE_DESTRUIDO;
    private final Texture TEXTURA_TANQUE_AZUL_DESTRUIDO;

    private final Texture HOJA_EXPLOSION_VERDE;
    private final Texture HOJA_EXPLOSION_AZUL;
    private final Animation<TextureRegion> ANIMACION_EXPLOSION_VERDE;
    private final Animation<TextureRegion> ANIMACION_EXPLOSION_AZUL;

    private final Texture TEXTURA_FLASH_ROJO;
    private final Texture TEXTURA_FLASH_AZUL;

    private final Texture HOJA_PROYECTIL_ROJO;
    private final Texture HOJA_PROYECTIL_AZUL;
    private final Animation<TextureRegion> ANIMACION_PROYECTIL_ROJO;
    private final Animation<TextureRegion> ANIMACION_PROYECTIL_AZUL;

    private final Texture TEXTURA_IMPACTO_ROJO;
    private final Texture TEXTURA_IMPACTO_AZUL;

    private final Texture TEXTURA_SUELO;
    private final Texture TEXTURA_ARBUSTO;
    private final TextureRegion REGION_SUELO;
    private final TextureRegion REGION_ARBUSTO;

    private final Texture HOJA_DESTRUCCION_PARED;
    private final Texture HOJA_DESTRUCCION_CAJA;
    private final Texture HOJA_DESTRUCCION_BARRIL;
    private final TextureRegion[] FRAMES_DESTRUCCION_PARED;
    private final TextureRegion[] FRAMES_DESTRUCCION_CAJA;
    private final TextureRegion[] FRAMES_DESTRUCCION_BARRIL;

    private final Texture TEXTURA_PARED_DESTRUIDA;
    private final Texture TEXTURA_CAJA_DESTRUIDA;
    private final Texture TEXTURA_BARRIL_DESTRUIDO;
    private final TextureRegion REGION_PARED_DESTRUIDA;
    private final TextureRegion REGION_CAJA_DESTRUIDA;
    private final TextureRegion REGION_BARRIL_DESTRUIDO;
    private final Texture TEXTURA_CORAZON;

    private final Texture TEXTURA_MINIATURA_BOSQUE;
    private final Texture TEXTURA_MARCO_MAPA;
    private final Texture TEXTURA_MARCO_MAPA_SELECCIONADO;
    private final Texture TEXTURA_SLOT_VACIO;
    private final Texture TEXTURA_ICONO_HOST;
    private final Texture TEXTURA_ICONO_LISTO;
    private final Texture TEXTURA_ICONO_NO_LISTO;
    private final Texture TEXTURA_ICONO_ACTUALIZAR;
    private final Texture TEXTURA_ICONO_SALA_LLENA;
    private final Texture TEXTURA_FLECHA_IZQUIERDA;
    private final Texture TEXTURA_FLECHA_DERECHA;
    private final Texture HOJA_ICONO_SENAL;
    private final TextureRegion[] FRAMES_ICONO_SENAL;
    private final Texture HOJA_ESPERANDO;
    private final Texture HOJA_CONECTANDO;
    private final Animation<TextureRegion> ANIMACION_ESPERANDO;
    private final Animation<TextureRegion> ANIMACION_CONECTANDO;

    private final Texture TEXTURA_GANADOR_JUGADOR1;
    private final Texture TEXTURA_GANADOR_JUGADOR2;
    private final Texture TEXTURA_BOTON_VOLVER_JUGAR;
    private final Texture TEXTURA_BOTON_VOLVER_MENU;

    public RecursosGraficos() {
        TEXTURA_PIXEL = crearTexturaPixel();

        TEXTURA_TANQUE_VERDE = cargar("tanque_verde.png");
        TEXTURA_TANQUE_AZUL = cargar("tanque_azul.png");
        TEXTURA_TANQUE_VERDE_DESTRUIDO = cargar("tanque_verde_muerto.png");
        TEXTURA_TANQUE_AZUL_DESTRUIDO = cargar("tanque_azul_muerto.png");

        HOJA_EXPLOSION_VERDE = cargar("explocion_tanque_verde.png");
        HOJA_EXPLOSION_AZUL = cargar("explocion_tanque_azul.png");
        ANIMACION_EXPLOSION_VERDE = crearAnimacion(HOJA_EXPLOSION_VERDE, 48, 40, 4, 0.15f);
        ANIMACION_EXPLOSION_AZUL = crearAnimacion(HOJA_EXPLOSION_AZUL, 48, 40, 4, 0.15f);

        TEXTURA_FLASH_ROJO = cargar("bola_fuego_roja_1.png");
        TEXTURA_FLASH_AZUL = cargar("bola_fuego_azul_1.png");

        HOJA_PROYECTIL_ROJO = cargar("bola_fuego_roja_2.png");
        HOJA_PROYECTIL_AZUL = cargar("bola_fuego_azul_2.png");
        ANIMACION_PROYECTIL_ROJO = crearAnimacion(HOJA_PROYECTIL_ROJO, 16, 16, 2, 0.08f);
        ANIMACION_PROYECTIL_AZUL = crearAnimacion(HOJA_PROYECTIL_AZUL, 16, 16, 2, 0.08f);

        TEXTURA_IMPACTO_ROJO = cargar("bola_fuego_roja_impacto.png");
        TEXTURA_IMPACTO_AZUL = cargar("bola_fuego_azul_impacto.png");

        TEXTURA_SUELO = cargar("suelo.png");
        TEXTURA_ARBUSTO = cargar("arbusto.png");
        REGION_SUELO = new TextureRegion(TEXTURA_SUELO);
        REGION_ARBUSTO = new TextureRegion(TEXTURA_ARBUSTO);

        HOJA_DESTRUCCION_PARED = cargar("pared_destruccion.png");
        HOJA_DESTRUCCION_CAJA = cargar("caja_destruccion.png");
        HOJA_DESTRUCCION_BARRIL = cargar("barril_destruccion.png");
        FRAMES_DESTRUCCION_PARED = crearFrames(HOJA_DESTRUCCION_PARED, 32, 32, 6);
        FRAMES_DESTRUCCION_CAJA = crearFrames(HOJA_DESTRUCCION_CAJA, 32, 32, 3);
        FRAMES_DESTRUCCION_BARRIL = crearFrames(HOJA_DESTRUCCION_BARRIL, 32, 32, 3);

        TEXTURA_PARED_DESTRUIDA = cargar("pared_destruida.png");
        TEXTURA_CAJA_DESTRUIDA = cargar("caja_destruida.png");
        TEXTURA_BARRIL_DESTRUIDO = cargar("barril_destruido.png");
        REGION_PARED_DESTRUIDA = new TextureRegion(TEXTURA_PARED_DESTRUIDA);
        REGION_CAJA_DESTRUIDA = new TextureRegion(TEXTURA_CAJA_DESTRUIDA);
        REGION_BARRIL_DESTRUIDO = new TextureRegion(TEXTURA_BARRIL_DESTRUIDO);
        TEXTURA_CORAZON = cargar("corazon.png");

        TEXTURA_MINIATURA_BOSQUE = cargar("mapa_bosque_miniatura.png");
        TEXTURA_MARCO_MAPA = cargar("marco_mapa.png");
        TEXTURA_MARCO_MAPA_SELECCIONADO = cargar("marco_mapa_seleccionado.png");
        TEXTURA_SLOT_VACIO = cargar("slot_vacio.png");
        TEXTURA_ICONO_HOST = cargar("icono_host.png");
        TEXTURA_ICONO_LISTO = cargar("icono_listo.png");
        TEXTURA_ICONO_NO_LISTO = cargar("icono_no_listo.png");
        TEXTURA_ICONO_ACTUALIZAR = cargar("icono_actualizar.png");
        TEXTURA_ICONO_SALA_LLENA = cargar("icono_sala_llena.png");
        TEXTURA_FLECHA_IZQUIERDA = cargar("flecha_izq.png");
        TEXTURA_FLECHA_DERECHA = cargar("flecha_der.png");
        HOJA_ICONO_SENAL = cargar("icono_senal.png");
        FRAMES_ICONO_SENAL = crearFrames(HOJA_ICONO_SENAL, 16, 16, 3);
        HOJA_ESPERANDO = cargar("esperando.png");
        HOJA_CONECTANDO = cargar("conectando.png");
        ANIMACION_ESPERANDO = crearAnimacion(HOJA_ESPERANDO, 16, 16, 4, 0.3f);
        ANIMACION_CONECTANDO = crearAnimacion(HOJA_CONECTANDO, 16, 16, 4, 0.2f);

        TEXTURA_GANADOR_JUGADOR1 = cargar("ganador_jugador1.png");
        TEXTURA_GANADOR_JUGADOR2 = cargar("ganador_jugador2.png");
        TEXTURA_BOTON_VOLVER_JUGAR = cargar("volver_jugar.png");
        TEXTURA_BOTON_VOLVER_MENU = cargar("volver_menu.png");
    }

    private Texture cargar(String nombreArchivo) {
        return new Texture(Gdx.files.internal(nombreArchivo));
    }

    private Texture crearTexturaPixel() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return textura;
    }

    public Texture obtenerTexturaPixel() {
        return TEXTURA_PIXEL;
    }

    private Animation<TextureRegion> crearAnimacion(Texture hoja, int anchoFrame, int altoFrame, int cantidadFrames, float duracionFrame) {
        return new Animation<>(duracionFrame, crearFrames(hoja, anchoFrame, altoFrame, cantidadFrames));
    }

    private TextureRegion[] crearFrames(Texture hoja, int anchoFrame, int altoFrame, int cantidadFrames) {
        TextureRegion[] frames = new TextureRegion[cantidadFrames];
        for (int i = 0; i < cantidadFrames; i++) {
            frames[i] = new TextureRegion(hoja, i * anchoFrame, 0, anchoFrame, altoFrame);
        }
        return frames;
    }

    public Texture obtenerTexturaTanqueVerde() {
        return TEXTURA_TANQUE_VERDE;
    }

    public Texture obtenerTexturaTanqueAzul() {
        return TEXTURA_TANQUE_AZUL;
    }

    public Texture obtenerTexturaTanqueVerdeDestruido() {
        return TEXTURA_TANQUE_VERDE_DESTRUIDO;
    }

    public Texture obtenerTexturaTanqueAzulDestruido() {
        return TEXTURA_TANQUE_AZUL_DESTRUIDO;
    }

    public Animation<TextureRegion> obtenerAnimacionExplosionVerde() {
        return ANIMACION_EXPLOSION_VERDE;
    }

    public Animation<TextureRegion> obtenerAnimacionExplosionAzul() {
        return ANIMACION_EXPLOSION_AZUL;
    }

    public Texture obtenerTexturaFlashRojo() {
        return TEXTURA_FLASH_ROJO;
    }

    public Texture obtenerTexturaFlashAzul() {
        return TEXTURA_FLASH_AZUL;
    }

    public Animation<TextureRegion> obtenerAnimacionProyectilRojo() {
        return ANIMACION_PROYECTIL_ROJO;
    }

    public Animation<TextureRegion> obtenerAnimacionProyectilAzul() {
        return ANIMACION_PROYECTIL_AZUL;
    }

    public Texture obtenerTexturaImpactoRojo() {
        return TEXTURA_IMPACTO_ROJO;
    }

    public Texture obtenerTexturaImpactoAzul() {
        return TEXTURA_IMPACTO_AZUL;
    }

    public TextureRegion obtenerRegionSuelo() {
        return REGION_SUELO;
    }

    public TextureRegion obtenerRegionArbusto() {
        return REGION_ARBUSTO;
    }

    public TextureRegion[] obtenerFramesDestruccionPared() {
        return FRAMES_DESTRUCCION_PARED;
    }

    public TextureRegion[] obtenerFramesDestruccionCaja() {
        return FRAMES_DESTRUCCION_CAJA;
    }

    public TextureRegion[] obtenerFramesDestruccionBarril() {
        return FRAMES_DESTRUCCION_BARRIL;
    }

    public TextureRegion obtenerRegionParedDestruida() {
        return REGION_PARED_DESTRUIDA;
    }

    public TextureRegion obtenerRegionCajaDestruida() {
        return REGION_CAJA_DESTRUIDA;
    }

    public TextureRegion obtenerRegionBarrilDestruido() {
        return REGION_BARRIL_DESTRUIDO;
    }

    public Texture obtenerTexturaCorazon() {
        return TEXTURA_CORAZON;
    }

    public Texture obtenerTexturaMiniaturaBosque() {
        return TEXTURA_MINIATURA_BOSQUE;
    }

    public Texture obtenerTexturaMarcoMapa() {
        return TEXTURA_MARCO_MAPA;
    }

    public Texture obtenerTexturaMarcoMapaSeleccionado() {
        return TEXTURA_MARCO_MAPA_SELECCIONADO;
    }

    public Texture obtenerTexturaSlotVacio() {
        return TEXTURA_SLOT_VACIO;
    }

    public Texture obtenerTexturaIconoHost() {
        return TEXTURA_ICONO_HOST;
    }

    public Texture obtenerTexturaIconoListo() {
        return TEXTURA_ICONO_LISTO;
    }

    public Texture obtenerTexturaIconoNoListo() {
        return TEXTURA_ICONO_NO_LISTO;
    }

    public Texture obtenerTexturaIconoActualizar() {
        return TEXTURA_ICONO_ACTUALIZAR;
    }

    public Texture obtenerTexturaIconoSalaLlena() {
        return TEXTURA_ICONO_SALA_LLENA;
    }

    public Texture obtenerTexturaFlechaIzquierda() {
        return TEXTURA_FLECHA_IZQUIERDA;
    }

    public Texture obtenerTexturaFlechaDerecha() {
        return TEXTURA_FLECHA_DERECHA;
    }

    public TextureRegion[] obtenerFramesIconoSenal() {
        return FRAMES_ICONO_SENAL;
    }

    public Animation<TextureRegion> obtenerAnimacionEsperando() {
        return ANIMACION_ESPERANDO;
    }

    public Animation<TextureRegion> obtenerAnimacionConectando() {
        return ANIMACION_CONECTANDO;
    }

    public Texture obtenerTexturaGanadorJugador1() {
        return TEXTURA_GANADOR_JUGADOR1;
    }

    public Texture obtenerTexturaGanadorJugador2() {
        return TEXTURA_GANADOR_JUGADOR2;
    }

    public Texture obtenerTexturaBotonVolverJugar() {
        return TEXTURA_BOTON_VOLVER_JUGAR;
    }

    public Texture obtenerTexturaBotonVolverMenu() {
        return TEXTURA_BOTON_VOLVER_MENU;
    }

    public void dispose() {
        TEXTURA_PIXEL.dispose();
        TEXTURA_TANQUE_VERDE.dispose();
        TEXTURA_TANQUE_AZUL.dispose();
        TEXTURA_TANQUE_VERDE_DESTRUIDO.dispose();
        TEXTURA_TANQUE_AZUL_DESTRUIDO.dispose();
        HOJA_EXPLOSION_VERDE.dispose();
        HOJA_EXPLOSION_AZUL.dispose();
        TEXTURA_FLASH_ROJO.dispose();
        TEXTURA_FLASH_AZUL.dispose();
        HOJA_PROYECTIL_ROJO.dispose();
        HOJA_PROYECTIL_AZUL.dispose();
        TEXTURA_IMPACTO_ROJO.dispose();
        TEXTURA_IMPACTO_AZUL.dispose();
        TEXTURA_SUELO.dispose();
        TEXTURA_ARBUSTO.dispose();
        HOJA_DESTRUCCION_PARED.dispose();
        HOJA_DESTRUCCION_CAJA.dispose();
        HOJA_DESTRUCCION_BARRIL.dispose();
        TEXTURA_PARED_DESTRUIDA.dispose();
        TEXTURA_CAJA_DESTRUIDA.dispose();
        TEXTURA_BARRIL_DESTRUIDO.dispose();
        TEXTURA_CORAZON.dispose();
        TEXTURA_MINIATURA_BOSQUE.dispose();
        TEXTURA_MARCO_MAPA.dispose();
        TEXTURA_MARCO_MAPA_SELECCIONADO.dispose();
        TEXTURA_SLOT_VACIO.dispose();
        TEXTURA_ICONO_HOST.dispose();
        TEXTURA_ICONO_LISTO.dispose();
        TEXTURA_ICONO_NO_LISTO.dispose();
        TEXTURA_ICONO_ACTUALIZAR.dispose();
        TEXTURA_ICONO_SALA_LLENA.dispose();
        TEXTURA_FLECHA_IZQUIERDA.dispose();
        TEXTURA_FLECHA_DERECHA.dispose();
        HOJA_ICONO_SENAL.dispose();
        HOJA_ESPERANDO.dispose();
        HOJA_CONECTANDO.dispose();
        TEXTURA_GANADOR_JUGADOR1.dispose();
        TEXTURA_GANADOR_JUGADOR2.dispose();
        TEXTURA_BOTON_VOLVER_JUGAR.dispose();
        TEXTURA_BOTON_VOLVER_MENU.dispose();
    }
}
