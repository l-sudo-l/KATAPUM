package com.mistacorp.game.mundo;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mistacorp.game.compartido.mundo.Decoracion;
import com.mistacorp.game.compartido.mundo.Obstaculo;
import com.mistacorp.game.compartido.mundo.Pared;

public class SpritesMapa {

    private static final int TAMANO_TILE = 32;
    private static final int CUADROS_PARED = 6;
    private static final int CUADROS_DECORACION = 3;

    private final Texture TEXTURA_SUELO;
    private final Texture HOJA_PARED;
    private final Texture TEXTURA_PARED_DESTRUIDA;
    private final Texture HOJA_CAJA;
    private final Texture TEXTURA_CAJA_DESTRUIDA;
    private final Texture HOJA_BARRIL;
    private final Texture TEXTURA_BARRIL_DESTRUIDO;
    private final Texture HOJA_VEGETACION;
    private final Texture TEXTURA_VEGETACION_DESTRUIDA;
    private final Texture TEXTURA_MINIATURA;
    private final Texture TEXTURA_MARCO;
    private final Texture TEXTURA_MARCO_SELECCIONADO;

    private final TextureRegion REGION_SUELO;
    private final TextureRegion[] FRAMES_PARED;
    private final TextureRegion[] FRAMES_CAJA;
    private final TextureRegion[] FRAMES_BARRIL;
    private final TextureRegion[] FRAMES_VEGETACION;
    private final TextureRegion REGION_PARED_DESTRUIDA;
    private final TextureRegion REGION_CAJA_DESTRUIDA;
    private final TextureRegion REGION_BARRIL_DESTRUIDO;
    private final TextureRegion REGION_VEGETACION_DESTRUIDA;

    public SpritesMapa(String carpeta, String suelo, String pared, String paredDestruida, String caja, String cajaDestruida,
                       String barril, String barrilDestruido, String vegetacion, String vegetacionDestruida,
                       String miniatura, String marco) {
        TEXTURA_SUELO = cargar(carpeta, suelo);
        HOJA_PARED = cargar(carpeta, pared + "_destruccion");
        TEXTURA_PARED_DESTRUIDA = cargar(carpeta, paredDestruida);
        HOJA_CAJA = cargar(carpeta, caja + "_destruccion");
        TEXTURA_CAJA_DESTRUIDA = cargar(carpeta, cajaDestruida);
        HOJA_BARRIL = cargar(carpeta, barril + "_destruccion");
        TEXTURA_BARRIL_DESTRUIDO = cargar(carpeta, barrilDestruido);
        HOJA_VEGETACION = cargar(carpeta, vegetacion + "_destruccion");
        TEXTURA_VEGETACION_DESTRUIDA = cargar(carpeta, vegetacionDestruida);
        TEXTURA_MINIATURA = cargar(carpeta, miniatura);
        TEXTURA_MARCO = cargar(carpeta, marco);
        TEXTURA_MARCO_SELECCIONADO = cargar(carpeta, marco + "_seleccionado");

        REGION_SUELO = new TextureRegion(TEXTURA_SUELO);
        FRAMES_PARED = crearFrames(HOJA_PARED, CUADROS_PARED);
        FRAMES_CAJA = crearFrames(HOJA_CAJA, CUADROS_DECORACION);
        FRAMES_BARRIL = crearFrames(HOJA_BARRIL, CUADROS_DECORACION);
        FRAMES_VEGETACION = crearFrames(HOJA_VEGETACION, CUADROS_DECORACION);
        REGION_PARED_DESTRUIDA = new TextureRegion(TEXTURA_PARED_DESTRUIDA);
        REGION_CAJA_DESTRUIDA = new TextureRegion(TEXTURA_CAJA_DESTRUIDA);
        REGION_BARRIL_DESTRUIDO = new TextureRegion(TEXTURA_BARRIL_DESTRUIDO);
        REGION_VEGETACION_DESTRUIDA = new TextureRegion(TEXTURA_VEGETACION_DESTRUIDA);
    }

    private Texture cargar(String carpeta, String nombre) {
        return new Texture(Gdx.files.internal(carpeta + "/" + nombre + ".png"));
    }

    private TextureRegion[] crearFrames(Texture hoja, int cantidad) {
        TextureRegion[] frames = new TextureRegion[cantidad];
        for (int i = 0; i < cantidad; i++) {
            frames[i] = new TextureRegion(hoja, i * TAMANO_TILE, 0, TAMANO_TILE, TAMANO_TILE);
        }
        return frames;
    }

    public TextureRegion obtenerRegionSuelo() {
        return REGION_SUELO;
    }

    public TextureRegion obtenerRegionPared(Pared pared) {
        return elegirRegion(pared, FRAMES_PARED, REGION_PARED_DESTRUIDA);
    }

    public TextureRegion obtenerRegionDecoracion(Decoracion decoracion) {
        switch (decoracion.obtenerTipo()) {
            case CAJA:
                return elegirRegion(decoracion, FRAMES_CAJA, REGION_CAJA_DESTRUIDA);
            case BARRIL:
                return elegirRegion(decoracion, FRAMES_BARRIL, REGION_BARRIL_DESTRUIDO);
            default:
                return elegirRegion(decoracion, FRAMES_VEGETACION, REGION_VEGETACION_DESTRUIDA);
        }
    }

    private TextureRegion elegirRegion(Obstaculo obstaculo, TextureRegion[] frames, TextureRegion destruida) {
        if (obstaculo.estaDestruido()) {
            return destruida;
        }
        return frames[obstaculo.obtenerImpactosRecibidos()];
    }

    public Texture obtenerTexturaMiniatura() {
        return TEXTURA_MINIATURA;
    }

    public Texture obtenerTexturaMarco() {
        return TEXTURA_MARCO;
    }

    public Texture obtenerTexturaMarcoSeleccionado() {
        return TEXTURA_MARCO_SELECCIONADO;
    }

    public void dispose() {
        TEXTURA_SUELO.dispose();
        HOJA_PARED.dispose();
        TEXTURA_PARED_DESTRUIDA.dispose();
        HOJA_CAJA.dispose();
        TEXTURA_CAJA_DESTRUIDA.dispose();
        HOJA_BARRIL.dispose();
        TEXTURA_BARRIL_DESTRUIDO.dispose();
        HOJA_VEGETACION.dispose();
        TEXTURA_VEGETACION_DESTRUIDA.dispose();
        TEXTURA_MINIATURA.dispose();
        TEXTURA_MARCO.dispose();
        TEXTURA_MARCO_SELECCIONADO.dispose();
    }
}
