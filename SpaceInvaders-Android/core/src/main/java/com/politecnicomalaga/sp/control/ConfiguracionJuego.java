package com.politecnicomalaga.sp.control;

/**
 * Clase de utilidad que contiene las constantes de configuración global del juego.
 * Todos los valores de tamaño están en píxeles y las velocidades en píxeles/segundo.
 */
public class ConfiguracionJuego {
    // --- Configuración: Nave del Jugador ---
    public static final float NAVE_ANCHO = 60f;
    public static final float NAVE_ALTO = 60f;
    public static final int   NAVE_VIDAS = 3;
    public static final float NAVE_VELOCIDAD = 400f;
    public static final float NAVE_CADENCIA = 0.5f;

    // --- Configuración: Disparos del Jugador ---
    public static final float BALA_AMI_ANCHO = 15f;
    public static final float BALA_AMI_ALTO = 30f;
    public static final float BALA_AMI_VELOCIDAD = 600f;

    // --- Configuración: Enemigos y Formación (Batallón) ---
    public static final float BAT_ESPACIO_VERT = 10f;
    public static final float BAT_ESPACIO_HORIZ = 10f;
    public static final float BAT_VELOCIDAD = 80f;

    public static final float ENE_ANCHO = 50f;
    public static final float ENE_ALTO = 40f;
    public static final int   ENE_VIDAS = 1;
    public static final float ENE_CADENCIA = 1.5f;
    public static final int   ENE_PROB_DISPARO = 7;

    // --- Configuración: Disparos Enemigos ---
    public static final float BALA_ENE_ANCHO = 5f;
    public static final float BALA_ENE_ALTO = 30f;
    public static final float BALA_ENE_VELOCIDAD = 400f;

    // --- Configuración: Resolución Virtual ---
    public static final float VIRTUAL_WIDTH = 800f;
    public static final float VIRTUAL_HEIGHT = 600f;

    // --- Configuración: Power-Ups ---
    public static final float PU_PROB_DROP = 0.15f;
    public static final float PU_VELOCIDAD = 150f;
    public static final float PU_ANCHO = 30f;
    public static final float PU_ALTO = 30f;
    public static final float DURACION_POWERUP = 10f;
    public static final float VELOCIDAD_BONUS = 200f;

    // Puntuación, vidas y textura por fila (índice 0 = fila superior)
    public static final int[]    ENE_PUNTOS_FILA  = {30, 20, 15, 10};
    public static final int[]    ENE_VIDAS_FILA   = {2,  2,  1,  1};
    public static final String[] ENE_TEXTURA_FILA = {"enemigo2.png", "enemigo2.png", "enemigo1.png", "enemigo1.png"};
}
