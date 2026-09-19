# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [No publicado]

## [0.4.0] - 2026-09-18

### Cambiado
- Mecánica de movimiento de los tanques reemplazada por completo: de desplazamiento por ejes fijos (4 direcciones discretas) a rotación libre estilo vehículo real. `A`/`D` giran el tanque sobre su propio eje sin desplazarlo; `W`/`S` avanzan o retroceden según el ángulo actual, calculado con trigonometría (`MathUtils.cosDeg`/`sinDeg`) en vez de vectores fijos por eje.
- `Tanque`: se elimina el enum `Direccion` (4 valores fijos) y se reemplaza por un campo `angulo` continuo (con wrap correcto al cruzar 360°/0°). El sprite rota visualmente sobre su propio centro, y el punto de disparo (`obtenerPosicionCanon()`) queda siempre sincronizado con esa rotación.
- `Proyectil`: ahora recibe un ángulo (float) en vez de `Tanque.Direccion`; su velocidad y su rotación visual salen del ángulo con el que fue disparado.
- `ManejadorEntradaJugador`: `obtenerMovimientoX()`/`obtenerMovimientoY()` renombrados a `obtenerGiro()`/`obtenerAvance()`.

### Nota de diseño
- La colisión sigue usando el rectángulo (AABB) de 40×40 del tanque sin rotar, aunque el sprite ya gire libremente. Es una simplificación deliberada para no tener que implementar colisión de rectángulos rotados (OBB) en esta etapa del prototipo.

## [0.3.0] - 2026-09-16

### Agregado
- `RecursosGraficos`: carga todos los sprites y arma las animaciones (`Animation<TextureRegion>`) a partir de los spritesheets de explosión (4 frames) y de vuelo del proyectil (2 frames).
- `RecursosSonido`: carga los efectos (`Sound`: disparo, impacto, muerte, victoria) y la música en loop (`Music`: menú y partida).
- `ControlVolumen`: estado compartido de volumen (0-1) y silenciado, con controles por teclado (`+`, `-`, `M`) disponibles tanto en el menú como durante la partida; el volumen actual se muestra en el HUD.
- `Efecto`: imagen breve para el fogonazo de disparo y el destello de impacto.
- `Tanque`: ahora dibuja el sprite real rotado según su dirección (cada instancia tiene su propio ángulo base de sprite) y reproduce una animación de 4 cuadros (normal → cargando → estallido → carcasa quemada) al perder su última vida; recién al terminar esa animación se pasa a la pantalla de fin de partida.
- `Proyectil`: vuela con una animación de 2 cuadros, rotada según su dirección de disparo.
- `Arena`: pinta suelo y paredes con texturas reales tileadas en 32×32 (sin estirar), y agrega elementos decorativos no colisionables (arbustos, cajas, barriles).
- `Hud`: reemplaza el texto `<3` por el ícono real de corazón.
- `PantallaFinPartida`: banner fijo según el ganador (`ganador_jugador1.png` / `ganador_jugador2.png`) y botones visuales `volver_jugar.png` / `volver_menu.png`, etiquetados con sus teclas (`ENTER` / `ESC`).
- `CREDITOS.md`: atribución de autor, licencia y fuente de cada pista de audio (OpenGameArt.org).

### Quitado
- El placeholder de textura de 1×1 (`pixel`) usado para dibujar todo con color sólido: ya no hace falta, todo el dibujado usa los sprites reales.

### Cambiado
- Todo el código fuente (clases, variables, métodos y subpaquetes propios) traducido de inglés a español, manteniendo el paquete raíz `com.mistacorp.game` tal como lo generó el asistente de configuración de LibGDX (gdx-liftoff) al crear el proyecto. Se mantienen en inglés únicamente los métodos heredados de la API de LibGDX (`create`, `render`, `dispose`, `keyDown`, etc.), que no se pueden renombrar sin romper el contrato de las clases base.
- Actualizadas las referencias `application.mainClass` en `lwjgl3/build.gradle` y `server/build.gradle` para que apunten a las clases con su nuevo nombre.

## [0.2.0] - 2026-09-09

### Agregado
- Arquitectura `Game`/`Screen`: `KatapumMain` pasó de `ApplicationAdapter` a `Game`, administrando los recursos compartidos (`SpriteBatch`, `BitmapFont`, textura placeholder).
- `MenuScreen`: pantalla inicial con título, resumen de controles e inicio de partida.
- `GameScreen`: bucle principal del prototipo jugable (mecánica central de combate de tanques).
- `GameOverScreen`: pantalla de fin de partida con el nombre del ganador.
- `PlayerInputHandler`: clase dedicada al manejo de teclado (basada en `InputAdapter`), una instancia por jugador, permitiendo modo local con dos esquemas de teclas simultáneos.
- `Tank`: entidad jugador con movimiento, colisión eje por eje, vidas y dirección de disparo.
- `Projectile`: entidad de proyectil con movimiento rectilíneo y detección de colisión.
- `Arena` y `Wall`: entorno de juego con obstáculos fijos e indestructibles.
- `Hud`: interfaz fija en pantalla con las vidas de cada jugador y el estado de pausa, usando un `Viewport` independiente del mundo de juego.
- `GameConfig`: constantes centralizadas de tamaños, velocidades y colores.
- Cámara y `FitViewport` para adaptar el mundo de juego a distintas resoluciones de ventana.
- Estado de pausa (tecla ESC) dentro de la pantalla de juego.

### Cambiado
- `README.md` actualizado: estado del prototipo, lista de funcionalidades implementadas, controles y espacio para el enlace del video de demostración.

### Pendiente (próxima entrega)
- Animación a partir de spritesheet.
- Música y efectos de sonido.
- Capa de red cliente-servidor (reemplazo del modo local por partidas entre dos computadoras).

## [0.1.0] - 2026-09-08

### Agregado
- Inicialización del proyecto con el framework LibGDX 1.14.2 y la plataforma de escritorio LWJGL3.
- Estructura de módulos del proyecto: `core`, `lwjgl3`, `server`, `shared`.
- Configuración del repositorio en GitHub para control de versiones.
- Archivo `.gitignore` adecuado para proyectos LibGDX.
- Archivo `README.md` con la información general del proyecto.
- Activación de la Wiki del repositorio y publicación de la propuesta formal del proyecto (Katapum).
