package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.vault.Vault;

import java.awt.Font;

public class LoadingScreen implements Screen {
    private Vault game;
    private AssetManager manager;
    private boolean assetsLoaded = false;
    private Texture loadingImage;
    public int level = 1;

    public LoadingScreen(Vault game, int level) {
        this.game = game;
        this.manager = game.manager;
        this.level = level;

        // Cargar recursos para la pantalla de carga
        manager.load("loadingScreenImage.png", Texture.class);

        // Cargar recursos para el juego
        manager.load("audio/music/forgotten-cave-159880.mp3", Music.class);
        manager.load("audio/sounds/Single-footstep-in-grass.mp3", Sound.class);
        manager.load("audio/sounds/rustling-grass.mp3", Sound.class);
        manager.load("audio/sounds/clickbutton.mp3", Sound.class);
        manager.load("audio/sounds/doorKey.mp3", Sound.class);

        try {
            // Cargar otros recursos del juego si es necesario
            loadingImage = manager.get("loadingScreenImage.png", Texture.class);
            manager.finishLoading(); // Espera a que todos los recursos se carguen antes de continuar
            assetsLoaded = true;
        } catch (Exception e) {
            // Manejo de excepciones, por ejemplo, mostrar un mensaje de error o cambiar de pantalla a una pantalla de error.
            Gdx.app.error("LoadingScreen", "Error loading assets: " + e.getMessage());
            assetsLoaded = false;
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        if (assetsLoaded) {
            // Cambia a la pantalla principal del juego cuando los recursos estén cargados
            game.setScreen(new PlayScreen(game, game.manager, level));
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
    }
}

