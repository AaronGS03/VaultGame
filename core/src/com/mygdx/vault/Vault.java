package com.mygdx.vault;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.vault.Screens.PlayScreen;

public class Vault extends Game {
	public static final float V_WIDTH = 17408;
	public static final float V_HEIGHT = 9216;
	public static  final float PPM = 200;//pixeles que se mueve un objeto por metro en box2d

	public static final short DEFAULT_BIT = 1;
	public static final short MAGE_BIT = 2;
	public static final short PLATAFORM_BIT = 4;
	public static final short DOOR_BIT = 8;
	public static final short BOTON_BIT = 16;

	//SpriteBatch contiene todos los sprites, que luego se muestran
	public static SpriteBatch batch;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		setScreen(new PlayScreen(this));
	}

	@Override
	public void render () {
		//Delega el método render al Playscreen en uso
		super.render();
	}

}
