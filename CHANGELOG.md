# Changelog

Todos los cambios notables de este proyecto se documentarán en este archivo.

El formato está basado en [Keep a Changelog](https://keepachangelog.com/es-ES/1.0.0/),
y este proyecto adhiere a [Semantic Versioning](https://semver.org/lang/es/).

## [No publicado]

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
