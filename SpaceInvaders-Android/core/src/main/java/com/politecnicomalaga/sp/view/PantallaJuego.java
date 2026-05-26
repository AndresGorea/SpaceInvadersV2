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

    // Objeto temporal para cálculos táctiles (evita creación de objetos en el render)
    private final Vector3 tempTouch = new Vector3();




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
            if (tipoControl == 1) { // MODERNO: Botones dedicados
                float btnMoverAncho = mundoAncho * ConfiguracionJuego.BTN_MOVER_ANCHO_PORCENTAJE;
                float btnFireAncho = mundoAncho * ConfiguracionJuego.BTN_FIRE_ANCHO_PORCENTAJE;
                float btnAlto = mundoAlto * ConfiguracionJuego.BTN_ALTO_PORCENTAJE;
                float btnFireAlto = mundoAlto * ConfiguracionJuego.BTN_FIRE_ALTO_PORCENTAJE;
                float margen = ConfiguracionJuego.BTN_MARGEN;

                boolean tocandoIzq = false;
                boolean tocandoDer = false;
                boolean tocandoFire = false;

                // Comprobar múltiples toques para permitir mover y disparar simultáneamente
                for (int i = 0; i < 5; i++) {
                    if (Gdx.input.isTouched(i)) {
                        tempTouch.set(Gdx.input.getX(i), Gdx.input.getY(i), 0);
                        viewport.unproject(tempTouch);
                        float touchX = tempTouch.x;
                        float touchY = tempTouch.y;

                        // Detección en coordenadas del mundo
                        // Botones Izquierda y Derecha (comparten la misma altura btnAlto)
                        if (touchY > margen && touchY < margen + btnAlto) {
                            // Izquierda
                            if (touchX > margen && touchX < btnMoverAncho + margen) {
                                tocandoIzq = true;
                            }
                            // Derecha
                            else if (touchX > btnMoverAncho + margen + 10 && touchX < (btnMoverAncho * 2) + margen + 10) {
                                tocandoDer = true;
                            }
                        }

                        // Botón de Fuego (usa btnFireAlto)
                        if (touchY > margen && touchY < margen + btnFireAlto) {
                            // Fuego (esquina derecha)
                            if (touchX > mundoAncho - btnFireAncho - margen && touchX < mundoAncho - margen) {
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
                    float gHeight = Gdx.graphics.getHeight();
                    if (y < gHeight * 0.2f) { // Toque en la parte superior para disparar
                        Controlador.getInstancia().dispararNaveAmiga();
                    } else {
                        tempTouch.set(x, y, 0);
                        viewport.unproject(tempTouch);
                        Controlador.getInstancia().click(tempTouch.x, tempTouch.y);
                    }
                }
            }
        } else {
            // Click en PC
            if (Gdx.input.justTouched()) {
                tempTouch.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                viewport.unproject(tempTouch);
                Controlador.getInstancia().click(tempTouch.x, tempTouch.y);
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
