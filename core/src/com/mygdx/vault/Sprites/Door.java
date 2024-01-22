package com.mygdx.vault.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Vault;

public class Door extends InteractiveTileObject{


    private Mage player;

    public Door(World world, TiledMap map, Rectangle bounds, Mage player){
        super(world, map, bounds);
        this.player= player;
        fixture.setSensor(true);
        fixture.setUserData(this);
        setCategoryFilter(Vault.DOOR_BIT);
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
