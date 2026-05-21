package com.politecnicomalaga.sp.control;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GestorPreferencias {
    public enum Dificultad {
        FACIL, NORMAL, DIFICIL, PERSONALIZADO
    }

    private static GestorPreferencias instancia;
    private final Preferences prefs;

    private Dificultad dificultadActual;
    private boolean musicaActivada;
    private boolean sfxActivado;
    private boolean pantallaCompleta;

    // Valores personalizados (multiplicadores)
    private float multProbDisparoPersonalizado;
    private float multVelBatallonPersonalizado;
    private float multVelBalaEnemigaPersonalizado;

    private GestorPreferencias() {
        prefs = Gdx.app.getPreferences("SpaceInvadersV2Prefs");
        cargarPreferencias();
    }

    public static GestorPreferencias getInstancia() {
        if (instancia == null) {
            instancia = new GestorPreferencias();
        }
        return instancia;
    }

    private void cargarPreferencias() {
        String difStr = prefs.getString("dificultad", "NORMAL");
        try {
            dificultadActual = Dificultad.valueOf(difStr);
        } catch (IllegalArgumentException e) {
            dificultadActual = Dificultad.NORMAL;
        }

        musicaActivada = prefs.getBoolean("musica", true);
        sfxActivado = prefs.getBoolean("sfx", true);
        pantallaCompleta = prefs.getBoolean("pantallaCompleta", false);

        multProbDisparoPersonalizado = prefs.getFloat("custMultProbDisp", 1.0f);
        multVelBatallonPersonalizado = prefs.getFloat("custMultVelBat", 1.0f);
        multVelBalaEnemigaPersonalizado = prefs.getFloat("custMultVelBalaEne", 1.0f);
    }

    public void guardarPreferencias() {
        prefs.putString("dificultad", dificultadActual.name());
        prefs.putBoolean("musica", musicaActivada);
        prefs.putBoolean("sfx", sfxActivado);
        prefs.putBoolean("pantallaCompleta", pantallaCompleta);

        prefs.putFloat("custMultProbDisp", multProbDisparoPersonalizado);
        prefs.putFloat("custMultVelBat", multVelBatallonPersonalizado);
        prefs.putFloat("custMultVelBalaEne", multVelBalaEnemigaPersonalizado);
        
        prefs.flush();
        aplicarAjustesPantalla();
    }

    public void aplicarAjustesPantalla() {
        if (pantallaCompleta) {
            Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
        } else {
            // Valores por defecto de ventana si estuviera en PC
            Gdx.graphics.setWindowedMode(800, 600);
        }
    }

    // --- Getters que aplican multiplicadores según la dificultad ---

    public int getProbabilidadDisparo() {
        switch (dificultadActual) {
            case FACIL: return (int)(ConfiguracionJuego.ENE_PROB_DISPARO * 0.5f);
            case NORMAL: return ConfiguracionJuego.ENE_PROB_DISPARO;
            case DIFICIL: return (int)(ConfiguracionJuego.ENE_PROB_DISPARO * 1.5f);
            case PERSONALIZADO: return (int)(ConfiguracionJuego.ENE_PROB_DISPARO * multProbDisparoPersonalizado);
            default: return ConfiguracionJuego.ENE_PROB_DISPARO;
        }
    }

    public float getVelocidadBatallon() {
        switch (dificultadActual) {
            case FACIL: return ConfiguracionJuego.BAT_VELOCIDAD * 0.7f;
            case NORMAL: return ConfiguracionJuego.BAT_VELOCIDAD;
            case DIFICIL: return ConfiguracionJuego.BAT_VELOCIDAD * 1.3f;
            case PERSONALIZADO: return ConfiguracionJuego.BAT_VELOCIDAD * multVelBatallonPersonalizado;
            default: return ConfiguracionJuego.BAT_VELOCIDAD;
        }
    }

    public float getVelocidadBalaEnemiga() {
        switch (dificultadActual) {
            case FACIL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD * 0.8f;
            case NORMAL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD;
            case DIFICIL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD * 1.2f;
            case PERSONALIZADO: return ConfiguracionJuego.BALA_ENE_VELOCIDAD * multVelBalaEnemigaPersonalizado;
            default: return ConfiguracionJuego.BALA_ENE_VELOCIDAD;
        }
    }

    // --- Getters y Setters Simples ---

    public Dificultad getDificultadActual() { return dificultadActual; }
    public void setDificultadActual(Dificultad dificultadActual) { this.dificultadActual = dificultadActual; }

    public boolean isMusicaActivada() { return musicaActivada; }
    public void setMusicaActivada(boolean musicaActivada) { this.musicaActivada = musicaActivada; }

    public boolean isSfxActivado() { return sfxActivado; }
    public void setSfxActivado(boolean sfxActivado) { this.sfxActivado = sfxActivado; }

    public boolean isPantallaCompleta() { return pantallaCompleta; }
    public void setPantallaCompleta(boolean pantallaCompleta) { this.pantallaCompleta = pantallaCompleta; }

    public float getMultProbDisparoPersonalizado() { return multProbDisparoPersonalizado; }
    public void setMultProbDisparoPersonalizado(float mult) { this.multProbDisparoPersonalizado = mult; }

    public float getMultVelBatallonPersonalizado() { return multVelBatallonPersonalizado; }
    public void setMultVelBatallonPersonalizado(float mult) { this.multVelBatallonPersonalizado = mult; }

    public float getMultVelBalaEnemigaPersonalizado() { return multVelBalaEnemigaPersonalizado; }
    public void setMultVelBalaEnemigaPersonalizado(float mult) { this.multVelBalaEnemigaPersonalizado = mult; }
}
