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
import com.mygdx.vault.Screens.MainMenuScreen;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class SubMenu implements Disposable {
    private Viewport viewport;
    private Stage stage;
    public Image continueImage;
    public Image menuScreenImage;
    private OrthographicCamera cam;
    private boolean pause;

    public SubMenu(Stage stageC, Hud hud, Vault game) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage=stageC;
        Gdx.input.setInputProcessor(stage);
        pause=hud.isPause();

        Table table = new Table();
        table.top().center().padTop(6).setFillParent(true);

        continueImage = new Image(new Texture("right.png"));
        continueImage.setSize(5, 5);
        continueImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       hud.setPause(false);
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                   }
                               }

        );
        menuScreenImage = new Image(new Texture("up.png"));
        menuScreenImage.setSize(5, 5);
        menuScreenImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       game.getScreen().dispose();
                                       game.setScreen(new MainMenuScreen(game));
                                       dispose();
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                   }
                               }

        );

        table.add(continueImage).size(continueImage.getWidth(), continueImage.getHeight());
        table.add(menuScreenImage).size(menuScreenImage.getWidth(), menuScreenImage.getHeight());
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
