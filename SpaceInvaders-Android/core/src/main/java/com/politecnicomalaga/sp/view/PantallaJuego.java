package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import com.politecnicomalaga.sp.Main;
import com.politecnicomalaga.sp.control.Controlador;

/**
 * Representa la pantalla principal donde ocurre la acción del juego.
 * Implementa la interfaz {@link com.badlogic.gdx.Screen} para integrarse en el ciclo de vida de LibGDX.
 */
public class PantallaJuego implements Screen {

    private final Main juego;
    private float anchoPantalla, altoPantalla;
    private float x, y;

    public PantallaJuego(Main juego) {
        this.juego = juego;
        anchoPantalla = Gdx.graphics.getWidth();
        altoPantalla = Gdx.graphics.getHeight();
    }

    @Override
    public void show() {
        // Se asegura de que no haya procesadores de entrada residuales de otras pantallas
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Bucle de renderizado de la pantalla de juego (se ejecuta muchas veces por segundo).
     * @param delta Tiempo en segundos transcurrido desde el último frame.
     */
    @Override
    public void render(float delta) {
        // Limpieza de pantalla con un color oscuro
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // 1. Capturar entrada del usuario
        if(Gdx.input.justTouched()){
            x = Gdx.input.getX();
            y = Gdx.input.getY();
            Controlador.getInstancia().click(x, y);
        }

        // 2. Simular el paso del tiempo en el modelo del juego
        Controlador.getInstancia().simulaMundo(anchoPantalla, altoPantalla, delta);

        // 3. Dibujar todos los elementos (Mundo y HUD)
        juego.getLote().begin();
        Controlador.getInstancia().pintar(juego.getLote());
        Controlador.getInstancia().pintarHUD(juego.getLote(), juego.getFuente(), anchoPantalla, altoPantalla);
        juego.getLote().end();
    }

    @Override
    public void resize(int width, int height) {
        // Actualizar dimensiones si la ventana cambia de tamaño
        anchoPantalla = width;
        altoPantalla = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // No disposing here as resources are managed by Main/Controlador mostly for now.
    }
}
