package com.mygdx.vault.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Contols.Controller;
import com.mygdx.vault.Scenes.Hud;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Vault;
import com.mygdx.vault.tools.B2WorldCreator;
import com.mygdx.vault.tools.WorldContactListener;

import java.util.ArrayList;

public class PlayScreen implements Screen {
    private Vault game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam; //camara del juego (2d)
    private OrthographicCamera backcam; //camara del fondo (2d)

    private Viewport gamePort; // determina la escala o forma en la que se muestra la pantalla

    private TmxMapLoader mapLoader; //carga el mapa
    private TiledMap map; //este es el mapa
    private OrthogonalTiledMapRenderer renderer;
    private Mage player;
    private boolean secretSetting = false;
    private boolean touch = true;
    private int touchCount;
    private int intervalTouch;
    //Variables de Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private ParallaxLayer[] layers;

    Controller controller;

    private Hud hud;


    public PlayScreen(Vault game) {
        atlas = new TextureAtlas("mage.atlas");

        this.game = game;
        intervalTouch = 500;

        gamecam = new OrthographicCamera();//camara que sigue al mapa
        backcam = new OrthographicCamera(Vault.V_WIDTH * 2 / Vault.PPM, Vault.V_HEIGHT / Vault.PPM);//camara que sigue al personaje
        gamePort = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, gamecam);//Muestra el mapa de forma que pone barras en los margenes
        hud = new Hud(game.batch);
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / Vault.PPM);
        gamecam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
        backcam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -45), true);
        b2dr = new Box2DDebugRenderer();

        new B2WorldCreator(world, map);

        player = new Mage(world, this);
        controller = new Controller();

        layers = new ParallaxLayer[7];
        layers[0] = new ParallaxLayer(new Texture("01.png"), 23f, true, false);
        layers[1] = new ParallaxLayer(new Texture("02.png"), 25f, true, false);
        layers[2] = new ParallaxLayer(new Texture("03.png"), 29f, true, false);
        layers[3] = new ParallaxLayer(new Texture("04.png"), 29f, true, false);
        layers[4] = new ParallaxLayer(new Texture("05.png"), 16, true, false);
        layers[5] = new ParallaxLayer(new Texture("06.png"), 16, true, false);

        for (int i = 5; i >= 0; i--) {
            layers[i].setCamera(backcam);
        }

        world.setContactListener(new WorldContactListener());
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    //maneja tocar pantalla
    public void handleInput(float dt) {
        //Funcion secreta tocar personaje
        // Manejar toques en el sprite del personaje
        if (Gdx.input.isTouched() && !touch) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            Vector3 touchPoint = new Vector3(touchX, touchY, 0);
            gamecam.unproject(touchPoint); // devuelve el punto tocado

            Rectangle playerBounds = player.getBoundingRectangle();

            // Verificar si el toque está dentro del rectángulo del sprite del personaje
            if (playerBounds.contains(touchPoint.x, touchPoint.y)) {
                touchCount++;

                // Realizar la acción después de 3 toques
                if (touchCount == 3) {
                    secretSetting = !secretSetting;
                    touch = true;
                    touchCount = 0;
                }
            }
            } else if (touch) {

                intervalTouch--;
                if (intervalTouch == 0) {
                    touch = false;
                    intervalTouch =5;

            }
        }

        //Moviento del personaje movil

        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {

            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);

            }
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0 && player.previousState != Mage.State.JUMPING && player.previousState != Mage.State.AIRTRANSITION) {//getlinearvelocity detecta que está tocando el suelo viendo que no esta cayendo ni subiendo
                player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
                controller.setUpPressed(false);
            }
        } else {

            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);
        }

        //Movimiento personaje teclado
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x <= 12) {
            player.b2body.applyForce(new Vector2(25f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x >= -12) {
            player.b2body.applyForce(new Vector2(-25f, 0), player.b2body.getWorldCenter(), true);
        }
    }

    //Aqui se maneja lo que ocurre en el juego
    public void update(float dt) {
        handleInput(dt);

        world.step(1 / 60f, 6, 2);

        player.update(dt);

        backcam.position.x = player.b2body.getPosition().x;

        backcam.update();
        gamecam.update();
        renderer.setView(gamecam); //esto hará que solo renderice lo que se ve en pantalla
    }

    @Override
    public void render(float delta) {
        update(delta);

        //limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(backcam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        if (secretSetting) {
            for (int i = 5; i >= 0; i--) {
                layers[i].render(game.batch);
            }
        }
        player.draw(game.batch);
        game.batch.end();
        //mostrar pantalla
        renderer.render();

        //render Lineas debug Box2d
        //b2dr.render(world, gamecam.combined);


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        controller.draw();
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);//ajusta el tamaño de la pantalla en resizes
        controller.resize(width, height);
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
        map.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        controller.dispose();
    }
}
