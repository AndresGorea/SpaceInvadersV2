package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.util.SettingsManager;

/**
 * Pantalla de configuración del juego.
 * Permite ajustar el volumen y el tipo de controles.
 */
public class PantallaOpciones implements Screen {

    private final Main juego;
    private final Stage escenario;
    private Skin apariencia;
    private final FondoEfectos fondoEfectos;

    public PantallaOpciones(final Main juego) {
        this.juego = juego;
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        crearAparienciaBasica();
        fondoEfectos = new FondoEfectos(true);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULO ---
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), Color.valueOf("00ffcc"));
        Label etiquetaTitulo = new Label("CONFIGURACIÓN", estiloTitulo);
        etiquetaTitulo.setFontScale(2.5f);
        etiquetaTitulo.setAlignment(Align.center);

        // --- OPCIONES ---
        final SettingsManager settings = SettingsManager.getInstancia();

        final TextButton botonVolumen = crearBotonAnimado("VOLUMEN: " + (int)(settings.getVolumen() * 100) + "%");
        final TextButton botonControles = crearBotonAnimado("CONTROLES: " + (settings.getTipoControl() == 0 ? "CLÁSICO" : "MODERNO"));
        TextButton botonVolver = crearBotonAnimado("VOLVER");

        botonVolumen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float nuevoVolumen = settings.getVolumen() + 0.1f;
                if (nuevoVolumen > 1.05f) nuevoVolumen = 0f;
                settings.setVolumen(nuevoVolumen);
                botonVolumen.setText("VOLUMEN: " + (int)(nuevoVolumen * 100) + "%");
            }
        });

        botonControles.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                int nuevoControl = settings.getTipoControl() == 0 ? 1 : 0;
                settings.setTipoControl(nuevoControl);
                botonControles.setText("CONTROLES: " + (nuevoControl == 0 ? "CLÁSICO" : "MODERNO"));
            }
        });

        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaMenuPrincipal(juego));
            }
        });

        tabla.add(etiquetaTitulo).padBottom(50).row();
        tabla.add(botonVolumen).width(400).height(60).padBottom(20).row();
        tabla.add(botonControles).width(400).height(60).padBottom(40).row();
        tabla.add(botonVolver).width(300).height(60).row();
    }

    private TextButton crearBotonAnimado(String texto) {
        final TextButton boton = new TextButton(texto, apariencia);
        boton.setTransform(true);
        boton.setOrigin(Align.center);

        boton.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (pointer == -1) {
                    boton.clearActions();
                    boton.addAction(Actions.scaleTo(1.05f, 1.05f, 0.1f));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (pointer == -1) {
                    boton.clearActions();
                    boton.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                }
            }
        });
        return boton;
    }

    private void crearAparienciaBasica() {
        apariencia = new Skin();
        apariencia.add("btn_up", crearTexturaBoton(Color.valueOf("0a0a1a"), Color.valueOf("00ffcc"), 2));
        apariencia.add("btn_down", crearTexturaBoton(Color.valueOf("004433"), Color.valueOf("00ffcc"), 4));
        apariencia.add("btn_hover", crearTexturaBoton(Color.valueOf("1a1a3a"), Color.valueOf("ffffff"), 2));
        apariencia.add("default", juego.getFuente());

        TextButton.TextButtonStyle estiloTextoBoton = new TextButton.TextButtonStyle();
        estiloTextoBoton.up = apariencia.newDrawable("btn_up");
        estiloTextoBoton.down = apariencia.newDrawable("btn_down");
        estiloTextoBoton.over = apariencia.newDrawable("btn_hover");
        estiloTextoBoton.font = apariencia.getFont("default");
        estiloTextoBoton.fontColor = Color.valueOf("00ffcc");
        estiloTextoBoton.overFontColor = Color.WHITE;
        estiloTextoBoton.downFontColor = Color.YELLOW;

        apariencia.add("default", estiloTextoBoton);
    }

    private Texture crearTexturaBoton(Color colorFondo, Color colorBorde, int grosorBorde) {
        int ancho = 400;
        int alto = 60;
        Pixmap pixmap = new Pixmap(ancho, alto, Pixmap.Format.RGBA8888);
        pixmap.setColor(colorFondo);
        pixmap.fill();
        pixmap.setColor(colorBorde);
        for(int i=0; i<grosorBorde; i++) {
            pixmap.drawRectangle(i, i, ancho - (i*2), alto - (i*2));
        }
        Texture textura = new Texture(pixmap);
        pixmap.dispose();
        return textura;
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        juego.getLote().begin();
        fondoEfectos.renderizar(juego.getLote(), delta);
        juego.getLote().end();

        escenario.act(delta);
        escenario.draw();
    }

    @Override
    public void resize(int width, int height) {
        escenario.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        escenario.dispose();
        apariencia.dispose();
        fondoEfectos.dispose();
    }
}
