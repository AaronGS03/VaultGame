package com.mygdx.vault.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
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
import com.mygdx.vault.Vault;

public class LevelSelectScreen implements Screen {
    private Vault game;
    private Stage stage;
    Texture buttonTexture;
    Texture buttonTexturedown;
    BitmapFont font;


    public LevelSelectScreen(Vault game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(1920, 1080));
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        Table table2 = new Table();
        Table table3 = new Table();
        Table table4 = new Table();
        table.setFillParent(true);
        table2.left().top().setFillParent(true);
        table3.right().setFillParent(true);
        table4.left().setFillParent(true);
        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);
        stage.addActor(table4);

        font = generateFont();

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = font;
        buttonStyle.fontColor = Color.BLACK;

        // Configura el fondo con la textura
        buttonTexture = new Texture("button_background.png"); // Asegúrate de tener esta textura
        buttonTexturedown = new Texture("button_backgroundDown.png"); // Asegúrate de tener esta textura
        TextureRegionDrawable buttonBackground = new TextureRegionDrawable(new TextureRegion(buttonTexture));
        buttonStyle.downFontColor = Color.WHITE;
        TextureRegionDrawable buttonBackgrounddown = new TextureRegionDrawable(new TextureRegion(buttonTexturedown));
        buttonStyle.down = buttonBackgrounddown;
        buttonStyle.up = buttonBackground;

        float paddingX = 40f; // Ajusta según sea necesario
        float paddingY = 10f; // Ajusta según sea necesario
        buttonStyle.up.setMinWidth(buttonStyle.up.getMinWidth() + paddingX * 2);
        buttonStyle.up.setMinHeight(buttonStyle.up.getMinHeight() + paddingY * 2);

        // Crea los botones con el estilo personalizado
        TextButton backButton = createButton("<", buttonStyle);
        TextButton nextButton = createButton(">", buttonStyle);

        TextButton backButtonToMenu = createButton("Volver", buttonStyle);
        float menuButtonWidth = Gdx.graphics.getWidth() * 2f;

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                // Implementa la lógica para retroceder entre niveles
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                // Implementa la lógica para avanzar entre niveles
            }
        });
        backButtonToMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                game.setScreen(new MainMenuScreen(game));
            }
        });

        float buttonWidth = Gdx.graphics.getWidth() * 0.1f;
        float buttonHeight = Gdx.graphics.getHeight() * 0.1f;


        table2.add(backButtonToMenu).padTop(20).row();
        table4.add(backButton);

        int buttonsPerRow = 3;
        int level = 0;

        for (int i = 1; i <= 12; i++) {
            TextButton levelButton = createButton(Integer.toString(i), buttonStyle);
            level = i;
            int finalLevel = level;
            levelButton.addListener(new ClickListener() {

                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Implementa la lógica para avanzar entre niveles
                    //game.setScreen(new PlayScreen(game,game.manager, finalLevel));
                    game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);
                    game.setScreen(new LoadingScreen(game, finalLevel));
                    dispose();
                }
            });
            levelButton.setWidth(buttonWidth);
            levelButton.setHeight(buttonHeight);

            table.add(levelButton).padRight(10);

            // Agregar una nueva fila después de cada grupo de 4 botones
            if (i % buttonsPerRow == 0 && i < 16) {
                table.row().padTop(10);
            }
        }

        table3.add(nextButton).row();

        backButtonToMenu.setWidth(menuButtonWidth);  // Ajusta el ancho del botón de menú
        backButton.setWidth(buttonWidth);
        backButton.setHeight(buttonHeight);
        nextButton.setWidth(buttonWidth);
        nextButton.setHeight(buttonHeight);

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
        buttonTexture.dispose();
        buttonTexturedown.dispose();
        font.dispose();
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
