package com.mygdx.vault.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Screens.MainMenuScreen;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Vault;

/**
 * Clase SubMenu que representa el menú de pausa del juego.
 * Permite al jugador realizar acciones como continuar, reiniciar, ajustar el volumen, entre otros.
 */
public class SubMenu implements Disposable {
    private Viewport viewport; // Viewport para el menú de pausa
    private Stage stage; // Escena para el menú de pausa
    public Image continueImage; // Imagen para el botón de continuar
    public Image menuScreenImage; // Imagen para el botón de volver al menú principal
    public Image respawnImage; // Imagen para el botón de reiniciar
    public Image volumeImage; // Imagen para el botón de ajustar volumen
    public Image effectsSoundImage; // Imagen para el botón de ajustar efectos de sonido
    public Image clueImage; // Imagen para el botón de pista
    public Label clueLabel; // Etiqueta para mostrar la pista
    private OrthographicCamera cam; // Cámara ortográfica para el menú de pausa
    public boolean touch=false; // Indica si se ha tocado algún botón del menú
    private Mage player; // Referencia al jugador
    Screen screen; // Pantalla actual del juego
    public Table table; // Tabla para organizar los elementos del menú de pausa
    private Vault game; // Instancia del juego
    public Label.LabelStyle labelStyle; // Estilo de las etiquetas de texto
    public String clue=""; // Pista actual

    /**
     * Constructor de la clase SubMenu.
     * @param stageC La escena en la que se dibujará el menú de pausa.
     * @param hud El HUD del juego.
     * @param game La instancia del juego.
     * @param player La instancia del jugador.
     * @param screen La pantalla actual del juego.
     */
    public SubMenu(Stage stageC, Hud hud, Vault game, Mage player, PlayScreen screen) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage=stageC;
        BitmapFont font = generateFont();

        Gdx.input.setInputProcessor(stage);
        this.screen= screen;
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        this.game=game;

        table = new Table();
        table.top().padTop(60).setFillParent(true);
        this.player=player;

        // Configuración de las imágenes de los botones y sus eventos de clic
        continueImage = new Image(new Texture("continueImage.png"));
        continueImage.setSize(150, 150);
        continueImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hud.pause= false;
                touchButton();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        menuScreenImage = new Image(new Texture("menuImage.png"));
        menuScreenImage.setSize(150, 150);
        menuScreenImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchButton();
                game.getScreen().dispose();
                game.setScreen(new MainMenuScreen(game));
                dispose();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        respawnImage = new Image(new Texture("respawnImage.png"));
        respawnImage.setSize(150, 150);
        respawnImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchButton();
                player.setDead(true);
                hud.pause=false;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        volumeImage = new Image(new Texture("volumeImage.png"));
        volumeImage.setSize(150, 150);
        volumeImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchButton();
                game.setSound(!game.isSound());
                prefs.putBoolean("sound",!prefs.getBoolean("sound"));
                prefs.flush();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        effectsSoundImage = new Image(new Texture("effectsSoundsImage.png"));
        effectsSoundImage.setSize(150, 150);
        effectsSoundImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchButton();
                game.setEffects(!game.isEffects());
                prefs.putBoolean("effects",!prefs.getBoolean("effects"));
                prefs.flush();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        clueImage = new Image(new Texture("clueImage.png"));
        clueImage.setSize(150, 150);
        clueImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                touchButton();
                clueLabel.setVisible(!clueLabel.isVisible());
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        clueLabel = new Label("+0.10 puntos por usar pista:\n"+clue,labelStyle);
        clueLabel.setColor(Color.WHITE);
        clueLabel.setVisible(false);

        // Configuración de la disposición de los elementos en la tabla
        table.add(continueImage).size(continueImage.getWidth(), continueImage.getHeight()).pad(10);
        table.add(respawnImage).size(respawnImage.getWidth(), respawnImage.getHeight()).pad(10);
        table.add(menuScreenImage).size(menuScreenImage.getWidth(), menuScreenImage.getHeight()).pad(10);
        table.add(volumeImage).size(volumeImage.getWidth(), volumeImage.getHeight()).pad(10);
        table.add(effectsSoundImage).size(effectsSoundImage.getWidth(), effectsSoundImage.getHeight()).pad(10);
        table.row();
        table.add(clueImage).colspan(5).size(clueImage.getWidth(), clueImage.getHeight()).pad(1).padTop(90).row();
        table.add(clueLabel).colspan(5);

        stage.addActor(table); // Agrega la tabla a la escena
    }

    /**
     * Método para dibujar el menú de pausa en pantalla.
     */
    public void draw() {
        stage.draw();
    }

    /**
     * Método para indicar que se ha tocado un botón del menú.
     */
    public void touchButton() {
        touch=true;
    }

    /**
     * Genera un nuevo tipo de letra personalizado.
     * @return La fuente de texto generada.
     */
    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cityburn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float fontSize = stage.getHeight() * 0.06f;
        parameter.size = (int) fontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Importante: liberar recursos del generador
        return font;
    }

    /**
     * Redimensiona la vista del menú de pausa.
     * @param width Ancho de la ventana.
     * @param height Altura de la ventana.
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        // No se realizan acciones de disposición
    }
}
