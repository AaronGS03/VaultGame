package com.mygdx.vault.Screens;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.mygdx.vault.Vault;

public class ParallaxLayer {
    Texture texture;
    float factor;
    boolean wrapHorizontally;
    boolean wrapVertically;
    Camera camera;

    ParallaxLayer(Texture texture, float factor, boolean wrapHorizontally, boolean wrapVertically) {
        this.texture = texture;
        this.factor = factor;
        this.wrapHorizontally = wrapHorizontally;
        this.wrapVertically = wrapVertically;
        this.texture.setWrap(
                Texture.TextureWrap.ClampToEdge,
                Texture.TextureWrap.ClampToEdge
        );
    }

    void setCamera(Camera camera) {
        this.camera = camera;
    }


    void render(SpriteBatch batch) {
        int xOffset = (int) (camera.position.x * factor);
        int yOffset = (int) (camera.position.y * factor);
        TextureRegion region = new TextureRegion(texture);
        region.setRegionX(xOffset % texture.getWidth());
        region.setRegionY(yOffset % texture.getHeight());
        region.setRegionWidth((int)texture.getWidth());
        region.setRegionHeight(wrapVertically ? (int) camera.viewportHeight : texture.getHeight());
        batch.draw(region, camera.position.x - camera.viewportWidth / 2, camera.position.y - camera.viewportHeight / 2,camera.viewportWidth, camera.viewportHeight);

    }
}