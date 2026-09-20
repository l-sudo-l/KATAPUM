# KATAPUM

## Integrante del Grupo
- Rodriguez Marquez Luis Manuel 

## Descripción Corta del Videojuego
Katapum es un videojuego de tanques en 2D con vista top-down (cenital). Dos jugadores se enfrentan controlando cada uno un tanque en un mapa de batalla, con el objetivo de eliminar al oponente disparando proyectiles y esquivando los ataques entrantes. El mapa cuenta con obstáculos fijos e indestructibles que funcionan como cobertura táctica. Cada tanque tiene 3 vidas; cuando un jugador las pierde todas, su oponente es declarado ganador.



## Estado actual del prototipo
Funcionalidades implementadas hasta el momento:
- Arquitectura basada en `Game`/`Screen` de LibGDX, con tres pantallas: **Menú**, **Juego** y **Fin de partida**.
- Manejo de entradas mediante una clase dedicada (`ManejadorEntradaJugador`, basada en `InputAdapter`), una instancia por jugador.
- Movimiento de dos tanques en modo local (mismo teclado): rotación libre sobre el propio eje (girar) y avance/retroceso según el ángulo actual (como un vehículo real, no por ejes fijos), con colisión resuelta eje por eje contra las paredes y contra el tanque rival (permite "deslizar" al tocar un obstáculo).
- Sprites reales para tanques, proyectiles, paredes, suelo y elementos decorativos (arbustos, cajas, barriles), con el tanque rotando según hacia dónde apunta.
- Animación por spritesheet: cada tanque tiene una secuencia de 4 cuadros (normal → cargando → estallido → carcasa quemada) que se reproduce al perder su última vida, y el proyectil vuela con una animación de 2 cuadros.
- Sistema de disparo: cada tanque dispara proyectiles animados en la dirección hacia la que está orientado, con fogonazo al salir y destello al impactar.
- Detección de colisiones: proyectil contra pared/decoración (se destruye), proyectil contra tanque rival (resta una vida) y tanque contra pared, decoración (arbusto/caja/barril) o tanque rival (bloquea el movimiento).
- Mapa/arena con obstáculos fijos e indestructibles (paredes y elementos decorativos, todos colisionables), dentro de una cámara con `Viewport` (`FitViewport`) que se adapta al tamaño de la ventana.
- HUD fijo en pantalla: nombre de cada jugador con contorno y corazones de vida junto al nombre (a la derecha para Jugador 1, a la izquierda para Jugador 2), volumen actual, con su propio `Viewport` independiente de la cámara del mundo.
- Música de fondo (menú y partida) y efectos de sonido (disparo, impacto, muerte, victoria), con controles de volumen y silencio (teclado y mouse).
- Menú de inicio y menú de pausa interactivos con mouse (botones y control deslizante de volumen), con estética retro (paneles y botones dibujados en bloques de color, sin depender de imágenes extra).
- Cooldown de 1.5s tras la animación de explosión antes de pasar a la pantalla de fin de partida, para que se alcance a ver la carcasa del tanque destruido.
- Condición de victoria: al perder sus 3 vidas, un jugador pierde la partida (se reproduce su animación de explosión completa) y se muestra la pantalla de Fin de partida con el banner del ganador.

Pendiente para próximas entregas: la capa de red (cliente-servidor) que reemplace el modo local por partidas entre dos computadoras.

## Controles
| Acción | Jugador 1 | Jugador 2 |
|---|---|---|
| Avanzar (en la dirección hacia la que apunta el tanque) | `W` | Flecha arriba |
| Retroceder (dirección contraria a la que apunta) | `S` | Flecha abajo |
| Girar sobre el propio eje (izquierda) | `A` | Flecha izquierda |
| Girar sobre el propio eje (derecha) | `D` | Flecha derecha |
| Disparar (en la dirección hacia la que apunta) | `Espacio` | `Ctrl derecho` |
| Confirmar / jugar de nuevo | `Enter` | `Enter` |
| Pausar / reanudar (durante la partida) | `Esc` o botón **CONTINUAR** (mouse) | `Esc` o botón **CONTINUAR** (mouse) |
| Volver al menú (en pausa o en fin de partida) | `Esc` o botón (mouse) | `Esc` o botón (mouse) |
| Subir / bajar volumen | `+` / `-` o clic/arrastre en la barra | `+` / `-` o clic/arrastre en la barra |
| Silenciar / activar sonido | `M` | `M` |

> El tanque se mueve como un vehículo real: `A`/`D` solo rotan el tanque sobre su propio eje (no desplazan lateralmente), y `W`/`S` avanzan o retroceden según hacia dónde esté apuntando en ese momento. El cañón y la dirección de disparo siempre coinciden con la orientación actual del tanque.
>
> El menú de inicio y el menú de pausa (ESC durante la partida) se manejan con **mouse**: botones clickeables y una barra de volumen donde se puede hacer clic o arrastrar para ajustar el nivel.

## Video de demostración
🎥 [Video de demostración de la Pre entrega N°2]()

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
- `assets/`: sprites, texturas y audio del juego. Ver [`CREDITOS.md`](CREDITOS.md) para la atribución de los assets de audio de terceros (OpenGameArt.org).
