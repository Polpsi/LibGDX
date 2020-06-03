package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile extends GameObject implements Poolable {

    private GameController.Owner ownerType;
    private TextureRegion texture;
    private Vector2 velocity;
    private float speed;
    private float angle;
    private float weaponEnergy;
    private boolean active;

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Projectile(GameController gc) {
        super(gc);
        this.velocity = new Vector2();
        this.speed = 640.0f;
    }

    public GameController.Owner getOwnerType() {
        return ownerType;
    }

    public float getWeaponEnergy() {
        return weaponEnergy;
    }

    public void setup(Vector2 startPosition, float angle, TextureRegion texture, float weaponEnergy, GameController.Owner owner) {
        this.texture = texture;
        this.position.set(startPosition);
        this.angle = angle;
        this.velocity.set(speed * MathUtils.cosDeg(angle), speed * MathUtils.sinDeg(angle));
        this.active = true;
        this.weaponEnergy = weaponEnergy;
        this.ownerType = owner;
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 8, position.y - 8);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0 || position.x > 1280 || position.y < 0 || position.y > 720) {
            deactivate();
        }
    }
}
