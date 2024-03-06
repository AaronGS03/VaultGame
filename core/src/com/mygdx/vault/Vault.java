package com.mygdx.vault;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.vault.Screens.MainMenuScreen;

/**
 * Clase principal del juego. Extiende de Game de libGDX.
 */
public class Vault extends Game {
    // Dimensiones virtuales del juego
    public static final float V_WIDTH = 17408;
    public static final float V_HEIGHT = 9216;
    public static final float PPM = 200; // Pixeles que se mueve un objeto por metro en box2d

    // Bits de colisión para los objetos del juego
    public static final short NOTHING_BIT = 0;
    public static final short DEFAULT_BIT = 1;
    public static final short MAGE_BIT = 2;
    public static final short PLATAFORM_BIT = 4;
    public static final short ITEM = 64;
    public static final short DOOR_BIT = 16;
    public static final short SPIKE_BIT = 8;
    public static final short WALL_BIT = 32;
    public static final short SENSOR_BIT = 128;

    // SpriteBatch contiene todos los sprites, que luego se muestran
    public static SpriteBatch batch;

    public AssetManager manager; // Administrador de recursos del juego
    public float volume = 0.2f; // Volumen de los efectos de sonido
    public int language = 1; // Idioma del juego (1: inglés, 2: español)

    public boolean effects; // Indica si los efectos de sonido están activados
    public boolean sound; // Indica si el sonido está activado
    private MainMenuScreen mainScreen; // Pantalla principal del juego

    /**
     * Método llamado al crear la instancia del juego.
     */
    @Override
    public void create() {
        batch = new SpriteBatch();

        manager = new AssetManager();
        // Carga de recursos de audio y textura
        manager.load("audio/sounds/Single-footstep-in-grass.mp3", Sound.class);
        manager.load("audio/sounds/rustling-grass.mp3", Sound.class);
        manager.load("audio/sounds/clickbutton.mp3", Sound.class);
        manager.load("audio/sounds/doorKey.mp3", Sound.class);
        manager.load("audio/sounds/lockedDoor.mp3", Sound.class);
        manager.load("audio/music/mainMenuMusic.mp3", Music.class);
        manager.load("loadingScreenImage.png", Texture.class);

        manager.finishLoading(); // Espera a que todos los recursos se carguen completamente

        // Obtención de las preferencias del juego
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        mainScreen = new MainMenuScreen(this);
        // Configuración de los efectos de sonido
        effects = prefs.getBoolean("effects", true);
        if (effects) {
            volume = 0.2f;
        } else {
            volume = 0;
        }
        // Configuración del sonido
        sound = prefs.getBoolean("sound", true);
        this.setScreen(mainScreen); // Establece la pantalla principal del juego
    }

    /**
     * Método llamado en cada frame para renderizar el juego.
     */
    @Override
    public void render() {
        super.render(); // Delega el método render al Playscreen en uso

        manager.update(); // Actualiza el administrador de recursos
    }
}
