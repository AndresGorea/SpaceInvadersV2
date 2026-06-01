package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.ConfiguracionJuego;
import com.politecnicomalaga.sp.control.Controlador;
import com.politecnicomalaga.sp.util.SettingsManager;

/**
 * Pantalla que se muestra al finalizar el juego (Victoria o Derrota).
 * Utiliza Scene2D para una interfaz pulida y animada similar al menú principal.
 */
public class PantallaGameOver implements Screen {

    private final Main juego;
    private final boolean victoria;

    private final Stage escenario;
    private Skin apariencia;
    private final FondoEfectos fondoEfectos;

    public PantallaGameOver(final Main juego, boolean victoria, int puntuacion) {
        this.juego = juego;
        this.victoria = victoria;

        // Actualizar la puntuación máxima si es necesario
        SettingsManager settings = SettingsManager.getInstancia();
        settings.setHighScore(puntuacion);
        int highScore = settings.getHighScore();

        escenario = new Stage(new ExtendViewport(ConfiguracionJuego.VIRTUAL_WIDTH, ConfiguracionJuego.VIRTUAL_HEIGHT));
        Gdx.input.setInputProcessor(escenario);

        crearAparienciaBasica();
        fondoEfectos = new FondoEfectos(true);

        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULO ---
        Color colorTitulo = victoria ? Color.valueOf("00ff00") : Color.valueOf("ff0000");
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), colorTitulo);
        String textoTitulo = victoria ? "¡VICTORIA!" : "GAME OVER";
        Label etiquetaTitulo = new Label(textoTitulo, estiloTitulo);
        etiquetaTitulo.setFontScale(3.5f);
        etiquetaTitulo.setAlignment(Align.center);
        etiquetaTitulo.setOrigin(Align.center);

        // Animación de escala para el título
        etiquetaTitulo.addAction(Actions.forever(
            Actions.sequence(
                Actions.scaleTo(1.1f, 1.1f, 1.2f),
                Actions.scaleTo(0.9f, 0.9f, 1.2f)
            )
        ));

        // --- PUNTUACIÓN (Sin cuadrado/fondo) ---
        Label.LabelStyle estiloPuntos = new Label.LabelStyle(juego.getFuente(), Color.WHITE);
        Label etiquetaPuntos = new Label("PUNTUACIÓN FINAL: " + puntuacion, estiloPuntos);
        etiquetaPuntos.setFontScale(1.5f);
        etiquetaPuntos.setAlignment(Align.center);

        Label etiquetaHighScore = new Label("MEJOR PUNTUACIÓN: " + highScore, estiloPuntos);
        etiquetaHighScore.setFontScale(1.2f);
        etiquetaHighScore.setColor(Color.YELLOW);
        etiquetaHighScore.setAlignment(Align.center);

        // --- BOTONES ---
        TextButton botonReintentar = crearBotonAnimado("REINTENTAR");
        TextButton botonMenu = crearBotonAnimado("VOLVER AL MENÚ");

        botonReintentar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Controlador.getInstancia().reiniciar();
                            juego.setScreen(new PantallaJuego(juego));
                        }
                    })
                ));
            }
        });

        botonMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            juego.setScreen(new PantallaMenuPrincipal(juego));
                        }
                    })
                ));
            }
        });

        // Configuración de la tabla
        tabla.add(etiquetaTitulo).padBottom(30).row();
        tabla.add(etiquetaPuntos).padBottom(10).row();
        tabla.add(etiquetaHighScore).padBottom(40).row();
        tabla.add(botonReintentar).width(350).height(70).padBottom(15).row();
        tabla.add(botonMenu).width(350).height(70).row();
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
        // Colores temáticos según victoria o derrota
        Color principal = victoria ? Color.valueOf("00ffcc") : Color.valueOf("ff3333");
        Color fondoBtn = victoria ? Color.valueOf("002211") : Color.valueOf("220000");

        apariencia.add("btn_up", crearTexturaBoton(fondoBtn, principal, 2));
        apariencia.add("btn_down", crearTexturaBoton(principal.cpy().lerp(Color.BLACK, 0.5f), principal, 4));
        apariencia.add("btn_hover", crearTexturaBoton(fondoBtn.cpy().lerp(Color.WHITE, 0.1f), Color.WHITE, 2));
        apariencia.add("default", juego.getFuente());

        TextButton.TextButtonStyle estiloTextoBoton = new TextButton.TextButtonStyle();
        estiloTextoBoton.up = apariencia.newDrawable("btn_up");
        estiloTextoBoton.down = apariencia.newDrawable("btn_down");
        estiloTextoBoton.over = apariencia.newDrawable("btn_hover");
        estiloTextoBoton.font = apariencia.getFont("default");
        estiloTextoBoton.fontColor = principal;
        estiloTextoBoton.overFontColor = Color.WHITE;
        estiloTextoBoton.downFontColor = Color.YELLOW;

        apariencia.add("default", estiloTextoBoton);
    }

    private Texture crearTexturaBoton(Color colorFondo, Color colorBorde, int grosorBorde) {
        int ancho = 350;
        int alto = 70;
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
        escenario.getRoot().getColor().a = 0;
        escenario.addAction(Actions.fadeIn(0.6f));
    }

    @Override
    public void render(float delta) {
        // Fondo temático sutil
        if (victoria) {
            ScreenUtils.clear(0.0f, 0.1f, 0.05f, 1f);
        } else {
            ScreenUtils.clear(0.1f, 0.0f, 0.0f, 1f);
        }

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
    public void hide() {}

    @Override
    public void dispose() {
        escenario.dispose();
        apariencia.dispose();
        fondoEfectos.dispose();
    }
}
