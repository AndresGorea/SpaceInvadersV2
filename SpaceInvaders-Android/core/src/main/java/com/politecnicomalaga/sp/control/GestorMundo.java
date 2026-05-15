package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.Ovni;

public class GestorMundo {
    private NaveAmi naveAmiga;
    private Batallon batallon;
    private float contadorTiempoAmigo;
    private float contadorTiempoEnemigo;

    public GestorMundo() {
        this.contadorTiempoAmigo = 0f;
        this.contadorTiempoEnemigo = 0f;
        inicializarMundo();
    }

    private void inicializarMundo() {
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

    public void actualizar(float anchoPantalla, float altoPantalla, float delta) {
        // Disparos Amigos
        contadorTiempoAmigo += delta;
        if (contadorTiempoAmigo >= ConfiguracionJuego.NAVE_CADENCIA) {
            naveAmiga.disparar();
            contadorTiempoAmigo = 0f;
        }

        // Disparos Enemigos
        contadorTiempoEnemigo += delta;
        if (contadorTiempoEnemigo >= ConfiguracionJuego.ENE_CADENCIA) {
            batallon.disparar();
            contadorTiempoEnemigo = 0f;
        }

        // Movimiento Nave Amiga
        if (naveAmiga.getX() > anchoPantalla - naveAmiga.getWidth()) {
            naveAmiga.setX(anchoPantalla - naveAmiga.getWidth());
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
        if (naveAmiga.getX() < 0) {
            naveAmiga.setX(0);
            naveAmiga.setDir(Ovni.Direccion.NOMOVER);
        }
        naveAmiga.mover(naveAmiga.getDir(), ConfiguracionJuego.NAVE_VELOCIDAD, delta);

        // Movimiento Batallón
        batallon.mover(anchoPantalla, altoPantalla, 20f, delta);

        // Gestión de disparos
        naveAmiga.gestionarMisDisparos(altoPantalla, delta);
        batallon.gestionarDisparos(0f, delta);
    }

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
}
