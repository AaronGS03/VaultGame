package com.mygdx.vault.Controls;

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
    Stage stage2;
    private float alpha = 1f; // Opacidad inicial
    private float fadeSpeed = 0.2f; // Velocidad de desvanecimiento
    boolean leftPressed, rightPressed, upPressed;
    OrthographicCamera cam;

    public Image leftImage;
    public Image rightImage;
    public Image upImage;
    private Image transitionTexture;



    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        stage = new Stage(new FitViewport(1920,1080));
        stage2 = new Stage(new FitViewport(1920,1080));
        Gdx.input.setInputProcessor(stage);

        //Botones a la izquierda
        Table tableL = new Table();
        tableL.left().bottom();
        //Botones a la derecha
        Table tableR = new Table();
        tableR.right().bottom();

        transitionTexture=new Image(new Texture("loadingScreenImage.png"));



        upImage = new Image(new Texture("up.png"));

        upImage.setSize(200, 200);
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
        leftImage.setSize(200, 200);

        leftImage.addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = true;
                return true;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                leftPressed = false;
            }
        });


        rightImage = new Image(new Texture("right.png"));
        rightImage.setSize(200, 200);
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
        tableL.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight()).padRight(100);
        tableL.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
        tableL.add(upImage).size(upImage.getWidth(), upImage.getHeight()).padLeft(1000);

        stage.addActor(tableL);
        stage.addActor(tableR);
        stage2.addActor(transitionTexture);
    }

    public void draw(float delta) {
        alpha -= fadeSpeed * delta;
        if (alpha < 0) {
            alpha = 0;
        }

        transitionTexture.setColor(1,1,1,alpha);

        stage.draw();
        if (stage2!=null){
            stage2.draw();

        }
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
        stage2.dispose();
    }
}
