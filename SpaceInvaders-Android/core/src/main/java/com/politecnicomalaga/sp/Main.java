package com.politecnicomalaga.sp;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.control.Controlador;

import java.util.HashMap;
import java.util.Map;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private BitmapFont font;

    private float anchoPantalla,altoPantalla;

    private float y,x;
    Map<String,Texture> galeriaImagenes;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(2f);
        galeriaImagenes = new HashMap<>();

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

        // Power-ups
        galeriaImagenes.put("DisparoMultiple.png", new Texture("DisparoMultiple.png"));
        galeriaImagenes.put("Escudo.png", new Texture("Escudo.png"));
        galeriaImagenes.put("Velocidad.png", new Texture("Velocidad.png"));

        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();

    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        //Control de entrada
        if(Gdx.input.justTouched()){
            x= Gdx.input.getX();
            y=Gdx.input.getY();
            Controlador.getInstance().click(x,y);
        }

        //Control de estado
        Controlador.getInstance().simulaMundo(anchoPantalla,altoPantalla, Gdx.graphics.getDeltaTime());


        //Pintar el mundo
        batch.begin();
        Controlador.getInstance().pintar(batch, galeriaImagenes);
        Controlador.getInstance().pintarHUD(batch, galeriaImagenes, font, anchoPantalla, altoPantalla);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        for (Texture imagen : galeriaImagenes.values()) {
            imagen.dispose();
        }
    }
}
