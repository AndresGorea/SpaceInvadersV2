package com.politecnicomalaga.sp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.util.Recursos;
import com.politecnicomalaga.sp.view.PantallaMenuPrincipal;

/** Implementación de {@link com.badlogic.gdx.ApplicationListener} compartida por todas las plataformas. */
public class Main extends Game {
    private SpriteBatch lote;
    private BitmapFont fuente;

    @Override
    public void create() {
        lote = new SpriteBatch();
        fuente = new BitmapFont();
        fuente.getData().setScale(2f);

        // Cargar texturas a través de Recursos
        Recursos recursos = Recursos.getInstancia();
        recursos.cargarTextura("enemigo1.png");
        recursos.cargarTextura("enemigo2.png");
        recursos.cargarTextura("naveJugador.png");
        recursos.cargarTextura("disparoAmi.png");
        recursos.cargarTextura("disparoEne.png");

        this.setScreen(new PantallaMenuPrincipal(this));
    }

    @Override
    public void render() {
        super.render(); // ¡importante!
    }

    @Override
    public void dispose() {
        lote.dispose();
        fuente.dispose();
        Recursos.getInstancia().dispose();
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }

    public SpriteBatch getLote() {
        return lote;
    }

    public BitmapFont getFuente() {
        return fuente;
    }
}
