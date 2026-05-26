package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.control.EstadoJuego;
import com.politecnicomalaga.sp.control.GestorMundo;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.NaveEspecial;
import com.politecnicomalaga.sp.model.PowerUp;
import com.politecnicomalaga.sp.util.Assets;

import java.util.List;

/**
 * Clase especializada en el dibujo de las entidades del modelo en la pantalla.
 * Separa la lógica de representación visual del estado del juego.
 */
public class RenderizadorMundo {

    /**
     * Dibuja todos los objetos del mundo (naves, enemigos, disparos).
     * @param lote El SpriteBatch donde se realiza el dibujo.
     * @param mundo El gestor del mundo que contiene las entidades a dibujar.
     */
    public void renderizar(SpriteBatch lote, GestorMundo mundo) {
        Assets assets = Assets.getInstance();

        // 1. Dibujar la nave del jugador
        NaveAmi naveAmiga = mundo.getNaveAmiga();
        lote.draw(assets.getTexture(naveAmiga.getTextura()), naveAmiga.getX(), naveAmiga.getY(), naveAmiga.getWidth(), naveAmiga.getHeight());

        // 2. Dibujar el batallón de enemigos y sus proyectiles activos
        Batallon batallon = mundo.getBatallon();
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron esc : escuadrones) {
            NaveEne[] naveEnes = esc.getNavesEnemigas();
            for (NaveEne navE : naveEnes) {
                if (navE.estaVivo()) {
                    lote.draw(assets.getTexture(navE.getTextura()), navE.getX(), navE.getY(), navE.getWidth(), navE.getHeight());
                }
                List<DisparoEne> disparosEnemigos = navE.getMisDisparos();
                for (DisparoEne disEne : disparosEnemigos) {
                    lote.draw(assets.getTexture(disEne.getTextura()), disEne.getX(), disEne.getY(), disEne.getWidth(), disEne.getHeight());
                }
            }
        }

        // 3. Dibujar los proyectiles disparados por el jugador
        List<DisparoAmi> disparosAmigos = naveAmiga.getMisDisparos();
        for (DisparoAmi dispAmi : disparosAmigos) {
            if (dispAmi.estaVivo()) {
                lote.draw(assets.getTexture(dispAmi.getTextura()), dispAmi.getX(), dispAmi.getY(), dispAmi.getWidth(), dispAmi.getHeight());
            }
        }

        // 4. Dibujar los Power-ups cayendo
        List<PowerUp> powerUps = mundo.getPowerUps();
        for (PowerUp p : powerUps) {
            if (p.estaVivo()) {
                lote.draw(assets.getTexture(p.getTextura()), p.getX(), p.getY(), p.getWidth(), p.getHeight());
            }
        }

        // 5. Dibujar la nave especial
        NaveEspecial esp = mundo.getNaveEspecial();
        if (esp != null) {
            if (esp.estaVivo()) {
                lote.draw(assets.getTexture(esp.getTextura()), esp.getX(), esp.getY(), esp.getWidth(), esp.getHeight());
            }
            // Dibujar sus disparos incluso si la nave ha muerto
            for (DisparoEne d : esp.getMisDisparos()) {
                lote.draw(assets.getTexture(d.getTextura()), d.getX(), d.getY(), d.getWidth(), d.getHeight());
            }
        }
    }

    /**
     * Dibuja la interfaz de usuario superpuesta al juego.
     * @param lote El SpriteBatch para el dibujo.
     * @param estado Estado actual del juego (puntos, vidas).
     * @param mundo Gestor del mundo para acceder a la nave y sus power-ups.
     * @param fuente Fuente para el texto.
     * @param anchoPantalla Ancho para cálculos de posición.
     * @param altoPantalla Alto para cálculos de posición.
     */
    public void renderizarHUD(SpriteBatch lote, EstadoJuego estado, GestorMundo mundo, BitmapFont fuente, float anchoPantalla, float altoPantalla) {
        Assets assets = Assets.getInstance();
        fuente.setColor(Color.WHITE);

        // Dibujar texto de puntuación en la esquina superior izquierda
        fuente.draw(lote, "Puntuación: " + estado.getPuntuacion(), 20, altoPantalla - 20);

        // Dibujar Power-ups activos debajo de la puntuación
        NaveAmi nave = mundo.getNaveAmiga();
        float yActual = altoPantalla - 60;
        float escalaOriginal = fuente.getScaleX();
        fuente.getData().setScale(1.2f); // Fuente algo más pequeña para los timers

        if (nave.getTiempoTripleDisparo() > 0) {
            fuente.draw(lote, "Triple Disparo: " + (int)nave.getTiempoTripleDisparo() + "s", 20, yActual);
            yActual -= 30;
        }
        if (nave.getTiempoEscudo() > 0) {
            fuente.draw(lote, "Escudo: " + (int)nave.getTiempoEscudo() + "s", 20, yActual);
            yActual -= 30;
        }
        if (nave.getTiempoVelocidad() > 0) {
            fuente.draw(lote, "Velocidad: " + (int)nave.getTiempoVelocidad() + "s", 20, yActual);
        }
        fuente.getData().setScale(escalaOriginal); // Restaurar escala

        // Dibujar iconos de naves representando las vidas en la esquina superior derecha
        float tamanoIcono = 30f;
        float margen = 10f;
        for (int i = 0; i < estado.getVidas(); i++) {
            lote.draw(assets.getTexture("naveJugador.png"),
                anchoPantalla - (i + 1) * (tamanoIcono + margen) - 10,
                altoPantalla - tamanoIcono - 15,
                tamanoIcono, tamanoIcono);
        }
    }
}
