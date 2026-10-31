package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mistacorp.game.compartido.TipoMapa;
import com.mistacorp.game.mundo.SpritesMapa;

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



    private final Texture TEXTURA_CORAZON;
    private final SpritesMapa SPRITES_BOSQUE;
    private final SpritesMapa SPRITES_DESIERTO;
    private final SpritesMapa SPRITES_CIUDAD;

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

        TEXTURA_TANQUE_VERDE = cargar("imagenes/tanques/tanque_verde.png");
        TEXTURA_TANQUE_AZUL = cargar("imagenes/tanques/tanque_azul.png");
        TEXTURA_TANQUE_VERDE_DESTRUIDO = cargar("imagenes/tanques/tanque_verde_muerto.png");
        TEXTURA_TANQUE_AZUL_DESTRUIDO = cargar("imagenes/tanques/tanque_azul_muerto.png");

        HOJA_EXPLOSION_VERDE = cargar("imagenes/tanques/explocion_tanque_verde.png");
        HOJA_EXPLOSION_AZUL = cargar("imagenes/tanques/explocion_tanque_azul.png");
        ANIMACION_EXPLOSION_VERDE = crearAnimacion(HOJA_EXPLOSION_VERDE, 48, 40, 4, 0.15f);
        ANIMACION_EXPLOSION_AZUL = crearAnimacion(HOJA_EXPLOSION_AZUL, 48, 40, 4, 0.15f);

        TEXTURA_FLASH_ROJO = cargar("imagenes/proyectiles/bola_fuego_roja_1.png");
        TEXTURA_FLASH_AZUL = cargar("imagenes/proyectiles/bola_fuego_azul_1.png");

        HOJA_PROYECTIL_ROJO = cargar("imagenes/proyectiles/bola_fuego_roja_2.png");
        HOJA_PROYECTIL_AZUL = cargar("imagenes/proyectiles/bola_fuego_azul_2.png");
        ANIMACION_PROYECTIL_ROJO = crearAnimacion(HOJA_PROYECTIL_ROJO, 16, 16, 2, 0.08f);
        ANIMACION_PROYECTIL_AZUL = crearAnimacion(HOJA_PROYECTIL_AZUL, 16, 16, 2, 0.08f);

        TEXTURA_IMPACTO_ROJO = cargar("imagenes/proyectiles/bola_fuego_roja_impacto.png");
        TEXTURA_IMPACTO_AZUL = cargar("imagenes/proyectiles/bola_fuego_azul_impacto.png");



        SPRITES_BOSQUE = new SpritesMapa("imagenes/mapas/bosque", "suelo", "pared", "pared_destruida",
            "caja", "caja_destruida", "barril", "barril_destruido", "arbusto", "arbusto_destruido",
            "mapa_bosque_miniatura", "marco_mapa");
        SPRITES_DESIERTO = new SpritesMapa("imagenes/mapas/desierto", "suelo_arena", "pared_arena", "pared_arena_destruida",
            "caja_roble", "caja_roble_destruida", "barril_roble", "barril_roble_destruido", "palmera_desierto",
            "palmera_desierto_destruida", "mapa_desierto_miniatura", "marco_mapa_desierto");
        SPRITES_CIUDAD = new SpritesMapa("imagenes/mapas/ciudad", "suelo_asfalto", "pared_ladrillo", "pared_ladrillo_destruida",
            "caja_vieja", "caja_vieja_destruida", "barril_oxidado", "barril_oxidado_destruido", "arbusto_seco",
            "arbusto_seco_destruido", "mapa_ciudad_miniatura", "marco_mapa_ciudad");
        TEXTURA_CORAZON = cargar("imagenes/interfaz/corazon.png");

        TEXTURA_SLOT_VACIO = cargar("imagenes/interfaz/slot_vacio.png");
        TEXTURA_ICONO_HOST = cargar("imagenes/interfaz/icono_host.png");
        TEXTURA_ICONO_LISTO = cargar("imagenes/interfaz/icono_listo.png");
        TEXTURA_ICONO_NO_LISTO = cargar("imagenes/interfaz/icono_no_listo.png");
        TEXTURA_ICONO_ACTUALIZAR = cargar("imagenes/interfaz/icono_actualizar.png");
        TEXTURA_ICONO_SALA_LLENA = cargar("imagenes/interfaz/icono_sala_llena.png");
        TEXTURA_FLECHA_IZQUIERDA = cargar("imagenes/interfaz/flecha_izq.png");
        TEXTURA_FLECHA_DERECHA = cargar("imagenes/interfaz/flecha_der.png");
        HOJA_ICONO_SENAL = cargar("imagenes/interfaz/icono_senal.png");
        FRAMES_ICONO_SENAL = crearFrames(HOJA_ICONO_SENAL, 16, 16, 3);
        HOJA_ESPERANDO = cargar("imagenes/interfaz/esperando.png");
        HOJA_CONECTANDO = cargar("imagenes/interfaz/conectando.png");
        ANIMACION_ESPERANDO = crearAnimacion(HOJA_ESPERANDO, 16, 16, 4, 0.3f);
        ANIMACION_CONECTANDO = crearAnimacion(HOJA_CONECTANDO, 16, 16, 4, 0.2f);

        TEXTURA_GANADOR_JUGADOR1 = cargar("imagenes/interfaz/ganador_jugador1.png");
        TEXTURA_GANADOR_JUGADOR2 = cargar("imagenes/interfaz/ganador_jugador2.png");
        TEXTURA_BOTON_VOLVER_JUGAR = cargar("imagenes/interfaz/volver_jugar.png");
        TEXTURA_BOTON_VOLVER_MENU = cargar("imagenes/interfaz/volver_menu.png");
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

    public SpritesMapa obtenerSpritesMapa(TipoMapa mapa) {
        switch (mapa) {
            case DESIERTO:
                return SPRITES_DESIERTO;
            case CIUDAD:
                return SPRITES_CIUDAD;
            default:
                return SPRITES_BOSQUE;
        }
    }

    public Texture obtenerTexturaCorazon() {
        return TEXTURA_CORAZON;
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
        TEXTURA_CORAZON.dispose();
        SPRITES_BOSQUE.dispose();
        SPRITES_DESIERTO.dispose();
        SPRITES_CIUDAD.dispose();
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
