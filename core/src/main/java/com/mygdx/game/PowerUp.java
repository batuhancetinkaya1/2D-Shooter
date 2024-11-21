package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
//import com.mygdx.game.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class PowerUp {
    public enum PowerUpType {
    	SPEED_BOOST, BULLET_SPEED, BULLET_RATE, HEALTH_RECOVERY, SCORE_BOOST, TIME_BOOST
        // Add more types as needed
    }

    private static final int MAX_POWER_UPS = 15; // Maximum number of power-ups on the screen
    private static List<PowerUp> activePowerUps = new ArrayList<>();
    private static Random random = new Random();

    private Vector2 position;
    private PowerUpType type;
    private Texture texture;
    private boolean isActive;

    private static final float SPEED_AMOUNT = 50f;
    private static final float BULLET_SPEED_AMOUNT = 100f;
    private static final float BULLET_RATE_AMOUNT = 0.1f;
    private static final int HEALTH_AMOUNT = 20;
    
    
    static float yourWorldWidth = 1920;
    static float yourWorldHeight = 1080;
    static float yourPowerUpSize = 30;// Decide on the size of your power-ups, e.g., 50;
    
    
    private float lifespan = 5.0f;

    private Player player;

    public PowerUp(Vector2 position, PowerUpType type) {
        this.position = position;
        this.type = type;

        switch (type) {
            case SPEED_BOOST:
                this.texture = new Texture("speed_boost.png");
                break;
            case BULLET_SPEED:
                this.texture = new Texture("bullet_speed.png");
                break;
            case BULLET_RATE:
                this.texture = new Texture("bullet_rate.png");
                break;
            case HEALTH_RECOVERY:
                this.texture = new Texture("health_recovery.png");
                break;
            case SCORE_BOOST:
            	this.texture = new Texture("score_boost.png");
            	break;
            case TIME_BOOST:
            	this.texture = new Texture("time_boost.png");
            	break;
            // Add cases for other power-up types
        }

        this.isActive = true;

        // Ensure the power-up doesn't spawn on the player's texture
        if (isCollidingWithPlayer()) {
            isActive = false;
        }
    }

    public void applyEffect(Player player) {
        if (!isActive) return;

        switch (type) {
            case SPEED_BOOST:
                player.increaseSpeed(SPEED_AMOUNT);
                break;
            case BULLET_SPEED:
                player.increaseBulletSpeed(BULLET_SPEED_AMOUNT);
                break;
            case BULLET_RATE:
                player.increaseBulletRate(BULLET_RATE_AMOUNT);
                break;
            case HEALTH_RECOVERY:
                player.recoverHealth(HEALTH_AMOUNT);
                break;
            case SCORE_BOOST:
                // Handled externally in GameScreenClass
                break;
            case TIME_BOOST:
            	break;
            
        }

        isActive = false; // Power-up is consumed
    }

    public void draw(SpriteBatch batch) {
        if (isActive) {
            batch.draw(texture, position.x, position.y);
        }
    }
    
    public PowerUpType getType() {
        return type;
    }

    public void dispose() {
        texture.dispose();
    }

    public static void spawnRandomPowerUp() {
        if (activePowerUps.size() < MAX_POWER_UPS) {
            float safeZoneHeight = 50; // Height of the area where power-ups should not spawn
            float spawnPositionX = random.nextFloat() * (yourWorldWidth - yourPowerUpSize);
            float spawnPositionY = random.nextFloat() * (yourWorldHeight - yourPowerUpSize - safeZoneHeight) + safeZoneHeight;

            PowerUpType randomType = getRandomPowerUpType();
            PowerUp powerUp = new PowerUp(new Vector2(spawnPositionX, spawnPositionY), randomType);
            activePowerUps.add(powerUp);
        }
    }



    private static PowerUpType getRandomPowerUpType() {
        PowerUpType[] values = PowerUpType.values();
        return values[random.nextInt(values.length)];
    }

    public static void updatePowerUps(float deltaTime) {
        for (PowerUp powerUp : activePowerUps) {
            powerUp.update(deltaTime);
        }
        removeInactivePowerUps();
    }

    public void update(float deltaTime) {
    	
    	if (type != PowerUpType.SCORE_BOOST) {
    		lifespan -= deltaTime;
            if (lifespan <= 0) {
                isActive = false;
            }
        }
        
    }

    private static void removeInactivePowerUps() {
    	
        Iterator<PowerUp> iterator = activePowerUps.iterator();

        while (iterator.hasNext()) {
            PowerUp powerUp = iterator.next();

            if (!powerUp.isActive()) {
                iterator.remove();
            }
        }
    }


    public boolean isActive() {
        return isActive;
    }


	public static List<PowerUp> getActivePowerUps() {
        return activePowerUps;
    }
    
	private boolean isCollidingWithPlayer() {
	    if (player != null) {
	        float playerLeft = player.getPosition().x;
	        float playerRight = playerLeft + player.getPlayerTexture().getWidth();
	        float playerTop = player.getPosition().y;
	        float playerBottom = playerTop + player.getPlayerTexture().getHeight();

	        // Assuming power-up image has no transparency around it
	        float powerUpRight = position.x + texture.getWidth();
	        float powerUpBottom = position.y + texture.getHeight();

	        // Check if the power-up is colliding with the player's texture
	        boolean isColliding = position.x < playerRight && powerUpRight > playerLeft &&
	                position.y < playerBottom && powerUpBottom > playerTop;

	        if (isColliding) {
	            isActive = false; // Set the power-up to inactive upon collision with the player
	        }

	        return isColliding;
	    }
	    return false;
	}
	
	public void setInactive() {
	    this.isActive = false;
	}
	
	public Vector2 getPosition() {
        return position;
    }

    // Getter for texture
    public Texture getTexture() {
        return texture;
    }




}

