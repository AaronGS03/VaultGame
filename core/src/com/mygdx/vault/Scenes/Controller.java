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

/**
 * Clase Controller que maneja los controles del juego.
 * Esta clase proporciona botones para controlar el movimiento del personaje en el juego.
 */
public class Controller implements Disposable {
    Viewport viewport; // Viewport para la escena de los controles
    Stage stage; // Escena para los controles
    Stage stage2; // Escena para el efecto de transición
    private float alpha = 1f; // Opacidad inicial del efecto de transición
    private float fadeSpeed = 0.2f; // Velocidad de desvanecimiento del efecto de transición
    boolean leftPressed, rightPressed, upPressed; // Estado de los botones de control
    OrthographicCamera cam; // Cámara ortográfica para la escena de los controles

    // Imágenes de los botones de control
    public Image leftImage;
    public Image rightImage;
    public Image upImage;
    private Image transitionTexture; // Imagen para el efecto de transición

    /**
     * Constructor de la clase Controller.
     * Inicializa los componentes necesarios para los controles del juego.
     */
    public Controller() {
        cam = new OrthographicCamera();
        viewport = new FitViewport(Vault.V_WIDTH / Vault.PPM, Vault.V_HEIGHT / Vault.PPM, cam);
        stage = new Stage(new FitViewport(1920,1080));
        stage2 = new Stage(new FitViewport(1920,1080));
        Gdx.input.setInputProcessor(stage);

        // Botones a la izquierda
        Table tableL = new Table();
        tableL.left().bottom();
        // Botones a la derecha
        Table tableR = new Table();
        tableR.right().bottom();

        transitionTexture=new Image(new Texture("loadingScreenImage.png"));

        // Configuración de los botones de control
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

        // Posicionamiento de los botones en las tablas
        tableL.add(leftImage).size(leftImage.getWidth(), leftImage.getHeight()).padRight(100);
        tableL.add(rightImage).size(rightImage.getWidth(), rightImage.getHeight());
        tableL.add(upImage).size(upImage.getWidth(), upImage.getHeight()).padLeft(1000);

        // Agregar las tablas a la escena
        stage.addActor(tableL);
        stage.addActor(tableR);
        stage2.addActor(transitionTexture);
    }

    /**
     * Dibuja los controles y el efecto de transición en la pantalla.
     * @param delta El tiempo transcurrido desde el último fotograma en segundos.
     */
    public void draw(float delta) {
        // Aplicar efecto de transición
        alpha -= fadeSpeed * delta;
        if (alpha < 0) {
            alpha = 0;
        }
        transitionTexture.setColor(1,1,1,alpha);

        // Dibujar la escena de los controles
        stage.draw();
        if (stage2 != null) {
            stage2.draw();
        }
    }

    // Métodos de acceso para obtener el estado de los botones de control
    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    // Métodos de configuración para establecer el estado de los botones de control
    public void setLeftPressed(boolean leftPressed) {
        this.leftPressed = leftPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        this.rightPressed = rightPressed;
    }

    public void setUpPressed(boolean upPressed) {
        this.upPressed = upPressed;
    }

    /**
     * Redimensiona la vista de los controles.
     * @param width Ancho de la vista.
     * @param height Alto de la vista.
     */
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /**
     * Libera los recursos utilizados por los controles.
     */
    @Override
    public void dispose() {
        stage.dispose();
        stage2.dispose();
    }
}
