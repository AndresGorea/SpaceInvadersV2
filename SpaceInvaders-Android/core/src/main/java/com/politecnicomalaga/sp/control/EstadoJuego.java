package com.politecnicomalaga.sp.control;

/**
 * Clase que encapsula el estado persistente del juego durante una sesión.
 * Mantiene información sobre si la partida sigue activa, la puntuación y las vidas restantes.
 */
public class EstadoJuego {
    private boolean jugando;
    private boolean pausado;
    private int puntuacion;
    private int vidas;
    private int nivel;

    /**
     * Constructor de EstadoJuego.
     * @param vidasIniciales Número de vidas con las que comienza el jugador.
     */
    public EstadoJuego(int vidasIniciales) {
        this.jugando = true;
        this.pausado = false;
        this.puntuacion = 0;
        this.vidas = vidasIniciales;
        this.nivel = 1;
    }

    public boolean isJugando() {
        return jugando;
    }

    public void setJugando(boolean jugando) {
        this.jugando = jugando;
    }

    public boolean isPausado() {
        return pausado;
    }

    public void setPausado(boolean pausado) {
        this.pausado = pausado;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Incrementa la puntuación actual.
     * @param puntos Cantidad de puntos a sumar.
     */
    public void addPuntuacion(int puntos) {
        this.puntuacion += puntos;
    }

    public int getVidas() {
        return vidas;
    }

    public void setVidas(int vidas) {
        this.vidas = vidas;
    }

    /**
     * Reduce una vida al jugador. Si las vidas llegan a cero, finaliza el juego.
     */
    public void perderVida() {
        this.vidas--;
        if (this.vidas <= 0) {
            this.vidas = 0;
            this.jugando = false;
        }
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void siguienteNivel() {
        this.nivel++;
    }
}
