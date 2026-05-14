package com.politecnicomalaga.sp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.util.Assets;
import com.politecnicomalaga.sp.view.MainMenuScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private BitmapFont font;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);

        // Cargar texturas a través de Assets
        Assets assets = Assets.getInstance();
        assets.loadTexture("enemigo1.png");
        assets.loadTexture("enemigo2.png");
        assets.loadTexture("naveJugador.png");
        assets.loadTexture("disparoAmi.png");
        assets.loadTexture("disparoEne.png");

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // important!
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        Assets.getInstance().dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }
}
