package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.ConfiguracionJuego;
import com.politecnicomalaga.sp.control.Controlador;
import com.politecnicomalaga.sp.control.EfectosCamara;
import com.politecnicomalaga.sp.model.Ovni;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Incluye el fondo animado compartido y la lógica de entidades.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private final Viewport viewport;
    private final OrthographicCamera camara;

    // Gestor de efectos visuales de fondo (reutilizable)
    private FondoEfectos fondoEfectos;

    private OrthographicCamera camara;
    private OrthographicCamera camaraHUD;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        camara = new OrthographicCamera();
        viewport = new ExtendViewport(ConfiguracionJuego.VIRTUAL_WIDTH, ConfiguracionJuego.VIRTUAL_HEIGHT, camara);
        // Desactivamos los ovnis flotantes en el fondo durante el juego
        fondoEfectos = new FondoEfectos(false);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void render(float delta) {
        // Fondo negro profundo para el espacio
        ScreenUtils.clear(0.01f, 0.01f, 0.05f, 1f);

        float mundoAncho = viewport.getWorldWidth();
        float mundoAlto = viewport.getWorldHeight();

        // 0. Gestión de Pausa (Tecla P)
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            boolean pausadoActual = Controlador.getInstancia().getEstadoJuego().isPausado();
            Controlador.getInstancia().getEstadoJuego().setPausado(!pausadoActual);
        }

        // 1. Entrada
        if (Controlador.getInstancia().esAndroid()) {
            float gWidth = Gdx.graphics.getWidth();
            float gHeight = Gdx.graphics.getHeight();

            // Zonas de botones Android (usando coordenadas físicas de pantalla para consistencia de botones táctiles)
            boolean tocandoIzq   = Gdx.input.isTouched() &&
                                   Gdx.input.getX() < gWidth * 0.2f &&
                                   Gdx.input.getY() > gHeight * 0.85f;
            boolean tocandoDer   = Gdx.input.isTouched() &&
                                   Gdx.input.getX() > gWidth * 0.2f && Gdx.input.getX() < gWidth * 0.5f &&
                                   Gdx.input.getY() > gHeight * 0.85f;
            boolean tocandoFire  = Gdx.input.justTouched() &&
                                   Gdx.input.getX() > gWidth * 0.8f &&
                                   Gdx.input.getY() > gHeight * 0.85f;

            if (tocandoIzq) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.IZQUIERDA);
            } else if (tocandoDer) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.DERECHA);
            } else if (!Gdx.input.isTouched()) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.NOMOVER);
            }
            if (tocandoFire) {
                Controlador.getInstancia().dispararNaveAmiga();
            }
        } else {
            // Click en PC
            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                viewport.unproject(touchPos);
                Controlador.getInstancia().click(touchPos.x, touchPos.y);
            }
        }

        // Controles de teclado (solo PC)
        if (!Controlador.getInstancia().esAndroid()) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.IZQUIERDA);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.DERECHA);
            } else if (!Gdx.input.justTouched() && !Gdx.input.isTouched()) {
                Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.NOMOVER);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                Controlador.getInstancia().dispararNaveAmiga();
            }
        }

        // 2. Lógica física
        Controlador.getInstancia().simulaMundo(mundoAncho, mundoAlto, delta);

        // Actualizar efectos de cámara
        EfectosCamara.getInstancia().actualizar(delta);

        // Aplicar shake si está activo
        camara.position.set(
            anchoPantalla / 2f + EfectosCamara.getInstancia().getOffsetX(),
            altoPantalla  / 2f + EfectosCamara.getInstancia().getOffsetY(),
            0
        );
        camara.update();
        juego.getLote().setProjectionMatrix(camara.combined);

        // 3. Renderizado
        camara.update();
        juego.getLote().setProjectionMatrix(camara.combined);
        juego.getLote().begin();

        // El fondo se dibuja primero para que quede detrás de las naves
        fondoEfectos.renderizar(juego.getLote(), delta);

        Controlador.getInstancia().pintar(juego.getLote());
        Controlador.getInstancia().pintarHUD(juego.getLote(), juego.getFuente(), mundoAncho, mundoAlto);

        // Mostrar texto de pausa si está activada
        if (Controlador.getInstancia().getEstadoJuego().isPausado()) {
            juego.getFuente().setColor(Color.YELLOW);
            juego.getFuente().draw(juego.getLote(), "PAUSA", mundoAncho / 2f - 50, mundoAlto / 2f);
        }


        // Dibujar botones táctiles en Android
        if (Controlador.getInstancia().esAndroid()) {
            Controlador.getInstancia().pintarBotonesAndroid(
                juego.getLote(), juego.getFuente(), mundoAncho, mundoAlto);
        }

        juego.getLote().end();

        // 4. Comprobar fin de juego
        if (!Controlador.getInstancia().getEstadoJuego().isJugando()) {
            boolean victoria = Controlador.getInstancia().getEstadoJuego().getVidas() > 0;
            int puntuacion = Controlador.getInstancia().getEstadoJuego().getPuntuacion();
            juego.setScreen(new PantallaGameOver(juego, victoria, puntuacion));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        fondoEfectos.dispose();
    }
}
