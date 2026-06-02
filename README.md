# Space Invaders Android - LibGDX

Proyecto de una version moderna del clasico Space Invaders desarrollado con el framework LibGDX para Android y Escritorio. El juego incluye mecanicas clasicas como oleadas de enemigos y búnkeres de proteccion, junto con adiciones modernas como potenciadores y efectos visuales.

## Caracteristicas Principales

- Jugabilidad clasica de Space Invaders con multiples oleadas.
- Sistema de puntuacion y vidas.
- Búnkeres destructibles que protegen al jugador.
- Nave especial (OVNI) que aparece aleatoriamente para puntos extra.
- Potenciadores (Power-ups):
  - Disparo Multiple: Permite disparar varias balas simultaneamente.
  - Escudo: Proporciona una capa protectora temporal.
  - Velocidad: Aumenta la velocidad de movimiento de la nave.
- Efectos visuales: Fondo animado con estrellas y nebulosas, sacudida de camara al recibir daño.
- Sistema de configuracion personalizada y opciones de control.

## Estructura del Proyecto

El proyecto utiliza una estructura multi-modulo de LibGDX:

- **core**: Contiene toda la logica principal del juego, modelos, controladores y vistas (Java).
- **android**: Proyecto especifico para la plataforma Android.
- **lwjgl3**: Proyecto para ejecutar el juego en sistemas de escritorio (Windows, macOS, Linux).
- **assets**: Contiene todos los recursos graficos y de audio (PNG, MP3).
- **html**: Proyecto para exportacion a navegador (GWT).

## Controles y Como Jugar

### Controles en Escritorio (PC)
- **Moverse**: Teclas de flecha IZQUIERDA/DERECHA o teclas A/D.
- **Disparar**: Tecla ESPACIO.
- **Pausar**: Tecla P.
- **Interactuar**: Click con el raton en los menus.

### Controles en Android
Existen dos modos de control configurables desde el menu de opciones:

1. **Modo Moderno (Predeterminado)**:
   - Botones virtuales en pantalla para moverse a la izquierda, derecha y disparar.
   - Permite el uso de multiples toques (multitouch) para moverse y disparar a la vez.
2. **Modo Clasico**:
   - Tocar un lado de la pantalla para que la nave se mueva en esa direccion.
   - Tocar la parte superior de la pantalla para disparar.

### Objetivo del Juego
Elimina a todas las naves enemigas antes de que lleguen a la parte inferior de la pantalla o destruyan tu nave. Utiliza los búnkeres para cubrirte y recoge los power-ups que caen de los enemigos para obtener ventajas temporales.

## Guia de Ejecucion (Runbook)

### Requisitos Previos
- Java Development Kit (JDK) 11 o superior.
- Android Studio (recomendado para desarrollo Android) o IntelliJ IDEA.
- Gradle (incluido en el proyecto mediante el wrapper).

### Configuracion Inicial
Si el proyecto no reconoce la ruta de tu Android SDK, debes crear o editar el archivo `local.properties` en la raiz del proyecto y añadir la siguiente linea con tu ruta local:
```properties
sdk.dir=RUTA_A_TU_ANDROID_SDK
```
(En Windows usa barras dobles o barras hacia adelante, por ejemplo: `C:/Users/Usuario/AppData/Local/Android/Sdk`)

### Ejecucion en Escritorio
Para ejecutar el juego en tu ordenador:
1. Abre una terminal en la carpeta raiz del proyecto.
2. Ejecuta el siguiente comando:
   ```bash
   ./gradlew lwjgl3:run
   ```

### Ejecucion en Android
Para ejecutar el juego en un dispositivo o emulador:
1. Conecta tu dispositivo Android o inicia un emulador.
2. Ejecuta el siguiente comando:
   ```bash
   ./gradlew android:installDebug android:run
   ```
O simplemente abre el proyecto en Android Studio, selecciona el modulo `android` y pulsa el boton Run.

### Generacion de APK
Para generar el archivo instalable de Android:
1. Ejecuta:
   ```bash
   ./gradlew android:assembleDebug
   ```
2. El archivo APK se encontrara en `android/build/outputs/apk/debug/`.

## Recursos Utilizados (Assets)

- **Graficos**: Naves enemigas, nave jugador, proyectiles, búnkeres y efectos de power-ups.
- **Audio**:
  - Musica de fondo: `One_Last_Quarter.mp3`.
  - Sonidos: Disparos, daño recibido, captura de power-ups y sonido del OVNI especial.

---
Desarrollado para el proyecto de programacion de dispositivos moviles.
