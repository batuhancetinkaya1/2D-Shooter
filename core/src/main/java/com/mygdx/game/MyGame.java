package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGame extends Game {
    public SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // Set the initial screen, for example, the main menu
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render(); // Important to call render of the superclass to render the current screen
    }

    @Override
    public void dispose() {
        batch.dispose();
        // Dispose of other global resources if needed
    }
}


