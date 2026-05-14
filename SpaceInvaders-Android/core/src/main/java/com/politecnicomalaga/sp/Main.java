package com.politecnicomalaga.sp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.view.MainMenuScreen;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    private SpriteBatch batch;
    private BitmapFont font;
    private Map<String,Texture> galeriaImagenes;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        galeriaImagenes = new HashMap<>();

        Texture image;
        image = new Texture("enemigo1.png");
        galeriaImagenes.put("enemigo1.png",image);
        image = new Texture("enemigo2.png");
        galeriaImagenes.put("enemigo2.png",image);
        image = new Texture("naveJugador.png");
        galeriaImagenes.put("naveJugador.png",image);
        image = new Texture("disparoAmi.png");
        galeriaImagenes.put("disparoAmi.png", image);
        image = new Texture("disparoEne.png");
        galeriaImagenes.put("disparoEne.png", image);

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
        for (Texture imagen : galeriaImagenes.values()) {
            imagen.dispose();
        }
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

    public Map<String, Texture> getGaleriaImagenes() {
        return galeriaImagenes;
    }
}
