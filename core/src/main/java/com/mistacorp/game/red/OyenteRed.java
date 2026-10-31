package com.mistacorp.game.red;

import com.mistacorp.game.compartido.red.TipoMensaje;

public interface OyenteRed {

    void mensajeRecibido(TipoMensaje tipo, String[] partes);
}
