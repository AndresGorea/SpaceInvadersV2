package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;

public class PantallaJuego implements Screen {

    private final Main juego;
    private float anchoPantalla, altoPantalla;
    private float x, y;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {
        // Reset control input state if needed or set proper InputProcessor
        Gdx.input.setInputProcessor(null); // Or assign to a specific gameplay InputProcessor later
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Control de entrada
        if(Gdx.input.justTouched()){
            x = Gdx.input.getX();
            y = Gdx.input.getY();
            Controlador.getInstancia().click(x, y);
        }

        // Control de estado
        Controlador.getInstancia().simulaMundo(anchoPantalla, altoPantalla, delta);

        // Pintar el mundo
        juego.getLote().begin();
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
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // No disposing here as resources are managed by Main/Controlador mostly for now.
    }
}
