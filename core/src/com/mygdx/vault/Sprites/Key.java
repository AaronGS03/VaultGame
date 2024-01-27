package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Key extends Item{
    private Animation idle;
    private float statetime;
    public float x;
    public float y;
    public float width;
    public float height;
    public boolean collected=false;
    public BodyDef bdef;
    Mage player;
    public Key(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        this.x= x;
        this.y= y;
        TextureAtlas.AtlasRegion atlasRegion= screen.getAtlas().findRegion("key-white");
        TextureRegion[][] temp= atlasRegion.split(atlasRegion.getRegionWidth()/12,atlasRegion.getRegionHeight());
        idle= new Animation(0.2f, temp[0]);
        statetime=0;
        this.width=atlasRegion.getRegionWidth();
        this.height=atlasRegion.getRegionHeight();
        defineItem();
    }

    @Override
    public void defineItem() {
        bdef = new BodyDef();
        bdef.position.set(x+width/2/Vault.PPM,y+height/2/Vault.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        body = world.createBody(bdef);


        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1f);
        fdef.filter.categoryBits = Vault.ITEM;
        fdef.filter.maskBits= Vault.MAGE_BIT | Vault.DOOR_BIT;

        fdef.shape = shape;

        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        body.setGravityScale(0);
        shape.dispose();
    }

    @Override
    public void take(Mage players) {
        collected=true;
        player=players;
    }

    @Override
    public void use(Door door) {

    }

    @Override
    public void update(float dt) {
        super.update(dt);
        statetime+=dt;
        if (collected){
            float newX = player.getX() + getWidth() / 2 - 1;
            float newY = player.getY() + 0.2f + getHeight() / 2;

            setPosition(newX, newY);

            // Actualiza la posición del cuerpo de la llave
            body.setTransform(newX, newY, body.getAngle());
        }else{
            setPosition(body.getPosition().x-getWidth()/2, body.getPosition().y-getHeight()/2);
        }
        setRegion(((TextureRegion)idle.getKeyFrame(statetime,true)));
    }
}
