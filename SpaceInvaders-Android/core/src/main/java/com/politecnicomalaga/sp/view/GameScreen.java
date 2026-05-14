package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;

public class GameScreen implements Screen {

    private final Main game;
    private float anchoPantalla, altoPantalla;
    private float x, y;

    public GameScreen(Main game) {
        this.game = game;
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

        //Control de entrada
        if(Gdx.input.justTouched()){
            x= Gdx.input.getX();
            y=Gdx.input.getY();
            Controlador.getInstance().click(x,y);
        }

        //Control de estado
        Controlador.getInstance().simulaMundo(anchoPantalla,altoPantalla, delta);

        //Pintar el mundo
        game.getBatch().begin();
        Controlador.getInstance().pintar(game.getBatch(), game.getGaleriaImagenes());
        Controlador.getInstance().pintarHUD(game.getBatch(), game.getGaleriaImagenes(), game.getFont(), anchoPantalla, altoPantalla);
        game.getBatch().end();
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
