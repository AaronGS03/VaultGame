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

/**
 * Clase que representa un sensor en el juego.
 * Los sensores son utilizados para activar eventos específicos cuando un cuerpo entra en contacto con ellos.
 */
public class Sensor extends Sprite {

    /** Nivel al que pertenece el objeto */
    public int level;

    /** Mundo del juego */
    protected World world;

    /** Pantalla de juego a la que pertenece el objeto */
    protected PlayScreen screen;

    /** Cuerpo físico del objeto */
    public Body body;

    /** Fixture del cuerpo físico */
    public Fixture fixture;

    /** Area rectangular de limites del objeto */
    protected Rectangle bounds;


    /**
     * Constructor de Sensor.
     *
     * @param screen  Instancia de PlayScreen donde se creará el sensor.
     * @param bounds  Límites del sensor.
     * @param level   Nivel asociado al sensor.
     */
    public Sensor(PlayScreen screen, Rectangle bounds, int level) {
        this.world = screen.getWorld();
        this.bounds = bounds;
        this.level = level;
        this.screen = screen;

        defineSensor();
        setBounds(bounds.getX() / Vault.PPM, bounds.getY() / Vault.PPM, bounds.getWidth() / Vault.PPM, bounds.getHeight() / Vault.PPM);
        Filter filter = new Filter();
        filter.categoryBits = Vault.SENSOR_BIT;
        fixture.setFilterData(filter);
    }

    /**
     * Método para definir el cuerpo y la fixture del sensor.
     */
    public void defineSensor() {
        BodyDef bdef = new BodyDef();
        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Vault.PPM, (bounds.getY() + bounds.getHeight() / 2) / Vault.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Vault.PPM, bounds.getHeight() / 2 / Vault.PPM);
        fdef.shape = shape;
        fdef.friction = 1;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        if (level == -1) {
            fixture.setSensor(false);
        } else {
            fixture.setSensor(true);
        }
    }

    /**
     * Método para activar eventos específicos asociados al sensor.
     */
    public void active() {
        switch (level) {
            case 3:
                screen.setLevel3gimmick(true);
                break;
            case 7:
                screen.setLevel7gimmick(true);
                break;
            case 8:
                screen.setLevel8gimmick(true);
                break;
            case 10:
                screen.setLevel10gimmick(true);
                break;
            case 13:
                screen.setLevel13gimmick(true);
                break;
            case 14:
                screen.setLevel14gimmick(true);
                break;
            case 69:
                screen.setLevel69gimmick(true);
                break;
            default:
                break;
        }
    }
}