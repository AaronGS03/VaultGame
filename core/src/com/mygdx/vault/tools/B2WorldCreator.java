package com.mygdx.vault.tools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
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

public class B2WorldCreator {
    AssetManager manager;
    private Array<Spike> spikes;

    public Array<Spike> getFakespikes() {
        return fakespikes;
    }

    public void setFakespikes(Array<Spike> fakespikes) {
        this.fakespikes = fakespikes;
    }

    private Array<Spike> fakespikes;
    private Array<Key> keys;

    public Array<Sensor> getSensors() {
        return sensors;
    }

    public void setSensors(Array<Sensor> sensors) {
        this.sensors = sensors;
    }

    private Array<Sensor> sensors;

    public Array<Spike> getSpikes() {
        return spikes;
    }
    public Array<Key> getKeys() {
        return keys;
    }

    public B2WorldCreator(PlayScreen screen, Mage player, Array<Room> habitaciones, Array<Door> doors, OrthographicCamera gamecam, OrthographicCamera backcam) {
        World world =screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            if (object.getProperties().containsKey("slide")){
                new Wall(screen, rect, player,((boolean) object.getProperties().get("slide")));

            }else {
                new Wall(screen, rect, player,false);

            }

        }
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Plataform(screen, rect, player);


        }

        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {
            if (object.getProperties().containsKey("level")) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                if (Integer.parseInt(object.getProperties().get("level")+"")!=-1){
                    doors.add(new Door(screen, rect, player,Integer.parseInt(object.getProperties().get("level")+"")));
                }else {
                    new Door(screen, rect, player,Integer.parseInt(object.getProperties().get("level")+""));
                }

            }


        }
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            if (object.getProperties().containsKey("spawnPoint")) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                habitaciones.get( Integer.parseInt(object.getProperties().get("spawnPoint")+"")).setSpawnPoint(rect);
            }else{
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                habitaciones.add(new Room(screen, rect, player, gamecam, Integer.parseInt(object.getProperties().get("level")+""), backcam));

            }

        }

        spikes= new Array<>();
        fakespikes= new Array<>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            if (object.getProperties().containsKey("fake")) {
                fakespikes.add(new Spike(screen, rect.getX()/ Vault.PPM, rect.getY()/ Vault.PPM, Integer.parseInt(object.getProperties().get("position")+""),player ));

            }else{
                spikes.add(new Spike(screen, rect.getX()/ Vault.PPM, rect.getY()/ Vault.PPM, Integer.parseInt(object.getProperties().get("position")+""),player ));
            }


        }
        keys= new Array<>();
        for (MapObject object : map.getLayers().get(7).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
                keys.add(new Key(screen,rect.getX()/ Vault.PPM, rect.getY()/ Vault.PPM,Integer.parseInt(object.getProperties().get("level")+"")));


        }
        sensors= new Array<>();

        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            sensors.add(new Sensor(screen,rect,Integer.parseInt(object.getProperties().get("level")+"")));


        }


    }
}
