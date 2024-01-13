package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Mage extends Sprite {
    public World world;
    public Body b2body;
    private TextureRegion mageIdle;

    public Mage(World world, PlayScreen screen){
        super(screen.getAtlas().findRegion(""));//TODO

        this.world= world;
        defineMage();
     //   mageIdle = new TextureRegion(getTexture(),);
        setBounds(0,0,16/Vault.PPM,16/Vault.PPM);
        setRegion(mageIdle);

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
