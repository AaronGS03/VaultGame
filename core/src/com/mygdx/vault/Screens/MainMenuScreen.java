package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.vault.Vault;

/**
 * Pantalla de menú principal del juego.
 */
public class MainMenuScreen implements Screen {
    /** Instancia principal del juego */
    private Vault game;

    /** Escenario donde se colocan los elementos visuales */
    private Stage stage;

    /** Textura del botón */
    private Texture buttonTexture;

    /** Textura del botón presionado */
    private Texture buttonTexturedown;

    /** Textura del título */
    private Texture titleTexture;

    /** Textura del personaje "mage" */
    private Texture mageTexture;

    /** Textura de la llave */
    private Texture keyTexture;

    /** Idioma actual del juego */
    int language;

    /** Botón para iniciar el juego */
    TextButton playButton;

    /** Música de fondo del juego */
    private Music music;

    /** Pantalla de selección de niveles */
    private LevelSelectScreen levelsScreen;



    /**
     * Constructor de la pantalla de menú principal.
     *
     * @param game Instancia principal del juego.
     */
    public MainMenuScreen(Vault game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);
        Preferences prefs = Gdx.app.getPreferences("My Preferences");

        this.language = prefs.getInteger("lan", 0);
        game.language = this.language;

        Table table = new Table();
        table.bottom().setFillParent(true);
        stage.addActor(table);

        BitmapFont font = generateFont();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        loadButtonTitles();


        music = game.manager.get("audio/music/mainMenuMusic.mp3", Music.class);
        music.setLooping(true);
        music.play();

        // Configura el fondo con la textura
        buttonTexture = new Texture("button_background.png"); // Asegúrate de tener esta textura
        buttonTexturedown = new Texture("button_backgroundDown.png"); // Asegúrate de tener esta textura
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        buttonStyle.downFontColor = Color.WHITE;
        TextureRegionDrawable buttonBackgrounddown = new TextureRegionDrawable(new TextureRegion(buttonTexturedown));
        buttonStyle.down = buttonBackgrounddown;
        buttonStyle.up = buttonBackground;
        float paddingX = 120f;  // Ajusta según sea necesario
        float paddingY = 10f;  // Ajusta según sea necesario
        buttonStyle.up.setMinWidth(buttonStyle.up.getMinWidth() + paddingX * 2);
        buttonStyle.up.setMinHeight(buttonStyle.up.getMinHeight() + paddingY * 2);


        // Crea los botones con el estilo personalizado
        if (prefs.getInteger("highestLevel", 0) == 0) {
            playButton = createButton(getButtonText("play"), buttonStyle);
        } else {
            playButton = createButton(getButtonText("continue"), buttonStyle);

        }
        TextButton levelsButton = createButton(getButtonText("levels"), buttonStyle);
        TextButton optionsButton = createButton(getButtonText("options"), buttonStyle);
        TextButton creditsButton = createButton(getButtonText("credits"), buttonStyle);

        // Agrega un Image con el título del juego
        titleTexture = new Texture("loadingScreenImage.png"); // Asegúrate de tener esta imagen
        Image titleImage = new Image(new TextureRegionDrawable(new TextureRegion(titleTexture)));

        // Ajusta la escala y posición del título según sea necesario
        titleImage.setScale(0.5f);  // Ajusta según tus necesidades
        titleImage.setPosition((stage.getWidth() - titleImage.getWidth() * titleImage.getScaleX()) / 2,
                stage.getHeight() - titleImage.getHeight() * titleImage.getScaleY() - 20); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(titleImage);

        // Agrega un Image con el título del juego
        mageTexture = new Texture("mageImage.png"); // Asegúrate de tener esta imagen
        Image mageImage = new Image(new TextureRegionDrawable(new TextureRegion(mageTexture)));

        // Ajusta la escala y posición del título según sea necesario
        mageImage.setScale(5f);  // Ajusta según tus necesidades
        mageImage.setPosition((stage.getWidth() - 400 - mageImage.getWidth() * mageImage.getScaleX()) / 2,
                stage.getHeight() - mageImage.getHeight() * mageImage.getScaleY() - 620); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(mageImage);

        // Agrega un Image con el título del juego
        keyTexture = new Texture("keyImage.png"); // Asegúrate de tener esta imagen
        Image keyImage = new Image(new TextureRegionDrawable(new TextureRegion(keyTexture)));

        // Ajusta la escala y posición del título según sea necesario
        keyImage.setScale(4f);  // Ajusta según tus necesidades
        keyImage.setPosition((stage.getWidth() + 440 - keyImage.getWidth() * keyImage.getScaleX()) / 2,
                stage.getHeight() - keyImage.getHeight() * keyImage.getScaleY() - 700); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(keyImage);


        playButton.setUserObject(this);
        playButton.addListener(new ClickListener() {

            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                game.setScreen(new LoadingScreen(game, music, prefs.getInteger("highestLevel", 0) + 1, (MainMenuScreen) playButton.getUserObject()));
                dispose();
            }
        });
        if (levelsScreen == null) {
            levelsScreen = new LevelSelectScreen(game, music, this);
        }
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                game.setScreen(levelsScreen);

            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                game.setScreen(new OptionsScreen(game, music));
                dispose();

            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                game.setScreen(new CredistScreen(game));
                dispose();
            }
        });

        float buttonWidth = Gdx.graphics.getWidth() * 0.2f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;

        playButton.setWidth(buttonWidth);
        playButton.setHeight(buttonHeight);
        levelsButton.setWidth(buttonWidth);
        levelsButton.setHeight(buttonHeight);
        optionsButton.setWidth(buttonWidth);
        optionsButton.setHeight(buttonHeight);
        creditsButton.setWidth(buttonWidth);
        creditsButton.setHeight(buttonHeight);

        table.add(playButton).padRight((stage.getWidth() / 2) - 350);
        table.add(optionsButton);
        table.row();
        table.add(levelsButton).padBottom(100).padRight((stage.getWidth() / 2) - 350);
        table.add(creditsButton).padBottom(100);

    }

    /**
     * Carga los textos de los botones desde un archivo JSON.
     */
    private JsonValue buttonsObject;

    public void loadButtonTitles() {
        FileHandle fileHandle = Gdx.files.internal("data/level_titles.json");
        String jsonData = fileHandle.readString();
        JsonValue root = new JsonReader().parse(jsonData);
        switch (language) {
            case 0:
                buttonsObject = root.get("buttons");
                break;
            case 1:
                buttonsObject = root.get("botones");
                break;
        }
    }

    /**
     * Obtiene el texto del botón especificado.
     *
     * @param button Nombre del botón.
     * @return Texto del botón.
     */
    public String getButtonText(String button) {
        return buttonsObject.getString(button, "default");
    }


    /**
     * Genera una fuente de texto personalizada.
     *
     * @return Fuente de texto generada.
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
     * Crea un botón con el texto y estilo especificados.
     *
     * @param text        Texto del botón.
     * @param buttonStyle Estilo del botón.
     * @return Botón creado.
     */
    private TextButton createButton(String text, TextButton.TextButtonStyle buttonStyle) {
        TextButton button = new TextButton(text, buttonStyle);
        return button;
    }

    /**
     * Método llamado cuando la pantalla se muestra.
     */
    @Override
    public void show() {
        // Puedes realizar alguna inicialización adicional si es necesario
        Gdx.input.setInputProcessor(stage);

    }

    /**
     * Método llamado en cada frame para renderizar la pantalla.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        if (!game.sound) {
            music.setVolume(0);
        } else {
            music.setVolume(1f);
        }

        stage.draw();
    }

    /**
     * Método llamado cuando se cambia el tamaño de la ventana.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Libera los recursos de la pantalla cuando se destruye.
     */
    @Override
    public void dispose() {
        stage.dispose();
        buttonTexture.dispose(); // Dispose de la textura del botón
        buttonTexturedown.dispose(); // Dispose de la textura del botón
        titleTexture.dispose(); // Dispose de la textura del título
        mageTexture.dispose(); // Dispose de la textura del mago
        keyTexture.dispose(); // Dispose de la textura de la llave
    }

    // Métodos restantes de la interfaz Screen, pero no se utilizan en este contexto

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }
}
