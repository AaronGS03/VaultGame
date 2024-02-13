package com.mygdx.vault.tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Sensor extends Sprite {

    public int level;

    protected World world;
    protected PlayScreen screen;
    public Body body;

    public Fixture getFixture() {
        return fixture;
    }

    protected Fixture fixture;
    protected Rectangle bounds;

    public Sensor(PlayScreen screen, Rectangle bounds, int level) {
        this.world = screen.getWorld();
        this.bounds = bounds;
        this.level = level;
        this.screen=screen;

        defineSensor();
        setBounds(bounds.getX()/Vault.PPM,bounds.getY()/Vault.PPM,bounds.getWidth() / Vault.PPM, bounds.getHeight() / Vault.PPM);
        Filter filter = new Filter();
        filter.categoryBits = Vault.SENSOR_BIT;
        fixture.setFilterData(filter);

    }

    public void defineSensor(){
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
        if (level==-1){
            fixture.setSensor(false);

        }else {
            fixture.setSensor(true);

        }
    }

    public void active() {
        if (level==3){
            screen.setLevel3gimmick(true);

        }
        if (level==7){
            screen.setLevel7gimmick(true);

        }
        if (level==8){
                screen.setLevel8gimmick(true);

        }
        if (level==10){
            screen.setLevel10gimmick(true);
        }

        if (level==13){
            screen.setLevel13gimmick(true);
        }
        if (level==14){
            screen.setLevel14gimmick(true);
        }

        if (level==69){
            screen.setLevel69gimmick(true);
        }
    }


}
