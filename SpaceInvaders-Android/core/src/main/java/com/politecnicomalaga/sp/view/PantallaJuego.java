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
import com.politecnicomalaga.sp.util.SettingsManager;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Incluye el fondo animado compartido y la lógica de entidades.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private final Viewport viewport;
    private final OrthographicCamera camara;

    // Gestor de efectos visuales de fondo (reutilizable)
    private final FondoEfectos fondoEfectos;




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
        SettingsManager settings = SettingsManager.getInstancia();
        int tipoControl = settings.getTipoControl();

        if (Controlador.getInstancia().esAndroid()) {
            float gWidth = Gdx.graphics.getWidth();
            float gHeight = Gdx.graphics.getHeight();

            if (tipoControl == 1) { // MODERNO: Botones dedicados
                float btnMoverAncho = gWidth * 0.15f;
                float btnFireAncho = gWidth * 0.12f;
                float btnAlto = gHeight * 0.12f;
                float margen = 20f;

                boolean tocandoIzq = false;
                boolean tocandoDer = false;
                boolean tocandoFire = false;

                // Comprobar múltiples toques para permitir mover y disparar simultáneamente
                for (int i = 0; i < 5; i++) {
                    if (Gdx.input.isTouched(i)) {
                        float touchX = Gdx.input.getX(i);
                        float touchY = Gdx.input.getY(i);

                        // Detección con margen: Y está entre gHeight - btnAlto - margen y gHeight - margen
                        if (touchY > gHeight - btnAlto - margen && touchY < gHeight - margen) {
                            // Izquierda
                            if (touchX > margen && touchX < btnMoverAncho + margen) {
                                tocandoIzq = true;
                            }
                            // Derecha
                            else if (touchX > btnMoverAncho + margen + 10 && touchX < (btnMoverAncho * 2) + margen + 10) {
                                tocandoDer = true;
                            }
                            // Fuego (esquina derecha)
                            else if (touchX > gWidth - btnFireAncho - margen && touchX < gWidth - margen) {
                                tocandoFire = true;
                            }
                        }
                    }
                }

                if (tocandoIzq) {
                    Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.IZQUIERDA);
                } else if (tocandoDer) {
                    Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.DERECHA);
                } else {
                    Controlador.getInstancia().moverNaveAmiga(Ovni.Direccion.NOMOVER);
                }

                if (tocandoFire) {
                    Controlador.getInstancia().dispararNaveAmiga();
                }
            } else { // CLÁSICO: Toque en pantalla para girar y disparo automático o toque arriba
                if (Gdx.input.justTouched()) {
                    int x = Gdx.input.getX();
                    int y = Gdx.input.getY();
                    if (y < gHeight * 0.2f) { // Toque en la parte superior para disparar
                        Controlador.getInstancia().dispararNaveAmiga();
                    } else {
                        Vector3 touchPos = new Vector3(x, y, 0);
                        viewport.unproject(touchPos);
                        Controlador.getInstancia().click(touchPos.x, touchPos.y);
                    }
                }
            }
        } else {
            // Click en PC
            if (Gdx.input.justTouched()) {
                Vector3 touchPos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
                viewport.unproject(touchPos);
                Controlador.getInstancia().click(touchPos.x, touchPos.y);
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
        Controlador.getInstancia().simulaMundo(mundoAncho, mundoAlto, delta);

        // Actualizar efectos de cámara
        EfectosCamara.getInstancia().actualizar(delta);

        // Aplicar shake si está activo
        camara.position.set(
            mundoAncho / 2f + EfectosCamara.getInstancia().getOffsetX(),
            mundoAlto  / 2f + EfectosCamara.getInstancia().getOffsetY(),
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


        // Dibujar botones táctiles en Android (solo si el control es Moderno)
        if (Controlador.getInstancia().esAndroid() && tipoControl == 1) {
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
