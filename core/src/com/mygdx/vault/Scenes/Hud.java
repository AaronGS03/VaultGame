package com.mygdx.vault.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Clase Hud que representa la interfaz de usuario durante el juego.
 * Proporciona elementos visuales como etiquetas y botones para el HUD del juego.
 */
public class Hud implements Disposable {
    private Viewport viewport; // Viewport para el HUD
    private Stage stage; // Escena para el HUD

    public boolean pause; // Indica si el juego está en pausa
    public Image pauseImage; // Imagen del botón de pausa
    private OrthographicCamera cam; // Cámara ortográfica para el HUD

    public Label.LabelStyle labelStyle; // Estilo de las etiquetas de texto
    public Label titleLabel; // Etiqueta de título del nivel
    private String text = "level1"; // Texto del título del nivel
    public Table table; // Tabla para organizar los elementos del HUD

    /**
     * Constructor de la clase Hud.
     *
     * @param stageC La escena en la que se dibujará el HUD.
     * @param game   La instancia del juego.
     */
    public Hud(Stage stageC, Vault game) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage = stageC;
        Gdx.input.setInputProcessor(stage);
        BitmapFont font = generateFont();

        table = new Table();
        table.top().right().padTop(120).padRight(120).setFillParent(true);

        labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        titleLabel = new Label(text, labelStyle);
        titleLabel.setColor(Color.WHITE);

        pauseImage = new Image(new Texture("pauseButton.png"));
        pauseImage.setSize(150, 150);
        pauseImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                pause = true;
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                // No se ejecuta nada al soltar el botón
            }
        });

        table.add(pauseImage).right().size(pauseImage.getWidth(), pauseImage.getHeight());
        table.row();
        table.add(titleLabel).expandX().top().right().padRight(240).padTop(-150);

        stage.addActor(table);
    }

    /**
     * Dibuja el HUD en la pantalla.
     */
    public void draw() {
        stage.draw();
    }

    /**
     * Redimensiona la vista del HUD.
     *
     * @param width  Ancho de la vista.
     * @param height Alto de la vista.
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Genera una fuente de texto personalizada para el HUD.
     *
     * @return La fuente de texto generada.
     */
    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cityburn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float fontSize = stage.getHeight() / 15;
        parameter.size = (int) fontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Liberar recursos del generador de fuentes
        return font;
    }

    @Override
    public void dispose() {
        // No se requiere disposición de recursos aquí
    }
}
