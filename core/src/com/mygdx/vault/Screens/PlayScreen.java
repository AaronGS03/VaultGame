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
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
import com.mygdx.vault.Scenes.Controller;
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


/**
 * Pantalla de juego que implementa la interfaz Screen.
 */
public class PlayScreen implements Screen {
    /** Instancia del juego */
    private Vault game;

    /** Atlas de texturas */
    private TextureAtlas atlas;

    /** Cámara del juego (2D) */
    private OrthographicCamera gamecam;

    /** Cámara del fondo (2D) */
    private OrthographicCamera backcam;

    /** Determina la escala o forma en la que se muestra la pantalla */
    private Viewport gamePort;

    /** Cargador de mapas Tiled */
    private TmxMapLoader mapLoader;

    /** Mapa actual */
    private TiledMap map;

    /** Renderizador de mapas Tiled */
    private OrthogonalTiledMapRenderer renderer;

    /** Jugador */
    public Mage player;

    /** Jugador 2 (no utilizado) */
    private Mage player2;

    /** Indica si la configuración secreta está activa */
    private boolean secretSetting = false;

    /** Indica si se permite el toque */
    private boolean touch = true;

    /** Contador de toques */
    private int touchCount;

    /** Indica si el sonido está activado */
    private boolean sound = true;

    /** Intervalo de toque */
    private int intervalTouch;

    /** Mundo de Box2D */
    private World world;

    /** Renderizador de depuración de Box2D */
    private Box2DDebugRenderer b2dr;

    /** Creador del mundo de Box2D */
    private B2WorldCreator creator;

    /** Capas de fondo paralaje */
    private ParallaxLayer[] layers;

    /** Administrador de recursos */
    private AssetManager manager;

    /** Música de fondo */
    private Music music;

    /** Controlador */
    Controller controller;

    /** Interfaz de usuario */
    private Hud hud;

    /** Submenú */
    private SubMenu submenu;

    /** Indica si el juego está en pausa */
    private boolean pause = false;

    /** Indica si el jugador está reapareciendo */
    private boolean isRespawning;

    /** Temporizador de reaparición */
    private float respawnTimer;

    /** Temporizador de sonido de pasos */
    private float stepSoundTimer = 0;

    /** Frecuencia deseada de los sonidos de pasos */
    private static float STEP_SOUND_INTERVAL = 0.4f;

    /** Frecuencia deseada de los sonidos de aterrizaje */
    private static float LAND_SOUND_INTERVAL = 0.22f;

    /** Frecuencia deseada de los sonidos de salto */
    private static float JUMP_SOUND_INTERVAL = 0.15f;

    /** Array de habitaciones */
    public Array<Room> habitaciones = new Array<Room>();

    /** Array de puertas */
    public Array<Door> doors = new Array<Door>();

    /** Nivel de aparición predeterminado */
    public int levelSpawn = 1;

    /** Volumen de sonido predeterminado */
    public float volume = 0.2f;

    /** Indica si los efectos están activados */
    private boolean effects = true;

    /**
     * Verifica si la puerta está abierta.
     * @return true si la puerta está abierta, false de lo contrario
     */
    public boolean isOpenDoor() {
        return openDoor;
    }

    /**
     * Establece el estado de la puerta.
     * @param openDoor true para abrir la puerta, false para cerrarla
     */
    public void setOpenDoor(boolean openDoor) {
        this.openDoor = openDoor;
    }

    private boolean openDoor = false; // Indica si la puerta está abierta

    /**
     * Verifica si la configuración secreta está activada.
     * @return true si la configuración secreta está activada, false de lo contrario
     */
    public boolean isSecretSetting() {
        return secretSetting;
    }

    public int hitdoorlevel = 0; // Nivel de la puerta impactada

    /**
     * Verifica si el nivel 10 tiene truco activado.
     * @return true si el truco del nivel 10 está activado, false de lo contrario
     */
    public boolean isLevel10gimmick() {
        return level10gimmick;
    }

    /**
     * Establece el estado del truco del nivel 10.
     * @param level10gimmick true para activar el truco del nivel 10, false para desactivarlo
     */
    public void setLevel10gimmick(boolean level10gimmick) {
        this.level10gimmick = level10gimmick;
    }

    private boolean level10gimmick = false; // Indica si el truco del nivel 10 está activado

    /**
     * Establece el estado del truco del nivel 13.
     * @param level13gimmick true para activar el truco del nivel 13, false para desactivarlo
     */
    public void setLevel13gimmick(boolean level13gimmick) {
        this.level13gimmick = level13gimmick;
    }

    private boolean level13gimmick = false; // Indica si el truco del nivel 13 está activado

    /**
     * Establece el estado del truco del nivel 14.
     * @param level14gimmick true para activar el truco del nivel 14, false para desactivarlo
     */
    public void setLevel14gimmick(boolean level14gimmick) {
        this.level14gimmick = level14gimmick;
    }

    private boolean level14gimmick = false; // Indica si el truco del nivel 14 está activado

    /**
     * Verifica si el truco del nivel 69 está activado.
     * @return true si el truco del nivel 69 está activado, false de lo contrario
     */
    public boolean isLevel69gimmick() {
        return level69gimmick;
    }

    /**
     * Establece el estado del truco del nivel 69.
     * @param level69gimmick true para activar el truco del nivel 69, false para desactivarlo
     */
    public void setLevel69gimmick(boolean level69gimmick) {
        this.level69gimmick = level69gimmick;
    }

    private boolean level69gimmick = false; // Indica si el truco del nivel 69 está activado

    /**
     * Verifica si el truco del nivel 7 está activado.
     * @return true si el truco del nivel 7 está activado, false de lo contrario
     */
    public boolean isLevel7gimmick() {
        return level7gimmick;
    }

    /**
     * Establece el estado del truco del nivel 7.
     * @param level7gimmick true para activar el truco del nivel 7, false para desactivarlo
     */
    public void setLevel7gimmick(boolean level7gimmick) {
        this.level7gimmick = level7gimmick;
    }

    private boolean level7gimmick = false; // Indica si el truco del nivel 7 está activado

    /**
     * Verifica si el truco del nivel 3 está activado.
     * @return true si el truco del nivel 3 está activado, false de lo contrario
     */
    public boolean isLevel3gimmick() {
        return level3gimmick;
    }

    /**
     * Establece el estado del truco del nivel 3.
     * @param level3gimmick true para activar el truco del nivel 3, false para desactivarlo
     */
    public void setLevel3gimmick(boolean level3gimmick) {
        this.level3gimmick = level3gimmick;
    }

    private boolean level3gimmick = false; // Indica si el truco del nivel 3 está activado

    /**
     * Verifica si el truco del nivel 8 está activado.
     * @return true si el truco del nivel 8 está activado, false de lo contrario
     */
    public boolean isLevel8gimmick() {
        return level8gimmick;
    }

    /**
     * Establece el estado del truco del nivel 8.
     * @param level8gimmick true para activar el truco del nivel 8, false para desactivarlo
     */
    public void setLevel8gimmick(boolean level8gimmick) {
        this.level8gimmick = level8gimmick;
    }

    private boolean level8gimmick = false; // Indica si el truco del nivel 8 está activado

    public MainMenuScreen mainMenuScreen; // Pantalla principal del menú


    /**
     * Constructor de la pantalla de juego.
     *
     * @param game        Instancia del juego.
     * @param manager     Administrador de recursos.
     * @param levelSpawn  Nivel de aparición inicial.
     */
    public PlayScreen(Vault game, AssetManager manager, int levelSpawn) {
        this.manager = manager;
        atlas = new TextureAtlas("Sprites.atlas");

        this.game = game;
        intervalTouch = 500;

        this.effects = game.effects;
        this.sound = game.sound;


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

    /**
     * Obtiene el atlas de texturas.
     * @return El atlas de texturas
     */
    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {
        // Método de la interfaz Screen que se ejecuta cuando la pantalla se muestra
    }

    public boolean reset = false; // Indica si se debe reiniciar el juego

    /**
     * Maneja la entrada del usuario.
     * @param dt El tiempo delta
     */
    public void handleInput(float dt) {
        if (!player.dead) { // Verifica si el jugador está vivo
            if (player.currentLevel == 5) { // Nivel 5
                if (!isRespawning) { // Si no está reapareciendo
                    handleDragInput(); // Maneja la entrada de arrastre
                } else {
                    isDragging = false; // No se está arrastrando
                }
            } else if (player.currentLevel == 4) { // Nivel 4
                gravityMovement(); // Movimiento gravitacional
            } else if (player.currentLevel == 3 && level3gimmick) { // Nivel 3 con truco activado
                inversedMovement(); // Movimiento invertido
            } else if (player.currentLevel == 6) { // Nivel 6
                gyroscopeMovement(); // Movimiento del giroscopio
            } else if (player.currentLevel == 7) { // Nivel 7
                world.setGravity(new Vector2(world.getGravity().x, -45)); // Ajusta la gravedad
                scrambleMovement(); // Movimiento de desorden
            } else { // Otros niveles
                world.setGravity(new Vector2(world.getGravity().x, -45)); // Ajusta la gravedad
                regularMovement(); // Movimiento regular
            }
        }
    }

    /**
     * Maneja el movimiento del giroscopio del dispositivo.
     */
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


    /**
     * Indica si el jugador está siendo arrastrado.
     */
    public boolean isDragging = false;

    /**
     * Indica si se debe detener el arrastre del jugador.
     */
    public boolean stopdraggin = false;

    /**
     * Maneja la entrada de arrastre del jugador.
     */
    private void handleDragInput() {
        Vector3 initialTouch = new Vector3();

        if (Gdx.input.isTouched()) { // Verifica si se está tocando la pantalla
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            Vector3 touchPoint = new Vector3(touchX, touchY, 0);
            gamecam.unproject(touchPoint); // Convierte las coordenadas de la pantalla a coordenadas de la cámara del juego

            Rectangle playerBounds = player.getBoundingRectangle();

            if (!isDragging) { // Si no se está arrastrando
                // Si el toque inicial está dentro de los límites del jugador, registra la posición inicial
                if (playerBounds.contains(touchPoint.x, touchPoint.y)) {
                    initialTouch.set(touchPoint);
                    isDragging = true;
                }
                if (stopdraggin) { // Si se debe detener el arrastre
                    // Aplica un impulso lineal para detener el movimiento del jugador
                    player.b2body.applyLinearImpulse(new Vector2(0, world.getGravity().y), player.b2body.getWorldCenter(), true);
                    stopdraggin = false;
                }
            } else { // Si ya se está arrastrando
                // Actualiza la posición del jugador según la posición del toque
                player.b2body.setTransform(touchPoint.x, touchPoint.y, player.b2body.getAngle());
                player.isTouchingGrass=false; // Indica que el jugador ya no está tocando la hierba
                stopdraggin = true; // Indica que se debe detener el arrastre
            }
        } else {
            // Reinicia el estado cuando se levanta el dedo
            isDragging = false;
        }
    }

    /**
     * Maneja el movimiento regular del jugador.
     */
    private void regularMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {
            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
            }
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0 && player.previousState != Mage.State.JUMPING && player.previousState != Mage.State.AIRTRANSITION) {
                // Aplica un impulso lineal hacia arriba si no está en el aire y no está saltando
                player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
                controller.setUpPressed(false);
            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);
        }

        // Movimiento del jugador usando el teclado
        keyboardMovement();
    }


    /**
     * Maneja el movimiento inverso del jugador.
     */
    private void inversedMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {
            if (controller.isRightPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
            }
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().y == 0 && player.previousState != Mage.State.JUMPING && player.previousState != Mage.State.AIRTRANSITION) {
                // Aplica un impulso lineal hacia arriba si no está en el aire y no está saltando
                player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
                controller.setUpPressed(false);
            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);
        }

        // Movimiento del jugador usando el teclado
        keyboardMovement();
    }

    /**
     * Maneja el movimiento caótico del jugador.
     */
    private void scrambleMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {
            if (controller.isUpPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
            }
            if (controller.isRightPressed() && player.b2body.getLinearVelocity().y == 0 && player.previousState != Mage.State.JUMPING && player.previousState != Mage.State.AIRTRANSITION) {
                // Aplica un impulso lineal hacia arriba si no está en el aire y no está saltando
                player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
                controller.setRightPressed(false);
            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);
        }

        // Movimiento del jugador usando el teclado
        keyboardMovement();
    }

    /**
     * Maneja el movimiento con gravedad invertida del jugador.
     */
    private void gravityMovement() {
        if (controller.isLeftPressed() || controller.isRightPressed() || controller.isUpPressed()) {
            if (controller.isLeftPressed() && player.b2body.getLinearVelocity().x >= -20) {
                player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
            } else if (controller.isRightPressed() && player.b2body.getLinearVelocity().x <= 20) {
                player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
            }
            if (controller.isUpPressed()) {
                // Invierte la gravedad vertical del mundo
                world.setGravity(new Vector2(world.getGravity().x, -world.getGravity().y));
                controller.setUpPressed(false);
            }
        } else {
            player.b2body.applyForce(new Vector2(0, 0), player.b2body.getWorldCenter(), true);
        }

        // Movimiento del jugador usando el teclado
        keyboardMovement();
    }

    /**
     * Maneja el movimiento del jugador mediante el teclado.
     */
    private void keyboardMovement() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            // Aplica un impulso lineal hacia arriba cuando se presiona la tecla de flecha hacia arriba
            player.b2body.applyLinearImpulse(new Vector2(0, 35f), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && player.b2body.getLinearVelocity().x >= -20) {
            // Aplica una fuerza hacia la derecha si se presiona la tecla de flecha hacia la derecha
            player.b2body.applyForce(new Vector2(50f, 0), player.b2body.getWorldCenter(), true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && player.b2body.getLinearVelocity().x <= 20) {
            // Aplica una fuerza hacia la izquierda si se presiona la tecla de flecha hacia la izquierda
            player.b2body.applyForce(new Vector2(-50f, 0), player.b2body.getWorldCenter(), true);
        }
    }



    /**
     * Updates Mage's state and position.
     *
     * @param dt The time difference since the last frame.
     */
    public void update(float dt) {

        if (!game.sound) {
            music.setVolume(0);
        } else {
            music.setVolume(1f);
        }
        if (player.currentLevel == 12) {
            music.stop();
        } else {
            music.play();
        }
        game.volume = volume;

        if (!game.effects) {
            volume = 0;
        } else {
            volume = 0.2f;

        }
        if (submenu.touch) {
            manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(volume);
            submenu.touch = false;
        }


        if (!hud.pause) {

            if (!player.dead) {
                handleInput(dt);
                if (player.currentLevel == 9) {
                    player.b2body.applyForce(new Vector2(-20f, 0), player.b2body.getWorldCenter(), true);
                }

            }
            if (level10gimmick && level69gimmick && player.keys.collected) {
                player.currentLevel=11;
                player.dead=true;
                level69gimmick = false;
            } else {
                level10gimmick = false;
            }

            if (level13gimmick) {
                player.currentLevel=(12);
                player.dead=(true);
                level13gimmick = false;
            }



            if (level7gimmick) {
                player.currentLevel=(8);
                player.dead=(true);
            }

            if (level8gimmick && player.currentLevel == 8) {
                for (Spike s :
                        creator.fakespikes) {
                    s.fake();
                }
                creator.sensors.get(3).fixture.setSensor(true);
                creator.sensors.get(6).fixture.setSensor(false);
                creator.sensors.get(2).body.destroyFixture(creator.sensors.get(2).fixture);

                level8gimmick = false;
            }

            if (player.currentState == Mage.State.LANDING && player.isTouchingGrass) {
                if (stepSoundTimer <= 0) {
                    manager.get("audio/sounds/Single-footstep-in-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = LAND_SOUND_INTERVAL; // Reinicia el temporizador
                }

            }
            if (player.currentState == Mage.State.JUMPING && player.isTouchingGrass) {
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
            if (player.isTouchingGrass && player.b2body.getLinearVelocity().x != 0 && (controller.isRightPressed() || controller.isLeftPressed())) {
                // Verifica el temporizador antes de reproducir el sonido

                if (stepSoundTimer <= 0) {
                    manager.get("audio/sounds/Single-footstep-in-grass.mp3", Sound.class).play(volume);
                    stepSoundTimer = STEP_SOUND_INTERVAL; // Reinicia el temporizador
                }
            }
            if (openDoor) {

                if (player.keys != null && hitdoorlevel != -1) {
                    if (player.keys.collected && (hitdoorlevel == player.currentLevel || (player.currentLevel == 10 && hitdoorlevel == 9)) && hitdoorlevel != 10) {
                        doors.get(hitdoorlevel).change = true;
                        doors.get(hitdoorlevel).fixture.setSensor(true);
                        manager.get("audio/sounds/doorKey.mp3", Sound.class).play(volume);

                    } else {
                        manager.get("audio/sounds/lockedDoor.mp3", Sound.class).play(volume);

                        doors.get(hitdoorlevel).fixture.setSensor(false);

                    }
                }
                setOpenDoor(false);


            }


            // Resta el tiempo delta al temporizador
            stepSoundTimer -= dt;

            world.step(1 / 60f, 6, 2);

            player.update(dt);


            for (Key key : creator.keys) {
                key.update(dt);
            }

            if (player.dead && stepSoundTimer <= -1.5) {

                if (!isRespawning) {
                    respawnTimer -= dt;
                    if (respawnTimer <= 0) {
                        // Permitir que el jugador se mueva después de esperar el tiempo de respawn
                        isRespawning = true;
                        respawnTimer = 0.5f; // Reiniciar el temporizador para futuras reapariciones
                    }
                } else {
                    prefs.putInteger("deaths",prefs.getInteger("deaths")+1);
                    prefs.flush();
                    // Mover el personaje solo si no está en proceso de respawn
                    if (level7gimmick) {
                        player.currentLevel=(8);
                    }
                    if (level10gimmick) {
                        player.currentLevel=(11);
                    }
                    player.b2body.setTransform(habitaciones.get(player.currentLevel).spawnPoint.x / Vault.PPM, habitaciones.get(player.currentLevel).spawnPoint.y / Vault.PPM, 0);
                    player.dead=(false);

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

    /**
     * Método para dibujar el juego.
     */
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
            player.currentLevel=(2);
        }
        player.draw(game.batch);
        for (Spike spike : creator.spikes) {
            spike.draw(game.batch);
        }
        for (Spike spike : creator.fakespikes) {
            spike.draw(game.batch);
        }
        for (Key key : creator.keys) {
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


        if (player.currentLevel != newLevel) {
            String title = getLevelTitle(player.currentLevel + 1);
            newLevel = player.currentLevel;
            if (player.currentLevel > prefs.getInteger("highestLevel", 0)) {
                prefs.putInteger("highestLevel", player.currentLevel);
                prefs.flush();
            }

            hud.table.reset();
            hud.table.top().right().padTop(120).padRight(120).setFillParent(true);
            hud.titleLabel=new Label(title, hud.labelStyle);
            hud.table.add(hud.pauseImage).right().size(hud.pauseImage.getWidth(), hud.pauseImage.getHeight());
            hud.table.row();
            hud.table.add(hud.titleLabel).expandX().top().right().padRight(240).padTop(-150);

            submenu.table.reset();
            submenu.table.add(submenu.continueImage).size(submenu.continueImage.getWidth(), submenu.continueImage.getHeight()).pad(10);
            submenu.table.add(submenu.respawnImage).size(submenu.respawnImage.getWidth(), submenu.respawnImage.getHeight()).pad(10);
            submenu.table.add(submenu.menuScreenImage).size(submenu.menuScreenImage.getWidth(), submenu.menuScreenImage.getHeight()).pad(10);
            submenu.table.add(submenu.volumeImage).size(submenu.volumeImage.getWidth(), submenu.volumeImage.getHeight()).pad(10);
            submenu.table.add(submenu.effectsSoundImage).size(submenu.effectsSoundImage.getWidth(), submenu.effectsSoundImage.getHeight()).pad(10);
            submenu.table.row();
            submenu.table.add(submenu.clueImage).colspan(5).size(submenu.clueImage.getWidth(), submenu.clueImage.getHeight()).pad(1).padTop(90).row();
            submenu.clue=getClue(player.currentLevel+1);
            submenu.clueLabel= new Label("+0.10p por ver pista\n"+submenu.clue,submenu.labelStyle);
            submenu.clueLabel.setVisible(false);
            submenu.table.add(submenu.clueLabel).colspan(5).padTop(200);

        }

        if (hud.pause) {
            controller.leftImage.getStage().addActor(submenu.table);


        } else {
            if (controller.leftImage.getStage().getActors().size == 4) {
                controller.leftImage.getStage().getActors().get(3).remove();

            }

        }


        submenu.draw();
        hud.draw();
        controller.draw(delta);
        if (level14gimmick) {
            game.setScreen(new CredistScreen(game));
            level14gimmick = false;
            this.dispose();
        }
    }

    /**
     * Representa el objeto JSON que contiene los títulos de los niveles.
     */
    private JsonValue titlesObject;

    /**
     * Representa el objeto JSON que contiene las pistas de los niveles.
     */
    private JsonValue cluesObject;

    /**
     * Carga los títulos y las pistas de los niveles desde un archivo JSON.
     */
    public void loadLevelTitles() {
        FileHandle fileHandle = Gdx.files.internal("data/level_titles.json");
        String jsonData = fileHandle.readString();
        JsonValue root = new JsonReader().parse(jsonData);
        switch (game.language) {
            case 0:
                titlesObject = root.get("titles");
                cluesObject = root.get("clues");
                break;
            case 1:
                titlesObject = root.get("titulos");
                cluesObject= root.get("pistas");
                break;
        }
    }

    /**
     * Obtiene el título del nivel especificado.
     *
     * @param level El número del nivel.
     * @return El título del nivel o "default" si no se encuentra.
     */
    public String getLevelTitle(int level) {
        return titlesObject.getString(level + "", "default");
    }

    /**
     * Obtiene la pista del nivel especificado.
     *
     * @param level El número del nivel.
     * @return La pista del nivel o "default" si no se encuentra.
     */
    public String getClue(int level) {
        return cluesObject.getString(level + "", "default");
    }

    /**
     * Redimensiona la pantalla del juego.
     *
     * @param width  Ancho de la pantalla.
     * @param height Altura de la pantalla.
     */
    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height); // ajusta el tamaño de la pantalla en resizes
        controller.resize(width, height);
        hud.resize(width, height);
        submenu.resize(width, height);
    }

    /**
     * Obtiene el mapa del juego.
     *
     * @return El mapa del juego.
     */
    public TiledMap getMap() {
        return map;
    }

    /**
     * Obtiene el mundo Box2D del juego.
     *
     * @return El mundo Box2D del juego.
     */
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

    /**
     * Libera los recursos en uso
     */
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