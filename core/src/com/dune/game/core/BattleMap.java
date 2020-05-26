package com.dune.game.core;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class BattleMap {
    private TextureRegion grassTexture;
    private TextureRegion spiceTexture;
    private int[][] fieldResources;

    public BattleMap() {
        this.grassTexture = Assets.getInstance().getAtlas().findRegion("grass");
        this.spiceTexture = Assets.getInstance().getAtlas().findRegion("spice");
        generateResources(16, 9);
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 9; j++) {
                batch.draw(grassTexture, i * 80, j * 80);
                if (fieldResources[i][j] == 1) {
                    // Текстура спайса круглая, лежит поверх травы.
                    batch.draw(spiceTexture, i * 80, j * 80);
                }
            }
        }
    }

    // Мапа знает, есть ли ресурсы в этой точке и какие.
    // Для учета сбора определённому танку - возвращаем тру.
    // Если будет несколько видов ресурсов - то отдавать не тру, а вид ресурса.
    // Для сбора ресурсов нужно заехать центром, а не просто коснуться.
    // Если ресурс собран - он уходит в ноль.
    public boolean grabSpice(Vector2 positionTank) {
        int x = (int)positionTank.x/80;
        int y = (int)positionTank.y/80;
        if (fieldResources[x][y] == 1) {
            fieldResources[x][y] = 0;
            return true;
        }
        return false;
    }

    private void generateResources(int width, int height) {
        // Коэффициент позволяет регулировать процентное соотношение ресурсов к размеру карты.
        // Чем выше ставка, тем больше ресурсов. Двойка - магическое число. :)
        int rate = 12;
        fieldResources = new int[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                fieldResources[i][j] = (MathUtils.random(width * height / rate) < 2) ? 1 : 0;
            }
        }
    }
}
