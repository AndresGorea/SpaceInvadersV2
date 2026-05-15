package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Incluye el fondo animado compartido y la lógica de entidades.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private float anchoPantalla, altoPantalla;
    private float x, y;

    // Gestor de efectos visuales de fondo (reutilizable)
    private FondoEfectos fondoEfectos;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
        fondoEfectos = new FondoEfectos();
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
        if(Gdx.input.justTouched()){
            x = Gdx.input.getX();
            y = Gdx.input.getY();
            Controlador.getInstancia().click(x, y);
        }

        // 2. Lógica física
        Controlador.getInstancia().simulaMundo(anchoPantalla, altoPantalla, delta);

        // 3. Renderizado
        juego.getLote().begin();
        
        // El fondo se dibuja primero para que quede detrás de las naves
        fondoEfectos.renderizar(juego.getLote(), delta);
        
        Controlador.getInstancia().pintar(juego.getLote());
        Controlador.getInstancia().pintarHUD(juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);
        
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
