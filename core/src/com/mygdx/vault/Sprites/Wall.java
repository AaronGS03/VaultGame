package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Representa un muro en el juego que puede interactuar con el jugador.
 */
public class Wall extends InteractiveTileObject {
    /** El jugador asociado al muro */
    private Mage player;

    /**
     * Constructor de la clase Wall.
     * @param screen La pantalla de juego.
     * @param bounds Los límites del muro.
     * @param player El jugador asociado al muro.
     * @param slide Indica si el jugador puede deslizarse por el muro.
     */
    public Wall(PlayScreen screen, Rectangle bounds, Mage player, boolean slide) {
        super(screen, bounds);
        this.player = player;
        // Establece la fricción del muro
        this.fdef.friction = 0.5f;
        fixture.setUserData(this);
        setCategoryFilter(Vault.WALL_BIT);
    }

    @Override
    public void onHeadHit() {
        // No se realiza ninguna acción cuando el jugador golpea la cabeza contra el muro
    }

    @Override
    public void onFeetHit() {
        // Indica al jugador que está tocando el muro con los pies
        player.isTouchingGrass=(true);
    }

    @Override
    public void onFeetNotHit() {
        // Indica al jugador que ya no está tocando el muro con los pies
        player.isTouchingGrass=(false);
    }

    @Override
    public void onSideLHit() {
        // Indica al jugador que está tocando el muro por el lado izquierdo
        player.isTouchingWall=(true);
    }

    @Override
    public void onSideLNotHit() {
        // Indica al jugador que ya no está tocando el muro por el lado izquierdo
        player.isTouchingWall=(false);
    }
}
