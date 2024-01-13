package com.mygdx.vault.Contols;

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

public class Controller implements Disposable {
    Viewport viewport;
    Stage stage;
    boolean leftPressed, rightPressed, upPressed;
    OrthographicCamera cam;

    public Image leftImage;
    public Image rightImage;
    public Image upImage;



    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        stage = new Stage(viewport, Vault.batch);
        Gdx.input.setInputProcessor(stage);

        //Botones a la izquierda
        Table tableL = new Table();
        tableL.left().bottom();
        //Botones a la derecha
        Table tableR = new Table();
        tableR.bottom();



        Image upImage = new Image(new Texture("up.png"));

        upImage.setSize(7, 7);
        upImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upPressed = false;

            }
        });


        leftImage = new Image(new Texture("left.png"));
        leftImage.setSize(7, 7);
        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                leftImage = new Image(new Texture("left2.png"));
                draw();
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
                leftImage = new Image(new Texture("left2.png"));
            }
        });


        Image rightImage = new Image(new Texture("right.png"));
        rightImage.setSize(7, 7);
        rightImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                rightPressed = false;

            }
        });

        //posicion en las tablas de los botones
        tableL.row().pad(2,2,2,2);
        tableL.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight());
        tableL.add();
        tableL.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());

        tableR.row().pad(2, cam.viewportWidth*2-20, 2,2);
        tableR.add(upImage).size(upImage.getWidth(), upImage.getHeight());

        stage.addActor(tableL);
        stage.addActor(tableR);
    }

    public void draw() {
        stage.draw();
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() {

        return upPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
