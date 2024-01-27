package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public abstract class Item extends Sprite {
protected PlayScreen screen;
protected World world;
protected Vector2 velocity;
protected boolean toDestroy;
protected boolean destroyed;
public Body body;
    protected Fixture fixture;


    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world =screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(),512/ Vault.PPM,512/ Vault.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void take(Mage player);
    public abstract void use(Door door);
    public void update(float dt){
        if (toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed=true;
        }
    }
    public  void draw(Batch batch){
        if (!destroyed){
            super.draw(batch);
        }
    }

    public void destroy(){
        toDestroy=true;
    }
}
