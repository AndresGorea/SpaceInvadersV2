package com.politecnicomalaga.sp.control;

import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.model.PowerUp;

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
        comprobarImpactoEnemigos(mundo, estado);
        comprobarColisionJugadorEnemigos(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
        comprobarRecogidaPowerUps(mundo);
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
    private void comprobarImpactoEnemigos(GestorMundo mundo, EstadoJuego estado) {
        Batallon batallon = mundo.getBatallon();
        List<DisparoAmi> disparosAmis = mundo.getNaveAmiga().getMisDisparos();
        Escuadron[] escuadrones = batallon.getEscuadrones();

        for (DisparoAmi disparoAmi : disparosAmis) {
            if (disparoAmi.getEstado() == Ovni.Estado.MUERTO) continue;

            for (Escuadron escuadron : escuadrones) {
                NaveEne[] navesEnemigas = escuadron.getNavesEnemigas();
                for (NaveEne n : navesEnemigas) {
                    if (n.estaVivo() && disparoAmi.colision(n)) {
                        n.recibirDisparo();
                        disparoAmi.setEstado(Ovni.Estado.MUERTO);
                        estado.addPuntuacion(10);

                        // Si la nave muere, soltamos Power-up
                        if (!n.estaVivo()) {
                            mundo.soltarPowerUp(n.getX(), n.getY());
                        }
                        break;
                    }
                }
                if (disparoAmi.getEstado() == Ovni.Estado.MUERTO) break;
            }
        }
    }

    private void comprobarRecogidaPowerUps(GestorMundo mundo) {
        NaveAmi nave = mundo.getNaveAmiga();
        List<PowerUp> powerUps = mundo.getPowerUps();
        for (PowerUp p : powerUps) {
            if (p.estaVivo() && p.colision(nave)) {
                nave.activarPowerUp(p.getTipo());
                p.setEstado(Ovni.Estado.MUERTO);
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
