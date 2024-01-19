package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Wall extends InteractiveTileObject{
    private Mage player;

    public Wall(World world, TiledMap map, Rectangle bounds, Mage player){
        super(world, map, bounds);
        this.player= player;
        fixture.setUserData(this);
        setCategoryFilter(Vault.WALL_BIT);
    }
    @Override
    public void onHeadHit() {

    }

    @Override
    public void onFeetHit() {
        player.setTouchingGrass(true);
    }

    @Override
    public void onFeetNotHit() {
        player.setTouchingGrass(false);

    }

    @Override
    public void onSideLHit() {
        player.setTouchingWall(true);
    }
    @Override
    public void onSideLNotHit() {
        player.setTouchingWall(false);
    }

}
