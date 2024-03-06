package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Representa una llave en el juego que el jugador puede recoger.
 * Extiende la clase Item para heredar funcionalidades comunes de los objetos.
 */
public class Key extends Item {
    /** Animación para la llave cuando está en reposo */
    private Animation idle;
    /** Estado de la animación */
    private float statetime;
    /** Posición x de la llave */
    public float x;
    /** Posición y de la llave */
    public float y;
    /** Ancho de la llave */
    public float width;
    /** Altura de la llave */
    public float height;
    /** Coordenada x del origen de la llave */
    public float originx;
    /** Coordenada y del origen de la llave */
    public float originy;
    /** Nivel al que pertenece la llave */
    public int level;
    /** Indica si la llave ha sido recogida */
    public boolean collected = false;
    /** Definición del cuerpo físico de la llave */
    BodyDef bdef;
    /** Jugador que recoge la llave */
    Mage player;

    /**
     * Constructor de la clase Key.
     * @param screen La pantalla de juego a la que pertenece la llave.
     * @param x La posición x inicial de la llave.
     * @param y La posición y inicial de la llave.
     * @param level El nivel al que conduce la llave.
     */
    public Key(PlayScreen screen, float x, float y, int level) {
        super(screen, x, y);
        this.x = x;
        this.y = y;
        TextureAtlas.AtlasRegion atlasRegion = screen.getAtlas().findRegion("key-white");
        TextureRegion[][] temp = atlasRegion.split(atlasRegion.getRegionWidth() / 12, atlasRegion.getRegionHeight());
        idle = new Animation(0.2f, temp[0]);
        statetime = 0;
        this.width = atlasRegion.getRegionWidth();
        this.height = atlasRegion.getRegionHeight();
        defineItem();
        this.level = level;
    }

    /**
     * Define el cuerpo físico de la llave en el mundo del juego.
     */
    @Override
    public void defineItem() {
        bdef = new BodyDef();
        bdef.position.set(x + width / 2 / Vault.PPM, y + height / 2 / Vault.PPM);

        bdef.type = BodyDef.BodyType.KinematicBody;
        body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(1f, 1f);
        fdef.filter.categoryBits = Vault.ITEM;
        fdef.filter.maskBits = Vault.MAGE_BIT | Vault.DOOR_BIT;

        fdef.shape = shape;

        fixture = body.createFixture(fdef);
        fixture.setUserData(this);
        body.setGravityScale(0);
        shape.dispose();
    }

    /**
     * Método que se ejecuta cuando el jugador recoge la llave.
     * @param player El jugador que recoge la llave.
     */
    @Override
    public void take(Mage player) {
        collected = true;
        this.player = player;
        player.keys = this;
    }

    /**
     * Método que se ejecuta cuando el jugador utiliza la llave.
     * @param door La puerta a la que se aplica la llave.
     */
    @Override
    public void use(Door door) {
        // La llave no tiene ninguna funcionalidad específica de uso en este juego
    }

    /**
     * Actualiza la posición y el estado de la llave.
     * @param dt El tiempo transcurrido desde la última actualización.
     */
    @Override
    public void update(float dt) {
        super.update(dt);
        statetime += dt;
        if (collected) {
            float newX = player.getX() + getWidth() / 2 - 1;
            float newY = player.getY() + 0.4f + getHeight() / 2;

            setPosition(newX, newY);

            // Actualiza la posición del cuerpo de la llave
            body.setTransform(newX, newY, body.getAngle());
        } else {
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            originx = x + getWidth() / 2 / Vault.PPM;
            originy = y + getHeight() / 2 / Vault.PPM;
            body.setTransform(originx, originy, body.getAngle());
        }

        setRegion(((TextureRegion) idle.getKeyFrame(statetime, true)));
    }
}
