package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy_1 extends Enemy {
	

	public Enemy_1(Vector2 position, float speed, int health, String texturePath, int initialScoreValue) {
		super(position, speed, health, texturePath, initialScoreValue);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update(float deltaTime, Player player) {
		// TODO Auto-generated method stub
		Vector2 direction = new Vector2(player.getPosition().x - position.x, player.getPosition().y - position.y);
        direction.nor(); // Normalize to get direction
        position.add(direction.scl(speed * deltaTime)); // Move towards player
        if (isAttackReady(deltaTime)) {
            // Check for collision with player to perform attack
            if (getBoundingRectangle().overlaps(player.getBoundingRectangle())) {
                performAttack(player);
                attackCooldownTimer = getAttackCooldown(); // Reset cooldown timer
            }
        } else {
            attackCooldownTimer -= deltaTime; // Decrease cooldown timer
        }
		
	}

	@Override
	public void drawHealthBar(ShapeRenderer shapeRenderer) {
		// TODO Auto-generated method stub
		if (isActive) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            float healthBarWidth = 100; // Width of the health bar
            float healthBarHeight = 5;  // Height of the health bar
            float healthPercentage = (float) health / getMaxHealth(); // Calculate the health percentage

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.rect(position.x, position.y + texture.getHeight() + 10, healthBarWidth, healthBarHeight);
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.rect(position.x, position.y + texture.getHeight() + 10, healthBarWidth * healthPercentage, healthBarHeight);

            shapeRenderer.end();
        }
		
	}

	@Override
	public void takeDamage(int damage) {
		// TODO Auto-generated method stub
		health -= damage;
        if (health <= 0) {
            isActive = false;
        }
	}

	@Override
	public float getAttackCooldown() {
		// TODO Auto-generated method stub
		return 2.0f;
	}

	@Override
	public int getMaxHealth() {
		// TODO Auto-generated method stub
		// Return the maximum health for Enemy_1
        return 100; // You can adjust this value as needed
	}

	@Override
	public int getDamage() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
    protected boolean isAttackReady(float deltaTime) {
        return attackCooldownTimer <= 0; // Ready to attack when cooldown has elapsed
    }

	@Override
	public int getMaxArmor() {
		// TODO Auto-generated method stub
		return 0;
	}

	
    
}
