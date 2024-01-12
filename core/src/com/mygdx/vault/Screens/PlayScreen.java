package com.mygdx.vault.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Vault;

public class PlayScreen implements Screen {
    private Vault game;
    Texture texture; //temporal para pruebas

    private OrthographicCamera gamecam; //camara del juego (2d)
    private Viewport gamePort; // determina la escala o forma en la que se muestra la pantalla


    public PlayScreen(Vault game) {
        this.game = game;
        texture= new Texture("badlogic.jpg");//prueba
        gamecam= new OrthographicCamera();
        gamePort= new FitViewport(800,480, gamecam);//Muestra el mapa de forma que pone barras en los margenes
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        //limpiar pantalla
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //sprites
        game.batch.setProjectionMatrix(gamecam.combined);//hace la camara solo cargue lo mostrado en pantalla
        game.batch.begin();
        game.batch.draw(texture,0,0);
        game.batch.end();

    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width,height);//ajusta el tamaño de la pantalla en resizes
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
