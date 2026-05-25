package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.math.MathUtils;

public class EfectosCamara {

    private static EfectosCamara instancia;

    private float tiempoShake    = 0f;
    private float magnitudShake  = 0f;
    private float tiempoHitStop  = 0f;

    private EfectosCamara() {}

    public static EfectosCamara getInstancia() {
        if (instancia == null) instancia = new EfectosCamara();
        return instancia;
    }

    /** Activa el temblor de cámara */
    public void shake(float magnitud, float duracion) {
        this.magnitudShake = magnitud;
        this.tiempoShake   = duracion;
    }

    /** Activa la micro-pausa de juego */
    public void hitStop(float duracion) {
        this.tiempoHitStop = duracion;
    }

    /** Llamar cada frame desde PantallaJuego */
    public void actualizar(float delta) {
        if (tiempoShake   > 0) tiempoShake   -= delta;
        if (tiempoHitStop > 0) tiempoHitStop -= delta;
    }

    public boolean isShakeActivo()   { return tiempoShake   > 0; }
    public boolean isHitStopActivo() { return tiempoHitStop > 0; }

    public float getOffsetX() {
        return isShakeActivo() ? MathUtils.random(-magnitudShake, magnitudShake) : 0f;
    }
    public float getOffsetY() {
        return isShakeActivo() ? MathUtils.random(-magnitudShake, magnitudShake) : 0f;
    }
}
