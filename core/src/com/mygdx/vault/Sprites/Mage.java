package com.mygdx.vault.Sprites;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.vault.Controls.Controller;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Mage extends Sprite {

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public enum State {FALLING, AIRTRANSITION, LANDING, JUMPING, STANDING, RUNNING, WALLSLIDEL, WALLSLIDER, DYING, DEAD}

    ;
    public State currentState;
    public State previousState;


    public World world;
    public Body b2body;
    public Fixture fixture;
    private TextureRegion mageStand;
    private Animation<TextureRegion> mageIdle;
    private Animation<TextureRegion> mageRun;
    private Animation<TextureRegion> mageJump;
    private Animation<TextureRegion> mageJumpTF;
    private Animation<TextureRegion> mageFalling;
    private Animation<TextureRegion> mageLanding;
    private TextureRegion mageDead;
    private Animation<TextureRegion> mageDying;
    private Animation<TextureRegion> mageWallSlideL;
    private float stateTimer;
    private TextureAtlas atlas;
    public TextureRegion region;
    private boolean runningRight;


    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
    private int currentLevel;

    private boolean dead = false;
    private Array<TextureRegion> frames;


    public boolean isTouchingWall() {
        return isTouchingWall;
    }

    public void setTouchingWall(boolean touchingWall) {
        isTouchingWall = touchingWall;
    }
    private boolean isTouchingWall = false;


    public boolean isTouchingGrass() {
        return isTouchingGrass;
    }

    public void setTouchingGrass(boolean touchingGrass) {
        isTouchingGrass = touchingGrass;
    }
    private boolean isTouchingGrass = false;

    private float runframeduration = 0.05f;


    public State getCurrentState() {
        return currentState;
    }

    public State getPreviousState() {
        return previousState;
    }
    private Controller controller;

    private AssetManager manager;
    private Array<Room> habitaciones;
    public Key keys;
    private PlayScreen screen;


    public Mage(PlayScreen screen, Controller controller, TextureAtlas atlas, AssetManager manager, Array<Room> habitaciones) {
        super(screen.getAtlas().findRegion("idle sheet-Sheet"));
        this.world = screen.getWorld();
        this.screen=screen;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;
        this.manager = manager;
        this.habitaciones = habitaciones;
        this.controller = controller;
        this.atlas = atlas;

        frames = new Array<>();

        //run
        for (int i = 0; i < 24; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 314, 64, 64));
        }
        mageRun = new Animation(runframeduration, frames);
        frames.clear();

        //idle
        for (int i = 0; i < 18; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80 - 2, 152, 64, 64));
        }
        mageIdle = new Animation(0.1f, frames);
        frames.clear();

        //jump
        for (int i = 0; i < 3; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 232, 64, 64));
        }
        mageJump = new Animation(0.1f, frames);
        frames.clear();

        //jumptrans
        for (int i = 3; i < 10; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 232, 64, 64));
        }
        mageJumpTF = new Animation(0.1f, frames);
        frames.clear();

        //falling
        for (int i = 10; i < 13; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 232, 64, 64));
        }
        mageFalling = new Animation(0.1f, frames);
        frames.clear();

        //landing
        for (int i = 15; i < 19; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 232, 64, 64));
        }
        mageLanding = new Animation(0.06f, frames);
        frames.clear();

        //wallslideL


        TextureAtlas.AtlasRegion atlasRegion = atlas.findRegion("wall slide-Sheet");
        TextureRegion[][] temp = atlasRegion.split(atlasRegion.getRegionWidth() / 4, atlasRegion.getRegionHeight());
        mageWallSlideL = new Animation(0.1f, temp[0]);

        //death
        atlasRegion = atlas.findRegion("itch hurt 2 sheet-Sheet");
        temp = atlasRegion.split(atlasRegion.getRegionWidth() / 7, atlasRegion.getRegionHeight());
        for (int i = 0; i < 5; i++) {
            frames.add(temp[0][i]);
        }
        mageDead = new TextureRegion(frames.get(4));
        mageDying = new Animation(0.1f, frames);
        frames.clear();


        //stand
        mageStand = new TextureRegion(getTexture(), 0, 152, 64, 64);

        defineMage();
        setBounds(0, 0, 1500 / Vault.PPM, 1500 / Vault.PPM);
        setRegion(mageStand);

    }
    public void update(float dt) {
        if (currentState == State.WALLSLIDEL || currentState == State.WALLSLIDER) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
        } else {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 1f - getHeight() / 2);

        }


        if (dead) {
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y + 0.6f - getHeight() / 2);

        }
        if (screen.levelSpawn!=-1){
            setPosition(screen.habitaciones.get(screen.levelSpawn-1).getSpawnPoint().x / Vault.PPM, screen.habitaciones.get(screen.levelSpawn-1).getSpawnPoint().y / Vault.PPM);
            b2body.setTransform(screen.habitaciones.get(screen.levelSpawn-1).getSpawnPoint().x / Vault.PPM, screen.habitaciones.get(screen.levelSpawn-1).getSpawnPoint().y / Vault.PPM,b2body.getAngle());
            screen.levelSpawn=-1;
        }

        setRegion(getFrame(dt));
    }
    public TextureRegion getFrame(float dt) {

        currentState = getState();
        switch (currentState) {
            case JUMPING:
                region = mageJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = mageRun.getKeyFrame(stateTimer, true);
                break;
            case WALLSLIDEL:
                region = mageWallSlideL.getKeyFrame(stateTimer);
                break;
            case WALLSLIDER:
                region = mageWallSlideL.getKeyFrame(stateTimer);
                region.flip(true, false);
                break;
            case AIRTRANSITION:
                region = mageJumpTF.getKeyFrame(stateTimer);
                break;
            case FALLING:
                region = mageFalling.getKeyFrame(stateTimer);
                break;
            case LANDING:
                region = mageLanding.getKeyFrame(stateTimer);
                break;
            case DYING:
                region = mageDying.getKeyFrame(stateTimer);
                break;
            case DEAD:
                region = mageDead;

                break;
            case STANDING:
            default:
                region = mageIdle.getKeyFrame(stateTimer, true);
                break;
        }
        if ((b2body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;

        } else if ((b2body.getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }
        stateTimer = currentState == previousState ? stateTimer + dt : 0;
        previousState = currentState;
        return region;
    }

    public State getState() {
        if (dead){
            return State.DYING;
        }else if (previousState == State.DYING && stateTimer < 0.5f) {
            return State.DEAD;
        }else if (currentLevel==3 && screen.isDragging) {
            return State.AIRTRANSITION;
        } else if (b2body.getLinearVelocity().y > 0) {
            return State.JUMPING;
        } else if (b2body.getLinearVelocity().y < 0 && (previousState == State.WALLSLIDER || previousState == State.WALLSLIDEL || previousState == State.JUMPING || previousState == State.AIRTRANSITION || previousState == State.FALLING) && isTouchingWall && controller.isRightPressed()) {
            return State.WALLSLIDER;
        } else if (b2body.getLinearVelocity().y < 0 && (previousState == State.WALLSLIDER || previousState == State.WALLSLIDEL || previousState == State.JUMPING || previousState == State.AIRTRANSITION || previousState == State.FALLING) && isTouchingWall && controller.isLeftPressed()) {
            return State.WALLSLIDEL;
        } else if (b2body.getLinearVelocity().y < 0 && (previousState == State.JUMPING || previousState == State.WALLSLIDEL || previousState == State.WALLSLIDER) && stateTimer < 0.2f) {
            return State.AIRTRANSITION;
        } else if ((b2body.getLinearVelocity().y < 0 && (previousState == State.AIRTRANSITION || previousState == State.WALLSLIDEL || previousState == State.WALLSLIDER)) || b2body.getLinearVelocity().y < -0.1) {
            return State.FALLING;
        } else if (b2body.getLinearVelocity().y == 0 && (previousState == State.FALLING || previousState == State.WALLSLIDEL || previousState == State.WALLSLIDER)) {
            return State.LANDING;
        } else if (b2body.getLinearVelocity().y == 0 && previousState == State.LANDING && stateTimer < 0.2f) {
            return State.LANDING;
        } else if (b2body.getLinearVelocity().x != 0 && b2body.getLinearVelocity().y == 0 && previousState != State.LANDING) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }


    }


    public void defineMage() {
        BodyDef bdef = new BodyDef();

        //y+5120 por cada nivel
            bdef.position.set(20 / Vault.PPM, 29736 / Vault.PPM);

        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1.6f);
        if (!dead){
            fdef.filter.categoryBits = Vault.MAGE_BIT;
            fdef.filter.maskBits = Vault.DEFAULT_BIT | Vault.SPIKE_BIT | Vault.PLATAFORM_BIT | Vault.ITEM | Vault.DOOR_BIT | Vault.WALL_BIT;
        }else{
            fdef.filter.maskBits = Vault.PLATAFORM_BIT | Vault.DOOR_BIT | Vault.WALL_BIT;

        }


        fdef.shape = shape;
        fdef.friction = 0.3f;
        b2body.createFixture(fdef).setUserData(this);


        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-160 / Vault.PPM, 360 / Vault.PPM), new Vector2(160 / Vault.PPM, 360 / Vault.PPM));
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("head");

        EdgeShape feet = new EdgeShape();
        feet.set(new Vector2(-80 / Vault.PPM, -360 / Vault.PPM), new Vector2(80 / Vault.PPM, -360 / Vault.PPM));
        fdef.shape = feet;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("feet");

        EdgeShape sideL = new EdgeShape();
        sideL.set(new Vector2(-220 / Vault.PPM, -280 / Vault.PPM), new Vector2(-220 / Vault.PPM, 280 / Vault.PPM));
        fdef.shape = sideL;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("sideL");
        EdgeShape sideR = new EdgeShape();
        sideR.set(new Vector2(230 / Vault.PPM, -280 / Vault.PPM), new Vector2(230 / Vault.PPM, 280 / Vault.PPM));
        fdef.shape = sideR;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData("sideR");

        shape.dispose();
        head.dispose();
        feet.dispose();
        sideL.dispose();
        sideR.dispose();
    }
}
