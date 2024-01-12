package com.mygdx.vault.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Scenes.Hud;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Vault;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    private Vault game;

    private OrthographicCamera gamecam; //camara del juego (2d)
    private Viewport gamePort; // determina la escala o forma en la que se muestra la pantalla

    private TmxMapLoader mapLoader; //carga el mapa
    private TiledMap map; //este es el mapa
    private OrthogonalTiledMapRenderer renderer;
    private Mage player;

    //Variables de Box2d
    private World world;
    private Box2DDebugRenderer b2dr;

    private Hud hud;

    public PlayScreen(Vault game) {
        this.game = game;
        gamecam = new OrthographicCamera();//camara que sigue al mapa
        gamePort = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, gamecam);//Muestra el mapa de forma que pone barras en los margenes
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Vault.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -45), true);
        b2dr = new Box2DDebugRenderer();

        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            bdef.type = BodyDef.BodyType.StaticBody;
            bdef.position.set((rect.getX() + rect.getWidth() / 2)/Vault.PPM, (rect.getY() + rect.getHeight() / 2)/Vault.PPM);

            body = world.createBody(bdef);

            shape.setAsBox(rect.getWidth() / 2/Vault.PPM, rect.getHeight() / 2/Vault.PPM);
            fdef.shape = shape;
            body.createFixture(fdef);

        }

        player = new Mage(world);

    }

    @Override
    public void show() {

    }

    //maneja tocar pantalla
    public void handleInput(float dt) {
        //Moviento del personaje
        if (Gdx.input.isTouched()) {
            if (Gdx.input.getX()< gamePort.getScreenWidth()/2 && player.b2body.getLinearVelocity().x>= -20){
                player.b2body.applyForce(new Vector2(-50f,0), player.b2body.getWorldCenter(),true);
            }
            if (Gdx.input.getX()>gamePort.getScreenWidth()/2 && player.b2body.getLinearVelocity().x<= 20){
                player.b2body.applyForce(new Vector2(50f,0), player.b2body.getWorldCenter(),true);

            }
        }else {
            player.b2body.applyForce(new Vector2(0,0), player.b2body.getWorldCenter(),true);

        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)){
            player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(),true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x<= 12) {
            player.b2body.applyForce(new Vector2(25f,0), player.b2body.getWorldCenter(),true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x>= -12) {
            player.b2body.applyForce(new Vector2(-25f,0), player.b2body.getWorldCenter(),true);
        }
    }

    //Aqui se maneja lo que ocurre en el juego
    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);


        gamecam.update();
        renderer.setView(gamecam); //esto hará que solo renderice lo que se ve en pantalla
    }

    @Override
    public void render(float delta) {
        update(delta);

        //limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //mostrar pantalla
        renderer.render();

        //render Lineas debug Box2d
        b2dr.render(world, gamecam.combined);

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
