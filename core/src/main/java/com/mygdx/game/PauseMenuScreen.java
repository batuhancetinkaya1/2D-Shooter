package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class PauseMenuScreen implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private Texture pauseMenuTexture;
    private Texture continueButtonTexture;
    private Texture exitButtonTexture;
    private Viewport viewport;

    private MusicManager musicManager;
    private GameScreenClass gameScreen;

    public PauseMenuScreen(MyGame game, GameScreenClass gameScreen) {
        this.game = game;
        this.gameScreen = gameScreen;
        batch = new SpriteBatch();
        pauseMenuTexture = new Texture("pause_menu.png");
        continueButtonTexture = new Texture("continue_button.png");
        exitButtonTexture = new Texture("exit_button.png");
        viewport = new FitViewport(1280, 753);
        viewport.apply();

        musicManager = new MusicManager("pause_menu.mp3");
        musicManager.play();
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(pauseMenuTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(continueButtonTexture, 100, 200);
        batch.draw(exitButtonTexture, 100, 100);
        batch.end();
        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector2 touchPoint = getUnprojectedTouch(Gdx.input.getX(), Gdx.input.getY());
            if (isButtonClicked(touchPoint, 100, 200, continueButtonTexture)) {
                continueGame();
            } else if (isButtonClicked(touchPoint, 100, 100, exitButtonTexture)) {
                returnToMainMenu();
            }
        }
    }
    
    private void stopMusic() {
        musicManager.stop();
    }

    private void continueGame() {
        // Stop the pause menu music
        stopMusic();

        // Resume the game music
        if (gameScreen != null) {
            gameScreen.resumeGameMusic();
        }

        // Switch back to the game screen
        game.setScreen(gameScreen);
    }

    private void returnToMainMenu() {
        stopMusic();
        game.setScreen(new MainMenuScreen(game));
    }

    private Vector2 getUnprojectedTouch(float screenX, float screenY) {
        Vector2 touchPoint = new Vector2(screenX, screenY);
        viewport.unproject(touchPoint);
        return touchPoint;
    }

    private boolean isButtonClicked(Vector2 touchPoint, float buttonX, float buttonY, Texture buttonTexture) {
        float mouseX = touchPoint.x, mouseY = touchPoint.y;
        return mouseX >= buttonX && mouseX <= buttonX + buttonTexture.getWidth() &&
                mouseY >= buttonY && mouseY <= buttonY + buttonTexture.getHeight();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        pauseMenuTexture.dispose();
        continueButtonTexture.dispose();
        exitButtonTexture.dispose();

        // Dispose of the music manager
        musicManager.dispose();
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

    // Implement other methods from the Screen interface as necessary
    // ...
}


