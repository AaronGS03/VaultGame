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

public class Hud implements Disposable {
    private Viewport viewport;
    private Stage stage;

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    private boolean pause;
    public Image pauseImage;
    private OrthographicCamera cam;
    private Label titleLabel;
    private Vault game;

    public Hud(Stage stageC, Vault game) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage=stageC;
        this.game=game;
        Gdx.input.setInputProcessor(stage);
        BitmapFont font= generateFont();

        Table table = new Table();
        table.top().right().padTop(6).padRight(6).setFillParent(true);
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;

        titleLabel = new Label("1. Plain and Simple", labelStyle);
        titleLabel.setSize(5,5);
        titleLabel.setColor(Color.WHITE);



        pauseImage = new Image(new Texture("pauseButton.png"));
        pauseImage.setSize(5, 5);
        pauseImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       pause = true;
                                       game.manager.get("audio/sounds/clickbutton.mp3", Sound.class).play(game.volume);

                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                   }
                               }

        );

        table.add(pauseImage).right().size(pauseImage.getWidth(), pauseImage.getHeight());
        table.row();
        table.add(titleLabel).expandX().top().right().padRight(15);

        stage.addActor(table);
    }

    public void draw() {
        stage.draw();
    }


    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    private BitmapFont generateFont() {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("cityburn.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        // Establecer el tamaño de la fuente
        float fontSize = stage.getHeight()*0.05f;
        parameter.size = (int) fontSize;

        // Generar la fuente con los parámetros
        BitmapFont font = generator.generateFont(parameter);

        // Liberar los recursos del generador
        generator.dispose();

        return font;
    }
    @Override
    public void dispose() {
        stage.dispose();
    }
}
