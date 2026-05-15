package com.politecnicomalaga.sp.control;

import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;

import java.util.List;

/**
 * Clase encargada de detectar y gestionar las colisiones entre los diferentes
 * objetos del juego (jugador, enemigos y proyectiles).
 */
public class GestorColisiones {

    /**
     * Coordina todas las comprobaciones de colisión en cada frame.
     * @param mundo Referencia al gestor del mundo para acceder a las entidades.
     * @param estado Referencia al estado del juego para actualizar puntuación/vidas.
     */
    public void comprobarColisiones(GestorMundo mundo, EstadoJuego estado) {
        comprobarImpactoJugador(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
        comprobarImpactoEnemigos(mundo.getBatallon(), mundo.getNaveAmiga().getMisDisparos(), estado);
        comprobarColisionJugadorEnemigos(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
    }

    /**
     * Verifica si algún proyectil enemigo ha impactado en la nave del jugador.
     */
    private void comprobarImpactoJugador(Batallon batallon, NaveAmi naveAmiga, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                List<DisparoEne> disparosEnes = naveEne.getMisDisparos();
                for (DisparoEne disparoEne : disparosEnes) {
                    // Solo comprobamos colisión si el disparo está activo ("vivo")
                    if (disparoEne.getEstado() == Ovni.Estado.VIVO && disparoEne.comprobarColision(naveAmiga)) {
                        estado.perderVida();
                        disparoEne.setEstado(Ovni.Estado.MUERTO);
                    }
                }
            }
        }
    }

    /**
     * Verifica si algún proyectil del jugador ha impactado en alguna nave enemiga.
     */
    private void comprobarImpactoEnemigos(Batallon batallon, List<DisparoAmi> disparosAmis, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (DisparoAmi disparoAmi : disparosAmis) {
            // Un disparo muerto no puede volver a impactar
            if (disparoAmi.getEstado() == Ovni.Estado.MUERTO) continue;

            for (Escuadron escuadron : escuadrones) {
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                if (disparoAmi.comprobarColision(navesEnemigas)) {
                    estado.addPuntuacion(10);
                    disparoAmi.setEstado(Ovni.Estado.MUERTO);
                    break; // Salimos del bucle de escuadrones para este disparo
                }
            }
        }
    }

    /**
     * Verifica si hay contacto directo entre la nave del jugador y las naves enemigas.
     */
    private void comprobarColisionJugadorEnemigos(Batallon batallon, NaveAmi naveAmiga, EstadoJuego estado) {
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
            for (NaveEne naveEne : navesEnemigas) {
                if (naveEne.estaVivo() && naveEne.colision(naveAmiga)) {
                    // Ambos reciben daño por el choque
                    naveEne.recibirDisparo();
                    estado.perderVida();
                }
            }
        }
    }
}
