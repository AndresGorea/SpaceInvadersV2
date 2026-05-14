package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.Ovni;

public class WorldManager {
    private NaveAmi naveAmiga;
    private Batallon batallon;
    private float contadorTiempoAmigo;
    private float contadorTiempoEnemigo;

    public WorldManager() {
        this.contadorTiempoAmigo = 0f;
        this.contadorTiempoEnemigo = 0f;
        inicializarMundo();
    }

    private void inicializarMundo() {
        float naveInicioX = (Gdx.graphics.getWidth() / 2f) - (GameConfig.NAVE_ANCHO / 2f);
        float naveInicioY = 10f;

        this.naveAmiga = new NaveAmi(
            naveInicioX, naveInicioY, GameConfig.NAVE_ANCHO, GameConfig.NAVE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.NOMOVER,
            "naveJugador.png",
            GameConfig.NAVE_VIDAS,
            GameConfig.NAVE_CADENCIA,
            GameConfig.BALA_AMI_ANCHO, GameConfig.BALA_AMI_ALTO, GameConfig.BALA_AMI_VELOCIDAD
        );

        float batInicioX = 20f;
        float batInicioY = Gdx.graphics.getHeight() - 40f;

        this.batallon = new Batallon(
            batInicioX, batInicioY, GameConfig.BAT_ESPACIO_VERT,
            GameConfig.ENE_ANCHO, GameConfig.ENE_ALTO,
            Ovni.Estado.VIVO,
            Ovni.Direccion.DERECHA,
            "enemigo1.png",
            GameConfig.ENE_VIDAS,
            GameConfig.ENE_CADENCIA,
            GameConfig.BALA_ENE_ANCHO, GameConfig.BALA_ENE_ALTO, GameConfig.BALA_ENE_VELOCIDAD,
            GameConfig.ENE_PROB_DISPARO,
            GameConfig.BAT_ESPACIO_HORIZ, GameConfig.BAT_VELOCIDAD
        );
    }

    public void update(float anchoPantalla, float altoPantalla, float delta) {
        // Disparos Amigos
        contadorTiempoAmigo += delta;
        if (contadorTiempoAmigo >= GameConfig.NAVE_CADENCIA) {
            naveAmiga.disparar();
            contadorTiempoAmigo = 0f;
        }

        // Disparos Enemigos
        contadorTiempoEnemigo += delta;
        if (contadorTiempoEnemigo >= GameConfig.ENE_CADENCIA) {
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
        naveAmiga.mover(naveAmiga.getDir(), GameConfig.NAVE_VELOCIDAD, delta);

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
