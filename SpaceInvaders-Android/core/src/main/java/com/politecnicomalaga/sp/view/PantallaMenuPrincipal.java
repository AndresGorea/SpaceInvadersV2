package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.util.Recursos;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de inicio del juego que presenta el menú principal
 * Incluye efectos visuales de fondo como estrellas parallax y entidades flotantes.
 */
public class PantallaMenuPrincipal implements Screen {

    private final Main juego;
    private Stage escenario; // Contenedor para actores de la UI (Scene2D)
    private Skin apariencia;

    // Elementos visuales de fondo
    private List<Estrella> estrellas;
    private Texture texturaEstrella1, texturaEstrella2, texturaEstrella3;
    private List<EntidadFlotante> entidadesFlotantes;

    public PantallaMenuPrincipal(final Main juego) {
        this.juego = juego;
        escenario = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(escenario);

        // Inicialización de estética y elementos de fondo
        crearAparienciaBasica();
        crearEstrellas();
        crearEntidadesFlotantes();

        // Estructura de la interfaz mediante una tabla de Scene2D
        Table tabla = new Table();
        tabla.setFillParent(true);
        escenario.addActor(tabla);

        // --- TÍTULO ---
        Label.LabelStyle estiloTitulo = new Label.LabelStyle(juego.getFuente(), Color.valueOf("00ffcc"));
        Label etiquetaTitulo = new Label("SPACE INVADERS", estiloTitulo);
        etiquetaTitulo.setFontScale(3.0f);
        etiquetaTitulo.setAlignment(Align.center);
        etiquetaTitulo.setOrigin(Align.center);

        // Animación de escala pulsante para el título
        etiquetaTitulo.addAction(Actions.forever(
            Actions.sequence(
                Actions.scaleTo(3.2f, 3.2f, 1.5f),
                Actions.scaleTo(2.8f, 2.8f, 1.5f)
            )
        ));

        // --- BOTONES ---
        TextButton botonIniciar = crearBotonAnimado("INICIAR");
        TextButton botonOpciones = crearBotonAnimado("CONFIGURACIÓN");
        TextButton botonInfo = crearBotonAnimado("INFORMACIÓN");
        TextButton botonSalir = crearBotonAnimado("SALIR");

        // Acción para el botón Iniciar: transitar a la pantalla de juego
        botonIniciar.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                escenario.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            juego.setScreen(new PantallaJuego(juego));
                        }
                    })
                ));
            }
        });

        // Acción para el botón Salir: cerrar la aplicación
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

        // Configuración del layout de la tabla
        tabla.add(etiquetaTitulo).padBottom(5).row();
        tabla.add(new Label("EDICIÓN ARCADE", new Label.LabelStyle(juego.getFuente(), Color.valueOf("aaaaaa")))).padBottom(50).row();

        float anchoBoton = 300f;
        float altoBoton = 60f;
        float rellenoBoton = 10f;

        tabla.add(botonIniciar).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonOpciones).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonInfo).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
        tabla.add(botonSalir).width(anchoBoton).height(altoBoton).pad(rellenoBoton).row();
    }

    /**
     * Crea un botón con comportamientos reactivos al pasar el ratón o hacer click.
     */
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

    /**
     * Dibuja los elementos estéticos de fondo (estrellas y naves flotantes).
     */
    private void actualizarYDibujarFondo(float delta) {
        juego.getLote().begin();

        // Efecto de estrellas con tres niveles de profundidad (parallax)
        for (Estrella estrella : estrellas) {
            estrella.y -= estrella.velocidad * delta;
            if (estrella.y < 0) {
                estrella.y = Gdx.graphics.getHeight();
                estrella.x = MathUtils.random(0, Gdx.graphics.getWidth());
            }

            Texture tex = (estrella.capa == 1) ? texturaEstrella1 : (estrella.capa == 2 ? texturaEstrella2 : texturaEstrella3);
            juego.getLote().draw(tex, estrella.x, estrella.y);
        }

        // Dibujar naves enemigas/jugador que flotan de forma aleatoria
        for (EntidadFlotante entidad : entidadesFlotantes) {
            entidad.actualizar(delta);
            juego.getLote().draw(
                entidad.regionTextura,
                entidad.x, entidad.y,
                entidad.ancho / 2f, entidad.alto / 2f,
                entidad.ancho, entidad.alto,
                1f, 1f,
                entidad.rotacion
            );
        }

        juego.getLote().end();
    }

    /**
     * Define la apariencia visual de los componentes de la interfaz.
     */
    private void crearAparienciaBasica() {
        apariencia = new Skin();

        // Estilo de botón Cyberpunk/Retro
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

    /**
     * Genera una textura procedimental para los botones.
     */
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

    /**
     * Genera el conjunto inicial de estrellas para el fondo.
     */
    private void crearEstrellas() {
        estrellas = new ArrayList<>();

        // 3 capas de estrellas parallax
        texturaEstrella1 = crearTexturaEstrella(1, Color.valueOf("444444"));
        texturaEstrella2 = crearTexturaEstrella(2, Color.valueOf("888888"));
        texturaEstrella3 = crearTexturaEstrella(3, Color.valueOf("ffffff"));

        for (int i = 0; i < 80; i++) estrellas.add(new Estrella(1));
        for (int i = 0; i < 40; i++) estrellas.add(new Estrella(2));
        for (int i = 0; i < 20; i++) estrellas.add(new Estrella(3));
    }

    /**
     * Crea una textura pequeña para representar una estrella.
     */
    private Texture crearTexturaEstrella(int tamano, Color color) {
        Pixmap pixmap = new Pixmap(tamano, tamano, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    /**
     * Inicializa las naves que flotan decorativamente en el menú.
     */
    private void crearEntidadesFlotantes() {
        entidadesFlotantes = new ArrayList<>();
        String[] texturas = {"enemigo1.png", "enemigo2.png", "naveJugador.png"};

        for (int i = 0; i < 4; i++) {
            String nombreTex = texturas[MathUtils.random(0, texturas.length - 1)];
            Texture tex = Recursos.getInstancia().getTextura(nombreTex);
            entidadesFlotantes.add(new EntidadFlotante(new TextureRegion(tex)));
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(escenario);
        // Efecto de aparición (fade in)
        escenario.getRoot().getColor().a = 0;
        escenario.addAction(Actions.fadeIn(0.5f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.05f, 1); // Negro de espacio profundo
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        actualizarYDibujarFondo(delta);

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
        texturaEstrella1.dispose();
        texturaEstrella2.dispose();
        texturaEstrella3.dispose();
    }

    // --- Clases Auxiliares ---

    /**
     * Representa una estrella individual en el fondo con efecto parallax.
     */
    private class Estrella {
        float x, y, velocidad;
        int capa;

        /**
         * Crea una estrella en una capa específica con velocidad aleatoria.
         * @param capa Nivel de profundidad (1 lejos, 3 cerca).
         */
        public Estrella(int capa) {
            this.capa = capa;
            this.x = MathUtils.random(0, Gdx.graphics.getWidth());
            this.y = MathUtils.random(0, Gdx.graphics.getHeight());

            // La velocidad depende de la capa para crear el efecto de profundidad
            if (capa == 1) velocidad = MathUtils.random(10, 30);
            else if (capa == 2) velocidad = MathUtils.random(40, 70);
            else velocidad = MathUtils.random(90, 150);
        }
    }

    /**
     * Entidad decorativa que flota en el menú con movimiento sinusoidal y rotación.
     */
    private class EntidadFlotante {
        TextureRegion regionTextura;
        float x, y, velocidadX;
        float ancho, alto;
        float rotacion, velocidadRotacion;
        float baseY, tiempoSeno, amplitudSeno, frecuenciaSeno;

        public EntidadFlotante(TextureRegion regionTextura) {
            this.regionTextura = regionTextura;
            this.ancho = 60f;
            this.alto = 60f;
            reiniciarPosicion();
        }

        /**
         * Actualiza la posición y rotación de la entidad.
         */
        public void actualizar(float delta) {
            x += velocidadX * delta;

            // Movimiento oscilante en el eje Y usando la función seno
            tiempoSeno += delta;
            y = baseY + MathUtils.sin(tiempoSeno * frecuenciaSeno) * amplitudSeno;

            // Rotación constante
            rotacion += velocidadRotacion * delta;

            // Si sale de la pantalla por los lados, se reinicia
            if (x > Gdx.graphics.getWidth() + 100 || x < -100) {
                reiniciarPosicion();
            }
        }

        /**
         * Reinicia la entidad con valores aleatorios para variar el movimiento.
         */
        private void reiniciarPosicion() {
            // Decide si aparece por la izquierda o por la derecha
            if (MathUtils.randomBoolean()) {
                x = -ancho;
                velocidadX = MathUtils.random(40, 120);
            } else {
                x = Gdx.graphics.getWidth();
                velocidadX = MathUtils.random(-120, -40);
            }
            
            // Valores para la oscilación sinusoidal
            baseY = MathUtils.random(50, Gdx.graphics.getHeight() - 50);
            tiempoSeno = MathUtils.random(0, 10);
            amplitudSeno = MathUtils.random(20, 80);
            frecuenciaSeno = MathUtils.random(1f, 3f);

            // Valores para la rotación
            rotacion = MathUtils.random(0, 360);
            velocidadRotacion = MathUtils.random(-50, 50);
        }
    }
}
