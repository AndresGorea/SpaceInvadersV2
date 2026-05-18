package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;
import com.politecnicomalaga.sp.model.Ovni;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Incluye el fondo animado compartido y la lógica de entidades.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private float anchoPantalla, altoPantalla;
    private float x, y;
    private float btnAncho, btnAlto;

    // Gestor de efectos visuales de fondo (reutilizable)
    private FondoEfectos fondoEfectos;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
        // Desactivamos los ovnis flotantes en el fondo durante el juego
        fondoEfectos = new FondoEfectos(false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        // Fondo negro profundo para el espacio
        ScreenUtils.clear(0.01f, 0.01f, 0.05f, 1f);

        // 1. Entrada
        // Dimensiones de botones
        btnAncho = anchoPantalla * 0.2f;
        btnAlto = altoPantalla * 0.15f;

        // Zonas de botones Android (izquierda, derecha, disparo)
        boolean tocandoIzq   = Gdx.input.isTouched() &&
                               Gdx.input.getX() < btnAncho &&
                               Gdx.input.getY() > altoPantalla - btnAlto;
        boolean tocandoDer   = Gdx.input.isTouched() &&
                               Gdx.input.getX() > btnAncho && Gdx.input.getX() < btnAncho * 2.5f &&
                               Gdx.input.getY() > altoPantalla - btnAlto;
        boolean tocandoFire  = Gdx.input.justTouched() &&
                               Gdx.input.getX() > anchoPantalla - btnAncho &&
                               Gdx.input.getY() > altoPantalla - btnAlto;

        if (Controlador.getInstancia().esAndroid()) {
            if (tocandoIzq) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.IZQUIERDA);
            } else if (tocandoDer) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.DERECHA);
            } else if (!Gdx.input.isTouched()) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.NOMOVER);
            }
            if (tocandoFire) {
                Controlador.getInstancia().dispararNaveAmiga();
            }
        } else {
            if (Gdx.input.justTouched()) {
                x = Gdx.input.getX();
                y = Gdx.input.getY();
                Controlador.getInstancia().click(x, y);
            }
        }

        // Controles de teclado (solo PC)
        if (!Controlador.getInstancia().esAndroid()) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.IZQUIERDA);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.DERECHA);
            } else if (!Gdx.input.justTouched() && !Gdx.input.isTouched()) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.NOMOVER);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Controlador.getInstancia().dispararNaveAmiga();
            }
        }

        // 2. Lógica física
        Controlador.getInstancia().simulaMundo(anchoPantalla, altoPantalla, delta);

        // 3. Renderizado
        juego.getLote().begin();
        
        // El fondo se dibuja primero para que quede detrás de las naves
        fondoEfectos.renderizar(juego.getLote(), delta);
        
        Controlador.getInstancia().pintar(juego.getLote());
        Controlador.getInstancia().pintarHUD(juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);

        // Dibujar botones táctiles en Android
        if (Controlador.getInstancia().esAndroid()) {
            Controlador.getInstancia().pintarBotonesAndroid(
                juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);
        }
        
        juego.getLote().end();
    }

    @Override
    public void resize(int width, int height) {
        anchoPantalla = width;
        altoPantalla = height;
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        fondoEfectos.dispose();
    }
}
