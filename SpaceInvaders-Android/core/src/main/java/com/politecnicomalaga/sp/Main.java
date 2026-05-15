package com.politecnicomalaga.sp;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.util.Recursos;
import com.politecnicomalaga.sp.view.PantallaMenuPrincipal;

/**
 * Clase principal del juego que extiende de {@link com.badlogic.gdx.Game}.
 * Se encarga de la inicialización de recursos globales y la gestión de pantallas.
 */
public class Main extends Game {
    private SpriteBatch lote;
    private BitmapFont fuente;

    @Override
    public void create() {
        // Inicialización del lote de dibujo y fuente de texto
        lote = new SpriteBatch();
        fuente = new BitmapFont();
        fuente.getData().setScale(2f);

        // Registro y carga de texturas base del juego
        Recursos recursos = Recursos.getInstancia();
        recursos.cargarTextura("enemigo1.png");
        recursos.cargarTextura("enemigo2.png");
        recursos.cargarTextura("naveJugador.png");
        recursos.cargarTextura("disparoAmi.png");
        recursos.cargarTextura("disparoEne.png");

        // Carga de texturas para Power-ups
        recursos.cargarTextura("DisparoMultiple.png");
        recursos.cargarTextura("Escudo.png");
        recursos.cargarTextura("Velocidad.png");

        // Inicio del juego con la pantalla del menú principal
        this.setScreen(new PantallaMenuPrincipal(this));
    }

    @Override
    public void render() {
        // Delega el renderizado a la pantalla activa
        super.render();
    }

    @Override
    public void dispose() {
        // Liberación de recursos de memoria al cerrar la aplicación
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
