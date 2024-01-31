package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Vault;

public class MainMenuScreen implements Screen {
    private Vault game;
    private Stage stage;
    public MainMenuScreen(Vault game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920,1080));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        BitmapFont font= generateFont();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        // Configura el fondo con la textura
        Texture buttonTexture = new Texture("button_background.png"); // Asegúrate de tener esta textura
        Texture buttonTexturedown = new Texture("button_backgroundDown.png"); // Asegúrate de tener esta textura
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        buttonStyle.downFontColor = Color.WHITE;
        TextureRegionDrawable buttonBackgrounddown = new TextureRegionDrawable(new TextureRegion(buttonTexturedown));
        buttonStyle.down =buttonBackgrounddown;
        buttonStyle.up = buttonBackground;
        float paddingX = 120f;  // Ajusta según sea necesario
        float paddingY = 10f;  // Ajusta según sea necesario
        buttonStyle.up.setMinWidth(buttonStyle.up.getMinWidth() + paddingX * 2);
        buttonStyle.up.setMinHeight(buttonStyle.up.getMinHeight() + paddingY * 2);


        // Crea los botones con el estilo personalizado
        TextButton playButton = createButton("Jugar", buttonStyle);
        TextButton levelsButton = createButton("Niveles", buttonStyle);
        TextButton optionsButton = createButton("Opciones", buttonStyle);
        TextButton creditsButton = createButton("Créditos", buttonStyle);

        // Agrega un Image con el título del juego
        Texture titleTexture = new Texture("loadingScreenImage.png"); // Asegúrate de tener esta imagen
        Image titleImage = new Image(new TextureRegionDrawable(new TextureRegion(titleTexture)));

        // Ajusta la escala y posición del título según sea necesario
        titleImage.setScale(0.5f);  // Ajusta según tus necesidades
        titleImage.setPosition((stage.getWidth() - titleImage.getWidth() * titleImage.getScaleX()) / 2,
                stage.getHeight() - titleImage.getHeight() * titleImage.getScaleY() - 20); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(titleImage);

        // Agrega un Image con el título del juego
        Texture mageTexture = new Texture("mageImage.png"); // Asegúrate de tener esta imagen
        Image mageImage = new Image(new TextureRegionDrawable(new TextureRegion(mageTexture)));

        // Ajusta la escala y posición del título según sea necesario
        mageImage.setScale(5f);  // Ajusta según tus necesidades
        mageImage.setPosition((stage.getWidth()-600 - mageImage.getWidth() * mageImage.getScaleX()) / 2,
                stage.getHeight() - mageImage.getHeight() * mageImage.getScaleY() - 620); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(mageImage);

        // Agrega un Image con el título del juego
        Texture keyTexture = new Texture("keyImage.png"); // Asegúrate de tener esta imagen
        Image keyImage = new Image(new TextureRegionDrawable(new TextureRegion(keyTexture)));

        // Ajusta la escala y posición del título según sea necesario
        keyImage.setScale(4f);  // Ajusta según tus necesidades
        keyImage.setPosition((stage.getWidth()+640 - keyImage.getWidth() * keyImage.getScaleX()) / 2,
                stage.getHeight() - keyImage.getHeight() * keyImage.getScaleY() - 700); // Ajusta según tus necesidades

        // Añade el título al escenario
        stage.addActor(keyImage);


        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new PlayScreen(game, game.manager));
            }
        });

        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón de niveles
                // Implementa la lógica correspondiente
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón de opciones
                // Implementa la lógica correspondiente
            }
        });

        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Acción al hacer clic en el botón de créditos
                // Implementa la lógica correspondiente
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

        table.add(playButton).padBottom(20).padTop(500).left();
        table.add(optionsButton).padLeft(stage.getWidth()/2).padTop(500).row();
        table.add(levelsButton).padBottom(20).left();
        table.add(creditsButton).padLeft(stage.getWidth()/2).row();
        table.row();
    }
    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cityburn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        float fontSize = stage.getHeight()*0.06f;
        parameter.size = (int) fontSize;
        BitmapFont font = generator.generateFont(parameter);
        generator.dispose(); // Importante: liberar recursos del generador
        return font;
    }

    private TextButton createButton(String text, TextButton.TextButtonStyle buttonStyle) {
        TextButton button = new TextButton(text, buttonStyle);
        return button;
    }

    @Override
    public void show() {
        // Puedes realizar alguna inicialización adicional si es necesario
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
    public void dispose() {
        stage.dispose();
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
}
