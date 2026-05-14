package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.politecnicomalaga.sp.Main;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    
    // Background Elements
    private List<Star> stars;
    private Texture starTexture;
    private List<FloatingEntity> floatingEntities;

    public MainMenuScreen(final Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createBasicSkin();
        createStars();
        createFloatingEntities();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.getFont(), Color.GREEN);
        Label titleLabel = new Label("SPACE INVADERS", titleStyle);
        titleLabel.setFontScale(3.0f);
        titleLabel.setAlignment(Align.center);

        // Subtitle / Version
        Label.LabelStyle subTitleStyle = new Label.LabelStyle(game.getFont(), Color.LIGHT_GRAY);
        Label subTitleLabel = new Label("V2 - UI Update", subTitleStyle);
        subTitleLabel.setFontScale(1.0f);

        // Buttons
        TextButton playButton = new TextButton("Jugar", skin);
        TextButton optionsButton = new TextButton("Opciones", skin);
        TextButton infoButton = new TextButton("Información", skin);
        TextButton exitButton = new TextButton("Salir", skin);

        // Button Actions
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });
        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Opciones clicked");
            }
        });
        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Información clicked");
            }
        });
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        // Layout layout
        table.add(titleLabel).padBottom(10).row();
        table.add(subTitleLabel).padBottom(60).row();
        
        float btnWidth = 250f;
        float btnHeight = 60f;
        float btnPadding = 15f;

        table.add(playButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(optionsButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(infoButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(exitButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
    }

    private void createBasicSkin() {
        skin = new Skin();

        // Create textures for button states (Up, Down, Hover) using Pixmap
        skin.add("btn_up", createButtonTexture(Color.DARK_GRAY, Color.valueOf("00ff00"))); // Green border
        skin.add("btn_down", createButtonTexture(Color.valueOf("004400"), Color.valueOf("00ff00"))); // Dark green fill
        skin.add("btn_hover", createButtonTexture(Color.GRAY, Color.valueOf("00ff00"))); // Light gray fill

        skin.add("default", game.getFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btn_up");
        textButtonStyle.down = skin.newDrawable("btn_down");
        textButtonStyle.over = skin.newDrawable("btn_hover"); // Hover effect
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.WHITE;
        textButtonStyle.overFontColor = Color.GREEN;
        textButtonStyle.downFontColor = Color.YELLOW;
        
        skin.add("default", textButtonStyle);
    }

    // Helper to draw a bordered rectangle programmatically
    private Texture createButtonTexture(Color fillColor, Color borderColor) {
        int width = 250;
        int height = 60;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        
        // Fill
        pixmap.setColor(fillColor);
        pixmap.fill();
        
        // Border
        pixmap.setColor(borderColor);
        pixmap.drawRectangle(0, 0, width, height);
        pixmap.drawRectangle(1, 1, width - 2, height - 2); // 2px border
        
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void createStars() {
        stars = new ArrayList<>();
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fillCircle(1, 1, 1);
        starTexture = new Texture(pixmap);
        pixmap.dispose();

        for (int i = 0; i < 150; i++) {
            stars.add(new Star());
        }
    }

    private void createFloatingEntities() {
        floatingEntities = new ArrayList<>();
        String[] textures = {"enemigo1.png", "enemigo2.png", "naveJugador.png"};
        
        for (int i = 0; i < 5; i++) {
            String texName = textures[MathUtils.random(0, textures.length - 1)];
            Texture tex = game.getGaleriaImagenes().get(texName);
            floatingEntities.add(new FloatingEntity(tex));
        }
    }

    private void updateAndDrawBackground(float delta) {
        game.getBatch().begin();
        
        // Draw Stars
        for (Star star : stars) {
            star.y -= star.speed * delta;
            if (star.y < 0) {
                star.y = Gdx.graphics.getHeight();
                star.x = MathUtils.random(0, Gdx.graphics.getWidth());
            }
            // Add a slight twinkle effect based on time and position
            float alpha = 0.5f + 0.5f * MathUtils.sin(star.y * 0.1f);
            game.getBatch().setColor(1, 1, 1, alpha);
            game.getBatch().draw(starTexture, star.x, star.y);
        }
        game.getBatch().setColor(1, 1, 1, 1); // Reset color

        // Draw Floating Entities
        for (FloatingEntity entity : floatingEntities) {
            entity.update(delta);
            game.getBatch().draw(entity.texture, entity.x, entity.y, entity.width, entity.height);
        }
        
        game.getBatch().end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.02f, 0.02f, 0.08f, 1); // Very dark blue/black space
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateAndDrawBackground(delta);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        starTexture.dispose();
    }

    // --- Helper Classes for Background Animation ---

    private class Star {
        float x, y, speed;
        public Star() {
            x = MathUtils.random(0, Gdx.graphics.getWidth());
            y = MathUtils.random(0, Gdx.graphics.getHeight());
            speed = MathUtils.random(10, 80);
        }
    }

    private class FloatingEntity {
        Texture texture;
        float x, y, speedX, speedY;
        float width, height;

        public FloatingEntity(Texture texture) {
            this.texture = texture;
            this.width = 50f;
            this.height = 50f;
            resetPosition();
        }

        public void update(float delta) {
            x += speedX * delta;
            y += speedY * delta;

            // If it goes completely off screen, reset it
            if (x > Gdx.graphics.getWidth() + 100 || x < -100 || 
                y > Gdx.graphics.getHeight() + 100 || y < -100) {
                resetPosition();
            }
        }

        private void resetPosition() {
            // Randomly start from left or right edge
            if (MathUtils.randomBoolean()) {
                x = -width;
                speedX = MathUtils.random(30, 100);
            } else {
                x = Gdx.graphics.getWidth();
                speedX = MathUtils.random(-100, -30);
            }
            y = MathUtils.random(0, Gdx.graphics.getHeight());
            speedY = MathUtils.random(-20, 20);
        }
    }
}