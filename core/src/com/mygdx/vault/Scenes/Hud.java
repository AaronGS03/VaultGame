package com.mygdx.vault.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
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

    public Hud(Stage stageC) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage=stageC;
        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.top().right().padTop(6).padRight(6).setFillParent(true);

        pauseImage = new Image(new Texture("pauseButton.png"));
        pauseImage.setSize(5, 5);
        pauseImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       pause = true;
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                                       pause=false;

                                   }
                               }

        );

        table.add(pauseImage).size(pauseImage.getWidth(), pauseImage.getHeight());
        stage.addActor(table);
    }

    public void draw() {
        stage.draw();
    }


    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
