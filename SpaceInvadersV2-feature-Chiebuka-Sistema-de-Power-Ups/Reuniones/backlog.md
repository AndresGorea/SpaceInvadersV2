  📋 Backlog de Tareas: Space Invaders V2



  🛠️ ÉPICA 1: Refactorización y Base Técnica (Prioridad: ALTA)
  Estas tareas deben hacerse primero porque afectan a cómo se moverá y renderizará todo el juego a partir de ahora.

* [TASK-101] Migración a Delta Time (getDeltaTime)
  
  * Prioridad: 🔴 Alta (Bloqueante)
  * Descripción: Cambiar la lógica de movimiento y disparo de "ticks" fijos al uso de DeltaTime. Esto asegurará que el juego corra a la misma velocidad independientemente de los FPS del dispositivo.

* [TASK-102] Implementación de Viewport y Reescalado
  
  * Prioridad: 🔴 Alta
  * Descripción: Integrar FitViewport o ExtendViewport de libGDX en la cámara principal para garantizar que la relación de aspecto se mantenga correcta en monitores de PC y pantallas de móvil.

* [TASK-103] Soporte de Controles Multiplataforma (PC & Mobile)
  
  * Prioridad: 🔴 Alta
  * Descripción: Implementar InputProcessor. Añadir movimiento con Flechas/WASD, mantener el control táctil para móviles, y cambiar el disparo automático por disparo manual (Barra Espaciadora en PC / Botón en pantalla para Android).
    
    
  
  🖥️ ÉPICA 2: UI, Menús y Flujo de Juego (Prioridad: ALTA/MEDIA)
  Ideal para que un desarrollador se enfoque exclusivamente en la experiencia de usuario y las pantallas.

* [TASK-201] Creación de Estados de Juego (Pausa y Game Over)
  
  * Prioridad: 🔴 Alta
  * Descripción: Implementar la lógica para pausar el juego (detener la actualización del modelo). Crear la pantalla de Game Over que muestre la puntuación final cuando el jugador pierda sus 3 vidas o el batallón gane.

* [TASK-202] Implementación del HUD (Head-Up Display)
  
  * Prioridad: 🟡 Media
  * Descripción: Mostrar en la pantalla principal de juego el marcador de puntuación (arriba) y los iconos representativos de las 3 vidas restantes del jugador.

* [TASK-203] Creación de Pantalla de Inicio (Main Menu)
  
  * Prioridad: 🟡 Media
  * Descripción: Crear escena principal con fondo animado, botones interactivos (Jugar, Opciones, Información, Salir).

* [TASK-204] Menú de Opciones y Pantalla de Información
  
  * Prioridad: 🟡 Media
  * Descripción: Pantalla para cambiar dificultad, mutear música/SFX y alternar pantalla completa. Pantalla extra con los créditos y guía de puntuaciones/controles.

* [TASK-205] Sistema de Persistencia de Datos (Preferences)
  
  * Prioridad: 🟡 Media
  * Descripción: Usar Preferences de libGDX para guardar la puntuación más alta (High Score) y la configuración seleccionada en el menú de opciones (volumen, controles) entre sesiones.
    
    
  
  👾 ÉPICA 3: Nuevas Mecánicas de Gameplay (Prioridad: MEDIA)
  Tareas para expandir la jugabilidad clásica. Se pueden hacer en paralelo a la Épica 2.

* [TASK-301] Sistema de Puntuación Variable y Vidas de Enemigos
  
  * Prioridad: 🟡 Media
  * Descripción: Asignar puntuaciones distintas según el tipo de nave destruida. Añadir lógica para que las naves de la fila superior del escuadrón requieran más de un impacto para ser destruidas.

* [TASK-302] Implementación de Búnkeres Destructibles
  
  * Prioridad: 🟡 Media
  * Descripción: Crear la entidad Bunker. Instanciar 4 barreras entre el jugador y los enemigos. Programar la lógica de colisión para que los disparos (amigos y enemigos) degraden visual y funcionalmente el búnker por fases.

* [TASK-303] Entidad: OVNI Misterioso
  
  * Prioridad: 🟢 Baja
  * Descripción: Crear una nave especial que aparezca aleatoriamente en la parte superior, se mueva horizontalmente de un lado a otro y otorgue una puntuación aleatoria extra si es destruida. Incluir sonido de alerta al aparecer.

* [TASK-304] Sistema de Power-Ups (Drops y Efectos)
  
  * Prioridad: 🟢 Baja
  * Descripción: Lógica de "drop" porcentual al destruir enemigos. Crear la entidad PowerUp que caiga hacia abajo. Implementar los 3 efectos al colisionar con el jugador (Disparo múltiple, Escudo temporal, Velocidad aumentada).
    
    
  
  ✨ ÉPICA 4: Audiovisual y "Game Feel" (Prioridad: BAJA)
  La capa de pulido. Estas tareas transforman el juego de algo funcional a algo divertido de jugar.

* [TASK-401] Integración de Audio Dinámico (BGM y SFX)
  
  * Prioridad: 🟡 Media
  * Descripción: Añadir música de fondo que aumente su velocidad/pitch según queden menos enemigos. Integrar sonidos para disparos, explosiones, daño, power-ups y el OVNI.

* [TASK-402] Efectos Visuales: Sistema de Partículas y Parallax
  
  * Prioridad: 🟢 Baja
  * Descripción: Usar el sistema de partículas de libGDX para explosiones de naves y desgaste de búnkeres. Implementar un fondo Parallax con múltiples capas de estrellas moviéndose a distintas velocidades.

* [TASK-403] Game Feel: Screen Shake y Hit Stop
  
  * Prioridad: 🟢 Baja
  * Descripción: Programar la cámara para que vibre/tiemble levemente al recibir daño o destruir el OVNI. Implementar micro-pausas (Hit Stop) de unos milisegundos en impactos críticos.

* [TASK-404] Feedback Háptico para Android
  
  * Prioridad: 🟢 Baja
  * Descripción: Utilizar Gdx.input.vibrate() para generar pequeñas vibraciones en el dispositivo móvil al disparar (muy leve) o al perder una vida (más fuerte).
