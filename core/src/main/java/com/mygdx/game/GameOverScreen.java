package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;


public class GameOverScreen implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private Texture gameOverTexture;
    private Stage stage;
    private Skin skin;
    private TextField playerNameField;
    private MusicManager musicManager;
    private FitViewport viewport;

    public String getPlayerName() {
        return playerNameField.getText();
    }
    
    public GameOverScreen(MyGame game) {
        this.game = game;
        initialize();
    }

    private void initialize() {
        batch = new SpriteBatch();
        gameOverTexture = new Texture("you_died.png");
        musicManager = new MusicManager("horn.mp3");

        setupMusic();
        setupViewport();
        setupUI();
    }

    private void setupMusic() {
        musicManager.setLooping(false);
        musicManager.play(); // Start playing the music
    }

    private void setupViewport() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage);
    }

    private void setupUI() {
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        VerticalGroup verticalGroup = createVerticalGroup();
        playerNameField = createPlayerNameField();
        TextButton returnButton = createReturnButton();

        verticalGroup.addActor(playerNameField);
        verticalGroup.addActor(returnButton);

        stage.addActor(verticalGroup);
    }

    private VerticalGroup createVerticalGroup() {
        VerticalGroup verticalGroup = new VerticalGroup();
        verticalGroup.center().space(10).padTop(300); // Adjust padTop for positioning
        verticalGroup.setFillParent(true); // Makes the group take up the whole stage
        return verticalGroup;
    }

    private TextField createPlayerNameField() {
        TextField playerNameField = new TextField("", skin);
        playerNameField.setMessageText("Enter your name");

        // Add key listener for Enter key
        playerNameField.addListener(new InputListener() {
            @Override
            public boolean keyTyped(InputEvent event, char character) {
                if (character == '\r' || character == '\n') { // '\r' for Windows, '\n' for Unix/Mac
                    performReturnAction();
                }
                return true;
            }
        });

        return playerNameField;
    }
    
    private void performReturnAction() {
        if (checkPlayerName()) {
            stopMusicAndReturnToMenu();
        }
    }

    private TextButton createReturnButton() {
        TextButton returnButton = new TextButton("Return to Main Menu", skin);
        returnButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                performReturnAction();
            }
        });
        return returnButton;
    }
    
    private boolean checkPlayerName() {
        String playerName = playerNameField.getText();
        if (playerName != null && !playerName.trim().isEmpty()) {
            Gdx.app.log("GameOverScreen", "Player Name: " + playerName);
            return true;
        } else {
            Gdx.app.log("GameOverScreen", "Player name not entered");
            return false;
        }
    }

    private void stopMusicAndReturnToMenu() {
        stopMusic();
        game.setScreen(new MainMenuScreen(game));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(gameOverTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void show() {
        // Additional logic when screen is shown, if necessary
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Handle pause, if necessary
    }

    @Override
    public void resume() {
        // Handle resume, if necessary
    }

    @Override
    public void hide() {
        // Handle hide, if necessary
    }

    @Override
    public void dispose() {
        batch.dispose();
        gameOverTexture.dispose();
        musicManager.dispose();
        stage.dispose();
        skin.dispose();
    }

    private void stopMusic() {
        musicManager.stop();
    }
}
