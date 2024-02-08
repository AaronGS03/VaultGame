package com.mygdx.vault.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
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
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Vault;

public class SubMenu implements Disposable {
    private Viewport viewport;
    private Stage stage;
    public Image continueImage;
    public Image menuScreenImage;
    public Image respawnImage;
    public Image volumeImage;
    public Image effectsSoundImage;
    public Image clueImage;
    private OrthographicCamera cam;
    public boolean touch=false;
    private Mage player;
    Screen screen;
    public Table table;

    public SubMenu(Stage stageC, Hud hud, Vault game, Mage player, PlayScreen screen) {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        this.stage=stageC;
        Gdx.input.setInputProcessor(stage);
        this.screen= screen;

        table = new Table();
        table.top().padTop(60).setFillParent(true);
        this.player=player;
        continueImage = new Image(new Texture("continueImage.png"));
        continueImage.setSize(150, 150);
        continueImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       hud.setPause(false);
                                       touchButton();
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                   }
                               }

        );
        menuScreenImage = new Image(new Texture("menuImage.png"));
        menuScreenImage.setSize(150, 150);
        menuScreenImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       touchButton();
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
        respawnImage = new Image(new Texture("respawnImage.png"));
        respawnImage.setSize(150, 150);
        respawnImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       touchButton();
                                       player.setDead(true);
                                       hud.setPause(false);
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {


                                   }
                               }

        );

        volumeImage = new Image(new Texture("volumeImage.png"));
        volumeImage.setSize(150, 150);
        volumeImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       touchButton();
                                       screen.setSound(!screen.isSound());
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {



                                   }
                               }

        );

        effectsSoundImage = new Image(new Texture("effectsSoundsImage.png"));
        effectsSoundImage.setSize(150, 150);
        effectsSoundImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       touchButton();
                                       screen.setEffects(!screen.isEffects());
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {



                                   }
                               }

        );

        clueImage = new Image(new Texture("clueImage.png"));
        clueImage.setSize(150, 150);
        clueImage.addListener(new InputListener() {
                                   @Override
                                   public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                       touchButton();
                                       return true;
                                   }

                                   @Override
                                   public void touchUp(InputEvent event, float x, float y, int pointer, int button) {



                                   }
                               }

        );

        table.add(continueImage).size(continueImage.getWidth(), continueImage.getHeight()).pad(10);
        table.add(respawnImage).size(respawnImage.getWidth(), respawnImage.getHeight()).pad(10);
        table.add(menuScreenImage).size(menuScreenImage.getWidth(), menuScreenImage.getHeight()).pad(10);
        table.add(volumeImage).size(volumeImage.getWidth(), volumeImage.getHeight()).pad(10);
        table.add(effectsSoundImage).size(effectsSoundImage.getWidth(), effectsSoundImage.getHeight()).pad(10);
        table.row();
        table.add(clueImage).colspan(5).size(clueImage.getWidth(), clueImage.getHeight()).pad(1).padTop(90);

        stage.addActor(table);
    }

    public void draw() {
            stage.draw();
    }
    public void touchButton() {
        touch=true;

    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
    }
}
