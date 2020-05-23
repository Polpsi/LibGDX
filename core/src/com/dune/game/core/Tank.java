package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Tank {
    private Vector2 position;
    private Vector2 tmp;
    private TextureRegion[] textures;
    private float angle;
    private float speed;
    private float moveTimer;
    private float timePerFrame;
    private Vector2 gunPoint = new Vector2(1, 0);
    //Привязываем снаряд к танку.
    private Projectile myProjectile;
    private TextureRegion myProjectileTexture;
    private Vector2 myProjectilePosition;

    public Vector2 getPosition() {
        return position;
    }

    public Tank(TextureAtlas atlas, float x, float y) {
        this.position = new Vector2(x, y);
        this.tmp = new Vector2(0, 0);
        this.textures = new TextureRegion(atlas.findRegion("tankanim")).split(64, 64)[0];
        this.myProjectileTexture = new TextureRegion(atlas.findRegion("bullet"));
        this.speed = 140.0f;
        this.timePerFrame = 0.08f;
    }

    private int getCurrentFrameIndex() {
        return (int) (moveTimer / timePerFrame) % textures.length;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            position.add(speed * MathUtils.cosDeg(angle) * dt, speed * MathUtils.sinDeg(angle) * dt);
            moveTimer += dt;
        } else {
            if (getCurrentFrameIndex() != 0) {
                moveTimer += dt;
            }
        }

        // Если поставить isKeyPressed, то при удерживании "К" (aka SPACE) можно стрелять
        // сразу после ухода снаряда за границы поля.
        if (Gdx.input.isKeyJustPressed(Input.Keys.K)) {
            // Если у нас еще нет снаряда - создаём новый! С той самой надписью!
            if (myProjectile == null) {
                myProjectile = new Projectile(getGunPoint(), angle);
                // Мы уже стреляли, но снаряд еще летит.
            } else if (!myProjectile.isActive()) {
                myProjectile.setup(getGunPoint(), angle);
            }
            // Ждём, пока долетит. У нас пока только один заряд.
        }
        // Если мы выстрелили и снаряд ещё не долетел - наблюдаем.
        // Если нет - наблюдать не имеет смысла.
        if (myProjectile != null && myProjectile.isActive()) {
            myProjectile.update(dt);
            myProjectilePosition = myProjectile.getPosition();
        }

        checkBounds();
    }

    // Метод получения координат дула.
    private Vector2 getGunPoint() {
        gunPoint.set(1, 0);
        return gunPoint.rotate(angle).scl(32).add(position);
    }

    public void checkBounds() {
        if (position.x < 40) {
            position.x = 40;
        }
        if (position.y < 40) {
            position.y = 40;
        }
        if (position.x > 1240) {
            position.x = 1240;
        }
        if (position.y > 680) {
            position.y = 680;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(textures[getCurrentFrameIndex()], position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
        // Если мы не стреляли или снаряд уже не активен - рендерить нечего.
        // В противном случае - рендерим.
        if (myProjectile != null && myProjectile.isActive()) {
            batch.draw(myProjectileTexture, myProjectilePosition.x, myProjectilePosition.y);
        }
    }
}
