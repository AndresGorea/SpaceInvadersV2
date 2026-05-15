package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.control.EstadoJuego;
import com.politecnicomalaga.sp.control.GestorMundo;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.util.Recursos;

import java.util.List;

public class RenderizadorMundo {

    public void renderizar(SpriteBatch lote, GestorMundo mundo) {
        Recursos recursos = Recursos.getInstancia();
        
        // Pintar naveAmiga
        NaveAmi naveAmiga = mundo.getNaveAmiga();
        lote.draw(recursos.getTextura(naveAmiga.getTextura()), naveAmiga.getX(), naveAmiga.getY(), naveAmiga.getWidth(), naveAmiga.getHeight());

        // Pintar navesEnemigas y sus disparos
        Batallon batallon = mundo.getBatallon();
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron esc : escuadrones) {
            NaveEne[] naveEnes = esc.getNavesEnemigas();
            for (NaveEne navE : naveEnes) {
                if (navE.estaVivo()) {
                    lote.draw(recursos.getTextura(navE.getTextura()), navE.getX(), navE.getY(), navE.getWidth(), navE.getHeight());
                }
                List<DisparoEne> disparosEnemigos = navE.getMisDisparos();
                for (DisparoEne disEne : disparosEnemigos) {
                    lote.draw(recursos.getTextura(disEne.getTextura()), disEne.getX(), disEne.getY(), disEne.getWidth(), disEne.getHeight());
                }
            }
        }

        // Pintar disparosAmigos
        List<DisparoAmi> disparosAmigos = naveAmiga.getMisDisparos();
        for (DisparoAmi dispAmi : disparosAmigos) {
            if (dispAmi.estaVivo()) {
                lote.draw(recursos.getTextura(dispAmi.getTextura()), dispAmi.getX(), dispAmi.getY(), dispAmi.getWidth(), dispAmi.getHeight());
            }
        }
    }

    public void renderizarHUD(SpriteBatch lote, EstadoJuego estado, BitmapFont fuente, float anchoPantalla, float altoPantalla) {
        Recursos recursos = Recursos.getInstancia();
        
        // Pintar Puntuación
        fuente.draw(lote, "Puntuación: " + estado.getPuntuacion(), 20, altoPantalla - 20);

        // Pintar Vidas
        float tamanoIcono = 30f;
        float margen = 10f;
        for (int i = 0; i < estado.getVidas(); i++) {
            lote.draw(recursos.getTextura("naveJugador.png"), 
                anchoPantalla - (i + 1) * (tamanoIcono + margen) - 10, 
                altoPantalla - tamanoIcono - 15, 
                tamanoIcono, tamanoIcono);
        }
    }
}
