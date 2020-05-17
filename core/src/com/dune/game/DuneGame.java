package com.dune.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DuneGame extends ApplicationAdapter {

    private SpriteBatch batch;
    private Tank tank;

    @Override
    public void create() {
        batch = new SpriteBatch();
        tank = new Tank(200, 200);
    }

    @Override
    public void render() {
        float dt = Gdx.graphics.getDeltaTime();
        update(dt);
        Gdx.gl.glClearColor(0, 0.4f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        tank.render(batch);
        batch.end();
    }

    public void update(float dt) {
        tank.update(dt);
    }

    @Override
    public void dispose() {
        batch.dispose();
        tank.dispose();
    }

    // Домашнее задание:
    // - Задать координаты точки, и нарисовать в ней круг (любой круг, радиусом пикселей 50)
    // - Если "танк" подъедет вплотную к кругу, то круг должен переместиться в случайную точку
    // - * Нельзя давать танку заезжать за экран
}