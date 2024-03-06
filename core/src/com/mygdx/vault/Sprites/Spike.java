package com.mygdx.vault.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.mygdx.vault.Screens.PlayScreen;
import com.mygdx.vault.Vault;

/**
 * Representa un pincho en el juego que puede dañar al jugador si lo toca.
 */
public class Spike extends Sprite {
    /** Mundo del juego */
    protected World world;
    /** Pantalla de juego */
    protected PlayScreen screen;
    /** Cuerpo del pincho */
    public Body b2body;
    /** Posición x del pincho */
    public float x;
    /** Posición y del pincho */
    public float y;
    /** Posición del pincho */
    public int position;
    /** Jugador asociado */
    public Mage player;
    /** Indicador de pincho falso */
    private boolean fake = false;
    /** Fixture del pincho */
    Fixture fixture;

    /** Región de textura del pincho */
    private TextureRegion texture;

    /**
     * Constructor de la clase Spike.
     * @param screen La pantalla de juego.
     * @param x La posición x del pincho.
     * @param y La posición y del pincho.
     * @param position La posición del pincho.
     * @param player El jugador asociado al pincho.
     */
    public Spike(PlayScreen screen, float x, float y, int position, Mage player) {
        this.world = screen.getWorld();
        this.screen = screen;
        this.position = position;
        this.player = player;
        this.x = x;
        this.y = y;

        // Determina la textura del pincho basándose en su posición
        if (position == 1) {
            texture = new TextureRegion(new Texture(("SpikeUP.png")));
        } else if (position == 2) {
            texture = new TextureRegion(new Texture(("SpikeLEFT.png")));
        } else if (position == 3) {
            texture = new TextureRegion(new Texture(("SpikeRIGHT.png")));
        } else {
            texture = new TextureRegion(new Texture(("Spike.png")));
        }

        setRegion(texture);
        defineSpike();
        setBounds(x - 0.4f, y - 0.5f, texture.getRegionWidth() / 1.2f / Vault.PPM, texture.getRegionHeight() / 1.2f / Vault.PPM);
    }

    /** Define las propiedades físicas del pincho. */
    protected void defineSpike() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(x - 0.6f + texture.getRegionWidth() / 2 / Vault.PPM, y - 0.6f + texture.getRegionHeight() / 2 / Vault.PPM);
        bdef.type = BodyDef.BodyType.StaticBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        Vector2[] vertices = new Vector2[3];

        // Determina los vértices del pincho basándose en su posición
        if (position == 1) {
            vertices[0] = new Vector2(-1.1f, 0.9f);
            vertices[1] = new Vector2(1f, 0.9f);
            vertices[2] = new Vector2(0.0f, -1f);
        } else if (position == 2) {
            vertices[0] = new Vector2(-1.1f, -1.2f);
            vertices[1] = new Vector2(1.1f, -0.1f);
            vertices[2] = new Vector2(-1.1f, 0.9f);
        } else if (position == 3) {
            vertices[0] = new Vector2(1.1f, -1.2f);
            vertices[1] = new Vector2(-1.1f, -0.1f);
            vertices[2] = new Vector2(1.1f, 0.9f);
        } else {
            vertices[0] = new Vector2(-1.1f, -1.2f);
            vertices[1] = new Vector2(1.1f, -1.2f);
            vertices[2] = new Vector2(0.0f, 0.9f);
        }

        shape.set(vertices);

        fdef.filter.categoryBits = Vault.SPIKE_BIT;
        fdef.filter.maskBits = Vault.MAGE_BIT | Vault.PLATAFORM_BIT | Vault.DOOR_BIT | Vault.WALL_BIT;

        fdef.shape = shape;
        fdef.friction = 0.3f;
        fixture = b2body.createFixture(fdef);
        fixture.setUserData(this);

        shape.dispose();
    }

    /** Actualiza el pincho. */
    public void update(float dt) {
        // No hay actualización necesaria para los pinchos estáticos
    }

    /** Activa el pincho falso. */
    public void fake() {
        if (b2body != null) {
            // Desactiva la colisión de la fixture del pincho
            Filter filter = new Filter();
            filter.categoryBits = Vault.NOTHING_BIT; // Categoría de bits que no colisiona con nada
            fixture.setFilterData(filter);
            fake = true;
        }
    }

    /** Dibuja el pincho. */
    public void draw(Batch batch) {
        if (!fake) {
            super.draw(batch);
        }
    }

    /** Maneja el impacto del pincho. */
    public void hit() {
        player.dead=(true);
        screen.reset = true;
        Gdx.app.log("V", "toque");
    }

    /** Libera los recursos utilizados por el pincho. */
    public void dispose() {
        // No hay recursos para liberar en el pincho estático
    }
}
