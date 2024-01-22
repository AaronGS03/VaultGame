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
import com.mygdx.vault.Sprites.Door;
import com.mygdx.vault.Sprites.Mage;
import com.mygdx.vault.Sprites.Plataform;
import com.mygdx.vault.Sprites.Room;
import com.mygdx.vault.Sprites.Wall;

public class B2WorldCreator {
    AssetManager manager;

    public B2WorldCreator(World world, TiledMap map, Mage player, AssetManager manager, Array<RoomTool> habitaciones, OrthographicCamera gamecam, OrthographicCamera backcam) {
        BodyDef bdef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fdef = new FixtureDef();
        Body body;

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();

            new Wall(world, map, rect, player);

        }
        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Plataform(world, map, rect, player);


        }
        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {

            Rectangle rect = ((RectangleMapObject) object).getRectangle();
            new Room(world, map, rect, player, gamecam,backcam);
            habitaciones.add(new RoomTool(rect,Integer.parseInt(object.getProperties().get("level")+"")));



        }
    }
}
