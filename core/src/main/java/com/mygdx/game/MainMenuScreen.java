package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.Input;


public class MainMenuScreen implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Texture startGameButtonTexture;
    private Texture exitButtonTexture;
    private Texture leaderboardButtonTexture;
    private Vector2 startButtonPosition;
    private Vector2 exitButtonPosition;
    private Vector2 leaderboardButtonPosition;
    private Viewport viewport;
    
    private MusicManager musicManager;
    
    

    public MainMenuScreen(MyGame game) {
        this.game = game;
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        startGameButtonTexture = new Texture("startButton.png");
        exitButtonTexture = new Texture("exitButton.png");
        leaderboardButtonTexture = new Texture("leaderboard_button.png");

        startButtonPosition = new Vector2(100, 200);
        exitButtonPosition = new Vector2(100, 100);
        leaderboardButtonPosition = new Vector2(1100, 550);

        // Use a FitViewport with your desired virtual screen size
        viewport = new FitViewport(1280, 753);
        viewport.apply();

        // Initialize background music
        musicManager = new MusicManager("shreksophone.mp3");
        musicManager.play(); // Start playing the music
    }


    @Override
    public void render(float delta) {
        // Set the projection matrix to the camera's combined matrix
        batch.setProjectionMatrix(viewport.getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Use viewport's dimensions for drawing
        batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        batch.draw(startGameButtonTexture, startButtonPosition.x, startButtonPosition.y);
        batch.draw(exitButtonTexture, exitButtonPosition.x, exitButtonPosition.y);
        batch.draw(leaderboardButtonTexture, leaderboardButtonPosition.x, leaderboardButtonPosition.y);

        batch.end();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            if (isButtonClicked(startButtonPosition)) {
                stopMusic();  // Stop the music before changing the screen
                GameScreenClass gameScreen = new GameScreenClass(game);
                gameScreen.startNewGame();
                game.setScreen(gameScreen);
            } else if (isButtonClicked(exitButtonPosition)) {
                Gdx.app.exit();
            } else if (isButtonClicked(leaderboardButtonPosition)) {
            	stopMusic();  // Stop the music before changing the screen
            	LeaderboardScreen gameScreen = new LeaderboardScreen(game);
                game.setScreen(gameScreen);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Update the viewport to handle resizing
        viewport.update(width, height, true);
    }

   

    private boolean isButtonClicked(Vector2 buttonPosition) {
        // Get the unprojected coordinates to handle different screen sizes
        Vector3 touchPoint = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        viewport.unproject(touchPoint);

        float mouseX = touchPoint.x;
        float mouseY = touchPoint.y;

        return mouseX >= buttonPosition.x && mouseX <= buttonPosition.x + startGameButtonTexture.getWidth() &&
               mouseY >= buttonPosition.y && mouseY <= buttonPosition.y + startGameButtonTexture.getHeight();
    }
    
    public void stopMusic() {
        musicManager.stop();
    }



    
    
    @Override
    public void pause() {
        // Pause handling if necessary
    }

    @Override
    public void resume() {
        // Resume handling if necessary
    }

    @Override
    public void hide() {
        // Hide handling if necessary
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        startGameButtonTexture.dispose();
        exitButtonTexture.dispose();

        // Dispose of background music manager
        if (musicManager != null) {
            musicManager.dispose();
        }
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}
}

