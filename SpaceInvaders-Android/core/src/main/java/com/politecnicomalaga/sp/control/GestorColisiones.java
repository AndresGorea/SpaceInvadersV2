package com.politecnicomalaga.sp.control;

import com.politecnicomalaga.sp.control.EfectosCamara;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.model.NaveEspecial;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.model.PowerUp;
import com.politecnicomalaga.sp.model.Bunker;

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
        comprobarImpactoJugadorEspecial(mundo.getNaveEspecial(), mundo.getNaveAmiga(), estado);
        comprobarImpactoEnemigos(mundo, estado);
        comprobarImpactoNaveEspecial(mundo, estado);
        comprobarColisionJugadorEnemigos(mundo.getBatallon(), mundo.getNaveAmiga(), estado);
        comprobarImpactoBunkeres(mundo);
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
                        if (!naveAmiga.tieneEscudo()) {
                            estado.perderVida();
                        }
                        EfectosCamara.getInstancia().shake(8f, 0.4f);
                        EfectosCamara.getInstancia().hitStop(0.1f);
                        disparoEne.setEstado(Ovni.Estado.MUERTO);
                    }
                }
            }
        }
    }

    private void comprobarImpactoJugadorEspecial(NaveEspecial esp, NaveAmi naveAmiga, EstadoJuego estado) {
        if (esp == null) return;
        List<DisparoEne> disparos = esp.getMisDisparos();
        for (DisparoEne d : disparos) {
            if (d.getEstado() == Ovni.Estado.VIVO && d.comprobarColision(naveAmiga)) {
                if (!naveAmiga.tieneEscudo()) {
                    estado.perderVida();
                }
                EfectosCamara.getInstancia().shake(8f, 0.4f);
                EfectosCamara.getInstancia().hitStop(0.1f);
                d.setEstado(Ovni.Estado.MUERTO);
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

                        // Si la nave muere, soltamos Power-up y damos los puntos
                        if (!n.estaVivo()) {
                            estado.addPuntuacion(n.getPuntos());
                            mundo.soltarPowerUp(n.getX(), n.getY());
                        }
                        break;
                    }
                }
                if (disparoAmi.getEstado() == Ovni.Estado.MUERTO) break;
            }
        }
    }

    private void comprobarImpactoNaveEspecial(GestorMundo mundo, EstadoJuego estado) {
        NaveEspecial esp = mundo.getNaveEspecial();
        if (esp == null || !esp.estaVivo()) return;

        List<DisparoAmi> disparosAmis = mundo.getNaveAmiga().getMisDisparos();
        for (DisparoAmi d : disparosAmis) {
            if (d.estaVivo() && d.colision(esp)) {
                esp.setEstado(Ovni.Estado.MUERTO);
                d.setEstado(Ovni.Estado.MUERTO);

                // Puntuación aleatoria
                int puntos = ConfiguracionJuego.ESP_PUNTOS_MIN + (int)(Math.random() * (ConfiguracionJuego.ESP_PUNTOS_MAX - ConfiguracionJuego.ESP_PUNTOS_MIN));
                estado.addPuntuacion(puntos);

                EfectosCamara.getInstancia().shake(15f, 0.6f);
                break;
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
                    if (!naveAmiga.tieneEscudo()) {
                        estado.perderVida();
                    }
                    EfectosCamara.getInstancia().shake(10f, 0.5f);
                    EfectosCamara.getInstancia().hitStop(0.15f);
                }
            }
        }
    }

    /**
     * Verifica las colisiones contra los búnkeres (disparos aliados, disparos enemigos, e impactos físicos directos).
     */
    private void comprobarImpactoBunkeres(GestorMundo mundo) {
        List<Bunker> bunkeres = mundo.getBunkeres();
        if (bunkeres == null || bunkeres.isEmpty()) return;

        // 1. Disparos aliados contra Búnkeres
        List<DisparoAmi> disparosAmis = mundo.getNaveAmiga().getMisDisparos();
        for (DisparoAmi d : disparosAmis) {
            if (d.getEstado() == Ovni.Estado.VIVO) {
                for (Bunker b : bunkeres) {
                    if (b.estaVivo() && d.colision(b)) {
                        b.recibirDano();
                        d.setEstado(Ovni.Estado.MUERTO);
                        EfectosCamara.getInstancia().hitStop(0.05f); // Pequeño hitstop
                        break;
                    }
                }
            }
        }

        // 2. Disparos enemigos (Batallón) contra Búnkeres
        Escuadron[] escuadrones = mundo.getBatallon().getEscuadrones();
        for (Escuadron escuadron : escuadrones) {
            for (NaveEne naveEne : escuadron.getNavesEnemigas()) {
                for (DisparoEne d : naveEne.getMisDisparos()) {
                    if (d.getEstado() == Ovni.Estado.VIVO) {
                        for (Bunker b : bunkeres) {
                            if (b.estaVivo() && d.colision(b)) {
                                b.recibirDano();
                                d.setEstado(Ovni.Estado.MUERTO);
                                EfectosCamara.getInstancia().shake(3f, 0.2f);
                                break;
                            }
                        }
                    }
                }
            }
        }

        // 3. Disparos de la nave especial contra Búnkeres
        NaveEspecial esp = mundo.getNaveEspecial();
        if (esp != null) {
            for (DisparoEne d : esp.getMisDisparos()) {
                if (d.getEstado() == Ovni.Estado.VIVO) {
                    for (Bunker b : bunkeres) {
                        if (b.estaVivo() && d.colision(b)) {
                            b.recibirDano();
                            d.setEstado(Ovni.Estado.MUERTO);
                            EfectosCamara.getInstancia().shake(3f, 0.2f);
                            break;
                        }
                    }
                }
            }
        }

        // 4. Kamikaze: Nave Enemiga choca físicamente con el Búnker
        for (Escuadron escuadron : escuadrones) {
            for (NaveEne naveEne : escuadron.getNavesEnemigas()) {
                if (naveEne.estaVivo()) {
                    for (Bunker b : bunkeres) {
                        if (b.estaVivo() && naveEne.colision(b)) {
                            b.destruir();
                            naveEne.recibirDisparo(); // Destruye la nave enemiga
                            EfectosCamara.getInstancia().shake(10f, 0.5f);
                            EfectosCamara.getInstancia().hitStop(0.1f);
                            break;
                        }
                    }
                }
            }
        }
    }
}
