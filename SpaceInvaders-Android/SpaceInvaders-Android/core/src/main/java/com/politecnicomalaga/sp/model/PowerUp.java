package com.politecnicomalaga.sp.model;

public class PowerUp extends Ovni {
    public enum Tipo { MULTI_DISPARO, ESCUDO, VELOCIDAD }
    private Tipo tipo;

    public PowerUp(float x, float y, float width, float height, Tipo tipo, String textura) {
        super(x, y, width, height, Estado.VIVO, Direccion.ABAJO, textura);
        this.tipo = tipo;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void desaparecer(float limiteInferior) {
        if (this.getY() < limiteInferior) {
            this.setEstado(Estado.MUERTO);
        }
    }
}
