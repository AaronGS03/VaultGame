package com.mygdx.vault.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Vault;

public class Plataform  extends InteractiveTileObject{
    private Mage player;

    public Plataform(World world, TiledMap map, Rectangle bounds, Mage player){
        super(world, map, bounds);
        this.player= player;
        fixture.setUserData(this);
        setCategoryFilter(Vault.PLATAFORM_BIT);
    }
    @Override
    public void onHeadHit() {
        setCategoryFilter(Vault.PLATAFORMB_BIT);
        //getCell().getTile().setOffsetY(getCell().getTile().getOffsetY()-512);
        getCell().setTile(null);
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
