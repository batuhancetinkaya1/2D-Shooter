package com.mygdx.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;

public abstract class Enemy {
    protected Vector2 position;
    protected float speed;
    protected Texture texture;
    protected float health;
    protected boolean isActive;
    private int scoreValue;
    public float attackCooldownTimer = 0;

    public Enemy(Vector2 position, float speed, float health, String texturePath, int initialScoreValue) {
        this.position = position;
        this.speed = speed;
        this.health = health;
        this.texture = new Texture(texturePath); // Load texture
        this.isActive = true; // Make sure to set isActive to true
        this.scoreValue = initialScoreValue;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getSpeed() {
        return speed;
    }

    public Texture getTexture() {
        return texture;
    }

    public float getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public abstract void update(float deltaTime, Player player);

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }
    
    protected void decreaseHealth(int damage) {//HASAR ALMA VE VERME METODLARIYLA BERABER DEĞİŞMELİ
        health -= damage;
        if (health <= 0) {
            isActive = false;
        }
    }

    public Rectangle getBoundingRectangle() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public boolean isAlive() {
        return health > 0;
    }
    
    protected void performAttack(Player player) {
        if (attackCooldownTimer <= 0) {
            int damage = getDamage();
            player.takeDamage(damage);
            attackCooldownTimer = getAttackCooldown(); // Reset the cooldown timer
        }
    }
    
    public abstract void drawHealthBar(ShapeRenderer shapeRenderer);
    
    public abstract void takeDamage(int damage);
    
    public abstract float getAttackCooldown();

    public abstract int getMaxHealth();
    
    public abstract int getDamage();

    protected abstract boolean isAttackReady(float deltaTime);
    
    public abstract int getMaxArmor();

    //protected abstract void performAttack(Player player, float deltaTime);
   
}