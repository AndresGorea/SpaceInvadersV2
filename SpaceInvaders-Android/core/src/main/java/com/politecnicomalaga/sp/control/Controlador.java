package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.view.RenderizadorMundo;
import java.util.Map;

public class Controlador {
    private static Controlador miSingle;

    //Atributos
    private GestorMundo gestorMundo;
    private GestorColisiones gestorColisiones;
    private EstadoJuego estadoJuego;
    private RenderizadorMundo renderizadorMundo;

    //Constructor
    private Controlador() {
        this.estadoJuego = new EstadoJuego(ConfiguracionJuego.NAVE_VIDAS);
        this.gestorMundo = new GestorMundo();
        this.gestorColisiones = new GestorColisiones();
        this.renderizadorMundo = new RenderizadorMundo();
    }

    //implementación del singleton
    public static Controlador getInstancia() {
        if (miSingle == null) {
            miSingle = new Controlador();
        }
        return miSingle;
    }

    //Cambio de sentido
    public void click(float x, float y) {
        gestorMundo.cambiarSentidoNaveAmiga(x);
    }

    //Bucle de la lógica del juego
    public void simulaMundo(float anchoPantalla, float altoPantalla, float delta) {
        if (estadoJuego.isJugando()) {
            // Actualizar lógica del mundo
            gestorMundo.actualizar(anchoPantalla, altoPantalla, delta);

            // Comprobar colisiones
            gestorColisiones.comprobarColisiones(gestorMundo, estadoJuego);

            // Comprobar si se ha ganado
            if (!gestorMundo.getBatallon().tieneTropas()) {
                estadoJuego.setJugando(false);
            }
        }
    }

    //Pintar el juego principal
    public void pintar(SpriteBatch lote) {
        renderizadorMundo.renderizar(lote, gestorMundo);
    }

    //Pintar el HUD
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
}
