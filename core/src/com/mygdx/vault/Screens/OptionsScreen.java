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
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.vault.Vault;

public class OptionsScreen implements Screen {
    private Vault game;
    private Stage stage;
    Texture buttonTexture;
    Texture buttonTexturedown;
    Image volumeImage;
    Image effectsSoundImage;
    int language;


    public OptionsScreen(Vault game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        this.language = game.language;
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font = generateFont();


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
        TextButton backButton = new TextButton("<", buttonStyle);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                game.setScreen(new MainMenuScreen(game));
            }
        });

        // Botón 1 sin texto
        volumeImage = new Image(new Texture("volumeImage.png"));
        volumeImage.setSize(200, 200);
        volumeImage.addListener(new InputListener() {
                                    @Override
                                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                        game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                                        game.setSound(!game.isSound());
                                        prefs.putBoolean("sound", !prefs.getBoolean("sound"));
                                        prefs.flush();
                                        return true;
                                    }

                                    @Override
                                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                    }
                                }

        );

        effectsSoundImage = new Image(new Texture("effectsSoundsImage.png"));
        effectsSoundImage.setSize(200, 200);
        effectsSoundImage.addListener(new InputListener() {
                                          @Override
                                          public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                              game.setEffects(!game.isEffects());
                                              prefs.putBoolean("effects", !prefs.getBoolean("effects"));
                                              game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                                              prefs.flush();
                                              return true;
                                          }

                                          @Override
                                          public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                          }
                                      }

        );
        // Botón desplegable con texto
        TextButton dropdownButton = new TextButton("Idioma", buttonStyle);

        table.add(backButton).top().left().pad(20).row();
        table.add(volumeImage).size(volumeImage.getWidth(), volumeImage.getHeight());
        table.add(effectsSoundImage).size(effectsSoundImage.getWidth(), effectsSoundImage.getHeight());
        table.row();

        table.add(dropdownButton).center().padBottom(20).row();
    }
    private  JsonValue buttonsObject;

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

    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cityburn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float fontSize = stage.getHeight() * 0.06f;
        parameter.size = (int) fontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Importante: liberar recursos del generador
        return font;
    }

}
