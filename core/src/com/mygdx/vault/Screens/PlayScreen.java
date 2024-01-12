package com.mygdx.vault.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Scenes.Hud;
import com.mygdx.vault.Vault;

public class PlayScreen implements Screen {
    private Vault game;

    private OrthographicCamera gamecam; //camara del juego (2d)
    private Viewport gamePort; // determina la escala o forma en la que se muestra la pantalla

    private TmxMapLoader mapLoader; //carga el mapa
    private TiledMap map; //este es el mapa
    private OrthogonalTiledMapRenderer renderer;

    //Variables de Box2d
    private World world;
    private Box2DDebugRenderer b2dr;


    private Hud hud;
    public PlayScreen(Vault game) {
        this.game = game;
        gamecam = new OrthographicCamera();//camara que sigue al mapa
        gamePort = new FitViewport(Vault.V_WIDTH,Vault.V_HEIGHT, gamecam);//Muestra el mapa de forma que pone barras en los margenes
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0,0), true);
        b2dr= new Box2DDebugRenderer();

    }

    @Override
    public void show() {

    }

    //maneja tocar pantalla
    public void handleInput(float dt) {
        //prueba para ver el mapa al tocar
        if (Gdx.input.isTouched()) {
            gamecam.position.x += 100 * dt;
        }
    }

    //Aqui se maneja lo que ocurre en el juego
    public void update(float dt) {
        handleInput(dt);
        gamecam.update();
        renderer.setView(gamecam); //esto hará que solo renderice lo que se ve en pantalla
    }

    @Override
    public void render(float delta) {
        update(delta);

        //limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();

        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();


    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);//ajusta el tamaño de la pantalla en resizes
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
