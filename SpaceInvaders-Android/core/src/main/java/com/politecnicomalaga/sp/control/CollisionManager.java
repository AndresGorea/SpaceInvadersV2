package com.politecnicomalaga.sp.control;

import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;

import java.util.List;

public class CollisionManager {

    public void checkCollisions(WorldManager world, GameState state) {
        checkPlayerHit(world.getBatallon(), world.getNaveAmiga(), state);
        checkEnemiesHit(world.getBatallon(), world.getNaveAmiga().getMisDisparos(), state);
        checkPlayerEnemiesCollision(world.getBatallon(), world.getNaveAmiga(), state);
    }

    private void checkPlayerHit(Batallon batallon, NaveAmi naveAmiga, GameState state) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                List<DisparoEne> disparosEnes = naveEne.getMisDisparos();
                for (DisparoEne disparoEne : disparosEnes) {
                    if (disparoEne.getEstado() == Ovni.Estado.VIVO && disparoEne.comprobarColision(naveAmiga)) {
                        state.perderVida();
                        disparoEne.setEstado(Ovni.Estado.MUERTO);
                    }
                }
            }
        }
    }

    private void checkEnemiesHit(Batallon batallon, List<DisparoAmi> disparosAmis, GameState state) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (DisparoAmi disparoAmi : disparosAmis) {
            for (Escuadron escuadron : escuadrones) {
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                if (disparoAmi.comprobarColision(navesEnemigas)) {
                    state.addPuntuacion(10);
                    disparoAmi.setEstado(Ovni.Estado.MUERTO);
                }
            }
        }
    }

    private void checkPlayerEnemiesCollision(Batallon batallon, NaveAmi naveAmiga, GameState state) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                if (naveEne.estaVivo() && naveEne.colision(naveAmiga)) {
                    naveEne.recibirDisparo();
                    state.perderVida();
                }
            }
        }
    }
}
