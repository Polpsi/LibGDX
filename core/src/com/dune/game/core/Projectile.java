package com.dune.game.core;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    private Vector2 position;
    private Vector2 velocity = new Vector2(0,0);
    private boolean isActive;

    // Снаряд новый - нужен конструктор.
    // И сразу в сетуп.
    Projectile(Vector2 startPosition, float angle) {
        setup(startPosition,angle);
    }

    // Новый выстрел, значит снова снаряд в строю, с надписью "МЫ ПОБЕДИМ!"
    // isActive = true;
    public void setup(Vector2 startPosition, float angle) {
        this.isActive=true;
        this.position=startPosition;
        velocity.set(400.0f * MathUtils.cosDeg(angle), 400.0f * MathUtils.sinDeg(angle));
    }

    public void update(float dt) {
        // position.x += velocity.x * dt;
        // position.y += velocity.y * dt;
        checkBounds();
        if (isActive) {
            position.mulAdd(velocity, dt);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    // Вышли за пределы - амба, никого не убиваем. Будет телепорт обратно в орудие при следующем выстреле.
    //  isActive = false;
    public void checkBounds() {
        if ((position.x < 0) || (position.y < 0) || (position.x > 1280) || (position.y > 720)) {
            isActive = false;
        }
    }

    public boolean isActive() {
        return isActive;
    }
}
