package com.politecnicomalaga.sp.model;

/**
 * Clase abstracta que representa cualquier proyectil del juego.
 * Hereda de Ovni para tener posición, dimensiones y textura.
 */

public abstract class Disparo extends Ovni {
    //No tiene atributos nuevos
    public Disparo( float x, float y, float width, float height, Estado estado, Direccion dir, String textura){
        super(x, y, width, height, estado, dir, textura);
    }

    //Métodos de para comprobar si debe ser eliminado, establece el estado a MUERTO y se implementa en sus clases hijas de formas diferentes
    public abstract void desaparecer(float limite);
}
