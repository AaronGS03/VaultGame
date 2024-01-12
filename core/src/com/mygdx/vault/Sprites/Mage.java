package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
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
        CircleShape shape= new CircleShape();
        shape.setRadius(250/Vault.PPM);

        fdef.shape=shape;
        b2body.createFixture(fdef);
    }
}
