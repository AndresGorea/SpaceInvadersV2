package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEspecial;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.model.PowerUp;
import com.politecnicomalaga.sp.model.Bunker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Clase que gestiona la existencia y el comportamiento de todos los objetos en el mundo de juego.
 * Controla el ciclo de vida, movimiento y acciones (como disparar) de naves y batallones.
 */
public class GestorMundo {
    private NaveAmi naveAmiga;
    private Batallon batallon;
    private NaveEspecial naveEspecial;
    private List<PowerUp> powerUps;
    private List<Bunker> bunkeres;
    private Random random;
    private float contadorTiempoAmigo;
    private float contadorTiempoEnemigo;

    public GestorMundo() {
        this.contadorTiempoAmigo = 0f;
        this.contadorTiempoEnemigo = 0f;
        this.powerUps = new ArrayList<>();
        this.bunkeres = new ArrayList<>();
        this.random = new Random();
        inicializarMundo();
    }

    /**
     * Configura la posición inicial y parámetros de las entidades al comenzar el juego.
     */
    private void inicializarMundo() {
        // Posicionamiento de la nave del jugador (centrada horizontalmente)
        float naveInicioX = (ConfiguracionJuego.VIRTUAL_WIDTH / 2f) - (ConfiguracionJuego.NAVE_ANCHO / 2f);
        float naveInicioY = 10f;

        this.naveAmiga = new NaveAmi(
            naveInicioX, naveInicioY, ConfiguracionJuego.NAVE_ANCHO, ConfiguracionJuego.NAVE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.NOMOVER,
            "naveJugador.png",
            ConfiguracionJuego.NAVE_VIDAS,
            ConfiguracionJuego.NAVE_CADENCIA,
            ConfiguracionJuego.BALA_AMI_ANCHO, ConfiguracionJuego.BALA_AMI_ALTO, ConfiguracionJuego.BALA_AMI_VELOCIDAD
        );

        // Posicionamiento inicial del batallón enemigo
        float batInicioX = 20f;
        float batInicioY = ConfiguracionJuego.VIRTUAL_HEIGHT - 40f;

        GestorPreferencias prefs = GestorPreferencias.getInstancia();

        this.batallon = new Batallon(
            batInicioX, batInicioY, ConfiguracionJuego.BAT_ESPACIO_VERT,
            ConfiguracionJuego.ENE_ANCHO, ConfiguracionJuego.ENE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.DERECHA,
            "enemigo1.png",
            ConfiguracionJuego.ENE_VIDAS,
            ConfiguracionJuego.ENE_CADENCIA,
            ConfiguracionJuego.BALA_ENE_ANCHO, ConfiguracionJuego.BALA_ENE_ALTO, prefs.getVelocidadBalaEnemiga(),
            prefs.getProbabilidadDisparo(),
            ConfiguracionJuego.BAT_ESPACIO_HORIZ, prefs.getVelocidadBatallon()
        );

        // Instanciar búnkeres
        float bunkerY = 120f; // Por encima del jugador
        float espacioRestante = ConfiguracionJuego.VIRTUAL_WIDTH - (ConfiguracionJuego.BUNKER_CANTIDAD * ConfiguracionJuego.BUNKER_ANCHO);
        float margenEntreBunkeres = espacioRestante / (ConfiguracionJuego.BUNKER_CANTIDAD + 1);
        
        for (int i = 0; i < ConfiguracionJuego.BUNKER_CANTIDAD; i++) {
            float bX = margenEntreBunkeres + i * (ConfiguracionJuego.BUNKER_ANCHO + margenEntreBunkeres);
            bunkeres.add(new Bunker(bX, bunkerY, ConfiguracionJuego.BUNKER_ANCHO, ConfiguracionJuego.BUNKER_ALTO, ConfiguracionJuego.BUNKER_VIDAS));
        }
    }

    /**
     * Actualiza el estado de todas las entidades del juego basándose en el tiempo transcurrido.
     * @param anchoPantalla Límite horizontal para el movimiento.
     * @param altoPantalla Límite vertical para proyectiles.
     * @param delta Tiempo desde el último frame.
     */
    public void actualizar(float anchoPantalla, float altoPantalla, float delta) {
        // Gestión de disparos del batallón enemigo
        contadorTiempoEnemigo += delta;
        contadorTiempoAmigo += delta;
        if (contadorTiempoEnemigo >= ConfiguracionJuego.ENE_CADENCIA) {
            batallon.disparar();
            contadorTiempoEnemigo = 0f;
        }

        // Control de límites y movimiento de la nave amiga
        if (naveAmiga.getX() > anchoPantalla - naveAmiga.getWidth()) {
            naveAmiga.setX(anchoPantalla - naveAmiga.getWidth());
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
        if (naveAmiga.getX() < 0) {
            naveAmiga.setX(0);
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
        naveAmiga.mover(naveAmiga.getDir(), ConfiguracionJuego.NAVE_VELOCIDAD, delta);

        // Movimiento de la formación enemiga
        batallon.mover(anchoPantalla, altoPantalla, 20f, delta);

        // Gestión de la nave especial
        actualizarNaveEspecial(anchoPantalla, delta);

        // Actualización de la posición de los proyectiles en pantalla
        naveAmiga.gestionarMisDisparos(altoPantalla, delta);
        batallon.gestionarDisparos(0f, delta);

        // Actualización de Power-ups
        naveAmiga.actualizarPowerUps(delta);
        actualizarPowerUps(delta);
    }

    private void actualizarPowerUps(float delta) {
        for (int i = powerUps.size() - 1; i >= 0; i--) {
            PowerUp p = powerUps.get(i);
            if (p.estaVivo()) {
                p.mover(Ovni.Direccion.ABAJO, ConfiguracionJuego.PU_VELOCIDAD, delta);
                p.desaparecer(0);
            }
            if (p.getEstado() == Ovni.Estado.MUERTO) {
                powerUps.remove(i);
            }
        }
    }

    private void actualizarNaveEspecial(float anchoPantalla, float delta) {
        if (naveEspecial == null) {
            // Probabilidad de que aparezca
            if (random.nextFloat() < ConfiguracionJuego.ESP_PROB_APARICION) {
                float y = ConfiguracionJuego.VIRTUAL_HEIGHT - ConfiguracionJuego.ESP_ALTO - 20;
                Ovni.Direccion dir = random.nextBoolean() ? Ovni.Direccion.DERECHA : Ovni.Direccion.IZQUIERDA;
                float x = (dir == Ovni.Direccion.DERECHA) ? -ConfiguracionJuego.ESP_ANCHO : anchoPantalla;

                naveEspecial = new NaveEspecial(x, y, ConfiguracionJuego.ESP_ANCHO, ConfiguracionJuego.ESP_ALTO, dir, ConfiguracionJuego.ESP_VELOCIDAD, "enemigoMisterioso.png");
            }
        } else {
            if (naveEspecial.estaVivo()) {
                naveEspecial.actualizar(delta);

                // Lógica de ataque
                if (random.nextFloat() < ConfiguracionJuego.ESP_PROB_DISPARO * delta) {
                    naveEspecial.disparar();
                }
            }

            naveEspecial.gestionarMisDisparos(0f, delta);

            if (!naveEspecial.estaVivo() || naveEspecial.haSalido(anchoPantalla)) {
                // Si sale de la pantalla, la matamos para que deje de moverse/disparar
                if (naveEspecial.haSalido(anchoPantalla)) {
                    naveEspecial.setEstado(Ovni.Estado.MUERTO);
                }

                // Solo la eliminamos si no quedan disparos activos
                if (naveEspecial.getMisDisparos().isEmpty()) {
                    naveEspecial = null;
                }
            }
        }
    }

    public void soltarPowerUp(float x, float y) {
        if (random.nextFloat() < ConfiguracionJuego.PU_PROB_DROP) {
            PowerUp.Tipo tipo = PowerUp.Tipo.values()[random.nextInt(PowerUp.Tipo.values().length)];
            String textura = "";
            switch (tipo) {
                case MULTI_DISPARO: textura = "DisparoMultiple.png"; break;
                case ESCUDO: textura = "Escudo.png"; break;
                case VELOCIDAD: textura = "Velocidad.png"; break;
            }
            powerUps.add(new PowerUp(x, y, ConfiguracionJuego.PU_ANCHO, ConfiguracionJuego.PU_ALTO, tipo, textura));
        }
    }

    /**
     * Cambia la dirección de movimiento de la nave del jugador según el punto de toque.
     * @param x Coordenada X donde el usuario ha pulsado.
     */
    public void cambiarSentidoNaveAmiga(float x) {
        if (x > naveAmiga.getX() && naveAmiga.getDir() != Ovni.Direccion.DERECHA) {
            naveAmiga.setDir(Ovni.Direccion.DERECHA);
        } else if (x > naveAmiga.getX() && naveAmiga.getDir() == Ovni.Direccion.DERECHA) {
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        } else if (x < naveAmiga.getX() && naveAmiga.getDir() != Ovni.Direccion.IZQUIERDA) {
            naveAmiga.setDir(Ovni.Direccion.IZQUIERDA);
        } else if (x < naveAmiga.getX() && naveAmiga.getDir() == Ovni.Direccion.IZQUIERDA) {
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
    }

    public NaveAmi getNaveAmiga() {
        return naveAmiga;
    }

    public Batallon getBatallon() {
        return batallon;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public NaveEspecial getNaveEspecial() {
        return naveEspecial;
    }

    public List<Bunker> getBunkeres() {
        return bunkeres;
    }

    public void moverNaveAmiga(Ovni.Direccion direccion) {
        naveAmiga.setDir(direccion);
    }

    public void dispararNaveAmiga() {
        if (contadorTiempoAmigo >= ConfiguracionJuego.NAVE_CADENCIA) {
            naveAmiga.disparar();
            contadorTiempoAmigo = 0f;
        }
    }
}
