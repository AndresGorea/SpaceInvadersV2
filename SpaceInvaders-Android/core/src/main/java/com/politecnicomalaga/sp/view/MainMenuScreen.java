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
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.politecnicomalaga.sp.Main;

import java.util.ArrayList;
import java.util.List;

public class MainMenuScreen implements Screen {

    private final Main game;
    private Stage stage;
    private Skin skin;
    private List<Star> stars;
    private Texture starTexture;

    public MainMenuScreen(final Main game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        createBasicSkin();
        createStars();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label.LabelStyle titleStyle = new Label.LabelStyle(game.getFont(), Color.YELLOW);
        Label titleLabel = new Label("SPACE INVADERS", titleStyle);
        titleLabel.setFontScale(2.0f);

        TextButton playButton = new TextButton("Jugar", skin);
        TextButton optionsButton = new TextButton("Opciones", skin);
        TextButton infoButton = new TextButton("Información", skin);
        TextButton exitButton = new TextButton("Salir", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game));
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(new OptionsScreen(game)); // To be implemented
                System.out.println("Opciones clicked");
            }
        });

        infoButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // game.setScreen(new InfoScreen(game)); // To be implemented
                System.out.println("Información clicked");
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        table.add(titleLabel).padBottom(50).row();
        table.add(playButton).fillX().uniformX().pad(10).row();
        table.add(optionsButton).fillX().uniformX().pad(10).row();
        table.add(infoButton).fillX().uniformX().pad(10).row();
        table.add(exitButton).fillX().uniformX().pad(10).row();
    }

    private void createBasicSkin() {
        skin = new Skin();
        
        // Generate a 1x1 white texture and store it in the skin
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));

        // Create a BitmapFont for the skin
        skin.add("default", new BitmapFont());

        // Configure a TextButtonStyle and add it to the skin
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
        textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);
    }

    private void createStars() {
        stars = new ArrayList<>();
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        starTexture = new Texture(pixmap);
        pixmap.dispose();

        for (int i = 0; i < 100; i++) {
            stars.add(new Star());
        }
    }

    private void updateAndDrawStars(float delta) {
        game.getBatch().begin();
        for (Star star : stars) {
            star.y -= star.speed * delta;
            if (star.y < 0) {
                star.y = Gdx.graphics.getHeight();
                star.x = MathUtils.random(0, Gdx.graphics.getWidth());
            }
            game.getBatch().draw(starTexture, star.x, star.y);
        }
        game.getBatch().end();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.05f, 0.05f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateAndDrawStars(delta);

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

    private class Star {
        float x, y, speed;
        public Star() {
            x = MathUtils.random(0, Gdx.graphics.getWidth());
            y = MathUtils.random(0, Gdx.graphics.getHeight());
            speed = MathUtils.random(20, 100);
        }
    }
}
