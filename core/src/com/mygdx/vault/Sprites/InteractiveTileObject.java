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

/**
 * Clase abstracta que representa un objeto interactivo en el mundo del juego.
 * Proporciona funcionalidades básicas para la creación y manipulación de objetos interactivos.
 */
public abstract class InteractiveTileObject {

    /** El mundo del juego */
    protected World world;
    /** El mapa en el que se encuentra el objeto interactivo */
    protected TiledMap map;
    /** El rectángulo que define los límites del objeto interactivo */
    protected Rectangle bounds;
    /** El cuerpo físico del objeto interactivo */
    protected Body body;
    /** La fixture del cuerpo físico */
    protected Fixture fixture;

    /** El administrador de assets del juego */
    protected AssetManager manager;
    /** La definición de la fixture */
    protected FixtureDef fdef;

    /**
     * Constructor de la clase InteractiveTileObject.
     * @param screen La pantalla de juego a la que pertenece el objeto interactivo.
     * @param bounds Los límites del objeto interactivo.
     */
    public InteractiveTileObject(PlayScreen screen, Rectangle bounds) {
        this.world = screen.getWorld();
        this.map = screen.getMap();
        this.bounds = bounds;

        BodyDef bdef = new BodyDef();
        fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();

        bdef.type = BodyDef.BodyType.StaticBody;
        bdef.position.set((bounds.getX() + bounds.getWidth() / 2) / Vault.PPM, (bounds.getY() + bounds.getHeight() / 2) / Vault.PPM);

        body = world.createBody(bdef);

        shape.setAsBox(bounds.getWidth() / 2 / Vault.PPM, bounds.getHeight() / 2 / Vault.PPM);
        fdef.shape = shape;
        fdef.friction = 1f;
        fixture = body.createFixture(fdef);
        shape.dispose();
    }

    /**
     * Método abstracto para manejar el evento de contacto en la cabeza del objeto interactivo.
     */
    public abstract void onHeadHit();

    /**
     * Método abstracto para manejar el evento de contacto en los pies del objeto interactivo.
     */
    public abstract void onFeetHit();

    /**
     * Método abstracto para manejar el evento de no contacto en los pies del objeto interactivo.
     */
    public abstract void onFeetNotHit();

    /**
     * Método abstracto para manejar el evento de contacto en el lado izquierdo del objeto interactivo.
     */
    public abstract void onSideLHit();

    /**
     * Método abstracto para manejar el evento de no contacto en el lado izquierdo del objeto interactivo.
     */
    public abstract void onSideLNotHit();

    /**
     * Establece el filtro de categoría para la fixture del objeto interactivo.
     * @param filterBit El bit de categoría del filtro.
     */
    public void setCategoryFilter(short filterBit) {
        Filter filter = new Filter();
        filter.categoryBits = filterBit;
        fixture.setFilterData(filter);
    }

    /**
     * Obtiene la celda del mapa en la que se encuentra el objeto interactivo.
     * @return La celda del mapa.
     */
    public TiledMapTileLayer.Cell getCell() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(1);
        return layer.getCell((int) (body.getPosition().x * Vault.PPM / 512), (int) (body.getPosition().y * Vault.PPM / 512));
    }
}
