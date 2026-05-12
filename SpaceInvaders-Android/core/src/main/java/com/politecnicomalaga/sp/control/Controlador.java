package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;

import java.util.List;
import java.util.Map;

public class Controlador {
    private static Controlador miSingle;
    private NaveAmi naveAmiga;
    private final float velocidadNave;
    private final int cadenciaAmiga,cadenciaEnemiga;
    private int contadorTiempoAmigo, getContadorTiempoEnemigo;
    private Batallon batallon;
    private boolean jugando;

    //CONSTRUCTOR
    private Controlador() {
        naveAmiga = new NaveAmi(300,0,60,60, Ovni.Estado.VIVO, Ovni.Direccion.NOMOVER,"naveJugador.png",1,120,15,30,8);
        velocidadNave = 1f;
        contadorTiempoAmigo=0;
        getContadorTiempoEnemigo=0;
        cadenciaAmiga= 180;
        cadenciaEnemiga=180;
        batallon=new Batallon(Gdx.graphics.getWidth()%2,Gdx.graphics.getHeight()-40,10, 50,40, Ovni.Estado.VIVO, Ovni.Direccion.DERECHA, "enemigo1.png",1,180,5,30,1,7,10,0.3f);
        jugando=true;
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
    public void simulaMundo(float anchoPantalla, float altoPantalla){
        //Comprobar si he muerto
        if (!naveAmiga.estaVivo()){
            jugando=false;
        }

        if (jugando){
            //Comprobar si he ganado
            jugando=comprobarSiGano(batallon);

            //disparo yo?
            contadorTiempoAmigo++;
            if (contadorTiempoAmigo==cadenciaAmiga){
                naveAmiga.disparar();
                contadorTiempoAmigo=0;
            }

            //disparan los enemigos?
            getContadorTiempoEnemigo++;
            if (getContadorTiempoEnemigo==cadenciaEnemiga){
                dispararTodosLosEnemigos(batallon);
                getContadorTiempoEnemigo=0;
            }

            //me han dado
            hanDadoNaveAmiga(batallon, naveAmiga);

            // he matado a alguien?
            List<DisparoAmi> disparoAmis = naveAmiga.getMisDisparos();
            hematado(batallon, disparoAmis);

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
            naveAmiga.mover(naveAmiga.getDir(),velocidadNave);

            //SE MUEVE EL ESCUADRÓN
            batallon.mover(anchoPantalla,altoPantalla,20);

            //gestiono todos los disparos
            //Los amigos
            naveAmiga.gestionarMisDisparos(altoPantalla);

            //Los enemigos
            gestioanrDisparosBatallon( batallon, 0);
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
            for (Escuadron escuadron: escuadrones){
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                disparoAmi.comprobarColision(navesEnemigas);
            }
        }

    }

    public void meHanTocado(Batallon batallon, NaveAmi naveAmiga) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                if (naveEne.estaVivo() && naveEne.colision(naveAmiga)) {
                    naveEne.setEstado(Ovni.Estado.MUERTO);
                    naveAmiga.setVidas(naveAmiga.getVidas() - 1);
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

    public void gestioanrDisparosBatallon(Batallon batallon, float limiteSuperior){
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                naveEne.gestionarMisDisparos(limiteSuperior);
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
