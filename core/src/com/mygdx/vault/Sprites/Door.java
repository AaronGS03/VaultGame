package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Door extends Sprite {

    public int level;
    private Mage player;

    public void setTexture(TextureRegion texture) {
        this.texture = texture;
    }

    private TextureRegion texture;
    protected World world;
    protected PlayScreen screen;
    public Body body;

    public Fixture getFixture() {
        return fixture;
    }

    protected Fixture fixture;
    protected Rectangle bounds;

    public Door(PlayScreen screen, Rectangle bounds, Mage player, int level) {
        this.world = screen.getWorld();
        this.bounds = bounds;
        this.player = player;
        this.level = level;
        this.screen=screen;

        texture = new TextureRegion(new Texture(("right.png")));
        setRegion(texture.getTexture());
        defineDoor();
        setBounds(bounds.getX()/Vault.PPM,bounds.getY()/Vault.PPM,texture.getRegionWidth() / Vault.PPM, texture.getRegionHeight() / Vault.PPM);
        Filter filter = new Filter();
        filter.categoryBits = Vault.DOOR_BIT;
        fixture.setFilterData(filter);

    }

    public void defineDoor(){
        BodyDef bdef= new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape= new PolygonShape();

        bdef.type =BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX()+bounds.getWidth()/2)/ Vault.PPM, (bounds.getY()+bounds.getHeight()/2)/Vault.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2/ Vault.PPM, bounds.getHeight()/2/Vault.PPM);
        fdef.shape = shape;
        fdef.friction=1;
        fixture= body.createFixture(fdef);
        fixture.setUserData(this);
    }

    public void open() {
        screen.setOpenDoor(true);
        screen.hitdoorlevel=level;
    }


}
