package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager {
    private Music backgroundMusic;
    private String musicFilePath;

    public MusicManager(String musicFilePath) {
        this.musicFilePath = musicFilePath;
        loadMusic();
        setupMusic();
    }
    
    public boolean isPlaying() {
        return backgroundMusic != null && backgroundMusic.isPlaying();
    }

    private void loadMusic() {
        FileHandle musicFile = Gdx.files.internal(musicFilePath);
        backgroundMusic = Gdx.audio.newMusic(musicFile);
    }

    private void setupMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(true);
            backgroundMusic.setVolume(0.5f);
        }
    }

    public void play() {
        if (backgroundMusic != null) {
            backgroundMusic.play();
        }
    }

    public void pause() {
        if (backgroundMusic != null && backgroundMusic.isPlaying()) {
            backgroundMusic.pause();
        }
    }

    public void resume() {
        if (backgroundMusic != null && !backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
    }

    public void stop() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
        }
    }

    public void setLooping(boolean isLooping) {
        if (backgroundMusic != null) {
            backgroundMusic.setLooping(isLooping);
        }
    }

    public void dispose() {
        if (backgroundMusic != null) {
            backgroundMusic.dispose();
        }
    }
    
    
}

