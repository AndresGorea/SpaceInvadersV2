package com.politecnicomalaga.sp.model;

/**
 * Representa una barrera defensiva (búnker) que protege al jugador de los disparos enemigos.
 * Tiene una cantidad de vidas y su textura cambia según el daño recibido.
 */
public class Bunker extends Ovni {

    private int vidas;
    private int vidasMaximas;

    public Bunker(float posX, float posY, float width, float height, int vidasMaximas) {
        super(posX, posY, width, height, Estado.VIVO, Direccion.NOMOVER, "bunker_" + vidasMaximas + ".png");
        this.vidasMaximas = vidasMaximas;
        this.vidas = vidasMaximas;
    }

    /**
     * Reduce la vida del búnker en 1. 
     * Actualiza la textura basada en la vida restante o cambia el estado a MUERTO si llega a 0.
     */
    public void recibirDano() {
        if (this.getEstado() == Estado.VIVO) {
            this.vidas--;
            if (this.vidas <= 0) {
                this.setEstado(Estado.MUERTO);
            } else {
                this.setTextura("bunker_" + this.vidas + ".png");
            }
        }
    }

    /**
     * Destruye el búnker instantáneamente (ej. colisión con nave enemiga).
     */
    public void destruir() {
        this.vidas = 0;
        this.setEstado(Estado.MUERTO);
    }

    public int getVidas() {
        return vidas;
    }

    // El búnker no se mueve, ni dispara, por lo que estas implementaciones están vacías o no existen.
    @Override
    public void mover(Direccion dir, float velocidad, float delta) {
        // Un búnker no se mueve
    }
}
