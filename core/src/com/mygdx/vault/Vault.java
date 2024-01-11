package com.mygdx.vault;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.vault.Screens.PlayScreen;

public class Vault extends Game {

	//SpriteBatch contiene todos los sprites, que luego se muestran
	public SpriteBatch batch;
	
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
