package com.politecnicomalaga.sp.util;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase Singleton que centraliza la carga y gestión de recursos (texturas).
 * Evita la carga duplicada de archivos desde el almacenamiento.
 */
public class Assets {
    private static Assets instancia;
    private Map<String, Texture> texturas;

    private Assets() {
        texturas = new HashMap<>();
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
     * Libera de la memoria de video todas las texturas cargadas.
     * Debe llamarse al cerrar el juego.
     */
    public void dispose() {
        for (Texture textura : texturas.values()) {
            textura.dispose();
        }
        texturas.clear();
    }
}
