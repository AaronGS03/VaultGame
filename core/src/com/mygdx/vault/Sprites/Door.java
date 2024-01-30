package com.mygdx.vault.Sprites;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Door extends InteractiveTileObject{

    private int level;
    private Mage player;

    public Door(PlayScreen screen, Rectangle bounds, Mage player, int level){
        super(screen, bounds);
        this.player= player;
        this.level=level;
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

    public void open(){
        if (player.keys!=null){

            if (player.getCurrentLevel()==level&&player.keys.collected){
                this.fixture.setSensor(true);
            }else{
                this.fixture.setSensor(false);
            }
        }
    }

    @Override
    public void onSideLNotHit() {

    }
}
