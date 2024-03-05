package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Spike extends Sprite {
    protected World world;
    protected PlayScreen screen;
    public Body b2body;
    public float x;
    public float y;
    public int position;
    public Mage player;
private boolean fake=false;
    Fixture fixture;

    private TextureRegion texture;
    public Spike(PlayScreen screen, float x, float y, int position, Mage player){
        this.world = screen.getWorld();
        this.screen= screen;
        this.position=position;
        this.player=player;
        this.x= x;
        this.y= y;
        if (position==1){
            texture = new TextureRegion(new Texture(("SpikeUP.png")));
        }else if (position==2) {
            texture = new TextureRegion(new Texture(("SpikeLEFT.png")));

        }else if (position==3) {
            texture = new TextureRegion(new Texture(("SpikeRIGHT.png")));

        }else {
                texture = new TextureRegion(new Texture(("Spike.png")));

        }


        setRegion(texture);
        defineSpike();
        setBounds(x-0.4f,y-0.5f, texture.getRegionWidth()/1.2f / Vault.PPM, texture.getRegionHeight()/1.2f / Vault.PPM);

    }

     protected void defineSpike(){
         BodyDef bdef = new BodyDef();
         bdef.position.set(x-0.6f+texture.getRegionWidth()/2/Vault.PPM,y-0.6f+texture.getRegionHeight()/2/Vault.PPM);
         bdef.type = BodyDef.BodyType.StaticBody;
         b2body = world.createBody(bdef);

         FixtureDef fdef = new FixtureDef();
         PolygonShape shape = new PolygonShape();
         Vector2[] vertices=new Vector2[3];

         if (position==1){
             vertices[0]= new Vector2(-1.1f, 0.9f);
             vertices[1]= new Vector2(1f, 0.9f);
             vertices[2]= new Vector2(0.0f, -1f);

         }else if (position==2){
             vertices[0]= new Vector2(-1.1f, -1.2f);
             vertices[1]= new Vector2(1.1f, -0.1f);
             vertices[2]= new Vector2(-1.1f, 0.9f);

         }else if (position==3){
             vertices[0]= new Vector2(1.1f, -1.2f);
             vertices[1]= new Vector2(-1.1f, -0.1f);
             vertices[2]= new Vector2(1.1f, 0.9f);

         }else{
             vertices[0]= new Vector2(-1.1f, -1.2f);
             vertices[1]= new Vector2(1.1f, -1.2f);
             vertices[2]= new Vector2(0.0f, 0.9f);

         }
         shape.set(vertices);

         fdef.filter.categoryBits = Vault.SPIKE_BIT;
         fdef.filter.maskBits =  Vault.MAGE_BIT | Vault.PLATAFORM_BIT | Vault.DOOR_BIT | Vault.WALL_BIT;


         fdef.shape = shape;
         fdef.friction = 0.3f;
         fixture= b2body.createFixture(fdef);
         fixture.setUserData(this);

         shape.dispose();

     }

     public void update(float dt){

     }

     public void fake(){
        if (b2body!=null){

            // Desactivar la colisión de la fixture del pincho
            Filter filter = new Filter();
            filter.categoryBits = Vault.NOTHING_BIT; // Categoría de bits que no colisiona con nada
            fixture.setFilterData(filter);
            fake=true;
        }

     }

     public void draw(Batch batch){
        if (!fake){
            super.draw(batch);
        }
     }

     public void hit(){
        player.setDead(true);
        screen.reset=true;
         Gdx.app.log("V","toque");
     }

     public void dispose(){

     }
}