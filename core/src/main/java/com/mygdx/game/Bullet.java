package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Bullet {
    private Vector2 position;
    private Vector2 velocity;
    private Texture texture;
    private int damage = 10;
    private boolean isActive;


    public Bullet(Vector2 position, Vector2 velocity) {
        this.position = position;
        this.velocity = velocity;
        this.texture = new Texture("bullet.png");
        this.isActive = true; // Set the initial value
    }
    
    public Vector2 getPosition() {
        return position;
    }

    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime); // Update position based on velocity
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public void dispose() {
        texture.dispose();
    }
    
    public Rectangle getBoundingRectangle() {
        return new Rectangle(position.x, position.y, texture.getWidth(), texture.getHeight());
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive; // Assuming you have a field 'isActive' in Bullet class
    }

    public boolean isActive() {
        return isActive;
    }

    public int getDamage() {
        return damage; // Assuming you have a field 'damage' in Bullet class
    }
}

