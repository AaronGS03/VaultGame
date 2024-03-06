package com.mygdx.vault.tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Sprites.Door;
import com.mygdx.vault.Sprites.InteractiveTileObject;
import com.mygdx.vault.Sprites.Item;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Sprites.Plataform;
import com.mygdx.vault.Sprites.Room;
import com.mygdx.vault.Sprites.Spike;
import com.mygdx.vault.Vault;

/**
 * Clase que gestiona los contactos entre fixtures en el mundo del juego.
 * Implementa la interfaz ContactListener de Box2D.
 */
public class WorldContactListener implements ContactListener {

    /** Booleano que indica si el contacto entre un ítem y el personaje principal ha sido manejado. */
    private boolean itemMageContactHandled = false;

    /**
     * Método invocado cuando comienza un contacto entre fixtures.
     * @param contact El objeto Contact que representa el contacto entre fixtures.
     */
    @Override
    public void beginContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        // Combinar las categorías de bits de las fixtures en contacto
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            // Caso de colisión entre ítem y personaje principal
            case Vault.ITEM | Vault.MAGE_BIT:
                // Manejar la interacción entre ítem y personaje principal
                if (fixA.getFilterData().categoryBits == Vault.ITEM) {
                    if (fixB.getUserData() != "sideL" && fixB.getUserData() != "sideR" && fixB.getUserData() != "head" && fixB.getUserData() != "feet") {
                        Gdx.input.vibrate(200);
                        ((Item) fixA.getUserData()).take((Mage) fixB.getUserData());
                    }
                } else {
                    if (fixA.getUserData() != "sideL" && fixA.getUserData() != "sideR" && fixA.getUserData() != "head" && fixA.getUserData() != "feet") {
                        Gdx.input.vibrate(200);
                        ((Item) fixB.getUserData()).take((Mage) fixA.getUserData());
                    }
                }
                break;
            // Caso de colisión entre trampa y personaje principal
            case Vault.SPIKE_BIT | Vault.MAGE_BIT:
                // Activar la trampa
                if (fixA.getFilterData().categoryBits == Vault.SPIKE_BIT) {
                    ((Spike) fixA.getUserData()).hit();
                } else {
                    ((Spike) fixB.getUserData()).hit();
                }
                break;
            // Caso de colisión entre sensor y personaje principal
            case Vault.SENSOR_BIT | Vault.MAGE_BIT:
                // Activar el sensor
                if (fixA.getFilterData().categoryBits == Vault.SENSOR_BIT) {
                    ((Sensor) fixA.getUserData()).active();
                } else {
                    ((Sensor) fixB.getUserData()).active();
                }
                break;
            // Caso de colisión entre puerta y personaje principal
            case Vault.DOOR_BIT | Vault.MAGE_BIT:
                // Abrir la puerta
                if (fixA.getFilterData().categoryBits == Vault.DOOR_BIT) {
                    ((Door) fixA.getUserData()).open();
                } else {
                    ((Door) fixB.getUserData()).open();
                }
                break;
        }

        // Manejar eventos específicos cuando el personaje principal contacta con los lados, cabeza o pies de un objeto interactivo
        // Activar métodos correspondientes para manejar estos eventos
        if (fixA.getUserData() == "sideL" || fixB.getUserData() == "sideL") {
            Fixture sideL = fixA.getUserData() == "sideL" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideLHit();
            }
        }
        if (fixA.getUserData() == "head" || fixB.getUserData() == "head") {
            Fixture sideL = fixA.getUserData() == "head" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideLHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && Room.class.isAssignableFrom(object.getUserData().getClass())) {
                ((Room) object.getUserData()).onRoomHit();
            }
        }
        if (fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            Fixture sideL = fixA.getUserData() == "feet" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onFeetHit();
            }
        }
    }

    /**
     * Método invocado cuando finaliza un contacto entre fixtures.
     * @param contact El objeto Contact que representa el contacto entre fixtures.
     */
    @Override
    public void endContact(Contact contact) {
        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();
        int cDef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;
        switch (cDef) {
            // Caso de finalización de contacto entre puerta y personaje principal
            case Vault.DOOR_BIT | Vault.MAGE_BIT:
                // Cerrar la puerta
                if (fixA.getFilterData().categoryBits == Vault.DOOR_BIT) {
                    ((Door) fixA.getUserData()).close();
                } else {
                    ((Door) fixB.getUserData()).close();
                }
                break;
        }

        // Manejar eventos específicos cuando el personaje principal deja de contactar con los lados o pies de un objeto interactivo
        // Activar métodos correspondientes para manejar estos eventos
        if (fixA.getUserData() == "sideL" || fixB.getUserData() == "sideL") {
            Fixture sideL = fixA.getUserData() == "sideL" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideLNotHit();
            }
        }
        if (fixA.getUserData() == "sideR" || fixB.getUserData() == "sideR") {
            Fixture sideL = fixA.getUserData() == "sideR" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onSideLNotHit();
            }
        }
        if (fixA.getUserData() == "feet" || fixB.getUserData() == "feet") {
            Fixture sideL = fixA.getUserData() == "feet" ? fixA : fixB;
            Fixture object = sideL == fixA ? fixB : fixA;
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {
                ((InteractiveTileObject) object.getUserData()).onFeetNotHit();
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        // Método no implementado
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        // Método no implementado
    }
}
