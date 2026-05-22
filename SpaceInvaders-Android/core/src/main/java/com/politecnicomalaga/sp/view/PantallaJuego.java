package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.util.SettingsManager;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Incluye el fondo animado compartido y la lógica de entidades.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private float anchoPantalla, altoPantalla;
    private float x, y;
    private float btnAncho, btnAlto;

    // Gestor de efectos visuales de fondo (reutilizable)
    private FondoEfectos fondoEfectos;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
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

        // 0. Gestión de Pausa (Tecla P)
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            boolean pausadoActual = Controlador.getInstancia().getEstadoJuego().isPausado();
            Controlador.getInstancia().getEstadoJuego().setPausado(!pausadoActual);
        }

        // 1. Entrada
        SettingsManager settings = SettingsManager.getInstancia();
        int tipoControl = settings.getTipoControl();

        if (Controlador.getInstancia().esAndroid()) {
            if (tipoControl == 1) { // MODERNO: Botones dedicados
                btnAncho = anchoPantalla * 0.2f;
                btnAlto = altoPantalla * 0.15f;

                boolean tocandoIzq = Gdx.input.isTouched() &&
                        Gdx.input.getX() < btnAncho &&
                        Gdx.input.getY() > altoPantalla - btnAlto;
                boolean tocandoDer = Gdx.input.isTouched() &&
                        Gdx.input.getX() > btnAncho && Gdx.input.getX() < btnAncho * 2.5f &&
                        Gdx.input.getY() > altoPantalla - btnAlto;
                boolean tocandoFire = Gdx.input.justTouched() &&
                        Gdx.input.getX() > anchoPantalla - btnAncho &&
                        Gdx.input.getY() > altoPantalla - btnAlto;

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
            } else { // CLÁSICO: Toque en pantalla para girar y disparo automático o toque arriba
                if (Gdx.input.justTouched()) {
                    x = Gdx.input.getX();
                    y = Gdx.input.getY();
                    if (y < altoPantalla * 0.2f) { // Toque en la parte superior para disparar
                        Controlador.getInstancia().dispararNaveAmiga();
                    } else {
                        Controlador.getInstancia().click(x, y);
                    }
                }
            }
        } else {
            // PC: Toque para cambiar de sentido (opcional) o teclado
            if (Gdx.input.justTouched()) {
                x = Gdx.input.getX();
                y = Gdx.input.getY();
                Controlador.getInstancia().click(x, y);
            }
        }

        // Controles de teclado (siempre activos en PC)
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
        Controlador.getInstancia().simulaMundo(anchoPantalla, altoPantalla, delta);

        // 3. Renderizado
        juego.getLote().begin();

        // El fondo se dibuja primero para que quede detrás de las naves
        fondoEfectos.renderizar(juego.getLote(), delta);

        Controlador.getInstancia().pintar(juego.getLote());
        Controlador.getInstancia().pintarHUD(juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);

        // Mostrar texto de pausa si está activada
        if (Controlador.getInstancia().getEstadoJuego().isPausado()) {
            juego.getFuente().setColor(Color.YELLOW);
            juego.getFuente().draw(juego.getLote(), "PAUSA", anchoPantalla / 2f - 50, altoPantalla / 2f);
        }


        // Dibujar botones táctiles en Android (solo si el control es Moderno)
        if (Controlador.getInstancia().esAndroid() && tipoControl == 1) {
            Controlador.getInstancia().pintarBotonesAndroid(
                juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);
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
        anchoPantalla = width;
        altoPantalla = height;
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
