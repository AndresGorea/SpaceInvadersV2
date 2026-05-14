package com.politecnicomalaga.sp.util;

import com.badlogic.gdx.graphics.Texture;
import java.util.HashMap;
import java.util.Map;

public class Assets {
    private static Assets instance;
    private Map<String, Texture> textures;

    private Assets() {
        textures = new HashMap<>();
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void loadTexture(String path) {
        if (!textures.containsKey(path)) {
            textures.put(path, new Texture(path));
        }
    }

    public Texture getTexture(String path) {
        return textures.get(path);
    }

    public Map<String, Texture> getTextures() {
        return textures;
    }

    public void dispose() {
        for (Texture texture : textures.values()) {
            texture.dispose();
        }
        textures.clear();
    }
}
