# KATAPUM

## Integrante del Grupo
- Rodriguez Marquez Luis Manuel 

## Descripción Corta del Videojuego
Katapum es un videojuego de tanques en 2D con vista top-down (cenital). Dos jugadores se enfrentan controlando cada uno un tanque en un mapa de batalla, con el objetivo de eliminar al oponente disparando proyectiles y esquivando los ataques entrantes. El mapa cuenta con obstáculos fijos e indestructibles que funcionan como cobertura táctica. Cada tanque tiene 3 vidas; cuando un jugador las pierde todas, su oponente es declarado ganador.

> **Nota sobre el alcance actual:** la propuesta original plantea que ambos jugadores se conecten desde computadoras distintas por red (cliente-servidor). Para este prototipo (Pre entrega N°2) se implementó primero toda la mecánica central en **modo local (hotseat, mismo teclado)**, ya que las pautas de esta entrega piden demostrar la jugabilidad del núcleo del juego, no la capa de red. El módulo `server` y la comunicación por sockets se incorporarán en una entrega posterior reemplazando el `PlayerInputHandler` del Jugador 2 por la entrada recibida desde la red, sin tener que rehacer la lógica de juego.

## Estado actual del prototipo (Pre entrega N°2)
Funcionalidades implementadas hasta el momento:
- Arquitectura basada en `Game`/`Screen` de LibGDX, con tres pantallas: **Menú**, **Juego** y **Fin de partida**.
- Manejo de entradas mediante una clase dedicada (`PlayerInputHandler`, basada en `InputAdapter`), una instancia por jugador.
- Movimiento de dos tanques en modo local (mismo teclado), con colisión resuelta eje por eje contra las paredes y contra el tanque rival (permite "deslizar" al tocar un obstáculo en diagonal).
- Sistema de disparo: cada tanque dispara proyectiles en la dirección hacia la que está orientado.
- Detección de colisiones: proyectil contra pared (se destruye), proyectil contra tanque rival (resta una vida) y tanque contra pared/tanque rival (bloquea el movimiento).
- Mapa/arena con obstáculos fijos e indestructibles, dentro de una cámara con `Viewport` (`FitViewport`) que se adapta al tamaño de la ventana.
- HUD fijo en pantalla (vidas de cada jugador), con su propio `Viewport` independiente de la cámara del mundo.
- Estado de pausa (tecla ESC) dentro de la pantalla de juego.
- Condición de victoria: al perder sus 3 vidas, un jugador pierde la partida y se muestra la pantalla de Fin de partida con el nombre del ganador.

Pendiente para próximas entregas: animación por spritesheet, música y efectos de sonido, y la capa de red (cliente-servidor) que reemplace el modo local por partidas entre dos computadoras.

## Controles
| Acción | Jugador 1 | Jugador 2 |
|---|---|---|
| Mover arriba/abajo/izquierda/derecha | `W` `A` `S` `D` | Flechas |
| Disparar | `Espacio` | `Ctrl derecho` |
| Confirmar (menú / fin de partida) | `Enter` | `Enter` |
| Pausar / reanudar | `Esc` | `Esc` |

## Video de demostración
🎥 [Video de demostración de la Pre entrega N°2](AGREGAR_ENLACE_AQUI) <!-- Completar con el enlace de YouTube/Drive con permisos de visualización habilitados -->

## Tecnologías Principales
- **Framework:** LibGDX 1.14.2
- **Plataforma de escritorio:** LWJGL3
- **Lenguaje:** Java 8
- **Networking:** Sockets Java (UDP para posiciones/inputs en tiempo real, TCP para conexión inicial, lobby e inicio/fin de partida)
- **Mapas:** Tiled Map Editor (archivos TMX)
- **Plataformas de desarrollo objetivo:** Escritorio (target principal). Web y Móvil no forman parte del alcance de este proyecto.

## Enlace a la Wiki del Proyecto
La propuesta detallada del proyecto se encuentra en la Wiki del repositorio:

[Propuesta del Proyecto - Wiki](https://github.com/l-sudo-l/KATAPUM/wiki/propuesta)

## Instrucciones Básicas de Compilación y Ejecución

### Requisitos previos
- JDK 8 o superior instalado
- Git instalado

### Clonar el repositorio
```bash
git clone https://github.com/l-sudo-l/KATAPUM.git
cd KATAPUM
```

### Compilar y ejecutar (plataforma de escritorio)
En Windows:
```bash
gradlew.bat lwjgl3:run
```

En Linux/macOS:
```bash
./gradlew lwjgl3:run
```

### Estructura del proyecto
- `core/`: lógica del juego (independiente de la plataforma), organizada en paquetes:
  - `com.mistacorp.game`: clase principal (`KatapumPrincipal`) y configuración (`ConfiguracionJuego`).
  - `com.mistacorp.game.pantallas`: pantallas del juego (`PantallaMenu`, `PantallaJuego`, `PantallaFinPartida`).
  - `com.mistacorp.game.entidades`: objetos del mundo de juego (`Tanque`, `Proyectil`).
  - `com.mistacorp.game.mundo`: entorno y mapa (`Arena`, `Pared`).
  - `com.mistacorp.game.entrada`: manejo de entradas (`ManejadorEntradaJugador`).
  - `com.mistacorp.game.interfaz`: interfaz de usuario en pantalla (`Hud`).
- `lwjgl3/`: cliente de escritorio (LibGDX + LWJGL3). Clase principal: `LanzadorLwjgl3`.
- `server/`: servidor headless que coordinará el estado de la partida en red (aún sin implementar; próximas entregas). Clase principal: `LanzadorServidor`.
- `shared/`: clases y utilidades que compartirán cliente y servidor una vez incorporada la red.

## Estado del proyecto
Proyecto en desarrollo — Pre entrega N°2: prototipo jugable local con mecánica central de combate de tanques.
