package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.vault.Vault;

/**
 * Pantalla de carga que se muestra mientras se cargan los recursos del juego.
 */
public class LoadingScreen implements Screen {
    /** Instancia principal del juego */
    private Vault game;

    /** Administrador de activos para cargar recursos */
    private AssetManager manager;

    /** Indica si los activos han sido cargados */
    private boolean assetsLoaded = false;

    /** Escenario donde se colocan los elementos visuales */
    private Stage stage;

    /** Imagen de carga que se muestra durante la carga */
    private Image loadingImage;

    /** Nivel actual del juego */
    public int level = 1;

    /** Música de fondo del juego */
    private Music music;

    /** Pantalla principal del menú */
    public MainMenuScreen mainMenuScreen;


    /**
     * Constructor de la pantalla de carga.
     *
     * @param game      Instancia principal del juego.
     * @param menuMusic Música del menú.
     * @param level     Nivel que se va a cargar.
     * @param screen    Pantalla principal del menú.
     */
    public LoadingScreen(Vault game, Music menuMusic, int level, MainMenuScreen screen) {
        this.game = game;
        this.manager = game.manager;
        this.level = level;
        this.music = menuMusic;

        // Cargar recursos para el juego
        manager.load("audio/music/forgotten-cave-159880.mp3", Music.class);
        manager.finishLoading(); // Espera a que todos los recursos se carguen antes de continuar
        assetsLoaded = true;
        this.mainMenuScreen = screen;
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
        stage.dispose();
        music.dispose();
    }
}
