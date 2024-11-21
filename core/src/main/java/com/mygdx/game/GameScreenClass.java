package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.PowerUp.PowerUpType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameScreenClass implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private Player player;
    private List<Enemy> enemies;
    private int waveNumber;
    private boolean isPaused;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private OrthographicCamera camera;
    private Viewport viewport;
    private int score;
    
    public int getScore() {
		return score;
	}

	private float waveTimer;
    //private float waveDuration = 30.0f; // 30 seconds for each wave

    
    private MusicManager musicManager;


    private Preferences preferences; // Preferences for saving and loading game state
    
    
    private float powerUpSpawnTimer = 0f;
    private float powerUpSpawnInterval = 2.0f;

    public GameScreenClass(MyGame game) {
        this.game = game;
        initialize();
    }
 
    private void initialize() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(1920, 1080, camera);
        viewport.apply();

        batch = new SpriteBatch();
        player = createPlayer();
        enemies = new ArrayList<>();

        // Reset score and wave when starting a new game
        score = 0;
        waveNumber = 1;
        spawnWave(waveNumber);

        isPaused = false;
        font = new BitmapFont();
        shapeRenderer = new ShapeRenderer();

        preferences = Gdx.app.getPreferences("MyGamePreferences");
        loadGameState(); // Load game state when the game starts

        musicManager = new MusicManager("synthesizer_song.mp3");
   
         // Start playing the music
    }
    
    private Player createPlayer() {
        return new Player(game, new Vector2(100, 100), 300, 100, musicManager, new Runnable() {
            @Override
            public void run() {
                handlePlayerDeath();
            }
        });
    }
    
    private void handlePlayerDeath() {
        stopMusic(); // Stop the game screen music
        game.setScreen(new GameOverScreen(game)); // Transition to GameOverScreen
    }
    
    private void stopMusic() {
        if (musicManager != null) {
            musicManager.stop();
        }
    }
    
    private void pauseMusic() {
        if (musicManager != null && musicManager.isPlaying()) {
            musicManager.pause();
        }
    }
    
    private void togglePause() {
        isPaused = !isPaused;
        if (isPaused) {
            saveGameState(); // Save game state
            pauseMusic(); // Stop the synthesizer music
            game.setScreen(new PauseMenuScreen(game, this)); // Pass reference to GameScreenClass
        } else {
            musicManager.resume(); // Resume the synthesizer music
        }
    }
    
    public void resumeGameMusic(boolean paused) {
        if (musicManager != null) {
            if (paused) {
                musicManager.resume(); // Resume the music from where it left off
            } else {
                musicManager.play(); // Start playing the music from the beginning
            }
        }
    }


    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
        	
            togglePause();
        }
    }

    

    private void updatePlayer(float delta) {
        player.update(delta);
    }

    private void updateEnemies(float delta) {
        boolean anyEnemyActive = false;

        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.update(delta, player);
                anyEnemyActive = true;
            }
        }

        if (!anyEnemyActive && enemies.isEmpty()) {
            waveNumber++; // Increment wave number for the next wave
            spawnWave(waveNumber); // Spawn the next wave
        }
    }


    private void handleCollisions() {
        handleBulletCollisions();
        handlePlayerEnemyCollisions();
    }

    private void handleBulletCollisions() {
        Iterator<Enemy> enemyIterator = enemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            Iterator<Bullet> bulletIterator = player.getBullets().iterator();
            
            while (bulletIterator.hasNext()) {
                Bullet bullet = bulletIterator.next();
                if (bullet.isActive() && bullet.getBoundingRectangle().overlaps(enemy.getBoundingRectangle())) {
                    enemy.takeDamage(bullet.getDamage()); // Deal instant damage
                    bulletIterator.remove(); // Remove the bullet after it hits the enemy
                }
            }

            if (!enemy.isAlive()) {
                enemyIterator.remove();
                score += enemy.getScoreValue();
            }
        }
    }


    private void handlePlayerEnemyCollisions() {
        for (Enemy enemy : enemies) {
            if (enemy.getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                player.takeDamage(enemy.getDamage()); // Deal instant damage upon collision
            }
        }
    }



    private void spawnWave(int waveNumber) {
        // Define parameters for enemy spawning based on the wave number
        int numEnemiesToSpawn = 5 + waveNumber * 2; // Increase the number of enemies with each wave
        float enemySpeed = 100 + waveNumber * 10; // Increase enemy speed with each wave
        
        waveTimer = 10.0f + 1.0f * (waveNumber - 1);
        

        for (int i = 0; i < numEnemiesToSpawn; i++) {
            float x, y;

            // Ensure enemies spawn off-screen and away from the player
            do {
                x = MathUtils.random(viewport.getWorldWidth());
                y = MathUtils.random(viewport.getWorldHeight());
            } while (Vector2.dst2(x, y, player.getPosition().x, player.getPosition().y) < 500 * 500
                    || x < 0 || x > viewport.getWorldWidth() || y < 0 || y > viewport.getWorldHeight());

            Enemy enemy;

            // Randomly select an enemy type
            int randomEnemyType = MathUtils.random(100);

            if (randomEnemyType < 60) {
                enemy = new Enemy_1(new Vector2(x, y), enemySpeed, 100, "enemy1.png", 10);
            } else if (randomEnemyType < 90) {
                enemy = new Enemy_2(new Vector2(x, y), enemySpeed , 100 , "enemy2.png", 20);
            } else {
                enemy = new Enemy_3(new Vector2(x, y), enemySpeed +20 , 10 , "enemy3.png", 50);
            }

            enemies.add(enemy);
        }
    }





    private void draw() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f); // Set to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        drawEntities();
        drawScoreAndWaveInfo();
        batch.end();

        drawHealthBars();
    }


    private void drawEntities() {
        player.draw(batch);
        for (Enemy enemy : enemies) {
            enemy.draw(batch);
        }
        for (PowerUp powerUp : PowerUp.getActivePowerUps()) {
            powerUp.draw(batch);
        }
    }

    private void drawScoreAndWaveInfo() {
        font.draw(batch, "Score: " + score, 10, viewport.getWorldHeight() - 10);
        font.draw(batch, "Wave: " + waveNumber, 10, viewport.getWorldHeight() - 30);
        font.draw(batch, "Time: " + Math.max((int)waveTimer, 0), 10, viewport.getWorldHeight() - 50); // Display timer
    }


    private void drawHealthBars() {
        shapeRenderer.setProjectionMatrix(camera.combined);
        if (shapeRenderer.isDrawing()) {
            shapeRenderer.end();
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        player.drawHealthBar(shapeRenderer);
        for (Enemy enemy : enemies) {
            enemy.drawHealthBar(shapeRenderer);
        }
        shapeRenderer.end();
    }
    
    
    
    // In GameScreenClass
    public void onPlayerDeath() {
        stopMusic(); // Stop the game screen music
        game.setScreen(new GameOverScreen(game)); // Transition to GameOverScreen
    }



    
    
    private void restartGame() {
        // Additional logic to restart the game as needed
        // For example, re-initialize enemies, player position, etc.
        initialize();
    }
    
    private void resetGame() {
        // Clear preferences
        clearPreferences();

        // Dispose of resources before restarting
        player.dispose();
        for (Enemy enemy : enemies) {
            enemy.dispose();
        }

        // Restart the game
        restartGame();
    }
    
    public void startNewGame() {
        resetGame();
        // Optionally, other logic to start a new game...
    }
    
    private void clearPreferences() {
        preferences.clear();
        preferences.flush(); // Save preferences
    }

    

    private void saveGameState() {
        // Save essential game state information to preferences
        preferences.putInteger("score", score);
        preferences.putInteger("waveNumber", waveNumber);
        // Add more preferences as needed
        preferences.flush(); // Save preferences
    }

    private void loadGameState() {
        // Load game state information from preferences
        score = preferences.getInteger("score", 0);
        waveNumber = preferences.getInteger("waveNumber", 1);
        // Retrieve more preferences as needed
    }
    
    
    public void resumeGameMusic() {
        if (musicManager != null) {
            musicManager.resume(); // Resume the music
        }
    }
    
    

    @Override
    public void render(float delta) {
        handleInput();
        update(delta);
        draw();
    }

    @Override
    public void show() {
    	if (!isPaused && musicManager != null && !musicManager.isPlaying()) {
            musicManager.resume();
        }
    }

    @Override
    public void pause() {
        togglePause();
        pauseMusic();
    }
    
    

    @Override
    public void resume() {
        if (isPaused) {
            togglePause(); // If paused, resume the game
        } else {
            resumeGameMusic(true); // Resume the music from where it left off
        }
    }

    @Override
    public void resize(int width, int height) {
        // Called when the screen size is changed.
        // Update the viewport to match the new screen size.
        viewport.update(width, height, true);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }

    

    @Override
    public void hide() {
        // Called when this screen is no longer the current screen.
        // This can happen when switching to another screen.
    }
    
    private void updatePowerUps(float delta) {
        powerUpSpawnTimer += delta;
        if (powerUpSpawnTimer >= powerUpSpawnInterval) {
            PowerUp.spawnRandomPowerUp();
            powerUpSpawnTimer = 0; // Reset the timer after spawning a power-up
        }

        // Update and remove inactive power-ups
        Iterator<PowerUp> iterator = PowerUp.getActivePowerUps().iterator();
        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();
            powerUp.update(delta);
            if (!powerUp.isActive()) {
                iterator.remove();
            }
        }
    }
    private void checkPowerUpCollisions() {
        for (PowerUp powerUp : PowerUp.getActivePowerUps()) {
            if (player.getBoundingRectangle().overlaps(new Rectangle(powerUp.getPosition().x, powerUp.getPosition().y, powerUp.getTexture().getWidth(), powerUp.getTexture().getHeight()))) {
                if (powerUp.getType() == PowerUpType.SCORE_BOOST) {
                    increaseScore(50); // Increase the score
                } else if (powerUp.getType() == PowerUpType.TIME_BOOST) {
                    increaseTime(1); // Increase the time, for example by 5 seconds
                } else {
                    powerUp.applyEffect(player);
                }
                powerUp.setInactive(); // Set the power-up as inactive
            }
        }
    }


    private void increaseScore(int amount) {
        score += amount;
        // Update any score display or perform other related actions here
    }
    private void increaseTime(int amount) {
    	waveTimer += amount;
        // Update any score display or perform other related actions here
    }


    
    
    private void update(float delta) {
        if (!isPaused) {
            updatePlayer(delta);
            updateEnemies(delta);
            updateWaveTimer(delta);
            handleCollisions();
            updatePowerUps(delta);
            checkPowerUpCollisions();
        }
    }

    private void updateWaveTimer(float delta) {
        if (waveTimer > 0) {
            waveTimer -= delta;
        }

        if (waveTimer <= 0 && !enemies.isEmpty()) {
            onPlayerDeath(); // Call the player death method when time runs out
        }
    }


    @Override
    public void dispose() {
        batch.dispose();
        player.dispose();
        shapeRenderer.dispose();
        font.dispose();

        if (musicManager != null) {
            musicManager.dispose();
        }

        // Dispose of power-ups
        for (PowerUp powerUp : PowerUp.getActivePowerUps()) {
            powerUp.dispose();
        }
    }
}





