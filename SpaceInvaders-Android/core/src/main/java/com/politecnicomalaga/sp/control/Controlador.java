package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.control.EfectosCamara;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.view.RenderizadorMundo;

/**
 * Clase controladora principal que sigue el patrón Singleton.
 * Orquestra la comunicación entre el modelo, la vista y la lógica de colisiones.
 */
public class Controlador {
    private static Controlador miSingle;

    // Atributos que representan los diferentes gestores y el estado
    private GestorMundo gestorMundo;
    private GestorColisiones gestorColisiones;
    private EstadoJuego estadoJuego;
    private final RenderizadorMundo renderizadorMundo;
    private final boolean esAndroid;

    /**
     * Constructor privado para garantizar el patrón Singleton.
     * Inicializa los componentes básicos del juego.
     */
    private Controlador() {
        esAndroid = com.badlogic.gdx.Gdx.app.getType() == Application.ApplicationType.Android;
        this.estadoJuego = new EstadoJuego(ConfiguracionJuego.NAVE_VIDAS);
        this.gestorMundo = new GestorMundo();
        this.gestorColisiones = new GestorColisiones();
        this.renderizadorMundo = new RenderizadorMundo();
    }

    /**
     * Obtiene la instancia única del controlador.
     * @return La instancia de Controlador.
     */
    public static Controlador getInstancia() {
        if (miSingle == null) {
            miSingle = new Controlador();
        }
        return miSingle;
    }

    /**
     * Gestiona la interacción del usuario (click/toque) para mover la nave.
     * @param x Coordenada X del toque.
     * @param y Coordenada Y del toque.
     */
    public void click(float x, float y) {
        gestorMundo.cambiarSentidoNaveAmiga(x);
    }

    public void moverNaveAmiga(Ovni.Direccion direccion) {
        gestorMundo.moverNaveAmiga(direccion);
    }

    public void dispararNaveAmiga() {
        gestorMundo.dispararNaveAmiga();
    }

    /**
     * Bucle principal de simulación del mundo.
     * Actualiza posiciones, comprueba colisiones y valida condiciones de victoria.
     * @param anchoPantalla Ancho actual de la pantalla.
     * @param altoPantalla Alto actual de la pantalla.
     * @param delta Tiempo transcurrido desde el último frame.
     */
    public void simulaMundo(float anchoPantalla, float altoPantalla, float delta) {
        if (estadoJuego.isJugando() && !estadoJuego.isPausado() && !EfectosCamara.getInstancia().isHitStopActivo()) {
            // 1. Actualizar posiciones y estados de todos los objetos
            gestorMundo.actualizar(anchoPantalla, altoPantalla, delta);

            // 2. Procesar interacciones físicas entre objetos
            gestorColisiones.comprobarColisiones(gestorMundo, estadoJuego);

            // 3. Verificar si el jugador ha eliminado a todos los enemigos
            if (!gestorMundo.getBatallon().tieneTropas()) {
                estadoJuego.setJugando(false);
            }
        }
    }

    /**
     * Reinicia el estado del juego para una nueva partida.
     */
    public void reiniciar() {
        this.estadoJuego = new EstadoJuego(ConfiguracionJuego.NAVE_VIDAS);
        this.gestorMundo = new GestorMundo();
        this.gestorColisiones = new GestorColisiones();
    }

    /**
     * Renderiza los elementos del mundo de juego.
     * @param lote El SpriteBatch utilizado para el dibujo.
     */
    public void pintar(SpriteBatch lote) {
        renderizadorMundo.renderizar(lote, gestorMundo);
    }

    /**
     * Renderiza la interfaz de usuario (puntuación, vidas).
     * @param lote El SpriteBatch utilizado para el dibujo.
     * @param fuente La fuente para los textos.
     * @param anchoPantalla Ancho de pantalla para posicionamiento.
     * @param altoPantalla Alto de pantalla para posicionamiento.
     */
    public void pintarHUD(SpriteBatch lote, BitmapFont fuente, float anchoPantalla, float altoPantalla) {
        renderizadorMundo.renderizarHUD(lote, estadoJuego, gestorMundo, fuente, anchoPantalla, altoPantalla);
    }

    public void pintarBotonesAndroid(SpriteBatch lote, BitmapFont fuente,
                                  float anchoPantalla, float altoPantalla) {
        // Reducimos el tamaño a aproximadamente la mitad de lo anterior y añadimos margen
        float btnMoverAncho = anchoPantalla * 0.14f; // 14% de la pantalla (más pequeño)
        float btnFireAncho = anchoPantalla * 0.12f;   // 12% para disparar
        float btnAlto  = altoPantalla  * 0.12f;      // 12% de altura
        float btnFireAlto  = altoPantalla  * 0.17f;      // 17% de altura
        float margen = 20f; // Margen respecto a los bordes

        com.politecnicomalaga.sp.util.Assets assets = com.politecnicomalaga.sp.util.Assets.getInstance();

        // Configurar transparencia para que no tapen el juego (60% alpha)
        lote.setColor(1, 1, 1, 0.6f);

        // Botón Izquierda
        lote.draw(assets.getTexture("row_left.png"), margen, margen, btnMoverAncho, btnAlto);

        // Botón Derecha
        lote.draw(assets.getTexture("row_right.png"), btnMoverAncho + margen + 10, margen, btnMoverAncho, btnAlto);

        // Botón Disparo (en la esquina derecha con margen)
        lote.draw(assets.getTexture("shot_button.png"), anchoPantalla - btnFireAncho - margen, margen, btnFireAncho, btnFireAlto);

        // Resetear color
        lote.setColor(1, 1, 1, 1f);
    }

    //Obtener estados
    public EstadoJuego getEstadoJuego() {
        return estadoJuego;
    }

    public GestorMundo getGestorMundo() {
        return gestorMundo;
    }

    public boolean esAndroid() {
        return esAndroid;
    }
}
