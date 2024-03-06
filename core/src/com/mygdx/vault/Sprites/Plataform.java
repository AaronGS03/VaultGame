package com.mygdx.vault.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Representa una plataforma en el juego que puede ser interactiva con el jugador.
 * Extiende la clase InteractiveTileObject para heredar funcionalidades comunes de los objetos interactivos.
 */
public class Plataform extends InteractiveTileObject {
    /** Jugador asociado a la plataforma */
    private Mage player;

    /**
     * Constructor de la clase Plataform.
     * @param screen La pantalla de juego a la que pertenece la plataforma.
     * @param bounds Los límites de la plataforma.
     * @param player El jugador asociado a la plataforma.
     */
    public Plataform(PlayScreen screen, Rectangle bounds, Mage player) {
        super(screen, bounds);
        this.player = player;
        fixture.setUserData(this);
        setCategoryFilter(Vault.PLATAFORM_BIT);
    }

    /** Método que se ejecuta cuando el jugador golpea la plataforma con la cabeza. */
    @Override
    public void onHeadHit() {
        // La plataforma no tiene ninguna interacción específica cuando es golpeada en la cabeza por el jugador
    }

    /** Método que se ejecuta cuando el jugador golpea la plataforma con los pies. */
    @Override
    public void onFeetHit() {
        // La plataforma no tiene ninguna interacción específica cuando es golpeada con los pies por el jugador
    }

    /** Método que se ejecuta cuando el jugador no golpea la plataforma con los pies. */
    @Override
    public void onFeetNotHit() {
        // La plataforma no tiene ninguna interacción específica cuando el jugador no la golpea con los pies
    }

    /** Método que se ejecuta cuando el jugador golpea la plataforma por el lado izquierdo. */
    @Override
    public void onSideLHit() {
        // La plataforma no tiene ninguna interacción específica cuando es golpeada por el lado izquierdo por el jugador
    }

    /** Método que se ejecuta cuando el jugador no golpea la plataforma por el lado izquierdo. */
    @Override
    public void onSideLNotHit() {
        // La plataforma no tiene ninguna interacción específica cuando el jugador no la golpea por el lado izquierdo
    }
}
