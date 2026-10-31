package com.mistacorp.game.compartido.mundo;

import com.mistacorp.game.compartido.TipoMapa;

public final class DisenoMapa {

    public static final char PARED = '#';
    public static final char VEGETACION = 'v';
    public static final char CAJA = 'c';
    public static final char BARRIL = 'b';

    private static final String[] BOSQUE = {
        "##############################",
        "#.....v................v.....#",
        "#..b.....###......###.....b..#",
        "#........#..v....v..#........#",
        "#..##....#....vv....#....##..#",
        "#..#..........vv..........#..#",
        "#.v....c..............c....v.#",
        "#..........##....##..........#",
        "#...v......#......#......v...#",
        "#..........##....##..........#",
        "#.v....b..............b....v.#",
        "#..#..........vv..........#..#",
        "#..##....#....vv....#....##..#",
        "#........#..v....v..#........#",
        "#..c.....###......###.....c..#",
        "#.....v................v.....#",
        "##############################"
    };

    private DisenoMapa() {
    }

    public static String[] obtenerGrilla(TipoMapa mapa) {
        return BOSQUE;
    }
}
