package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;
import com.mygdx.vault.tools.RoomTool;

public class Room extends InteractiveTileObject {


    private Mage player;
    private OrthographicCamera gamecam;
    private OrthographicCamera backcam;

    public Room(PlayScreen screen, Rectangle bounds, Mage player, OrthographicCamera gamecam, OrthographicCamera backcam) {
        super(screen, bounds);
        this.player = player;
        this.gamecam = gamecam;
        this.backcam = backcam;
        fixture.setSensor(true);
        fixture.setUserData(this);
    }

    @Override
    public void onHeadHit() {

    }

    @Override
    public void onFeetHit() {

    }

    @Override
    public void onFeetNotHit() {

    }

    @Override
    public void onSideLHit() {

    }

    public void onRoomHit() {
        float centerY = bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2;
        float playerCenterY = player.b2body.getPosition().y;

        // Centra la cámara verticalmente tomando en cuenta la posición del jugador
        gamecam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2, bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);
        backcam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2,bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);

    }


    @Override
    public void onSideLNotHit() {

    }
}
