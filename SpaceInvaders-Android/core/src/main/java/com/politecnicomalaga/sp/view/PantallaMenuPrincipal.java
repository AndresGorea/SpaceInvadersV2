package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.ConfiguracionJuego;
import com.politecnicomalaga.sp.control.Controlador;
import com.politecnicomalaga.sp.util.Assets;
import com.politecnicomalaga.sp.util.SettingsManager;

/**
 * Pantalla de inicio del juego que presenta el menú principal.
 * Utiliza FondoEfectos para el fondo animado y Scene2D para la interfaz.
 */
public class PantallaMenuPrincipal implements Screen {

    private final Main juego;
    private final Stage  escenario;
    private Skin apariencia;
    private final FondoEfectos fondoEfectos;
    private Music musicaFondo;

    public PantallaMenuPrincipal(final Main juego) {
        this.juego = juego;
        escenario = new Stage(new ExtendViewport(ConfiguracionJuego.VIRTUAL_WIDTH, ConfiguracionJuego.VIRTUAL_HEIGHT));
        Gdx.input.setInputProcessor(escenario);

        crearAparienciaBasica();
        // El menú principal sí muestra los ovnis flotantes
        fondoEfectos = new FondoEfectos(true);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULO ---
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), Color.valueOf("00ffcc"));
        Label etiquetaTitulo = new Label("SPACE INVADERS", estiloTitulo);
        etiquetaTitulo.setFontScale(3.0f);
        etiquetaTitulo.setAlignment(Align.center);
        etiquetaTitulo.setOrigin(Align.center);

        etiquetaTitulo.addAction(Actions.forever(
            Actions.sequence(
                Actions.scaleTo(3.2f, 3.2f, 1.5f),
                Actions.scaleTo(2.8f, 2.8f, 1.5f)
            )
        ));

        // --- HIGH SCORE ---
        int highScore = SettingsManager.getInstancia().getHighScore();
        Label etiquetaHighScore = new Label("RECORD: " + highScore, new Label.LabelStyle(juego.getFuente(), Color.YELLOW));
        etiquetaHighScore.setFontScale(1.2f);
        etiquetaHighScore.setAlignment(Align.center);

        // --- BOTONES ---
        TextButton botonIniciar = crearBotonAnimado("INICIAR");
        TextButton botonOpciones = crearBotonAnimado("CONFIGURACIÓN");
        TextButton botonInfo = crearBotonAnimado("INFORMACIÓN");
        TextButton botonSalir = crearBotonAnimado("SALIR");

        botonIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Controlador.getInstancia().reiniciar(escenario.getViewport().getWorldWidth());
                            juego.setScreen(new PantallaJuego(juego));
                        }
                    })
                ));
            }
        });

        botonOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                juego.setScreen(new PantallaOpciones(juego));
            }
        });

        botonSalir.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Gdx.app.exit();
                        }
                    })
                ));
            }
        });

        tabla.add(etiquetaTitulo).padBottom(5).row();
        tabla.add(new Label("EDICIÓN ARCADE", new Label.LabelStyle(juego.getFuente(), Color.valueOf("aaaaaa")))).padBottom(10).row();
        tabla.add(etiquetaHighScore).padBottom(30).row();

        botonOpciones.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

        botonInfo.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            juego.setScreen(new PantallaInformacion(juego));
                        }
                    })
                ));
            }
        });

        float anchoBoton = 350f;
        float altoBoton = 60f;
        float rellenoBoton = 10f;

        tabla.add(botonIniciar).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonOpciones).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonInfo).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonSalir).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
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
        int ancho = 350;
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

        musicaFondo = Assets.getInstance().getMusic("One_Last_Quarter.mp3");
        if (musicaFondo != null) {
            musicaFondo.setVolume(0.15f);
            musicaFondo.setLooping(true);
            musicaFondo.play();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.05f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float mundoAncho = escenario.getViewport().getWorldWidth();
        float mundoAlto = escenario.getViewport().getWorldHeight();

        juego.getLote().setProjectionMatrix(escenario.getCamera().combined);
        juego.getLote().begin();
        fondoEfectos.renderizar(juego.getLote(), delta, mundoAncho, mundoAlto);
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
    public void hide() {
        if (musicaFondo != null) {
            musicaFondo.stop();
        }
    }

    @Override
    public void dispose() {
        escenario.dispose();
        apariencia.dispose();
        fondoEfectos.dispose();
    }
}
