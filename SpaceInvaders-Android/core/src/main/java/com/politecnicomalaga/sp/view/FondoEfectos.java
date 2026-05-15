package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.politecnicomalaga.sp.util.Recursos;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase encargada de gestionar y renderizar los efectos de fondo (estrellas parallax y entidades flotantes).
 * Proporciona una estética de "espacio profundo" consistente en todo el juego.
 */
public class FondoEfectos {

    // Colecciones de elementos visuales
    private List<Estrella> estrellas;
    private List<EntidadFlotante> entidadesFlotantes;
    private boolean mostrarEntidadesFlotantes;
    
    // Texturas generadas procedimentalmente para las estrellas
    private Texture texturaEstrella1, texturaEstrella2, texturaEstrella3;

    /**
     * Inicializa los efectos de fondo creando las estrellas y entidades decorativas.
     */
    public FondoEfectos(boolean mostrarEntidadesFlotantes) {
        this.mostrarEntidadesFlotantes = mostrarEntidadesFlotantes;
        crearEstrellas();
        if (mostrarEntidadesFlotantes) {
            crearEntidadesFlotantes();
        }
    }

    public FondoEfectos() {
        this(true);
    }

    /**
     * Crea diferentes capas de estrellas para simular profundidad mediante el efecto parallax.
     * Las estrellas más lejanas son más oscuras y pequeñas, las cercanas son brillantes.
     */
    private void crearEstrellas() {
        estrellas = new ArrayList<>();

        // Generar texturas de 1x1 píxel con diferentes tonos de gris/blanco
        texturaEstrella1 = crearTexturaEstrella(1, Color.valueOf("444444")); // Fondo lejano
        texturaEstrella2 = crearTexturaEstrella(2, Color.valueOf("888888")); // Plano medio
        texturaEstrella3 = crearTexturaEstrella(3, Color.valueOf("ffffff")); // Plano cercano

        // Distribuir estrellas aleatoriamente en las tres capas
        for (int i = 0; i < 80; i++) estrellas.add(new Estrella(1));
        for (int i = 0; i < 40; i++) estrellas.add(new Estrella(2));
        for (int i = 0; i < 20; i++) estrellas.add(new Estrella(3));
    }

    /**
     * Crea una textura pequeña (píxel) de un color específico.
     */
    private Texture crearTexturaEstrella(int tamano, Color color) {
        Pixmap pixmap = new Pixmap(tamano, tamano, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    /**
     * Genera entidades que "flotan" por el fondo (naves de adorno) para dar vida al escenario.
     */
    private void crearEntidadesFlotantes() {
        entidadesFlotantes = new ArrayList<>();
        String[] texturas = {"enemigo1.png", "enemigo2.png", "naveJugador.png"};

        // Creamos unas pocas entidades que se moverán de forma sinusoidal
        for (int i = 0; i < 4; i++) {
            String nombreTex = texturas[MathUtils.random(0, texturas.length - 1)];
            Texture tex = Recursos.getInstancia().getTextura(nombreTex);
            entidadesFlotantes.add(new EntidadFlotante(new TextureRegion(tex)));
        }
    }

    /**
     * Actualiza y dibuja todos los elementos del fondo.
     * @param lote El SpriteBatch donde se realiza el dibujo.
     * @param delta Tiempo transcurrido entre frames.
     */
    public void renderizar(SpriteBatch lote, float delta) {
        // 1. Dibujar estrellas con movimiento descendente (parallax)
        for (Estrella estrella : estrellas) {
            estrella.y -= estrella.velocidad * delta;
            
            // Si la estrella sale por abajo, reaparece arriba en una X aleatoria
            if (estrella.y < 0) {
                estrella.y = Gdx.graphics.getHeight();
                estrella.x = MathUtils.random(0, Gdx.graphics.getWidth());
            }

            // Seleccionar textura según la capa de profundidad
            Texture tex = (estrella.capa == 1) ? texturaEstrella1 : (estrella.capa == 2 ? texturaEstrella2 : texturaEstrella3);
            lote.draw(tex, estrella.x, estrella.y);
        }

        // 2. Dibujar entidades flotantes con sus transformaciones
        if (mostrarEntidadesFlotantes) {
            for (EntidadFlotante entidad : entidadesFlotantes) {
                entidad.actualizar(delta);
                lote.draw(
                    entidad.regionTextura,
                    entidad.x, entidad.y,
                    entidad.ancho / 2f, entidad.alto / 2f, // Origen en el centro para rotación
                    entidad.ancho, entidad.alto,
                    1f, 1f, // Escala sin cambios
                    entidad.rotacion
                );
            }
        }
    }

    /**
     * Libera los recursos de memoria (texturas) utilizados por el fondo.
     */
    public void dispose() {
        texturaEstrella1.dispose();
        texturaEstrella2.dispose();
        texturaEstrella3.dispose();
    }

    // --- Clases Internas de Soporte ---

    /**
     * Representa un punto de luz individual en el fondo.
     */
    private class Estrella {
        float x, y, velocidad;
        int capa;

        public Estrella(int capa) {
            this.capa = capa;
            this.x = MathUtils.random(0, Gdx.graphics.getWidth());
            this.y = MathUtils.random(0, Gdx.graphics.getHeight());

            // A mayor capa (más cerca), mayor velocidad de movimiento
            if (capa == 1) velocidad = MathUtils.random(10, 30);
            else if (capa == 2) velocidad = MathUtils.random(40, 70);
            else velocidad = MathUtils.random(90, 150);
        }
    }

    /**
     * Representa una nave o decoración que se mueve de forma elegante por el escenario.
     */
    private class EntidadFlotante {
        TextureRegion regionTextura;
        float x, y, velocidadX;
        float ancho, alto;
        float rotacion, velocidadRotacion;
        float baseY, tiempoSeno, amplitudSeno, frecuenciaSeno;

        public EntidadFlotante(TextureRegion regionTextura) {
            this.regionTextura = regionTextura;
            this.ancho = 60f;
            this.alto = 60f;
            reiniciarPosicion();
        }

        /**
         * Calcula la nueva posición usando movimiento sinusoidal para el eje Y.
         */
        public void actualizar(float delta) {
            // Movimiento horizontal lineal
            x += velocidadX * delta;
            
            // Movimiento vertical suave usando la función seno
            tiempoSeno += delta;
            y = baseY + MathUtils.sin(tiempoSeno * frecuenciaSeno) * amplitudSeno;
            
            // Rotación constante sobre sí misma
            rotacion += velocidadRotacion * delta;

            // Reiniciar si sale de los límites laterales
            if (x > Gdx.graphics.getWidth() + 100 || x < -100) {
                reiniciarPosicion();
            }
        }

        /**
         * Asigna valores aleatorios a la entidad para que su comportamiento sea impredecible.
         */
        private void reiniciarPosicion() {
            // Dirección aleatoria (izquierda -> derecha o viceversa)
            if (MathUtils.randomBoolean()) {
                x = -ancho;
                velocidadX = MathUtils.random(40, 120);
            } else {
                x = Gdx.graphics.getWidth();
                velocidadX = MathUtils.random(-120, -40);
            }
            
            // Parámetros de la onda sinusoidal
            baseY = MathUtils.random(50, Gdx.graphics.getHeight() - 50);
            tiempoSeno = MathUtils.random(0, 10);
            amplitudSeno = MathUtils.random(20, 80);
            frecuenciaSeno = MathUtils.random(1f, 3f);
            
            // Parámetros de rotación
            rotacion = MathUtils.random(0, 360);
            velocidadRotacion = MathUtils.random(-50, 50);
        }
    }
}
