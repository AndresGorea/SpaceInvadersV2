package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.politecnicomalaga.sp.view.WorldRenderer;
import java.util.Map;

public class Controlador {
    private static Controlador miSingle;

    private WorldManager worldManager;
    private CollisionManager collisionManager;
    private GameState gameState;
    private WorldRenderer worldRenderer;

    private Controlador() {
        this.gameState = new GameState(GameConfig.NAVE_VIDAS);
        this.worldManager = new WorldManager();
        this.collisionManager = new CollisionManager();
        this.worldRenderer = new WorldRenderer();
    }

    public static Controlador getInstance() {
        if (miSingle == null) {
            miSingle = new Controlador();
        }
        return miSingle;
    }

    public void click(float x, float y) {
        worldManager.cambiarSentidoNaveAmiga(x);
    }

    public void simulaMundo(float anchoPantalla, float altoPantalla, float delta) {
        if (gameState.isJugando()) {
            // Actualizar lógica del mundo
            worldManager.update(anchoPantalla, altoPantalla, delta);
            
            // Comprobar colisiones
            collisionManager.checkCollisions(worldManager, gameState);

            // Comprobar si se ha ganado
            if (!worldManager.getBatallon().tieneTropas()) {
                gameState.setJugando(false);
            }
        }
    }

    public void pintar(SpriteBatch batch, Map<String, Texture> galeriaImagenes) {
        worldRenderer.render(batch, worldManager);
    }

    public void pintarHUD(SpriteBatch batch, Map<String, Texture> galeriaImagenes, BitmapFont font, float anchoPantalla, float altoPantalla) {
        worldRenderer.renderHUD(batch, gameState, font, anchoPantalla, altoPantalla);
    }

    // Getters para acceso si es necesario (aunque se prefiere delegación)
    public GameState getGameState() {
        return gameState;
    }

    public WorldManager getWorldManager() {
        return worldManager;
    }
}
