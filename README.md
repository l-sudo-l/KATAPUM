# Katapum

## Integrantes del Grupo
- Rodriguez luis (proyeto individual)

## Descripción Corta del Videojuego
Katapum es un videojuego multijugador de tanques en 2D con vista top-down (cenital), pensado para ser jugado a través de una red local. Dos jugadores se enfrentan controlando cada uno un tanque en un mapa de batalla, con el objetivo de eliminar al oponente disparando proyectiles y esquivando los ataques entrantes. El mapa cuenta con obstáculos fijos e indestructibles que funcionan como cobertura táctica. Cada tanque tiene 3 vidas; cuando un jugador las pierde todas, su oponente es declarado ganador.

## Tecnologías Principales
- **Framework:** LibGDX 1.14.2.1
- **Lenguaje:** Java 8
- **Networking:** Sockets Java (UDP para posiciones/inputs en tiempo real, TCP para conexión inicial, lobby e inicio/fin de partida)
- **Mapas:** Tiled Map Editor (archivos TMX)
- **Plataformas de desarrollo objetivo:** Escritorio (target principal). Web y Móvil no forman parte del alcance de este proyecto.

## Enlace a la Wiki del Proyecto
La propuesta detallada del proyecto se encuentra en la Wiki del repositorio:
👉 [Propuesta del Proyecto - Wiki] https://github.com/l-sudo-l/KATAPUM/wiki/propuesta 

## Instrucciones Básicas de Compilación y Ejecución

### Requisitos previos
- JDK 8 o superior instalado
- Git instalado

### Clonar el repositorio
```bash
git clone https://github.com/[usuario]/[repositorio].git
cd [repositorio]
```

### Compilar y ejecutar (plataforma de escritorio)
En Linux/macOS:
```bash
./gradlew desktop:run
```

En Windows:
```bash
gradlew.bat desktop:run
```

### Estructura del proyecto
- `core/`: lógica compartida del juego (modelo, entidades, reglas).
- `desktop/`: cliente de escritorio con interfaz gráfica (LibGDX).
- `server/`: servidor headless que coordina el estado de la partida.

## Estado del proyecto
Proyecto en desarrollo — Pre entrega N°1: Configuración Inicial del Proyecto y Repositorio.
