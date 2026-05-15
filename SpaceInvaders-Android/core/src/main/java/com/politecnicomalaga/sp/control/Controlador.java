package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.view.RenderizadorMundo;
import java.util.Map;

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
    private RenderizadorMundo renderizadorMundo;
    private boolean esAndroid;

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

    /**
     * Bucle principal de simulación del mundo.
     * Actualiza posiciones, comprueba colisiones y valida condiciones de victoria.
     * @param anchoPantalla Ancho actual de la pantalla.
     * @param altoPantalla Alto actual de la pantalla.
     * @param delta Tiempo transcurrido desde el último frame.
     */
    public void simulaMundo(float anchoPantalla, float altoPantalla, float delta) {
        if (estadoJuego.isJugando()) {
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
        renderizadorMundo.renderizarHUD(lote, estadoJuego, fuente, anchoPantalla, altoPantalla);
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
