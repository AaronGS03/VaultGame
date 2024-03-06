package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.vault.Vault;

/**
 * Pantalla que muestra los créditos del juego.
 */
public class CredistScreen implements Screen {

    private Stage stage;
    private Texture buttonTexture;
    private Texture buttonTexturedown;
    private int language;
    private Label.LabelStyle labelStyle;
    private Label titleLabel;
    private JsonValue buttonsObject;
    private String creditsObject;

    /**
     * Constructor de la pantalla de créditos.
     *
     * @param game Instancia principal del juego.
     */
    public CredistScreen(Vault game) {
        this.stage = new Stage(new FitViewport(1920, 1080));
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        this.language = game.language;
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font = generateFont();

        loadButtonTitles();

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        titleLabel = new Label("\nDeaths: " + prefs.getInteger("deaths") + "\n" + creditsObject, labelStyle);
        titleLabel.setColor(Color.WHITE);

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;
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

        // Botón de volver arriba a la izquierda
        TextButton backButton = new TextButton(getButtonText("back"), buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        posTable(table, backButton);
    }

    /**
     * Posiciona los elementos en la tabla de la interfaz gráfica.
     *
     * @param table      Tabla de la interfaz gráfica.
     * @param backButton Botón de retroceso.
     */
    private void posTable(Table table, TextButton backButton) {
        table.add(backButton).left().pad(20).row();
        ScrollPane scrollPane = new ScrollPane(titleLabel);
        scrollPane.setFadeScrollBars(false); // Opcional: desactivar el desvanecimiento de las barras de desplazamiento
        table.add(scrollPane).expand().fill().colspan(2).pad(20);
    }

    /**
     * Carga los textos de los botones y los créditos desde un archivo JSON.
     */
    public void loadButtonTitles() {
        FileHandle fileHandle = Gdx.files.internal("data/level_titles.json");
        String jsonData = fileHandle.readString();
        JsonValue root = new JsonReader().parse(jsonData);
        switch (language) {
            case 0:
                buttonsObject = root.get("buttons");
                creditsObject = root.getString("credits");
                break;
            case 1:
                buttonsObject = root.get("botones");
                creditsObject = root.getString("creditos");
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

    // Métodos de la interfaz Screen

    @Override
    public void show() {
        // No se requiere inicialización adicional
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
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
        generator.dispose();
        return font;
    }
}
