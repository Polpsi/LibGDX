package com.dune.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

class Tank {
    private Vector2 position;
    private Vector2 fieldSize;
    private Texture texture;
    private float angle;
    private float speed;


    public Tank(float x, float y, Vector2 fieldSize) {
        this.position = new Vector2(x, y);
        this.fieldSize = fieldSize;
        this.texture = new Texture("tank.png");
        this.speed = 200.0f;
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            angle += 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            angle -= 180.0f * dt;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            move(speed,angle,dt);
        }
        // Не мог не сделать движение назад, но в два раза медленее.
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            move(speed/2,angle+180, dt);
            }

    }

    private void move(float speed, float angle, float dt) {
        float newX = position.x + speed * MathUtils.cosDeg(angle) * dt;
        float newY = position.y + speed * MathUtils.sinDeg(angle) * dt;
        // Если достигли края поля, то координаты останутся прежними, то есть - упёрлись.
        if ((newX > texture.getWidth() / 2f) && (newX < fieldSize.x - texture.getWidth() / 2f) &&
                (newY > texture.getHeight() / 2f) && (newY < fieldSize.y - texture.getHeight() / 2f)) {
            position.x = newX;
            position.y = newY;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 40, position.y - 40, 40, 40, 80, 80, 1, 1, angle, 0, 0, 80, 80, false, false);
    }

    public void dispose() {
        texture.dispose();
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getSize() {
        return new Vector2(texture.getWidth(), texture.getHeight());
    }
}