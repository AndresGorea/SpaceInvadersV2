package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.model.PowerUp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Controlador {
    /// SINGLETON
    private static Controlador miSingle;

    /// CONFIGURACIÓN: NAVE AMIGA (Valores fijos)
    private static final float NAVE_ANCHO = 60f;
    private static final float NAVE_ALTO = 60f;
    private static final int   NAVE_VIDAS = 3;
    private static final float NAVE_VELOCIDAD = 400f;
    private static final float NAVE_CADENCIA = 0.5f;

    private static final float BALA_AMI_ANCHO = 15f;
    private static final float BALA_AMI_ALTO = 30f;
    private static final float BALA_AMI_VELOCIDAD = 600f;

    /// CONFIGURACIÓN: ENEMIGOS Y BATALLÓN
    private static final float BAT_ESPACIO_VERT = 10f;
    private static final float BAT_ESPACIO_HORIZ = 10f;
    private static final float BAT_VELOCIDAD = 80f;

    private static final float ENE_ANCHO = 50f;
    private static final float ENE_ALTO = 40f;
    private static final int   ENE_VIDAS = 1;
    private static final float ENE_CADENCIA = 1.5f;
    private static final int   ENE_PROB_DISPARO = 7;

    private static final float BALA_ENE_ANCHO = 5f;
    private static final float BALA_ENE_ALTO = 30f;
    private static final float BALA_ENE_VELOCIDAD = 400f;

    /// VARIABLES DE ESTADO Y ENTIDADES
    private NaveAmi naveAmiga;
    private Batallon batallon;

    private float contadorTiempoAmigo;
    private float contadorTiempoEnemigo;
    private boolean jugando;
    private int puntuacion;

    private List<PowerUp> powerUps;
    private Random random = new Random();
    private static final float PROB_DROP = 0.30f; // 30% probabilidad

    private final float velocidadNave = NAVE_VELOCIDAD;
    private final float cadenciaAmiga = NAVE_CADENCIA;
    private final float cadenciaEnemiga = ENE_CADENCIA;


    /// CONSTRUCTOR
    private Controlador() {
        // Inicializamos estado del juego
        this.jugando = true;
        this.contadorTiempoAmigo = 0f;
        this.contadorTiempoEnemigo = 0f;
        this.puntuacion = 0;
        this.powerUps = new ArrayList<>();

        // Calculamos posiciones dinámicas de inicio con LibGDX
        // Centramos la nave horizontalmente y la separamos 10 píxeles del suelo
        float naveInicioX = (Gdx.graphics.getWidth() / 2f) - (NAVE_ANCHO / 2f);
        float naveInicioY = 10f;

        // Instanciamos la nave amiga
        this.naveAmiga = new NaveAmi(
            naveInicioX, naveInicioY, NAVE_ANCHO, NAVE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.NOMOVER,
            "naveJugador.png",
            NAVE_VIDAS,
            NAVE_CADENCIA,
            BALA_AMI_ANCHO, BALA_AMI_ALTO, BALA_AMI_VELOCIDAD
        );

        // Calculamos la posición inicial del batallón
        float batInicioX = 20f; // Margen izquierdo
        float batInicioY = Gdx.graphics.getHeight() - 40f; // Margen superior

        // Instanciamos el batallón enemigo
        this.batallon = new Batallon(
            batInicioX, batInicioY, BAT_ESPACIO_VERT,
            ENE_ANCHO, ENE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.DERECHA,
            "enemigo1.png",
            ENE_VIDAS,
            ENE_CADENCIA,
            BALA_ENE_ANCHO, BALA_ENE_ALTO, BALA_ENE_VELOCIDAD,
            ENE_PROB_DISPARO,
            BAT_ESPACIO_HORIZ, BAT_VELOCIDAD
        );
    }

    //Otros métodos
    public static Controlador getInstance(){
        if (miSingle == null){
            miSingle= new Controlador();
        }
        return miSingle;
    }
    public void click (float x, float y){
        cambiarSentidoNaveAmiga(x);
    }
    public void simulaMundo(float anchoPantalla, float altoPantalla, float delta){
        //Comprobar si he muerto
        if (!naveAmiga.estaVivo()){
            jugando=false;
        }

        if (jugando){
            //Comprobar si he ganado
            jugando=comprobarSiGano(batallon);

            //disparo yo?
            contadorTiempoAmigo+=delta;
            if (contadorTiempoAmigo>=cadenciaAmiga){
                naveAmiga.disparar();
                contadorTiempoAmigo=0f;
            }

            //disparan los enemigos?
            contadorTiempoEnemigo+=delta;
            if (contadorTiempoEnemigo>=cadenciaEnemiga){
                dispararTodosLosEnemigos(batallon);
                contadorTiempoEnemigo=0f;
            }

            //me han dado
            hanDadoNaveAmiga(batallon, naveAmiga);

            // he matado a alguien?
            List<DisparoAmi> disparoAmis = naveAmiga.getMisDisparos();
            hematado(batallon, disparoAmis);

            // Gestionar Power-ups
            naveAmiga.actualizarPowerUps(delta);
            gestionarPowerUps(delta);

            //me han tocado los aliens?
            meHanTocado(batallon, naveAmiga);

            //me muevo?
            if (naveAmiga.getX()>anchoPantalla-naveAmiga.getWidth()){
                naveAmiga.setX(anchoPantalla-naveAmiga.getWidth());
                naveAmiga.setDir(Ovni.Direccion.NOMOVER);
            }
            if (naveAmiga.getX()<0){
                naveAmiga.setX(0);
                naveAmiga.setDir(Ovni.Direccion.NOMOVER);
            }
            naveAmiga.mover(naveAmiga.getDir(),velocidadNave + naveAmiga.getVelocidadExtra(), delta);

            //SE MUEVE EL ESCUADRÓN
            batallon.mover(anchoPantalla,altoPantalla,20, delta);

            //gestiono todos los disparos
            //Los amigos
            naveAmiga.gestionarMisDisparos(altoPantalla, delta);

            //Los enemigos
            gestioanrDisparosBatallon( batallon, 0, delta);
        }
    }

    public void pintar(SpriteBatch batch, Map<String, Texture> galeriaImagenes){
        //pintar naveAmiga
        batch.draw(galeriaImagenes.get(naveAmiga.getTextura()),naveAmiga.getX(),naveAmiga.getY(),naveAmiga.getWidth(),naveAmiga.getHeight());

        //pintar navesEnemigas y sus disparos
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron esc: escuadrones){
            NaveEne[] naveEnes = esc.getNavesEnemigas();
            for (NaveEne navE: naveEnes){
                if (navE.estaVivo()) {
                    batch.draw(galeriaImagenes.get(navE.getTextura()), navE.getX(), navE.getY(), navE.getWidth(), navE.getHeight());
                }
                List<DisparoEne> disparosEnemigos = navE.getMisDisparos();
                for (DisparoEne disEne: disparosEnemigos){
                    batch.draw(galeriaImagenes.get(disEne.getTextura()),disEne.getX(),disEne.getY(),disEne.getWidth(),disEne.getHeight());
                }
            }
        }
        //Pintar disparosAmigos
        List<DisparoAmi> disparosAmigos = naveAmiga.getMisDisparos();
        for (DisparoAmi dispAmi: disparosAmigos){
            if (dispAmi.estaVivo()){
                batch.draw(galeriaImagenes.get(dispAmi.getTextura()),dispAmi.getX(),dispAmi.getY(),dispAmi.getWidth(),dispAmi.getHeight());
            }
        }

        //Pintar Power-ups
        for (PowerUp p : powerUps) {
            if (p.estaVivo()) {
                Texture t = galeriaImagenes.get(p.getTextura());
                if (t != null) {
                    batch.draw(t, p.getX(), p.getY(), p.getWidth(), p.getHeight());
                }
            }
        }

    }

    public void cambiarSentidoNaveAmiga (float x){
        if (x>naveAmiga.getX() && naveAmiga.getDir()!= Ovni.Direccion.DERECHA){
            naveAmiga.setDir(Ovni.Direccion.DERECHA);
        } else if (x>naveAmiga.getX() && naveAmiga.getDir()== Ovni.Direccion.DERECHA) {
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        } else if (x<naveAmiga.getX() && naveAmiga.getDir()!= Ovni.Direccion.IZQUIERDA){
            naveAmiga.setDir(Ovni.Direccion.IZQUIERDA);
        } else if (x<naveAmiga.getX() && naveAmiga.getDir() == Ovni.Direccion.IZQUIERDA){
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
    }

    public void hanDadoNaveAmiga(Batallon batallon, NaveAmi naveAmiga){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron: escuadrones){
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne: navesEnemigas){
                List<DisparoEne> disparoEnes = naveEne.getMisDisparos();
                for (DisparoEne disparoEne: disparoEnes){
                    disparoEne.comprobarColision(naveAmiga);
                }
            }
        }
    }

    public  void hematado(Batallon batallon, List<DisparoAmi> disparoAmis){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (DisparoAmi disparoAmi: disparoAmis){
            if (!disparoAmi.estaVivo()) continue;
            for (Escuadron escuadron: escuadrones){
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                for (NaveEne n : navesEnemigas) {
                    if (n.estaVivo() && disparoAmi.colision(n)) {
                        n.recibirDisparo();
                        disparoAmi.setEstado(Ovni.Estado.MUERTO);
                        puntuacion += 10;

                        // Lógica de drop
                        if (!n.estaVivo() && random.nextFloat() < PROB_DROP) {
                            soltarPowerUp(n.getX(), n.getY());
                        }
                        break;
                    }
                }
                if (!disparoAmi.estaVivo()) break;
            }
        }

    }

    private void soltarPowerUp(float x, float y) {
        PowerUp.Tipo tipo = PowerUp.Tipo.values()[random.nextInt(PowerUp.Tipo.values().length)];
        String textura = "";
        switch (tipo) {
            case MULTI_DISPARO:
                textura = "DisparoMultiple.png";
                break;
            case ESCUDO:
                textura = "Escudo.png";
                break;
            case VELOCIDAD:
                textura = "Velocidad.png";
                break;
        }

        powerUps.add(new PowerUp(x, y, 30, 30, tipo, textura));
    }

    private void gestionarPowerUps(float delta) {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            if (p.estaVivo()) {
                p.mover(Ovni.Direccion.ABAJO, 150f, delta);
                p.desaparecer(0);

                if (p.colision(naveAmiga)) {
                    naveAmiga.activarPowerUp(p.getTipo());
                    p.setEstado(Ovni.Estado.MUERTO);
                }
            }

            if (!p.estaVivo()) {
                powerUps.remove(i);
            }
        }
    }

    public void pintarHUD(SpriteBatch batch, Map<String, Texture> galeriaImagenes, BitmapFont font, float anchoPantalla, float altoPantalla) {
        // Pintar Puntuación (Arriba a la izquierda)
        font.draw(batch, "Puntuación: " + puntuacion, 20, altoPantalla - 20);

        // Pintar Vidas (Arriba a la derecha como iconos)
        float tamañoIcono = 30f;
        float margen = 10f;
        for (int i = 0; i < naveAmiga.getVidas(); i++) {
            batch.draw(galeriaImagenes.get("naveJugador.png"),
                anchoPantalla - (i + 1) * (tamañoIcono + margen) - 10,
                altoPantalla - tamañoIcono - 15,
                tamañoIcono, tamañoIcono);
        }
    }

    public void meHanTocado(Batallon batallon, NaveAmi naveAmiga) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                if (naveEne.estaVivo() && naveEne.colision(naveAmiga)) {
                    naveEne.setEstado(Ovni.Estado.MUERTO);
                    naveAmiga.recibirDisparo();
                }
            }
        }


    }

    public void dispararTodosLosEnemigos (Batallon batallon){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                naveEne.disparar();
            }
        }
    }

    public void gestioanrDisparosBatallon(Batallon batallon, float limiteSuperior, float delta){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                naveEne.gestionarMisDisparos(limiteSuperior, delta);
            }
        }
    }

    public boolean comprobarSiGano(Batallon batallon){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron: escuadrones){
            NaveEne[] naveEnes = escuadron.getNavesEnemigas();
            for (NaveEne naveEne: naveEnes){
                if (naveEne.estaVivo()){
                    return true;
                }
            }
        }
        return false;
    }
}
