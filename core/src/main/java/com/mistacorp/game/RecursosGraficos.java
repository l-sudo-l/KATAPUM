package com.mistacorp.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RecursosGraficos {

    private final Texture texturaTanqueVerde;
    private final Texture texturaTanqueAzul;
    private final Texture texturaTanqueVerdeDestruido;
    private final Texture texturaTanqueAzulDestruido;

    private final Texture hojaExplosionVerde;
    private final Texture hojaExplosionAzul;
    private final Animation<TextureRegion> animacionExplosionVerde;
    private final Animation<TextureRegion> animacionExplosionAzul;

    private final Texture texturaFlashRojo;
    private final Texture texturaFlashAzul;

    private final Texture hojaProyectilRojo;
    private final Texture hojaProyectilAzul;
    private final Animation<TextureRegion> animacionProyectilRojo;
    private final Animation<TextureRegion> animacionProyectilAzul;

    private final Texture texturaImpactoRojo;
    private final Texture texturaImpactoAzul;

    private final Texture texturaPared;
    private final Texture texturaSuelo;
    private final Texture texturaArbusto;
    private final Texture texturaCaja;
    private final Texture texturaBarril;
    private final Texture texturaCorazon;

    public RecursosGraficos() {
        texturaTanqueVerde = cargar("tanque_verde.png");
        texturaTanqueAzul = cargar("tanque_azul.png");
        texturaTanqueVerdeDestruido = cargar("tanque_verde_muerto.png");
        texturaTanqueAzulDestruido = cargar("tanque_azul_muerto.png");

        hojaExplosionVerde = cargar("explocion_tanque_verde.png");
        hojaExplosionAzul = cargar("explocion_tanque_azul.png");
        animacionExplosionVerde = crearAnimacion(hojaExplosionVerde, 48, 40, 4, 0.15f);
        animacionExplosionAzul = crearAnimacion(hojaExplosionAzul, 48, 40, 4, 0.15f);

        texturaFlashRojo = cargar("bola_fuego_roja_1.png");
        texturaFlashAzul = cargar("bola_fuego_azul_1.png");

        hojaProyectilRojo = cargar("bola_fuego_roja_2.png");
        hojaProyectilAzul = cargar("bola_fuego_azul_2.png");
        animacionProyectilRojo = crearAnimacion(hojaProyectilRojo, 16, 16, 2, 0.08f);
        animacionProyectilAzul = crearAnimacion(hojaProyectilAzul, 16, 16, 2, 0.08f);

        texturaImpactoRojo = cargar("bola_fuego_roja_impacto.png");
        texturaImpactoAzul = cargar("bola_fuego_azul_impacto.png");

        texturaPared = cargar("pared.png");
        texturaSuelo = cargar("suelo.png");
        texturaArbusto = cargar("arbusto.png");
        texturaCaja = cargar("caja.png");
        texturaBarril = cargar("barril.png");
        texturaCorazon = cargar("corazon.png");
    }

    private Texture cargar(String nombreArchivo) {
        return new Texture(Gdx.files.internal(nombreArchivo));
    }

    private Animation<TextureRegion> crearAnimacion(Texture hoja, int anchoFrame, int altoFrame, int cantidadFrames, float duracionFrame) {
        TextureRegion[] frames = new TextureRegion[cantidadFrames];
        for (int i = 0; i < cantidadFrames; i++) {
            frames[i] = new TextureRegion(hoja, i * anchoFrame, 0, anchoFrame, altoFrame);
        }
        return new Animation<>(duracionFrame, frames);
    }

    public Texture obtenerTexturaTanqueVerde() {
        return texturaTanqueVerde;
    }

    public Texture obtenerTexturaTanqueAzul() {
        return texturaTanqueAzul;
    }

    public Texture obtenerTexturaTanqueVerdeDestruido() {
        return texturaTanqueVerdeDestruido;
    }

    public Texture obtenerTexturaTanqueAzulDestruido() {
        return texturaTanqueAzulDestruido;
    }

    public Animation<TextureRegion> obtenerAnimacionExplosionVerde() {
        return animacionExplosionVerde;
    }

    public Animation<TextureRegion> obtenerAnimacionExplosionAzul() {
        return animacionExplosionAzul;
    }

    public Texture obtenerTexturaFlashRojo() {
        return texturaFlashRojo;
    }

    public Texture obtenerTexturaFlashAzul() {
        return texturaFlashAzul;
    }

    public Animation<TextureRegion> obtenerAnimacionProyectilRojo() {
        return animacionProyectilRojo;
    }

    public Animation<TextureRegion> obtenerAnimacionProyectilAzul() {
        return animacionProyectilAzul;
    }

    public Texture obtenerTexturaImpactoRojo() {
        return texturaImpactoRojo;
    }

    public Texture obtenerTexturaImpactoAzul() {
        return texturaImpactoAzul;
    }

    public Texture obtenerTexturaPared() {
        return texturaPared;
    }

    public Texture obtenerTexturaSuelo() {
        return texturaSuelo;
    }

    public Texture obtenerTexturaArbusto() {
        return texturaArbusto;
    }

    public Texture obtenerTexturaCaja() {
        return texturaCaja;
    }

    public Texture obtenerTexturaBarril() {
        return texturaBarril;
    }

    public Texture obtenerTexturaCorazon() {
        return texturaCorazon;
    }

    public void dispose() {
        texturaTanqueVerde.dispose();
        texturaTanqueAzul.dispose();
        texturaTanqueVerdeDestruido.dispose();
        texturaTanqueAzulDestruido.dispose();
        hojaExplosionVerde.dispose();
        hojaExplosionAzul.dispose();
        texturaFlashRojo.dispose();
        texturaFlashAzul.dispose();
        hojaProyectilRojo.dispose();
        hojaProyectilAzul.dispose();
        texturaImpactoRojo.dispose();
        texturaImpactoAzul.dispose();
        texturaPared.dispose();
        texturaSuelo.dispose();
        texturaArbusto.dispose();
        texturaCaja.dispose();
        texturaBarril.dispose();
        texturaCorazon.dispose();
    }
}
