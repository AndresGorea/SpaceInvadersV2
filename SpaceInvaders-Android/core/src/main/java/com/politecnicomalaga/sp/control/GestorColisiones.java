package com.politecnicomalaga.sp.control;

import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;

import java.util.List;

public class GestorColisiones {

    public void comprobarColisiones(GestorMundo mundo, EstadoJuego estado) {
        comprobarImpactoJugador(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
        comprobarImpactoEnemigos(mundo.getBatallon(), mundo.getNaveAmiga().getMisDisparos(), estado);
        comprobarColisionJugadorEnemigos(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
    }

    private void comprobarImpactoJugador(Batallon batallon, NaveAmi naveAmiga, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                List<DisparoEne> disparosEnes = naveEne.getMisDisparos();
                for (DisparoEne disparoEne : disparosEnes) {
                    if (disparoEne.getEstado() == Ovni.Estado.VIVO && disparoEne.comprobarColision(naveAmiga)) {
                        estado.perderVida();
                        disparoEne.setEstado(Ovni.Estado.MUERTO);
                    }
                }
            }
        }
    }

    private void comprobarImpactoEnemigos(Batallon batallon, List<DisparoAmi> disparosAmis, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (DisparoAmi disparoAmi : disparosAmis) {
            for (Escuadron escuadron : escuadrones) {
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                if (disparoAmi.comprobarColision(navesEnemigas)) {
                    estado.addPuntuacion(10);
                    disparoAmi.setEstado(Ovni.Estado.MUERTO);
                }
            }
        }
    }

    private void comprobarColisionJugadorEnemigos(Batallon batallon, NaveAmi naveAmiga, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                if (naveEne.estaVivo() && naveEne.colision(naveAmiga)) {
                    naveEne.recibirDisparo();
                    estado.perderVida();
                }
            }
        }
    }
}
