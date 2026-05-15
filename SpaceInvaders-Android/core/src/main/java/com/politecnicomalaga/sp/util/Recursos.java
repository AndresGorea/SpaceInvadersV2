package com.politecnicomalaga.sp.util;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class Recursos {
    private static Recursos instancia;
    private Map<String, Texture> texturas;

    private Recursos() {
        texturas = new HashMap<>();
    }

    public static Recursos getInstancia() {
        if (instancia == null) {
            instancia = new Recursos();
        }
        return instancia;
    }

    public void cargarTextura(String ruta) {
        if (!texturas.containsKey(ruta)) {
            texturas.put(ruta, new Texture(ruta));
        }
    }

    public Texture getTextura(String ruta) {
        return texturas.get(ruta);
    }

    public Map<String, Texture> getTexturas() {
        return texturas;
    }

    public void dispose() {
        for (Texture textura : texturas.values()) {
            textura.dispose();
        }
        texturas.clear();
    }
}
