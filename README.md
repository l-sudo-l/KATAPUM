# KATAPUM

## Integrante del Grupo
- Rodriguez Marquez Luis Manuel 

## Descripción Corta del Videojuego
Katapum es un videojuego multijugador de tanques en 2D con vista top-down (cenital), pensado para ser jugado a través de una red local. Dos jugadores se enfrentan controlando cada uno un tanque en un mapa de batalla, con el objetivo de eliminar al oponente disparando proyectiles y esquivando los ataques entrantes. El mapa cuenta con obstáculos fijos e indestructibles que funcionan como cobertura táctica. Cada tanque tiene 3 vidas; cuando un jugador las pierde todas, su oponente es declarado ganador.

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
- `core/`: lógica compartida del juego (modelo, entidades, reglas).
- `lwjgl3/`: cliente de escritorio con interfaz gráfica (LibGDX + LWJGL3).
- `server/`: servidor headless que coordina el estado de la partida.
- `shared/`: clases y utilidades compartidas entre cliente y servidor.

## Estado del proyecto
Proyecto en desarrollo — Pre entrega N°1: Configuración Inicial del Proyecto y Repositorio.
