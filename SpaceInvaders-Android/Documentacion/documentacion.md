# Análisis de Características: Space Invaders (libGDX) Version 1

## ⚙️ Mecánicas Principales

* **Control del Jugador**: El movimiento de la nave del jugador (`NaveAmi`) se controla mediante toques o clics en la pantalla.
  
  * Si se toca a la derecha de la nave, esta se mueve hacia la derecha.
  * Si se toca a la izquierda, se mueve a la izquierda.
  * Si se vuelve a tocar en la misma dirección en la que ya se está moviendo, la nave se detiene (`NOMOVER`).
  * La nave tiene límites y no puede salir de los bordes de la pantalla.

* **Disparo Automático**: El jugador no presiona un botón para disparar. La nave amiga dispara automáticamente proyectiles (`DisparoAmi`) hacia arriba con una cadencia fija definida en el controlador (cada 180 "ticks" o fotogramas).

* **Comportamiento Enemigo**:
  
  * Los enemigos están organizados en un **Batallón** (`Batallon`), que a su vez se divide en 4 **Escuadrones** (`Escuadron`) de 8 naves enemigas (`NaveEne`) cada uno.
  * El batallón se mueve en bloque de forma lateral. Cuando cualquier nave enemiga viva toca el borde de la pantalla, todo el batallón invierte su dirección horizontal y desciende una cantidad fija de píxeles hacia el jugador.
  * Las naves enemigas disparan hacia abajo con una cadencia compartida, pero cada disparo está condicionado por un sistema de probabilidad individual (porcentaje de acierto o `probabilidadDisparo`).

* **Colisiones y Daño**:
  
  * Los disparos del jugador destruyen a las naves enemigas al impactar.
  * Los disparos enemigos que impactan contra el jugador le restan una vida.
  * Si una nave enemiga choca físicamente contra la nave del jugador (ataque kamikaze / invasión), la nave enemiga muere y el jugador pierde una vida.

* **Condiciones de Victoria y Derrota**:
  
  * **Victoria**: Se logra cuando todas las naves enemigas del batallón han sido destruidas (`comprobarSiGano` devuelve *false* y se deja de jugar).
  * **Derrota**: Ocurre si la nave del jugador pierde todas sus vidas (`estaVivo()` devuelve *false*).

## 🧩 Arquitectura y Entidades (Modelo)

El juego utiliza herencia para reutilizar lógica común de los elementos en pantalla:

* **`Nave` (Clase Base)**: De ella heredan tanto el jugador como los enemigos. Gestiona posición, tamaño, estado (VIVO/MUERTO), vidas y las características de las balas.
* **`NaveAmi`**: Gestiona la lista de disparos aliados, calculando la posición central para instanciar las balas y eliminándolas cuando salen de la pantalla superior.
* **`NaveEne`**: De manera similar, gestiona los disparos enemigos y los elimina al cruzar el límite inferior de la pantalla.
* **`Batallon` y `Escuadron`**: Gestionan el movimiento grupal, asegurando que las naves no se solapen, coordinando el descenso de bloque y delegando las órdenes de disparo a cada nave de forma jerárquica.
* **`DisparoAmi` y `DisparoEne`**: Representan los proyectiles y su lógica individual de movimiento y desaparición.

# 🚀 Funcionalidades para la Versión 2

## 🖥️ Interfaz y Menús (UI/UX)

- **Pantalla de Inicio (Main Menu)**:
  
  - Fondo animado (ej. estrellas en movimiento o naves pasando).
  
  - **Botón Jugar**: Acceso directo a la acción.
  
  - **Botón Opciones**:
    
    - **Dificultad/Velocidad**: Selector de nivel (Fácil, Normal, Difícil).
    
    - **Audio**: Toggles independientes para Música y Efectos de Sonido (SFX).
    
    - **Pantalla**: Cambio entre Pantalla Completa y Ventana.
  
  - **Botón Información (Créditos/Tutorial)**:
    
    - Guía visual de las naves (puntuación que otorga cada una).
    
    - Esquema de controles para Móvil y PC.
  
  - **Botón Salir**: Cierre limpio de la aplicación.

- **HUD (Head-Up Display)**:
  
  - Marcador de puntuación en tiempo real en la parte superior.
  
  - Visualización clara de las **3 vidas** iniciales del jugador mediante iconos de la nave.

## 🎮 Mejoras en el Gameplay y Mecánicas Clásicas

- **Control Multiplataforma (PC Support)**:
  
  - Implementación de `InputProcessor` para detectar teclas.
  
  - Movimiento con **Flechas de dirección** o **WASD**.
  
  - Disparo manual con la **Barra Espaciadora o Boton en android**.

- **Sistema de Puntuación (Score)**:
  
  - Los puntos varían según el tipo de nave enemiga destruida.

- **Gestión de Estados**:
  
  - **Botón de Pausa**: Interrupción de la lógica del juego con menú de "Reanudar" o "Salir al Menú".
  
  - **Game Over Screen**: Pantalla de fin de juego con la puntuación final obtenida.

- **Escudos / Búnkeres Destructibles**:
  
  - Añadir las clásicas 4 barreras defensivas entre el jugador y los aliens.
  
  - Cada disparo (aliado o enemigo) degrada visual y funcionalmente el búnker, añadiendo una importante capa estratégica al posicionamiento.

- **El OVNI Misterioso (Mystery Ship)**:
  
  - Una nave especial que cruza la parte superior de la pantalla de forma aleatoria.
  
  - Cuenta con un sonido característico de aviso y otorga una cantidad de puntos al azar si se logra destruir.

- **Power-Ups (Cápsulas de Mejora)**:
  
  - Probabilidad de que algunos enemigos suelten un ítem al morir que caiga lentamente.
  
  - Beneficios temporales al recogerlos:
    
    - *Disparo doble/triple.*
    
    - *Escudo temporal* (invulnerabilidad por 5 segundos).
    
    - *Aumento de velocidad* de movimiento de la nave.

- **Variabilidad de Enemigos**:
  
  - Otorgar más resistencia (más vidas/impactos necesarios) a las naves enemigas situadas en la fila superior del escuadrón.

## ✨ Game Feel (Sensación de Juego y Pulido)

- **Screen Shake (Temblor de Pantalla)**:
  
  - Aplicar un ligero temblor a la cámara (utilizando el sistema de `Camera` y `Viewport`) cuando el jugador recibe daño o cuando explota un enemigo grande. Esto hace que el juego se sienta mucho más impactante.

- **Feedback Háptico (Vibración)**:
  
  - Utilizar la API de vibración de libGDX (`Gdx.input.vibrate()`) para los dispositivos Android, haciendo que el móvil vibre ligeramente al perder una vida o al disparar.

- **Hit Stop (Micro-pausas)**:
  
  - Congelar el juego durante un par de milisegundos al destruir a un enemigo importante (como el OVNI). Esto proporciona una gran sensación de impacto físico al jugador.

## 🎵 Apartado Audiovisual

- **Audio Dinámico**:
  
  - Música de fondo (BGM) en bucle que aumente de ritmo o tensión a medida que queden menos enemigos en pantalla.
  
  - Efectos de sonido (SFX) para disparos, explosiones, recogida de power-ups y pérdida de vidas.

- **Efectos Visuales**:
  
  - **Sistema de Partículas**: Explosiones realistas al destruir naves o degradar los búnkeres.
  
  - **Parallax Scrolling**: Varias capas de estrellas en el fondo moviéndose a distintas velocidades para dar sensación de profundidad 3D.

## 🛠️ Mejoras Técnicas (Arquitectura)

- **Sincronización Independiente del Frame Rate (Delta Time)**:
  
  - Migración de la lógica de "ticks" fijos a `Gdx.graphics.getDeltaTime()`. Esto garantiza que el juego corra a la misma velocidad real independientemente de si el dispositivo va a 30 FPS o a 144 FPS.

- **Resolución Dinámica y Reescalado**:
  
  - Uso del sistema de `Viewport` de libGDX (`FitViewport` o `ExtendViewport`) para asegurar que el juego mantenga su relación de aspecto y se vea bien tanto en móviles pequeños como en monitores grandes de PC.

- **Persistencia de Datos**:
  
  - Uso de `Preferences` de libGDX para guardar la puntuación más alta (High Score) y la configuración de opciones (volumen, controles) para futuras sesiones.