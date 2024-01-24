package com.mygdx.vault.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.vault.Sprites.InteractiveTileObject;
import com.mygdx.vault.Sprites.Room;
import com.mygdx.vault.Sprites.Spike;
import com.mygdx.vault.Vault;

import java.io.Console;

import sun.security.util.Debug;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        if (fixA.getUserData() == "sideL" || fixB.getUserData() == "sideL") {
            Fixture sideL = fixA.getUserData() == "sideL" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onSideLHit();
            }
        }if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture sideL = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onHeadHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onSideLHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && Room.class.isAssignableFrom(object.getUserData().getClass())){
                ((Room)object.getUserData()).onRoomHit();
            }
        }
        if (fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            Fixture sideL = fixA.getUserData() == "feet" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onFeetHit();
            }
        }
        switch (cDef){
            case Vault.SPIKE_BIT | Vault.MAGE_BIT:
                if (fixA.getFilterData().categoryBits==Vault.SPIKE_BIT) {
//                    ((Spike) fixA.getUserData()).hit();
                }else{
//                    ((Spike) fixB.getUserData()).hit();

                }
                break;
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        if (fixA.getUserData() == "sideL" || fixB.getUserData() == "sideL") {
            Fixture sideL = fixA.getUserData() == "sideL" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onSideLNotHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onSideLNotHit();
            }
        }
        if (fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            Fixture sideL = fixA.getUserData() == "feet" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
                ((InteractiveTileObject)object.getUserData()).onFeetNotHit();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
