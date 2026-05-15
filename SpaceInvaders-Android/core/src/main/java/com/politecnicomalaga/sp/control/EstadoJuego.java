package com.politecnicomalaga.sp.control;

public class EstadoJuego {
    private boolean jugando;
    private int puntuacion;
    private int vidas;

    public EstadoJuego(int vidasIniciales) {
        this.jugando = true;
        this.puntuacion = 0;
        this.vidas = vidasIniciales;
    }

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void addPuntuacion(int puntos) {
        this.puntuacion += puntos;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    public void perderVida() {
        this.vidas--;
        if (this.vidas <= 0) {
            this.vidas = 0;
            this.jugando = false;
        }
    }
}
