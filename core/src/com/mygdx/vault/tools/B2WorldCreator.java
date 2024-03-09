package com.mygdx.vault.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Sprites.Door;
import com.mygdx.vault.Sprites.Key;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Sprites.Plataform;
import com.mygdx.vault.Sprites.Room;
import com.mygdx.vault.Sprites.Spike;
import com.mygdx.vault.Sprites.Wall;
import com.mygdx.vault.Vault;

/**
 * Clase encargada de crear el mundo de Box2D basado en los objetos del mapa cargado.
 */
public class B2WorldCreator {
    /** Manejador de activos */
    AssetManager manager;

    /** Array de objetos de tipo Spike */
    public Array<Spike> spikes;

    /** Array de objetos de tipo Spike falsos */
    public Array<Spike> fakespikes;

    /** Array de objetos de tipo Key */
    public Array<Key> keys;

    /** Array de objetos de tipo Sensor */
    public Array<Sensor> sensors;

    /** Escenario para la representación visual */
    private Stage stage;

    /** Mapa tiled */
    public TiledMap map;

    /**
     * Constructor de B2WorldCreator.
     *
     * @param screen       PlayScreen donde se creará el mundo Box2D.
     * @param player       Personaje principal del juego.
     * @param habitaciones Array de habitaciones en el juego.
     * @param doors        Puertas del juego.
     * @param gamecam      Cámara del juego.
     * @param backcam      Cámara de fondo del juego.
     */
    public B2WorldCreator(PlayScreen screen, Mage player, Array<Room> habitaciones, Array<Door> doors, OrthographicCamera gamecam, OrthographicCamera backcam) {
        map = screen.getMap();

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().containsKey("slide")) {
                new Wall(screen, rect, player, ((boolean) object.getProperties().get("slide")));
            } else {
                new Wall(screen, rect, player, false);
            }
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Plataform(screen, rect, player);
        }

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            if (object.getProperties().containsKey("level")) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Integer.parseInt(object.getProperties().get("level") + "") != -1) {
                    doors.add(new Door(screen, rect, player, Integer.parseInt(object.getProperties().get("level") + "")));
                } else {
                    new Door(screen, rect, player, Integer.parseInt(object.getProperties().get("level") + ""));
                }
            }
        }

        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {
            if (object.getProperties().containsKey("spawnPoint")) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                habitaciones.get(Integer.parseInt(object.getProperties().get("spawnPoint") + "")).spawnPoint=rect;
            } else {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                habitaciones.add(new Room(screen, rect, player, gamecam, Integer.parseInt(object.getProperties().get("level") + ""), backcam));
            }
        }

        spikes = new Array<>();
        fakespikes = new Array<>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().containsKey("fake")) {
                fakespikes.add(new Spike(screen, rect.getX() / Vault.PPM, rect.getY() / Vault.PPM, Integer.parseInt(object.getProperties().get("position") + ""), player));
            } else {
                spikes.add(new Spike(screen, rect.getX() / Vault.PPM, rect.getY() / Vault.PPM, Integer.parseInt(object.getProperties().get("position") + ""), player));
            }
        }

        keys = new Array<>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            keys.add(new Key(screen, rect.getX() / Vault.PPM, rect.getY() / Vault.PPM, Integer.parseInt(object.getProperties().get("level") + "")));
        }

        sensors = new Array<>();
        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            sensors.add(new Sensor(screen, rect, Integer.parseInt(object.getProperties().get("level") + "")));
        }
    }

    /**
     * Método para liberar los recursos utilizados por B2WorldCreator.
     */
    public void dispose() {
        if (map != null) {
            map.dispose();
        }
        keys.clear();
        spikes.clear();
        fakespikes.clear();
    }
}
