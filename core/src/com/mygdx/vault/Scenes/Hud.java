package com.mygdx.vault.Scenes;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.vault.Vault;

public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    public Hud(SpriteBatch sb){
        viewport= new FitViewport(Vault.V_WIDTH,Vault.V_HEIGHT,new OrthographicCamera());
        stage= new Stage(viewport, sb);

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
