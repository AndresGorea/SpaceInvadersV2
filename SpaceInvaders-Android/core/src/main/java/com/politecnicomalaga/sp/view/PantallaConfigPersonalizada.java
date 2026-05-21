package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.politecnicomalaga.sp.control.GestorPreferencias;

public class PantallaConfigPersonalizada implements Screen {

    private final Main juego;
    private final Stage escenario;
    private Skin apariencia;
    private final FondoEfectos fondoEfectos;
    private GestorPreferencias prefs;

    public PantallaConfigPersonalizada(final Main juego) {
        this.juego = juego;
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);
        prefs = GestorPreferencias.getInstancia();

        crearAparienciaBasica();
        fondoEfectos = new FondoEfectos(true);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULO ---
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), Color.valueOf("00ffcc"));
        Label etiquetaTitulo = new Label("VALORES PERSONALIZADOS", estiloTitulo);
        etiquetaTitulo.setFontScale(1.5f);
        etiquetaTitulo.setAlignment(Align.center);

        // --- BOTONES ---
        final TextButton botonProbDisparo = crearBotonAnimado("Prob. Disparo: x" + prefs.getMultProbDisparoPersonalizado());
        final TextButton botonVelBatallon = crearBotonAnimado("Vel. Batallon: x" + prefs.getMultVelBatallonPersonalizado());
        final TextButton botonVelBala = crearBotonAnimado("Vel. Bala Ene: x" + prefs.getMultVelBalaEnemigaPersonalizado());
        TextButton botonVolver = crearBotonAnimado("VOLVER");

        botonProbDisparo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float newVal = cycleMultiplier(prefs.getMultProbDisparoPersonalizado());
                prefs.setMultProbDisparoPersonalizado(newVal);
                botonProbDisparo.setText("Prob. Disparo: x" + newVal);
            }
        });

        botonVelBatallon.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float newVal = cycleMultiplier(prefs.getMultVelBatallonPersonalizado());
                prefs.setMultVelBatallonPersonalizado(newVal);
                botonVelBatallon.setText("Vel. Batallon: x" + newVal);
            }
        });

        botonVelBala.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                float newVal = cycleMultiplier(prefs.getMultVelBalaEnemigaPersonalizado());
                prefs.setMultVelBalaEnemigaPersonalizado(newVal);
                botonVelBala.setText("Vel. Bala Ene: x" + newVal);
            }
        });

        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                prefs.guardarPreferencias();
                escenario.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            juego.setScreen(new PantallaOpciones(juego));
                        }
                    })
                ));
            }
        });

        tabla.add(etiquetaTitulo).padBottom(40).row();

        float anchoBoton = 350f;
        float altoBoton = 60f;
        float rellenoBoton = 10f;

        tabla.add(botonProbDisparo).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonVelBatallon).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonVelBala).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonVolver).width(anchoBoton).height(altoBoton).padTop(30).row();
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
                    boton.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
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
        int ancho = 300;
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
    public void show() {
        Gdx.input.setInputProcessor(escenario);
        escenario.getRoot().getColor().a = 0;
        escenario.addAction(Actions.fadeIn(0.5f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        juego.getLote().begin();
        fondoEfectos.renderizar(juego.getLote(), delta);
        juego.getLote().end();

        escenario.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
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

    private float cycleMultiplier(float current) {
        if (current < 0.6f) return 0.75f;
        if (current < 0.8f) return 1.0f;
        if (current < 1.1f) return 1.25f;
        if (current < 1.3f) return 1.5f;
        if (current < 1.6f) return 2.0f;
        return 0.5f;
    }
}