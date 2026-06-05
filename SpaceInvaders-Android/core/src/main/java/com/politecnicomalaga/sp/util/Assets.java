package com.politecnicomalaga.sp.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase Singleton que centraliza la carga y gestión de recursos (texturas y música).
 * Evita la carga duplicada de archivos desde el almacenamiento.
 */
public class Assets {
    private static Assets instancia;
    private final Map<String, Texture> texturas;
    private final Map<String, Music> musicas;
    private final Map<String, Sound> sonidos;

    private Assets() {
        texturas = new HashMap<>();
        musicas = new HashMap<>();
        sonidos = new HashMap<>();
    }

    /**
     * Obtiene la instancia única del gestor de recursos.
     * @return La instancia de Assets.
     */
    public static Assets getInstance() {
        if (instancia == null) {
            instancia = new Assets();
        }
        return instancia;
    }

    /**
     * Carga una textura desde disco si no ha sido cargada previamente.
     * @param ruta Camino relativo al archivo en la carpeta assets.
     */
    public void cargarTextura(String ruta) {
        if (!texturas.containsKey(ruta)) {
            texturas.put(ruta, new Texture(ruta));
        }
    }

    /**
     * Recupera una textura ya cargada.
     * @param ruta Nombre o ruta de la textura.
     * @return El objeto {@link com.badlogic.gdx.graphics.Texture}.
     */
    public Texture getTexture(String ruta) {
        return texturas.get(ruta);
    }

    /**
     * Carga un archivo de música si no ha sido cargado previamente.
     * @param ruta Camino al archivo en assets.
     */
    public void cargarMusica(String ruta) {
        if (!musicas.containsKey(ruta)) {
            musicas.put(ruta, Gdx.audio.newMusic(Gdx.files.internal(ruta)));
        }
    }

    /**
     * Recupera una música ya cargada.
     * @param ruta Nombre o ruta del archivo.
     * @return El objeto Music.
     */
    public Music getMusic(String ruta) {
        return musicas.get(ruta);
    }

    public void cargarSonido(String ruta) {
        if (!sonidos.containsKey(ruta)) {
            sonidos.put(ruta, Gdx.audio.newSound(Gdx.files.internal(ruta)));
        }
    }

    public Sound getSound(String ruta) {
        return sonidos.get(ruta);
    }

    /**
     * Libera de la memoria todos los recursos cargados.
     */
    public void dispose() {
        for (Texture textura : texturas.values()) {
            textura.dispose();
        }
        for (Music musica : musicas.values()) {
            musica.dispose();
        }
        for (Sound sonido : sonidos.values()) {
            sonido.dispose();
        }
        texturas.clear();
        musicas.clear();
        sonidos.clear();
    }
}
