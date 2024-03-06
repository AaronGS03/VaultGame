package com.mygdx.vault.Screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.vault.Vault;

/**
 * Clase que representa una capa de paralaje en un fondo.
 */
public class ParallaxLayer {
    Texture texture;
    float factor;
    boolean wrapHorizontally;
    boolean wrapVertically;
    Camera camera;

    /**
     * Constructor de la capa de paralaje.
     *
     * @param texture          Textura de la capa.
     * @param factor           Factor de paralaje que determina la velocidad de desplazamiento.
     * @param wrapHorizontally Indica si la textura se debe envolver horizontalmente.
     * @param wrapVertically   Indica si la textura se debe envolver verticalmente.
     */
    ParallaxLayer(Texture texture, float factor, boolean wrapHorizontally, boolean wrapVertically) {
        this.texture = texture;
        this.factor = factor;
        this.wrapHorizontally = wrapHorizontally;
        this.wrapVertically = wrapVertically;
        this.texture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    /**
     * Establece la cámara que se utilizará para renderizar la capa de paralaje.
     *
     * @param camera Cámara utilizada para renderizar la capa de paralaje.
     */
    void setCamera(Camera camera) {
        this.camera = camera;
    }

    /**
     * Renderiza la capa de paralaje.
     *
     * @param batch SpriteBatch utilizado para dibujar la capa.
     */
    void render(SpriteBatch batch) {
        int xOffset = (int) (camera.position.x * factor);
        int yOffset = (int) (camera.position.y + camera.viewportHeight / Vault.PPM / 2);
        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset % texture.getHeight());
        region.setRegionWidth((int) texture.getWidth());
        region.setRegionHeight(wrapVertically ? (int) camera.viewportHeight : texture.getHeight());
        batch.draw(region, camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2, camera.viewportWidth, camera.viewportHeight);
    }
}
