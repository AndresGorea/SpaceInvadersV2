package com.politecnicomalaga.sp.model;

import com.badlogic.gdx.audio.Sound;
import java.util.ArrayList;
import com.politecnicomalaga.sp.control.ConfiguracionJuego;
import com.politecnicomalaga.sp.util.Assets;

public class NaveAmi extends Nave {
    //Atributos
    private ArrayList<DisparoAmi> misDisparos; //Para el crud de disparos

    private float tiempoTripleDisparo = 0;
    private float tiempoEscudo = 0;
    private float tiempoVelocidad = 0;

    //private static final float DURACION_POWERUP = 10f;

    //Constructor
    public NaveAmi(float x, float y, float width, float height, Estado estado, Direccion dir, String textura, int vidas, float cadencia, float anchoBala, float altoBala, float velocidadBala) {
        super(x, y, width, height, estado, dir, textura, vidas, cadencia, anchoBala, altoBala, velocidadBala);
        this.misDisparos = new ArrayList<>();
    }
    //Getters y Setters
    public ArrayList<DisparoAmi> getMisDisparos() {
        return misDisparos;
    }
    public void setMisDisparos(ArrayList<DisparoAmi> misDisparos) {
        this.misDisparos = misDisparos;
    }

    //Métodos
    //Crear los disparos(Create)
    @Override
    public void disparar() { //Aquí no tenemos random no tiene sentido usarlo, Definimos la cadencia que tendra nuestra nave en el controlador
        if (estaVivo()) {
            // Sonido de disparo
            Sound s = Assets.getInstance().getSound("Disparo.mp3");
            if (s != null) {
                s.play(0.4f);
            }

            // Cálculo de posición centrada (Igual que en NaveEne pero hacia ARRIBA)
            float posX = getX() + getMitadWidth() - (getAnchoBala() / 2f);
            float posY = getY() + getMitadHeight();

            // Creamos el disparo y lo añadimos a la lista
            DisparoAmi nuevoDisparo = new DisparoAmi(posX, posY, getAnchoBala(), getAltoBala(), Estado.VIVO, Direccion.ARRIBA, "disparoAmi.png");
            misDisparos.add(nuevoDisparo);

            if (tiempoTripleDisparo > 0) {
                DisparoAmi izq = new DisparoAmi(posX - 20, posY - 10, getAnchoBala(), getAltoBala(), Estado.VIVO, Direccion.ARRIBA, "disparoAmi.png");
                DisparoAmi der = new DisparoAmi(posX + 20, posY - 10, getAnchoBala(), getAltoBala(), Estado.VIVO, Direccion.ARRIBA, "disparoAmi.png");
                misDisparos.add(izq);
                misDisparos.add(der);
            }
        }
    }

    public void actualizarPowerUps(float delta) {
        if (tiempoTripleDisparo > 0) tiempoTripleDisparo -= delta;
        if (tiempoEscudo > 0) tiempoEscudo -= delta;
        if (tiempoVelocidad > 0) tiempoVelocidad -= delta;
    }

    public void activarPowerUp(PowerUp.Tipo tipo) {
        switch (tipo) {
            case MULTI_DISPARO:
                tiempoTripleDisparo = ConfiguracionJuego.DURACION_POWERUP;
                break;
            case ESCUDO:
                tiempoEscudo = ConfiguracionJuego.DURACION_POWERUP;
                break;
            case VELOCIDAD:
                tiempoVelocidad = ConfiguracionJuego.DURACION_POWERUP;
                break;
        }
    }

    public boolean tieneEscudo() {
        return tiempoEscudo > 0;
    }

    public float getVelocidadExtra() {
        return tiempoVelocidad > 0 ? ConfiguracionJuego.VELOCIDAD_BONUS : 0f;
    }

    public float getTiempoTripleDisparo() {
        return tiempoTripleDisparo;
    }

    public float getTiempoEscudo() {
        return tiempoEscudo;
    }

    public float getTiempoVelocidad() {
        return tiempoVelocidad;
    }

    @Override
    public void recibirDisparo() {
        if (tieneEscudo()) {
            // No recibe daño si tiene escudo
            return;
        }

        // Sonido de recibir daño
        Sound s = Assets.getInstance().getSound("RecibirDaño.mp3");
        if (s != null) {
            s.play(0.7f);
        }

        super.recibirDisparo();
    }

    //Gestionar la salida de la pantalla de los disparos (Delete)
    @Override
    public void gestionarMisDisparos(float limiteSuperior, float delta) {
        for (int i = misDisparos.size() - 1 ; i >= 0; i--){//Recorremos el array al reves para no liarla con los indices
            DisparoAmi d = misDisparos.get(i);

            //Si el disparo esta vivo es decir no ha colisionado lo movemos
            if (d.getEstado() == Estado.VIVO){
                d.mover(Direccion.ARRIBA, getVelocidadBala(), delta);
                d.desaparecer(limiteSuperior); //Preguntamos si se ha salido de la pantalla y este setea el estado a muerto
            }
            if (d.getEstado() == Estado.MUERTO) misDisparos.remove(i); //Lo eliminamos si la comprobación de desaparecer de la pantalla ya nos da que esta Muerto
        }
    }
}
