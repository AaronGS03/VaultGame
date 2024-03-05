package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Controls.Controller;
import com.mygdx.vault.Scenes.Hud;
import com.mygdx.vault.Scenes.SubMenu;
import com.mygdx.vault.Sprites.Door;
import com.mygdx.vault.Sprites.Key;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Sprites.Room;
import com.mygdx.vault.Sprites.Spike;
import com.mygdx.vault.Vault;
import com.mygdx.vault.tools.B2WorldCreator;
import com.mygdx.vault.tools.WorldContactListener;

public class PlayScreen implements Screen {
    private Vault game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam; //camara del juego (2d)
    private OrthographicCamera backcam; //camara del fondo (2d)

    private Viewport gamePort; // determina la escala o forma en la que se muestra la pantalla

    private TmxMapLoader mapLoader; //carga el mapa
    private TiledMap map; //este es el mapa
    private OrthogonalTiledMapRenderer renderer;
    public Mage player;
    private Mage player2;
    private boolean secretSetting = false;
    private boolean touch = true;
    private int touchCount;


    private boolean sound = true;
    private int intervalTouch;
    //Variables de Box2d
    private World world;
    private Box2DDebugRenderer b2dr;
    private B2WorldCreator creator;
    private ParallaxLayer[] layers;
    private AssetManager manager;

    private Music music;
    Controller controller;

    private Hud hud;
    private SubMenu submenu;
    private boolean pause = false;
    private boolean isRespawning;
    private float respawnTimer;
    private float stepSoundTimer = 0;
    private static float STEP_SOUND_INTERVAL = 0.4f; // Frecuencia deseada de los sonidos de pasos
    private static float LAND_SOUND_INTERVAL = 0.22f; // Frecuencia deseada de los sonidos de pasos
    private static float JUMP_SOUND_INTERVAL = 0.15f; // Frecuencia deseada de los sonidos de pasos


    public Array<Room> habitaciones = new Array<Room>();
    public Array<Door> doors = new Array<Door>();

    public int levelSpawn = 1;
    public float volume = 0.2f;
    private boolean effects = true;

    public boolean isOpenDoor() {
        return openDoor;
    }

    public void setOpenDoor(boolean openDoor) {
        this.openDoor = openDoor;
    }

    private boolean openDoor = false;

    public boolean isSecretSetting() {
        return secretSetting;
    }

    public int hitdoorlevel = 0;

    public boolean isLevel10gimmick() {
        return level10gimmick;
    }

    public void setLevel10gimmick(boolean level10gimmick) {
        this.level10gimmick = level10gimmick;
    }

    private boolean level10gimmick = false;

    public void setLevel13gimmick(boolean level13gimmick) {
        this.level13gimmick = level13gimmick;
    }

    private boolean level13gimmick = false;

    public void setLevel14gimmick(boolean level14gimmick) {
        this.level14gimmick = level14gimmick;
    }

    private boolean level14gimmick = false;

    public boolean isLevel69gimmick() {
        return level69gimmick;
    }

    public void setLevel69gimmick(boolean level69gimmick) {
        this.level69gimmick = level69gimmick;
    }

    private boolean level69gimmick = false;

    public boolean isLevel7gimmick() {
        return level7gimmick;
    }

    public void setLevel7gimmick(boolean level7gimmick) {
        this.level7gimmick = level7gimmick;
    }

    private boolean level7gimmick = false;

    public boolean isLevel3gimmick() {
        return level3gimmick;
    }

    public void setLevel3gimmick(boolean level3gimmick) {
        this.level3gimmick = level3gimmick;
    }

    private boolean level3gimmick = false;

    public boolean isLevel8gimmick() {
        return level8gimmick;
    }

    public void setLevel8gimmick(boolean level8gimmick) {
        this.level8gimmick = level8gimmick;
    }

    private boolean level8gimmick = false;

    public MainMenuScreen mainMenuScreen;


    public PlayScreen(Vault game, AssetManager manager, int levelSpawn) {
        this.manager = manager;
        atlas = new TextureAtlas("Sprites.atlas");
       
        this.game = game;
        intervalTouch = 500;

        this.effects = game.isEffects();
        this.sound = game.isSound();


        this.levelSpawn = levelSpawn;

        gamecam = new OrthographicCamera();//camara que sigue al mapa
        backcam = new OrthographicCamera(Vault.V_WIDTH * 2 / Vault.PPM, Vault.V_HEIGHT / Vault.PPM);//camara que sigue al personaje
        gamePort = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, gamecam);//Muestra el mapa de forma que pone barras en los margenes

        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");

        renderer = new OrthogonalTiledMapRenderer(map, 1 / Vault.PPM);
        gamecam.position.set(17408 / Vault.PPM / 2, 11264 / Vault.PPM + 9216 / Vault.PPM / 2, 0);
        backcam.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -45), true);
        b2dr = new Box2DDebugRenderer();

        controller = new Controller();
        hud = new Hud(controller.leftImage.getStage(), game);

        player = new Mage(this, controller, atlas, manager, habitaciones);

        submenu = new SubMenu(controller.leftImage.getStage(), hud, game, player, this);

        creator = new B2WorldCreator(this, player, habitaciones, doors, gamecam, backcam);


        layers = new ParallaxLayer[4];
        layers[0] = new ParallaxLayer(new Texture("ParallaxCave1.png"), 7f, false, false);
        layers[1] = new ParallaxLayer(new Texture("ParallaxCave2.png"), 7.2f, false, false);
        layers[2] = new ParallaxLayer(new Texture("ParallaxCave3.png"), 7.3f, false, false);
        layers[3] = new ParallaxLayer(new Texture("ParallaxCave4.png"), 7.4f, false, false);

        for (int i = layers.length - 1; i >= 0; i--) {
            layers[i].setCamera(backcam);
        }

        world.setContactListener(new WorldContactListener());

        music = manager.get("audio/music/forgotten-cave-159880.mp3", Music.class);
        music.setLooping(true);
        music.play();

        loadLevelTitles();
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }

    public boolean reset = false;

    public void handleInput(float dt) {
        if (!player.isDead()) {
            if (player.getCurrentLevel() == 5) {

                if (!isRespawning) {
                    handleDragInput();
                } else {
                    isDragging = false;
                }
            } else if (player.getCurrentLevel() == 4) {
                gravityMovement();
            } else if (player.getCurrentLevel() == 3 && level3gimmick) {
                inversedMovement();
            } else if (player.getCurrentLevel() == 6) {
                gyroscopeMovement();
            } else if (player.getCurrentLevel() == 7) {
                world.setGravity(new Vector2(world.getGravity().x, -45));
                scrambleMovement();
            } else {
                world.setGravity(new Vector2(world.getGravity().x, -45));
                regularMovement();

            }

        }
    }

    private void gyroscopeMovement() {
        world.setGravity(new Vector2(world.getGravity().x, 0));

        float roll = Gdx.input.getRoll();
        float pitch = Gdx.input.getPitch();

        float forceX = pitch * -2;
        float forceY = roll * 2;

        // Aplicar las fuerzas al cuerpo del personaje
        player.b2body.applyForceToCenter(forceX, forceY, true);
    }

    //Moviento del personaje movil


    public boolean isDragging = false;
    public boolean stopdraggin = false;

    private void handleDragInput() {
        Vector3 initialTouch = new Vector3();

        if (Gdx.input.isTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            Vector3 touchPoint = new Vector3(touchX, touchY, 0);
            gamecam.unproject(touchPoint); // devuelve el punto tocado

            Rectangle playerBounds = player.getBoundingRectangle();

            if (!isDragging) {
                // Si es el inicio del toque, registra la posición inicial
                if (playerBounds.contains(touchPoint.x, touchPoint.y)) {
                    initialTouch.set(touchPoint);
                    isDragging = true;
                }
                if (stopdraggin) {

                    player.b2body.applyLinearImpulse(new Vector2(0, world.getGravity().y), player.b2body.getWorldCenter(), true);
                    stopdraggin = false;

                }
            } else {
                // Si ya se está arrastrando, actualiza la posición del personaje

                player.b2body.setTransform(touchPoint.x, touchPoint.y, player.b2body.getAngle());
                player.setTouchingGrass(false);
                stopdraggin = true;

            }

        } else {
            // Reinicia el estado cuando se levanta el dedo
            isDragging = false;
        }
    }

    private void regularMovement() {
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
        keyboardMovement();
    }

    private void inversedMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {

            if (controller.isRightPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x <= 20) {
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
        keyboardMovement();
    }

    private void scrambleMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {

            if (controller.isUpPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);

            }
            if (controller.isRightPressed() && player.b2body.getLinearVelocity().y == 0 && player.previousState != Mage.State.JUMPING && player.previousState != Mage.State.AIRTRANSITION) {//getlinearvelocity detecta que está tocando el suelo viendo que no esta cayendo ni subiendo
                player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
                controller.setRightPressed(false);
            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);

        }

        //Movimiento personaje teclado
        keyboardMovement();
    }

    private void gravityMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {

            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);

            }
            if (controller.isUpPressed()) {//getlinearvelocity detecta que está tocando el suelo viendo que no esta cayendo ni subiendo
                world.setGravity(new Vector2(world.getGravity().x, -world.getGravity().y));

                controller.setUpPressed(false);

            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);

        }

        //Movimiento personaje teclado
        keyboardMovement();
    }

    private void keyboardMovement() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x >= -20) {
            player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x <= 20) {
            player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
        }
    }


    //Aqui se maneja lo que ocurre en el juego
    public void update(float dt) {

        if (!game.isSound()) {
            music.setVolume(0);
        } else {
            music.setVolume(1f);
        }
        if (player.getCurrentLevel() == 12) {
            music.stop();
        } else {
            music.play();
        }
        game.volume = volume;

        if (!game.isEffects()) {
            volume = 0;
        } else {
            volume = 0.2f;

        }
        if (submenu.touch) {
            manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(volume);
            submenu.touch = false;
        }


        if (!hud.isPause()) {

            if (!player.isDead()) {
                handleInput(dt);
                if (player.getCurrentLevel() == 9) {
                    player.b2body.applyForce(new Vector2(-20f, 0), player.b2body.getWorldCenter(), true);
                }

            }
            if (level10gimmick && level69gimmick && player.keys.collected) {
                player.setCurrentLevel(11);
                player.setDead(true);
                level69gimmick = false;
            } else {
                level10gimmick = false;
            }

            if (level13gimmick) {
                player.setCurrentLevel(12);
                player.setDead(true);
                level13gimmick = false;
            }

            if (level14gimmick) {
                game.setScreen(new MainMenuScreen(game));
                dispose();
                level14gimmick = false;
            }

            if (level7gimmick) {
                player.setCurrentLevel(8);
                player.setDead(true);
            }

            if (level8gimmick && player.getCurrentLevel() == 8) {
                for (Spike s :
                        creator.getFakespikes()) {
                    s.fake();
                }
                creator.getSensors().get(3).getFixture().setSensor(true);
                creator.getSensors().get(6).getFixture().setSensor(false);
                creator.getSensors().get(2).body.destroyFixture(creator.getSensors().get(2).getFixture());

                level8gimmick = false;
            }

            if (player.currentState == Mage.State.LANDING && player.isTouchingGrass()) {
                if (stepSoundTimer <= 0) {
                    manager.get("audio/sounds/Single-footstep-in-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = LAND_SOUND_INTERVAL; // Reinicia el temporizador
                }

            }
            if (player.currentState == Mage.State.JUMPING && player.isTouchingGrass()) {
                if (stepSoundTimer <= -0.3) {
                    manager.get("audio/sounds/Single-footstep-in-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = JUMP_SOUND_INTERVAL; // Reinicia el temporizador
                }

            }
            if (player.currentState == Mage.State.WALLSLIDER || player.currentState == Mage.State.WALLSLIDEL) {
                if (stepSoundTimer <= 0) {
                    manager.get("audio/sounds/rustling-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = JUMP_SOUND_INTERVAL; // Reinicia el temporizador
                }

            }
            if (player.isTouchingGrass() && player.b2body.getLinearVelocity().x != 0 && (controller.isRightPressed() || controller.isLeftPressed())) {
                // Verifica el temporizador antes de reproducir el sonido

                if (stepSoundTimer <= 0) {
                    manager.get("audio/sounds/Single-footstep-in-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = STEP_SOUND_INTERVAL; // Reinicia el temporizador
                }
            }
            if (openDoor) {

                if (player.keys != null && hitdoorlevel != -1) {
                    if (player.keys.collected && (hitdoorlevel == player.getCurrentLevel() || (player.getCurrentLevel() == 10 && hitdoorlevel == 9)) && hitdoorlevel != 10) {
                        doors.get(hitdoorlevel).change = true;
                        doors.get(hitdoorlevel).getFixture().setSensor(true);
                        manager.get("audio/sounds/doorKey.mp3", Sound.class).play(volume);

                    } else {
                        manager.get("audio/sounds/lockedDoor.mp3", Sound.class).play(volume);

                        doors.get(hitdoorlevel).getFixture().setSensor(false);

                    }
                }
                setOpenDoor(false);


            }


            // Resta el tiempo delta al temporizador
            stepSoundTimer -= dt;

            world.step(1 / 60f, 6, 2);

            player.update(dt);


            for (Key key : creator.getKeys()) {
                key.update(dt);
            }

            if (player.isDead() && stepSoundTimer <= -1.5) {

                if (!isRespawning) {
                    respawnTimer -= dt;
                    if (respawnTimer <= 0) {
                        // Permitir que el jugador se mueva después de esperar el tiempo de respawn
                        isRespawning = true;
                        respawnTimer = 0.5f; // Reiniciar el temporizador para futuras reapariciones
                    }
                } else {
                    // Mover el personaje solo si no está en proceso de respawn
                    if (level7gimmick) {
                        player.setCurrentLevel(8);
                    }
                    if (level10gimmick) {
                        player.setCurrentLevel(11);
                    }
                    player.b2body.setTransform(habitaciones.get(player.getCurrentLevel()).getSpawnPoint().x / Vault.PPM, habitaciones.get(player.getCurrentLevel()).getSpawnPoint().y / Vault.PPM, 0);
                    player.setDead(false);

                    if (player.keys != null) {
                        player.keys.collected = false;
                    }
                    world.setGravity(new Vector2(world.getGravity().x, -45));
                    level10gimmick = false;
                    level7gimmick = false;
                    isRespawning = false; // Marcar que el jugador ha terminado de respawnear
                }
            }


        }
        backcam.position.x = player.b2body.getPosition().x;
        backcam.update();
        gamecam.update();
        renderer.setView(gamecam); //esto hará que solo renderice lo que se ve en pantalla
    }

    int newLevel = -1;
    Preferences prefs = Gdx.app.getPreferences("My Preferences");


    @Override
    public void render(float delta) {
        update(delta);

        //limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(backcam.combined);
        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();

        for (int i = layers.length - 1; i >= 0; i--) {
            layers[i].render(game.batch);


        }
        if (secretSetting) {
            player.setCurrentLevel(2);
        }
        player.draw(game.batch);
        for (Spike spike : creator.getSpikes()) {
            spike.draw(game.batch);
        }
        for (Spike spike : creator.getFakespikes()) {
            spike.draw(game.batch);
        }
        for (Key key : creator.getKeys()) {
            key.draw(game.batch);
        }
        for (Door door :
                doors) {
            door.draw(game.batch);
        }

        game.batch.end();
        //mostrar pantalla

        renderer.render();

        //render Lineas debug Box2d
        //b2dr.render(world, gamecam.combined);


        if (player.getCurrentLevel() != newLevel) {
            String title = getLevelTitle(player.getCurrentLevel() + 1);
            newLevel = player.getCurrentLevel();
            if (player.getCurrentLevel() > prefs.getInteger("highestLevel", 0)) {
                prefs.putInteger("highestLevel", player.getCurrentLevel());
                prefs.flush();
            }

            hud.table.reset();
            hud.table.top().right().padTop(120).padRight(120).setFillParent(true);
            hud.setTitleLabel(new Label(title, hud.getLabelStyle()));
            hud.table.add(hud.pauseImage).right().size(hud.pauseImage.getWidth(), hud.pauseImage.getHeight());
            hud.table.row();
            hud.table.add(hud.getTitleLabel()).expandX().top().right().padRight(240).padTop(-150);


        }

        if (hud.isPause()) {
            controller.leftImage.getStage().addActor(submenu.table);


        } else {
            if (controller.leftImage.getStage().getActors().size == 4) {
                controller.leftImage.getStage().getActors().get(3).remove();

            }

        }


        submenu.draw();
        hud.draw();
        controller.draw(delta);
    }

    private JsonValue titlesObject;

    public void loadLevelTitles() {
        FileHandle fileHandle = Gdx.files.internal("data/level_titles.json");
        String jsonData = fileHandle.readString();
        JsonValue root = new JsonReader().parse(jsonData);
        switch (game.language) {
            case 0:
                titlesObject = root.get("titles");
                break;
            case 1:
                titlesObject = root.get("titulos");
                break;
        }
    }

    public String getLevelTitle(int level) {
        return titlesObject.getString(level + "", "default");
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);//ajusta el tamaño de la pantalla en resizes
        controller.resize(width, height);
        hud.resize(width, height);
        submenu.resize(width, height);
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
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
        atlas.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
        submenu.dispose();
        controller.dispose();
        music.dispose();
        creator.dispose();


    }
}
