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
    private Stage stage;
    Texture buttonTexture;
    Texture buttonTexturedown;
    Image volumeImage;
    Image effectsSoundImage;
    TextButton dropdownButton;
    TextButton buttonEffectsSounds;
    TextButton buttonMusic;
    TextButton resetGameSave;
    Vault game;

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    private int language;
    private Music music;


    public OptionsScreen(Vault game, Music music) {
        this.stage = new Stage(new FitViewport(1920, 1080));
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        this.language = game.language;
        this.music=music;
        this.game=game;
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font = generateFont();

        loadButtonTitles();

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

        // Botón 1 sin texto

        if (prefs.getBoolean("sound")) {
            volumeImage = new Image(new Texture("volumeImage.png"));

        }else {
            volumeImage = new Image(new Texture("noVolumeImage.png"));

        }
        volumeImage.setSize(200, 200);

        buttonMusic = new TextButton(getButtonText("music"), buttonStyle);
        buttonMusic.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                game.setSound(!game.isSound());

                prefs.putBoolean("sound", !prefs.getBoolean("sound"));
                prefs.flush();
                if (prefs.getBoolean("sound")) {
                    table.reset();
                    volumeImage=new Image(new Texture("volumeImage.png"));
                    volumeImage.setSize(200, 200);

                    posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);

                }else{
                    table.reset();
                    volumeImage=new Image(new Texture("noVolumeImage.png"));
                    volumeImage.setSize(200, 200);

                    posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);
                }
            }
        });

        if (prefs.getBoolean("effects")) {
            effectsSoundImage = new Image(new Texture("effectsSoundsImage.png"));

        }else {
            effectsSoundImage=new Image(new Texture("noEffectsImage.png"));

        }
        effectsSoundImage.setSize(200, 200);

        buttonEffectsSounds = new TextButton(getButtonText("effects"), buttonStyle);
        buttonEffectsSounds.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                game.setEffects(!game.isEffects());
                if (game.isEffects()){
                    game.volume=0.2f;
                }else {
                    game.volume=0;

                }
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                prefs.putBoolean("effects", !prefs.getBoolean("effects"));
                prefs.flush();
                if (prefs.getBoolean("effects")) {
                    table.reset();
                    effectsSoundImage=new Image(new Texture("effectsSoundsImage.png"));
                    effectsSoundImage.setSize(200, 200);

                    posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);

                }else{
                    table.reset();
                    effectsSoundImage=new Image(new Texture("noEffectsImage.png"));
                    effectsSoundImage.setSize(200, 200);

                    posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);
                }
            }
        });

        // Botón desplegable con texto
        dropdownButton = new TextButton(getButtonText("language"), buttonStyle);
        dropdownButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                if (game.language==1){
                    game.language=0;
                }else{
                    game.language++;

                }
                setLanguage(game.language);
                loadButtonTitles();
                table.reset();
                backButton.setText(getButtonText("back"));
                dropdownButton.setText(getButtonText("language"));
                buttonMusic.setText(getButtonText("music"));
                buttonEffectsSounds.setText(getButtonText("effects"));
                resetGameSave.setText(getButtonText("reset"));
                posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);
            }
        });

        resetGameSave = new TextButton(getButtonText("reset"), buttonStyle);
        resetGameSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                prefs.putInteger("highestLevel",0);
                prefs.flush();
            }
        });

        posTable(table, backButton, dropdownButton,volumeImage,buttonMusic,effectsSoundImage,buttonEffectsSounds);
    }

    private void posTable(Table table, TextButton backButton, TextButton dropdownButton,Image volumeImage,TextButton buttonMusic, Image effectsSoundImage, TextButton buttonEffectsSounds) {
        table.add(backButton).left().pad(20).row();
        table.add(buttonMusic);
        table.add(volumeImage).size(volumeImage.getWidth(), volumeImage.getHeight());
        table.row();
        table.add(buttonEffectsSounds);
        table.add(effectsSoundImage).size(effectsSoundImage.getWidth(), effectsSoundImage.getHeight());
        table.row();

        table.add(dropdownButton).center().padBottom(20);
        table.add(resetGameSave).center().padBottom(20).row();
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
    public  String getButtonText(String button) {
        return buttonsObject.getString(button, "default");
    }

    @Override
    public void show() {
        // No se requiere inicialización adicional
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (game.isSound()){
            music.setVolume(1);
        }else{
            music.setVolume(0);
        }

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
