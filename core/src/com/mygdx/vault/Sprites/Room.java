package com.mygdx.vault.Sprites;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Representa una habitación en el juego que puede ser interactiva con el jugador.
 * Extiende la clase InteractiveTileObject para heredar funcionalidades comunes de los objetos interactivos.
 */
public class Room extends InteractiveTileObject {
    /** Jugador asociado a la habitación */
    private Mage player;
    /** Cámara ortográfica utilizada en el juego */
    private OrthographicCamera gamecam;
    /** Nivel de la habitación */
    private int level;
    /** Cámara ortográfica de fondo */
    private OrthographicCamera backcam;
    /** Punto de aparición en la habitación */
    public Rectangle spawnPoint;
    /** Pantalla de juego */
    private PlayScreen screen;

    /**
     * Constructor de la clase Room.
     * @param screen La pantalla de juego a la que pertenece la habitación.
     * @param bounds Los límites de la habitación.
     * @param player El jugador asociado a la habitación.
     * @param gamecam La cámara ortográfica utilizada en el juego.
     * @param level El nivel de la habitación.
     * @param backcam La cámara ortográfica de fondo.
     */
    public Room(PlayScreen screen, Rectangle bounds, Mage player, OrthographicCamera gamecam, int level, OrthographicCamera backcam) {
        super(screen, bounds);
        this.screen = screen;
        this.player = player;
        this.gamecam = gamecam;
        this.level = level;
        this.backcam = backcam;
        fixture.setSensor(true);
        fixture.setUserData(this);
    }

    /** Método que se ejecuta cuando el jugador golpea la habitación con la cabeza. */
    @Override
    public void onHeadHit() {
        // La habitación no tiene ninguna interacción específica cuando es golpeada en la cabeza por el jugador
    }

    /** Método que se ejecuta cuando el jugador golpea la habitación con los pies. */
    @Override
    public void onFeetHit() {
        // La habitación no tiene ninguna interacción específica cuando es golpeada con los pies por el jugador
    }

    /** Método que se ejecuta cuando el jugador no golpea la habitación con los pies. */
    @Override
    public void onFeetNotHit() {
        // La habitación no tiene ninguna interacción específica cuando el jugador no la golpea con los pies
    }

    /** Método que se ejecuta cuando el jugador golpea la habitación por el lado izquierdo. */
    @Override
    public void onSideLHit() {
        // La habitación no tiene ninguna interacción específica cuando es golpeada por el lado izquierdo por el jugador
    }

    /** Método que se ejecuta cuando el jugador no golpea la habitación por el lado izquierdo. */
    @Override
    public void onSideLNotHit() {
        // La habitación no tiene ninguna interacción específica cuando el jugador no la golpea por el lado izquierdo
    }

    /** Método que se ejecuta cuando el jugador entra en la habitación. */
    public void onRoomHit() {
        // Centra la cámara verticalmente tomando en cuenta la posición del jugador
        gamecam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2, bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);
        backcam.position.set((bounds.x / Vault.PPM) + bounds.getWidth() / Vault.PPM / 2, bounds.y / Vault.PPM + bounds.getHeight() / Vault.PPM / 2, 0);
        player.currentLevel=(this.level);

        // Si el jugador tiene una llave y no está en el mismo nivel que la habitación, se marca como no recogida
        if (player.keys != null) {
            if (player.currentLevel != player.keys.level) {
                player.keys.collected = false;
            }
        }
    }
}
