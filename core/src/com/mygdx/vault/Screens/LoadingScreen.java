package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.vault.Vault;

public class LoadingScreen implements Screen {
    private Vault game;
    private AssetManager manager;
    private boolean assetsLoaded = false;
    private Stage stage;
    private Image loadingImage;
    public int level = 1;

    public LoadingScreen(Vault game, int level) {
        this.game = game;
        this.manager = game.manager;
        this.level = level;

        // Cargar recursos para el juego
        manager.load("audio/music/forgotten-cave-159880.mp3", Music.class);
        manager.finishLoading(); // Espera a que todos los recursos se carguen antes de continuar
        assetsLoaded = true;
    }

    @Override
    public void show() {
        // Crear la imagen de carga y agregarla al escenario
        stage = new Stage();
        loadingImage = new Image(new Texture("loadingScreenImage.png"));
        stage.addActor(loadingImage);
    }

    @Override
    public void render(float delta) {
     //   Gdx.gl.glClearColor(0, 0, 0, 1);
    //    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

  //      stage.draw();

        if (assetsLoaded) {
            // Cambiar a la pantalla principal del juego cuando los recursos estén cargados
            game.setScreen(new PlayScreen(game, game.manager, level));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
    }
}
