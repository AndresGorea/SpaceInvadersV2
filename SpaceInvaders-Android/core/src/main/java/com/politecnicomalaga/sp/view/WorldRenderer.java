package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.control.GameState;
import com.politecnicomalaga.sp.control.WorldManager;
import com.politecnicomalaga.sp.model.Batallon;
import com.politecnicomalaga.sp.model.DisparoAmi;
import com.politecnicomalaga.sp.model.DisparoEne;
import com.politecnicomalaga.sp.model.Escuadron;
import com.politecnicomalaga.sp.model.NaveAmi;
import com.politecnicomalaga.sp.model.NaveEne;
import com.politecnicomalaga.sp.util.Assets;

import java.util.List;
import java.util.Map;

public class WorldRenderer {

    public void render(SpriteBatch batch, WorldManager world) {
        Assets assets = Assets.getInstance();
        
        // Pintar naveAmiga
        NaveAmi naveAmiga = world.getNaveAmiga();
        batch.draw(assets.getTexture(naveAmiga.getTextura()), naveAmiga.getX(), naveAmiga.getY(), naveAmiga.getWidth(), naveAmiga.getHeight());

        // Pintar navesEnemigas y sus disparos
        Batallon batallon = world.getBatallon();
        Escuadron[] escuadrones = batallon.getEscuadrones();
        for (Escuadron esc : escuadrones) {
            NaveEne[] naveEnes = esc.getNavesEnemigas();
            for (NaveEne navE : naveEnes) {
                if (navE.estaVivo()) {
                    batch.draw(assets.getTexture(navE.getTextura()), navE.getX(), navE.getY(), navE.getWidth(), navE.getHeight());
                }
                List<DisparoEne> disparosEnemigos = navE.getMisDisparos();
                for (DisparoEne disEne : disparosEnemigos) {
                    batch.draw(assets.getTexture(disEne.getTextura()), disEne.getX(), disEne.getY(), disEne.getWidth(), disEne.getHeight());
                }
            }
        }

        // Pintar disparosAmigos
        List<DisparoAmi> disparosAmigos = naveAmiga.getMisDisparos();
        for (DisparoAmi dispAmi : disparosAmigos) {
            if (dispAmi.estaVivo()) {
                batch.draw(assets.getTexture(dispAmi.getTextura()), dispAmi.getX(), dispAmi.getY(), dispAmi.getWidth(), dispAmi.getHeight());
            }
        }
    }

    public void renderHUD(SpriteBatch batch, GameState state, BitmapFont font, float anchoPantalla, float altoPantalla) {
        Assets assets = Assets.getInstance();
        
        // Pintar Puntuación
        font.draw(batch, "Puntuación: " + state.getPuntuacion(), 20, altoPantalla - 20);

        // Pintar Vidas
        float tamañoIcono = 30f;
        float margen = 10f;
        for (int i = 0; i < state.getVidas(); i++) {
            batch.draw(assets.getTexture("naveJugador.png"), 
                anchoPantalla - (i + 1) * (tamañoIcono + margen) - 10, 
                altoPantalla - tamañoIcono - 15, 
                tamañoIcono, tamañoIcono);
        }
    }
}
