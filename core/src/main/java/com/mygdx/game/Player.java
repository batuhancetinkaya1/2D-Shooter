package com.mygdx.game;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Player {
    private MyGame game;
    private Vector2 position;
    private float speed;
    private int health;
    private Texture playerTexture;
    private Texture bulletTexture;
    private Array<Bullet> bullets;
    private float bulletSpeed;
    private float shootCooldown;
    private float shootTimer;
    private float speedBoost;
    private float bulletSpeedBoost;
    private float bulletRateBoost;
    //private float damageOverTime;
    private MusicManager musicManager;

    private Runnable onDeath;

    public Player(MyGame game, Vector2 startPosition, float speed, int health, MusicManager musicManager, Runnable onDeath) {
        this.game = game;
        this.position = startPosition;
        this.speed = speed;
        this.health = health;
        this.playerTexture = new Texture("skull.png");
        this.bulletTexture = new Texture("bullet.png");
        this.bullets = new Array<>();
        this.bulletSpeed = 1000;
        this.shootCooldown = 0f;
        this.shootTimer = 0;
        //this.damageOverTime = 0;
        this.musicManager = musicManager;
        this.onDeath = onDeath;
    }

    /*public void setDamageOverTime(float damagePerSecond) {
        this.damageOverTime = damagePerSecond;
    }*/

    public void update(float deltaTime) {
        handleInput();
        updateMovement(deltaTime);
        updateShooting(deltaTime);
        updateBullets(deltaTime);
        //updateContinuousDamage(deltaTime);
    }

    private void handleInput() {
        // Handle player input for movement and shooting
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            position.y += (speed + speedBoost) * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            position.y -= (speed + speedBoost) * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            position.x -= (speed + speedBoost) * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
        }
    }

    private void updateMovement(float deltaTime) {
        float newX = position.x;
        float newY = position.y;

        // Update player movement based on input
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += (speed + speedBoost) * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= (speed + speedBoost) * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= (speed + speedBoost) * deltaTime;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += (speed + speedBoost) * deltaTime;
        }

        // Adjust the player's position to keep it within the screen boundaries
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float playerWidth = playerTexture.getWidth();
        float playerHeight = playerTexture.getHeight();

        newX = MathUtils.clamp(newX, 0, screenWidth - playerWidth);
        newY = MathUtils.clamp(newY, 0, screenHeight - playerHeight);

        position.set(newX, newY);
    }

    private void updateShooting(float deltaTime) {
        // Shooting logic
        shootTimer += deltaTime;
        if (Gdx.input.isButtonPressed(Input.Buttons.LEFT) && shootTimer >= shootCooldown) {
            shoot();
            shootTimer = 0;
        }
    }

    private void updateBullets(float deltaTime) {
        // Update bullets
        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet bullet = iter.next();
            bullet.update(deltaTime);
            if (bullet.getPosition().y > Gdx.graphics.getHeight()) {
                iter.remove(); // Remove bullet if it's off the screen
            }
        }
    }

    /*private void updateContinuousDamage(float deltaTime) {
        // Continuous damage-over-time logic
        if (damageOverTime > 0) {
            int totalDamage = Math.round(damageOverTime * deltaTime);
            takeDamage(totalDamage);
        }
    }*/

    private void shoot() {
        // Shooting logic
        Vector2 bulletOrigin = new Vector2(position.x + playerTexture.getWidth() / 2, position.y + playerTexture.getHeight());
        Vector2 mousePosition = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
        Vector2 bulletDirection = mousePosition.sub(bulletOrigin).nor();

        bullets.add(new Bullet(bulletOrigin, bulletDirection.scl(bulletSpeed + bulletSpeedBoost)));
    }

    public void draw(SpriteBatch batch) {
        // Draw the player
        batch.draw(playerTexture, position.x, position.y);

        // Draw the bullets
        for (Bullet bullet : bullets) {
            bullet.draw(batch);
        }
    }

    public void drawHealthBar(ShapeRenderer shapeRenderer) {
        if (shapeRenderer.isDrawing()) {
            shapeRenderer.end();
        }
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Implement the health bar drawing for the player
        float healthBarWidth = 100; // Width of the health bar
        float healthBarHeight = 5;  // Height of the health bar
        float healthPercentage = (float) health / 100; // Calculate the health percentage

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(position.x, position.y + playerTexture.getHeight() + 10, healthBarWidth, healthBarHeight);
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x, position.y + playerTexture.getHeight() + 10, healthBarWidth * healthPercentage, healthBarHeight);

        shapeRenderer.end();
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0) {
            health = 0;
            handlePlayerDeath();
        }
    }

    private void stopMusic() {
        if (musicManager != null) {
            musicManager.stop();
        }
    }

    private void handlePlayerDeath() {
        // Additional logic for player death
        stopMusic();

        if (onDeath != null) {
            onDeath.run();
        }
    
        game.setScreen(new GameOverScreen(game));
    }

    public void increaseSpeed(float amount) {
        this.speedBoost += amount;
    }

    public void increaseBulletSpeed(float amount) {
        this.bulletSpeedBoost += amount;
    }

    public void increaseBulletRate(float amount) {
        this.bulletRateBoost += amount;
    }

    /*public void setContinuousDamage(float damagePerSecond) {
        this.damageOverTime = damagePerSecond;
    }*/

    public void recoverHealth(int amount) {
        this.health += amount;
        if (this.health > 100) {
            this.health = 100;
        }
    }

    public void dispose() {
        playerTexture.dispose();
        bulletTexture.dispose();

        // Dispose of bullets
        for (Bullet bullet : bullets) {
            bullet.dispose();
        }
    }

    public Array<Bullet> getBullets() {
        return bullets;
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(position.x, position.y, playerTexture.getWidth(), playerTexture.getHeight());
    }

    public double getHealth() {
        return health;
    }

    
    

    // In the Player class
    public Vector2 getPosition() {
        return position;
    }
    
    public Texture getPlayerTexture() {
        return playerTexture;
    }
    
    

}
