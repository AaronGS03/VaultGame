package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

public class Room extends InteractiveTileObject {


    private Mage player;
    private OrthographicCamera gamecam;
    private int level;
    private OrthographicCamera backcam;

    public Rectangle getSpawnPoint() {
        return spawnPoint;
    }

    public void setSpawnPoint(Rectangle spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    private Rectangle spawnPoint;

    public Room(PlayScreen screen, Rectangle bounds, Mage player, OrthographicCamera gamecam, int level, OrthographicCamera backcam) {
        super(screen, bounds);
        this.player = player;
        this.gamecam = gamecam;
        this.level = level;
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
        // Centra la cámara verticalmente tomando en cuenta la posición del jugador
        gamecam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2, bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);
        backcam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2,bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);
        player.setCurrentLevel(this.level);

        if (player.keys!=null){
            if (player.getCurrentLevel()!=player.keys.level){
                player.keys.collected=false;

            }
        }

    }


    @Override
    public void onSideLNotHit() {

    }
}
