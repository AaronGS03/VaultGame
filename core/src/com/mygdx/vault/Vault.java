package com.mygdx.vault;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.vault.Screens.LoadingScreen;
import com.mygdx.vault.Screens.MainMenuScreen;
import com.mygdx.vault.Screens.PlayScreen;

public class Vault extends Game {
	public static final float V_WIDTH = 17408;
	public static final float V_HEIGHT = 9216;
	public static  final float PPM = 200;//pixeles que se mueve un objeto por metro en box2d

	public static final short NOTHING_BIT = 0;
	public static final short DEFAULT_BIT = 1;
	public static final short MAGE_BIT = 2;
	public static final short PLATAFORM_BIT = 4;
	public static final short ITEM =64;
	public static final short DOOR_BIT = 16;
	public static final short SPIKE_BIT = 8;
	public static final short WALL_BIT = 32;
	public static final short SENSOR_BIT = 128;

	//SpriteBatch contiene todos los sprites, que luego se muestran
	public static SpriteBatch batch;


	public AssetManager manager;
	public float volume=0.2f;
	public int language=1;

	public boolean isEffects() {
		return effects;
	}

	public void setEffects(boolean effects) {
		this.effects = effects;
	}

	private boolean effects;

	public boolean isSound() {
		return sound;
	}

	public void setSound(boolean sound) {
		this.sound = sound;
	}

	private boolean sound;


	@Override
	public void create () {

		batch = new SpriteBatch();
		manager = new AssetManager();
		manager.load("audio/sounds/Single-footstep-in-grass.mp3", Sound.class);
		manager.load("audio/sounds/rustling-grass.mp3", Sound.class);
		manager.load("audio/sounds/clickbutton.mp3", Sound.class);
		manager.load("audio/sounds/doorKey.mp3", Sound.class);
		manager.load("audio/sounds/lockedDoor.mp3", Sound.class);

		manager.load("loadingScreenImage.png", Texture.class);

		manager.finishLoading();
		Preferences prefs = Gdx.app.getPreferences("My Preferences");

		effects= prefs.getBoolean("effects",true);
		if (effects){
			volume=0.2f;
		}else {
			volume=0;
		}
		sound=prefs.getBoolean("sound",true);
		this.setScreen(new MainMenuScreen(this));


	}

	@Override
	public void render () {
		//Delega el método render al Playscreen en uso
		super.render();

		manager.update();

	}

}
