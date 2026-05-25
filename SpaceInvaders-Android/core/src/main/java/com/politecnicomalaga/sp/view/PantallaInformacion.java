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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.util.Recursos;

public class PantallaInformacion implements Screen {

    private final Main juego;
    private final Stage escenario;
    private Skin apariencia;
    private final FondoEfectos fondoEfectos;

    public PantallaInformacion(final Main juego) {
        this.juego = juego;
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        crearAparienciaBasica();
        fondoEfectos = new FondoEfectos(true);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULOS ---
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), Color.valueOf("00ffcc"));
        Label etiquetaTitulo = new Label("INFORMACIÓN Y GUÍA", estiloTitulo);
        etiquetaTitulo.setFontScale(1.8f);
        etiquetaTitulo.setAlignment(Align.center);
        tabla.add(etiquetaTitulo).colspan(2).padBottom(30).row();

        Label.LabelStyle estiloTexto = new Label.LabelStyle(juego.getFuente(), Color.WHITE);

        // --- ENEMIGOS ---
        Label lblEnemigos = new Label("GUÍA DE NAVES:", estiloTitulo);
        tabla.add(lblEnemigos).colspan(2).padBottom(10).row();

        Image imgEne1 = new Image(Recursos.getInstancia().getTextura("enemigo1.png"));
        Label lblEne1 = new Label("Nave Élite\n100 Puntos\n3 Vidas", estiloTexto);
        lblEne1.setAlignment(Align.center);

        Image imgEne2 = new Image(Recursos.getInstancia().getTextura("enemigo2.png"));
        Label lblEne2 = new Label("Nave Básica\n20 Puntos\n1 Vida", estiloTexto);
        lblEne2.setAlignment(Align.center);

        Table tablaNaves = new Table();
        tablaNaves.add(imgEne1).size(50, 40).pad(10);
        tablaNaves.add(lblEne1).pad(10);
        tablaNaves.add(imgEne2).size(50, 40).pad(10).padLeft(30);
        tablaNaves.add(lblEne2).pad(10);

        tabla.add(tablaNaves).colspan(2).padBottom(30).row();

        // --- CONTROLES ---
        Label lblControles = new Label("CONTROLES:", estiloTitulo);
        tabla.add(lblControles).colspan(2).padBottom(10).row();

        Label lblControlesMovil = new Label("[MÓVIL]\nTocar debajo de flechas izquierdas: Mover Izquierda\nTocar debajo de flechas derechas: Mover Derecha\nDisparo: Tocar Debajo de SHOT", estiloTexto);
        lblControlesMovil.setAlignment(Align.center);

        Label lblControlesPC = new Label("[PC]\nFlechas Izq/Der A/D o Click en pantalla: Moverse\nDisparo: Pulsar espacio", estiloTexto);
        lblControlesPC.setAlignment(Align.center);

        Table tablaControles = new Table();
        tablaControles.add(lblControlesMovil).pad(10).padRight(40);
        tablaControles.add(lblControlesPC).pad(10);

        tabla.add(tablaControles).colspan(2).padBottom(40).row();

        // --- BOTÓN VOLVER ---
        TextButton botonVolver = crearBotonAnimado("VOLVER");
        botonVolver.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            juego.setScreen(new PantallaMenuPrincipal(juego));
                        }
                    })
                ));
            }
        });

        tabla.add(botonVolver).colspan(2).width(300).height(60).pad(10).row();
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
}
