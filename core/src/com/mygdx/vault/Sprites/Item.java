package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Clase abstracta que representa un objeto en el juego que el jugador puede recoger y usar.
 * Extiende la clase Sprite y proporciona funcionalidades básicas para la creación y manipulación de objetos.
 */
public abstract class Item extends Sprite {

    /** La pantalla de juego a la que pertenece el objeto */
    protected PlayScreen screen;
    /** El mundo del juego */
    protected World world;
    /** La velocidad del objeto */
    protected Vector2 velocity;
    /** Indica si el objeto debe ser destruido */
    protected boolean toDestroy;
    /** Indica si el objeto ha sido destruido */
    protected boolean destroyed;
    /** El cuerpo físico del objeto */
    public Body body;
    /** La fixture del cuerpo físico */
    protected Fixture fixture;

    /**
     * Constructor de la clase Item.
     * @param screen La pantalla de juego a la que pertenece el objeto.
     * @param x La coordenada x inicial del objeto.
     * @param y La coordenada y inicial del objeto.
     */
    public Item(PlayScreen screen, float x, float y) {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x, y);
        setBounds(getX(), getY(), 512 / Vault.PPM, 512 / Vault.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    /**
     * Método abstracto para definir las características del objeto.
     */
    public abstract void defineItem();

    /**
     * Método abstracto para manejar la acción de recoger el objeto.
     * @param player El jugador que recoge el objeto.
     */
    public abstract void take(Mage player);

    /**
     * Método abstracto para manejar la acción de usar el objeto.
     * @param door La puerta sobre la que se utiliza el objeto.
     */
    public abstract void use(Door door);

    /**
     * Actualiza el estado del objeto en cada ciclo de actualización del juego.
     * @param dt El tiempo transcurrido desde el último ciclo de actualización.
     */
    public void update(float dt) {
        if (toDestroy && !destroyed) {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    /**
     * Dibuja el objeto en el batch de renderizado.
     * @param batch El batch de renderizado.
     */
    public void draw(Batch batch) {
        if (!destroyed) {
            super.draw(batch);
        }
    }

    /**
     * Marca el objeto para ser destruido.
     */
    public void destroy() {
        toDestroy = true;
    }
}
