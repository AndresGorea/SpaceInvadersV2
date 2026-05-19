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

    // Valores personalizados
    private int probDisparoPersonalizado;
    private float velBatallonPersonalizado;
    private float velBalaEnemigaPersonalizado;

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

        probDisparoPersonalizado = prefs.getInteger("custProbDisp", 7);
        velBatallonPersonalizado = prefs.getFloat("custVelBat", 80f);
        velBalaEnemigaPersonalizado = prefs.getFloat("custVelBalaEne", 400f);
    }

    public void guardarPreferencias() {
        prefs.putString("dificultad", dificultadActual.name());
        prefs.putBoolean("musica", musicaActivada);
        prefs.putBoolean("sfx", sfxActivado);
        prefs.putBoolean("pantallaCompleta", pantallaCompleta);

        prefs.putInteger("custProbDisp", probDisparoPersonalizado);
        prefs.putFloat("custVelBat", velBatallonPersonalizado);
        prefs.putFloat("custVelBalaEne", velBalaEnemigaPersonalizado);
        
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
            case PERSONALIZADO: return probDisparoPersonalizado;
            default: return ConfiguracionJuego.ENE_PROB_DISPARO;
        }
    }

    public float getVelocidadBatallon() {
        switch (dificultadActual) {
            case FACIL: return ConfiguracionJuego.BAT_VELOCIDAD * 0.7f;
            case NORMAL: return ConfiguracionJuego.BAT_VELOCIDAD;
            case DIFICIL: return ConfiguracionJuego.BAT_VELOCIDAD * 1.3f;
            case PERSONALIZADO: return velBatallonPersonalizado;
            default: return ConfiguracionJuego.BAT_VELOCIDAD;
        }
    }

    public float getVelocidadBalaEnemiga() {
        switch (dificultadActual) {
            case FACIL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD * 0.8f;
            case NORMAL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD;
            case DIFICIL: return ConfiguracionJuego.BALA_ENE_VELOCIDAD * 1.2f;
            case PERSONALIZADO: return velBalaEnemigaPersonalizado;
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

    public int getProbDisparoPersonalizado() { return probDisparoPersonalizado; }
    public void setProbDisparoPersonalizado(int prob) { this.probDisparoPersonalizado = prob; }

    public float getVelBatallonPersonalizado() { return velBatallonPersonalizado; }
    public void setVelBatallonPersonalizado(float vel) { this.velBatallonPersonalizado = vel; }

    public float getVelBalaEnemigaPersonalizado() { return velBalaEnemigaPersonalizado; }
    public void setVelBalaEnemigaPersonalizado(float vel) { this.velBalaEnemigaPersonalizado = vel; }
}
