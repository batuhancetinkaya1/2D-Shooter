package com.mygdx.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Enemy_2 extends Enemy {
	private int armor;
	

	public Enemy_2(Vector2 position, float speed, float health, String texturePath, int initialScoreValue) {
		super(position, speed, health, texturePath, initialScoreValue);
		// TODO Auto-generated constructor stub;
		this.armor =100;
	}

	@Override
    public void update(float deltaTime, Player player) {
        // Implement the specific update logic for Enemy_2
        Vector2 direction = new Vector2(player.getPosition().x - position.x, player.getPosition().y - position.y);
        direction.nor();
        position.add(direction.scl(speed * deltaTime * 0.5f)); // Slower movement
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
	    if (isActive) {
	        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
	        float healthBarWidth = 100;
	        float healthBarHeight = 5;
	        float healthPercentage = Math.max(0, (float) health / getMaxHealth());
	        float armorPercentage = Math.max(0, (float) armor / getMaxArmor());

	        shapeRenderer.setColor(Color.RED);
	        shapeRenderer.rect(position.x, position.y + texture.getHeight() + 10, healthBarWidth, healthBarHeight);

	        shapeRenderer.setColor(Color.GREEN);
	        shapeRenderer.rect(position.x, position.y + texture.getHeight() + 10, healthBarWidth * healthPercentage, healthBarHeight);

	        shapeRenderer.setColor(Color.BLUE); // Additional color for armor
	        shapeRenderer.rect(position.x, position.y + texture.getHeight() + 10, healthBarWidth * armorPercentage, healthBarHeight);

	        shapeRenderer.end();
	    }
	}

	
	@Override
    public int getMaxArmor() {
        // Implement the specific logic for getting the maximum armor of Enemy_2
        return 100; // You can adjust this value as needed
    }



	@Override
	public void takeDamage(int damage) {
	    if (armor > 0) {
	        armor = Math.max(0, armor - damage); // Subtract damage from armor
	    } else {
	        health -= damage; // If armor is already depleted, subtract damage from health
	    }

	    if (health <= 0) {
	        isActive = false;
	    }
	}



	@Override
    public float getAttackCooldown() {
        return 2.0f; // Example cooldown duration in seconds
    }

	@Override
    public int getMaxHealth() {
        // Implement the specific logic for getting the maximum health of Enemy_2
        return 100 ; // You can adjust this value as needed
    }

	@Override
    public int getDamage() {
        // Implement the specific damage logic for Enemy_2 (same as Enemy_1)
        return 10;
    }

	@Override
    protected boolean isAttackReady(float deltaTime) {
        return attackCooldownTimer <= 0; // Ready to attack when cooldown has elapsed
    }

	

}

