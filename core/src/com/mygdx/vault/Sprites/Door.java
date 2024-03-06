package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

/**
 * Representa una puerta en el juego que puede abrirse y cerrarse.
 * Extiende la clase Sprite para la representación gráfica de la puerta.
 */
public class Door extends Sprite {

    /** El nivel al que conduce la puerta */
    public int level;
    /** El jugador asociado a la puerta */
    private Mage player;

    /** La textura de la puerta */
    private TextureRegion texture;
    /** El mundo del juego */
    protected World world;
    /** La pantalla de juego */
    protected PlayScreen screen;
    /** El cuerpo físico de la puerta */
    public Body body;
    /** La fixture del cuerpo físico */
    public Fixture fixture;
    /** Los límites de la puerta */
    protected Rectangle bounds;
    /** Indica si la puerta ha cambiado su estado */
    public boolean change = false;

    /**
     * Constructor de la clase Door.
     * @param screen La pantalla de juego a la que pertenece la puerta.
     * @param bounds Los límites de la puerta.
     * @param player El jugador asociado a la puerta.
     * @param level El nivel al que conduce la puerta.
     */
    public Door(PlayScreen screen, Rectangle bounds, Mage player, int level) {
        this.world = screen.getWorld();
        this.bounds = bounds;
        this.player = player;
        this.level = level;
        this.screen = screen;

        texture = new TextureRegion(new Texture(("closedDoor.png")));
        setRegion(texture.getTexture());
        defineDoor();
        setBounds(bounds.getX() / Vault.PPM, bounds.getY() / Vault.PPM, texture.getRegionWidth() / Vault.PPM, texture.getRegionHeight() / Vault.PPM);
        Filter filter = new Filter();
        filter.categoryBits = Vault.DOOR_BIT;
        fixture.setFilterData(filter);
    }

    /**
     * Define el cuerpo físico de la puerta en el mundo del juego.
     */
    public void defineDoor() {
        BodyDef bdef = new BodyDef();
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Vault.PPM, (bounds.getY() + bounds.getHeight() / 2) / Vault.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        shape.setAsBox(bounds.getWidth() / 2 / Vault.PPM, bounds.getHeight() / 2 / Vault.PPM);
        fdef.shape = shape;
        fdef.friction = 1;
        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
    }

    /**
     * Dibuja la puerta en el batch de renderizado.
     * @param batch El batch de renderizado.
     */
    @Override
    public void draw(Batch batch) {
        change = false;
        super.draw(batch);
    }

    /**
     * Abre la puerta.
     */
    public void open() {
        screen.setOpenDoor(true);
        screen.hitdoorlevel = level;
    }

    /**
     * Cierra la puerta.
     */
    public void close() {
        if (screen.isOpenDoor()) {
            // No se realiza ninguna acción específica de cierre
        }
    }
}
