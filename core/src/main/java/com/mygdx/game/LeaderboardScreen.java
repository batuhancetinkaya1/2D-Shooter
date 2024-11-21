package com.mygdx.game;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LeaderboardScreen implements Screen {
    private MyGame game;
    private SpriteBatch batch;
    private BitmapFont font;
    private DatabaseConnection connect;
    private List<String[]> leaderboard;
    private ExecutorService executorService;
    private Future<?> leaderboardLoadTask;
    private boolean isLoading = true;

    public LeaderboardScreen(MyGame game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.executorService = Executors.newSingleThreadExecutor();

        fetchLeaderboardData();
    }

    private void fetchLeaderboardData() {
    	leaderboardLoadTask = executorService.submit(new Runnable() {
    	    @Override
    	    public void run() {
    	        DatabaseConnection connection = new DatabaseConnection();
    	        leaderboard = connection.getTopScores();
    	        isLoading = false;
    	    }
    	});

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        if (!isLoading) {
            int y = 300; // Start position for leaderboard entries
            for (String[] entry : leaderboard) {
                font.draw(batch, entry[0] + " - " + entry[1], 100, y);
                y -= 20; // Move to the next line for the next entry
            }
        } else {
            font.draw(batch, "Loading leaderboard...", 100, 300);
        }
        batch.end();
    }

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
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

	@Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        executorService.shutdown();
    }

    // Implement other necessary methods (show, hide, pause, resume, resize, dispose)
}
