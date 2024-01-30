package com.mygdx.vault.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
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


public abstract class   InteractiveTileObject {
protected World world;
    protected TiledMap map;
    protected TiledMapTile tile;
    protected Rectangle bounds;
    protected Body body;
    protected Fixture fixture;

    protected AssetManager manager;


    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

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
        shape.dispose();
    }

    public abstract void onHeadHit();
    public abstract void onFeetHit();
    public abstract void onFeetNotHit();
    public abstract void onSideLHit();

    public abstract void onSideLNotHit();
    public void setCategoryFilter(short filterBit){
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    public TiledMapTileLayer.Cell getCell(){
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Vault.PPM/512), (int) (body.getPosition().y * Vault.PPM /512));
    }
}
