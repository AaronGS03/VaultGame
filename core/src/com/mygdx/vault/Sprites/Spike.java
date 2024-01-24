package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Spike extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    float x;
    float y;

    private TextureRegion texture;
    public Spike(PlayScreen screen, float x, float y){
        this.world = screen.getWorld();
        this.screen= screen;
        this.x= x;
        this.y= y;
        texture = new TextureRegion(new Texture(("Spike.png")));
        defineSpike();
        setRegion(texture);
        setBounds(x,y, texture.getRegionWidth() / Vault.PPM, texture.getRegionHeight() / Vault.PPM);

    }

     protected void defineSpike(){
         BodyDef bdef = new BodyDef();
         bdef.position.set(x+texture.getRegionWidth()/2/Vault.PPM,y+texture.getRegionHeight()/2/Vault.PPM);
         bdef.type = BodyDef.BodyType.StaticBody;
         b2body = world.createBody(bdef);

         FixtureDef fdef = new FixtureDef();
         PolygonShape shape = new PolygonShape();
         Vector2[] vertices=new Vector2[3];
         vertices[0]= new Vector2(-1.2f, -1.3f);
         vertices[1]= new Vector2(1.2f, -1.3f);
         vertices[2]= new Vector2(0.0f, 0.8f);
         shape.set(vertices);

         fdef.filter.categoryBits = Vault.SPIKE_BIT;
         fdef.filter.maskBits = Vault.DEFAULT_BIT | Vault.MAGE_BIT | Vault.PLATAFORM_BIT | Vault.DOOR_BIT | Vault.WALL_BIT;

         fdef.shape = shape;
         fdef.friction = 0.3f;
         b2body.createFixture(fdef);

         shape.dispose();

     }

     public void update(float dt){
        //setPosition(b2body.getPosition().x  - getWidth() / 2, b2body.getPosition().y  - getHeight() / 2);
     }

     public void hit(){

         Gdx.app.log("V","toque");
     }
}