package com.politecnicomalaga.sp.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Gestor de persistencia de datos del juego.
 * Utiliza LibGDX Preferences para guardar puntuaciones y configuración.
 */
public class SettingsManager {
    private static final String PREFS_NAME = "SpaceInvadersPrefs";
    private static final String KEY_HIGH_SCORE = "high_score";
    private static final String KEY_VOLUME = "volume";
    private static final String KEY_CONTROLS = "controls";

    private static SettingsManager instancia;
    private final Preferences prefs;

    private SettingsManager() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
    }

    public static SettingsManager getInstancia() {
        if (instancia == null) {
            instancia = new SettingsManager();
        }
        return instancia;
    }

    // --- High Score ---
    public int getHighScore() {
        return prefs.getInteger(KEY_HIGH_SCORE, 0);
    }

    public void setHighScore(int score) {
        if (score > getHighScore()) {
            prefs.putInteger(KEY_HIGH_SCORE, score);
            prefs.flush();
        }
    }

    // --- Volumen ---
    public float getVolumen() {
        return prefs.getFloat(KEY_VOLUME, 1.0f);
    }

    public void setVolumen(float volumen) {
        prefs.putFloat(KEY_VOLUME, volumen);
        prefs.flush();
    }

    // --- Controles ---
    // 0 para Clásico (Toque para girar), 1 para Moderno (Botones dedicados)
    public int getTipoControl() {
        int defecto = (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) ? 1 : 0;
        return prefs.getInteger(KEY_CONTROLS, defecto);
    }

    public void setTipoControl(int tipo) {
        prefs.putInteger(KEY_CONTROLS, tipo);
        prefs.flush();
    }
}
