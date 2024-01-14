package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Mage extends Sprite {

    public enum State {FALLING, AIRTRANSITION, LANDING, JUMPING, STANDING, RUNNING}

    ;
    public State currentState;
    public State previousState;

    public World world;
    public Body b2body;
    private TextureRegion mageStand;
    private Animation<TextureRegion> mageIdle;
    private Animation<TextureRegion> mageRun;
    private Animation<TextureRegion> mageJump;
    private Animation<TextureRegion> mageJumpTF;
    private Animation<TextureRegion> mageFalling;
    private Animation<TextureRegion> mageLanding;
    private float stateTimer;
    private boolean runningRight;

    public Mage(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("idle sheet-Sheet"));
        this.world = world;
        currentState = State.STANDING;
        previousState = State.STANDING;
        stateTimer = 0;
        runningRight = true;

        Array<TextureRegion> frames = new Array<>();
        //run
        for (int i = 0; i < 24; i++) {
            frames.add(new TextureRegion(getTexture(), i * 80, 314, 64, 64));
        }
        mageRun = new Animation(0.1f, frames);
        frames.clear();
        //idle
        for (int i = 0; i < 18; i++) {
            frames.add(new TextureRegion(getTexture(), i*80, 152, 64, 64));
        }
        mageIdle = new Animation(0.1f, frames);
        frames.clear();

        mageStand = new TextureRegion(getTexture(), 0, 152, 64, 64);

        defineMage();
        setBounds(0, 0, 1500 / Vault.PPM, 1500 / Vault.PPM);
        setRegion(mageStand);

    }

    public void update(float dt) {
        setPosition(b2body.getPosition().x+0.2f - getWidth() / 2, b2body.getPosition().y+1f - getHeight() / 2);
        setRegion(getFrame(dt));
    }

    public TextureRegion getFrame(float dt) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case JUMPING:
                region = mageJump.getKeyFrame(stateTimer);
                break;
            case RUNNING:
                region = mageRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
                region = mageFalling.getKeyFrame(stateTimer);
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
        previousState= currentState;
        return region;
    }

    public State getState() {
        //  if (b2body.getLinearVelocity().y > 0.5) {
        //     return State.JUMPING;
        // } else if (b2body.getLinearVelocity().y < 0 && previousState == State.JUMPING) {
        //   return State.AIRTRANSITION;
        //} else if (b2body.getLinearVelocity().y < 0 && previousState == State.AIRTRANSITION) {
        //   return State.FALLING;
        //}else if (b2body.getLinearVelocity().y == 0 && previousState == State.FALLING) {
        //   return State.LANDING;
        // } else
        if (b2body.getLinearVelocity().x != 0) {
            return State.RUNNING;
        } else {
            return State.STANDING;
        }

    }

    public void defineMage() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(6200 / Vault.PPM, 4400 / Vault.PPM);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1, 2);

        fdef.shape = shape;
        fdef.friction = 0.3f;
        b2body.createFixture(fdef);
    }
}
