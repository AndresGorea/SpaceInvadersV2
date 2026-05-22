package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.Ovni;
import com.politecnicomalaga.sp.model.PowerUp;

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
    private GestorBunker gestorBunker;
    private List<PowerUp> powerUps;
    private Random random;
    private float contadorTiempoAmigo;
    private float contadorTiempoEnemigo;

    public GestorMundo() {
        this.contadorTiempoAmigo = 0f;
        this.contadorTiempoEnemigo = 0f;
        this.powerUps = new ArrayList<>();
        this.random = new Random();
        inicializarMundo();
    }

    /**
     * Configura la posición inicial y parámetros de las entidades al comenzar el juego.
     */
    private void inicializarMundo() {
        // Posicionamiento de la nave del jugador (centrada horizontalmente)
        float naveInicioX = (Gdx.graphics.getWidth() / 2f) - (ConfiguracionJuego.NAVE_ANCHO / 2f);
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
        float batInicioY = Gdx.graphics.getHeight() - 40f;

        this.batallon = new Batallon(
            batInicioX, batInicioY, ConfiguracionJuego.BAT_ESPACIO_VERT,
            ConfiguracionJuego.ENE_ANCHO, ConfiguracionJuego.ENE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.DERECHA,
            "enemigo1.png",
            ConfiguracionJuego.ENE_VIDAS,
            ConfiguracionJuego.ENE_CADENCIA,
            ConfiguracionJuego.BALA_ENE_ANCHO, ConfiguracionJuego.BALA_ENE_ALTO, ConfiguracionJuego.BALA_ENE_VELOCIDAD,
            ConfiguracionJuego.ENE_PROB_DISPARO,
            ConfiguracionJuego.BAT_ESPACIO_HORIZ, ConfiguracionJuego.BAT_VELOCIDAD
        );
    }

    /**
     * Actualiza el estado de todas las entidades del juego basándose en el tiempo transcurrido.
     * @param anchoPantalla Límite horizontal para el movimiento.
     * @param altoPantalla Límite vertical para proyectiles.
     * @param delta Tiempo desde el último frame.
     */
    public void actualizar(float anchoPantalla, float altoPantalla, float delta) {
        // Gestión de disparos automáticos del jugador según cadencia
        contadorTiempoAmigo += delta;
        if (contadorTiempoAmigo >= ConfiguracionJuego.NAVE_CADENCIA) {
            naveAmiga.disparar();
            contadorTiempoAmigo = 0f;
        }

        // Gestión de disparos del batallón enemigo
        contadorTiempoEnemigo += delta;
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
}
