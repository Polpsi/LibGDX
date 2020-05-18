package com.dune.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class RunningCircle {
    private final int RADIUS = 50;
    private Vector2 fieldSize;
    private Vector2 pos;
    Texture texture;

    RunningCircle(int x, int y, Vector2 fieldSize) {
        this.pos = new Vector2(x, y);
        this.fieldSize = fieldSize;
        this.texture = new Texture("circle.png");
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, pos.x - RADIUS, pos.y - RADIUS);
    }

    public void update(Vector2 enemyPos, Vector2 enemySize) {
        // Если расстояние между обоими соответствующими координатами центров объектов
        // равно или меньше половины суммы их размеров,
        // значит они уже условно совсем вплотную(если не учитывать конфигурацию рисунка на текстуре).

        float deltaX = Math.abs(pos.x - enemyPos.x);
        float deltaY = Math.abs(pos.y - enemyPos.y);

        // Если через if, то круг мог периодически появляться под танком
        // Через while сразу и проверяется, что если новые координаты в пределах досягаемости танка, то пробуем новые.
        while (deltaX <= (RADIUS + enemySize.x / 2) && deltaY <= (RADIUS + enemySize.y / 2)) {
            pos.x = (float) (Math.random() * (fieldSize.x - RADIUS * 2f) + RADIUS);
            pos.y = (float) (Math.random() * (fieldSize.y - RADIUS * 2f) + RADIUS);
            deltaX = Math.abs(pos.x - enemyPos.x);
            deltaY = Math.abs(pos.y - enemyPos.y);
        }
    }

    public void dispose() {
        texture.dispose();
    }
}