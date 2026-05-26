package com.politecnicomalaga.sp.model;

import com.politecnicomalaga.sp.control.ConfiguracionJuego;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una nave especial que aparece raramente, cruza la pantalla horizontalmente
 * y ataca al jugador. Otorga una puntuación extra al ser destruida.
 */
public class NaveEspecial extends Nave {
    private float velocidad;
    private List<DisparoEne> misDisparos;

    public NaveEspecial(float x, float y, float width, float height, Direccion dir, float velocidad, String textura) {
        super(x, y, width, height, Estado.VIVO, dir, textura, 1, 1f,
              ConfiguracionJuego.BALA_ENE_ANCHO, ConfiguracionJuego.BALA_ENE_ALTO, ConfiguracionJuego.BALA_ENE_VELOCIDAD);
        this.velocidad = velocidad;
        this.misDisparos = new ArrayList<>();
    }

    /**
     * Actualiza el movimiento de la nave especial.
     * @param delta Tiempo transcurrido.
     */
    public void actualizar(float delta) {
        mover(getDir(), velocidad, delta);
    }

    @Override
    public void disparar() {
        if (estaVivo()) {
            float posX = getX() + getMitadWidth() - (getAnchoBala() / 2f);
            float posY = getY() - getMitadHeight();
            misDisparos.add(new DisparoEne(posX, posY, getAnchoBala(), getAltoBala(), Estado.VIVO, Direccion.ABAJO, "disparoEne.png"));
        }
    }

    @Override
    public void gestionarMisDisparos(float limiteInferior, float delta) {
        for (int i = misDisparos.size() - 1; i >= 0; i--) {
            DisparoEne d = misDisparos.get(i);
            if (d.getEstado() == Estado.VIVO) {
                d.mover(Direccion.ABAJO, getVelocidadBala(), delta);
                d.desaparecer(limiteInferior);
            }
            if (d.getEstado() == Estado.MUERTO) {
                misDisparos.remove(i);
            }
        }
    }

    public List<DisparoEne> getMisDisparos() {
        return misDisparos;
    }

    /**
     * Comprueba si la nave ha salido de los límites de la pantalla.
     * @param anchoMundo Ancho del mundo virtual.
     * @return true si ha salido.
     */
    public boolean haSalido(float anchoMundo) {
        return (getDir() == Direccion.DERECHA && getX() > anchoMundo) ||
               (getDir() == Direccion.IZQUIERDA && getX() < -getWidth());
    }
}
