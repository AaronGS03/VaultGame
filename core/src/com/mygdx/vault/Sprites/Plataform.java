package com.mygdx.vault.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Plataform  extends InteractiveTileObject{
    private Mage player;

    public Plataform(PlayScreen screen, Rectangle bounds, Mage player){
        super(screen, bounds);
        this.player= player;
        fixture.setUserData(this);
        setCategoryFilter(Vault.PLATAFORM_BIT);
    }
    @Override
    public void onHeadHit() {
    }

    @Override
    public void onFeetHit() {
    }

    @Override
    public void onFeetNotHit() {
    }

    @Override
    public void onSideLHit() {

    }
    @Override
    public void onSideLNotHit() {

    }

}
