package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Vault;

public class Mage extends Sprite {
    public World world;
    public Body b2body;

    public Mage(World world){
        this.world= world;
        defineMage();
    }

    public void defineMage(){
        BodyDef bdef= new BodyDef();
        bdef.position.set(1200/Vault.PPM,1400/Vault.PPM);
        bdef.type= BodyDef.BodyType.DynamicBody;
        b2body= world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape= new PolygonShape();
        shape.setAsBox(1,2);

        fdef.shape=shape;
        fdef.friction=0.3f;
        b2body.createFixture(fdef);
    }
}
