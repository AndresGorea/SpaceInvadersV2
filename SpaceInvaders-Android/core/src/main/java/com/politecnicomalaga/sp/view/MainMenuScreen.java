package com.politecnicomalaga.sp.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private Texture starTexture1, starTexture2, starTexture3;
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

        // --- TITLE ---
        Label.LabelStyle titleStyle = new Label.LabelStyle(game.getFont(), Color.valueOf("00ffcc")); // Cyan-green
        Label titleLabel = new Label("SPACE INVADERS", titleStyle);
        titleLabel.setFontScale(3.0f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setOrigin(Align.center);

        // Pulsating animation for title
        titleLabel.addAction(Actions.forever(
            Actions.sequence(
                Actions.scaleTo(3.2f, 3.2f, 1.5f),
                Actions.scaleTo(2.8f, 2.8f, 1.5f)
            )
        ));

        // --- SUBTITLE ---
        Label.LabelStyle subTitleStyle = new Label.LabelStyle(game.getFont(), Color.valueOf("aaaaaa"));
        Label subTitleLabel = new Label("ARCADE EDITION", subTitleStyle);
        subTitleLabel.setFontScale(1.2f);
        subTitleLabel.setAlignment(Align.center);

        // --- BUTTONS ---
        TextButton playButton = createAnimatedButton("INICIAR");
        TextButton optionsButton = createAnimatedButton("CONFIGURACIÓN");
        TextButton infoButton = createAnimatedButton("INFORMACIÓN");
        TextButton exitButton = createAnimatedButton("SALIR");

        // Button Actions
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Add a small delay before changing screen for the click effect to finish
                stage.addAction(Actions.sequence(
                    Actions.delay(0.1f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            game.setScreen(new GameScreen(game));
                        }
                    })
                ));
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
                stage.addAction(Actions.sequence(
                    Actions.fadeOut(0.3f),
                    Actions.run(new Runnable() {
                        @Override
                        public void run() {
                            Gdx.app.exit();
                        }
                    })
                ));
            }
        });

        // --- LAYOUT ---
        table.add(titleLabel).padBottom(5).row();
        table.add(subTitleLabel).padBottom(50).row();

        float btnWidth = 300f;
        float btnHeight = 60f;
        float btnPadding = 10f;

        table.add(playButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(optionsButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(infoButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
        table.add(exitButton).width(btnWidth).height(btnHeight).pad(btnPadding).row();
    }

    private TextButton createAnimatedButton(String text) {
        final TextButton button = new TextButton(text, skin);
        button.setTransform(true); // Required for scaling/rotation
        button.setOrigin(Align.center);

        button.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                if (pointer == -1) { // Only trigger on mouse enter, not touch drag
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1.1f, 1.1f, 0.1f));
                }
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (pointer == -1) {
                    button.clearActions();
                    button.addAction(Actions.scaleTo(1f, 1f, 0.1f));
                }
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int buttonKey) {
                button.clearActions();
                button.addAction(Actions.scaleTo(0.9f, 0.9f, 0.05f));
                return super.touchDown(event, x, y, pointer, buttonKey);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int buttonKey) {
                super.touchUp(event, x, y, pointer, buttonKey);
                button.clearActions();
                button.addAction(Actions.scaleTo(1.1f, 1.1f, 0.05f));
            }
        });
        return button;
    }

    private void createBasicSkin() {
        skin = new Skin();

        // Cyberpunk/Retro Button Style
        skin.add("btn_up", createButtonTexture(Color.valueOf("0a0a1a"), Color.valueOf("00ffcc"), 2));
        skin.add("btn_down", createButtonTexture(Color.valueOf("004433"), Color.valueOf("00ffcc"), 4));
        skin.add("btn_hover", createButtonTexture(Color.valueOf("1a1a3a"), Color.valueOf("ffffff"), 2));

        skin.add("default", game.getFont());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("btn_up");
        textButtonStyle.down = skin.newDrawable("btn_down");
        textButtonStyle.over = skin.newDrawable("btn_hover");
        textButtonStyle.font = skin.getFont("default");
        textButtonStyle.fontColor = Color.valueOf("00ffcc");
        textButtonStyle.overFontColor = Color.WHITE;
        textButtonStyle.downFontColor = Color.YELLOW;

        skin.add("default", textButtonStyle);
    }

    private Texture createButtonTexture(Color fillColor, Color borderColor, int borderThickness) {
        int width = 300;
        int height = 60;
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);

        pixmap.setColor(fillColor);
        pixmap.fill();

        pixmap.setColor(borderColor);
        for(int i=0; i<borderThickness; i++) {
            pixmap.drawRectangle(i, i, width - (i*2), height - (i*2));
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void createStars() {
        stars = new ArrayList<>();

        // 3 Layers of parallax stars
        starTexture1 = createStarTexture(1, Color.valueOf("444444")); // Far (slow, dark)
        starTexture2 = createStarTexture(2, Color.valueOf("888888")); // Mid (medium, gray)
        starTexture3 = createStarTexture(3, Color.valueOf("ffffff")); // Near (fast, white)

        for (int i = 0; i < 80; i++) stars.add(new Star(1));
        for (int i = 0; i < 40; i++) stars.add(new Star(2));
        for (int i = 0; i < 20; i++) stars.add(new Star(3));
    }

    private Texture createStarTexture(int size, Color color) {
        Pixmap pixmap = new Pixmap(size, size, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();
        Texture tex = new Texture(pixmap);
        pixmap.dispose();
        return tex;
    }

    private void createFloatingEntities() {
        floatingEntities = new ArrayList<>();
        String[] textures = {"enemigo1.png", "enemigo2.png", "naveJugador.png"};

        for (int i = 0; i < 4; i++) {
            String texName = textures[MathUtils.random(0, textures.length - 1)];
            Texture tex = game.getGaleriaImagenes().get(texName);
            floatingEntities.add(new FloatingEntity(new TextureRegion(tex)));
        }
    }

    private void updateAndDrawBackground(float delta) {
        game.getBatch().begin();

        // Draw Parallax Stars
        for (Star star : stars) {
            star.y -= star.speed * delta;
            if (star.y < 0) {
                star.y = Gdx.graphics.getHeight();
                star.x = MathUtils.random(0, Gdx.graphics.getWidth());
            }

            Texture tex = starTexture1;
            if (star.layer == 2) tex = starTexture2;
            else if (star.layer == 3) tex = starTexture3;

            game.getBatch().draw(tex, star.x, star.y);
        }

        // Draw Floating Entities (with rotation and sine wave)
        for (FloatingEntity entity : floatingEntities) {
            entity.update(delta);
            game.getBatch().draw(
                entity.textureRegion,
                entity.x, entity.y,
                entity.width / 2f, entity.height / 2f, // origin for rotation
                entity.width, entity.height,
                1f, 1f, // scale
                entity.rotation
            );
        }

        game.getBatch().end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        // Fade in effect
        stage.getRoot().getColor().a = 0;
        stage.addAction(Actions.fadeIn(0.5f));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.01f, 0.01f, 0.05f, 1); // Deep space black
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
        starTexture1.dispose();
        starTexture2.dispose();
        starTexture3.dispose();
    }

    // --- Helper Classes ---

    private class Star {
        float x, y, speed;
        int layer;

        public Star(int layer) {
            this.layer = layer;
            this.x = MathUtils.random(0, Gdx.graphics.getWidth());
            this.y = MathUtils.random(0, Gdx.graphics.getHeight());

            if (layer == 1) speed = MathUtils.random(10, 30);
            else if (layer == 2) speed = MathUtils.random(40, 70);
            else speed = MathUtils.random(90, 150);
        }
    }

    private class FloatingEntity {
        TextureRegion textureRegion;
        float x, y, speedX;
        float width, height;
        float rotation, rotationSpeed;
        float baseY, sineTime, sineAmplitude, sineFrequency;

        public FloatingEntity(TextureRegion textureRegion) {
            this.textureRegion = textureRegion;
            this.width = 60f;
            this.height = 60f;
            resetPosition();
        }

        public void update(float delta) {
            x += speedX * delta;

            // Sine wave movement on Y axis
            sineTime += delta;
            y = baseY + MathUtils.sin(sineTime * sineFrequency) * sineAmplitude;

            // Rotation
            rotation += rotationSpeed * delta;

            if (x > Gdx.graphics.getWidth() + 100 || x < -100) {
                resetPosition();
            }
        }

        private void resetPosition() {
            if (MathUtils.randomBoolean()) {
                x = -width;
                speedX = MathUtils.random(40, 120);
            } else {
                x = Gdx.graphics.getWidth();
                speedX = MathUtils.random(-120, -40);
            }
            baseY = MathUtils.random(50, Gdx.graphics.getHeight() - 50);
            sineTime = MathUtils.random(0, 10);
            sineAmplitude = MathUtils.random(20, 80);
            sineFrequency = MathUtils.random(1f, 3f);

            rotation = MathUtils.random(0, 360);
            rotationSpeed = MathUtils.random(-50, 50);
        }
    }
}
