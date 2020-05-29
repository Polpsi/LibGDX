package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class GameController {
    private BattleMap map;
    private ProjectilesController projectilesController;
    private TanksController tanksController;

    public TanksController getTanksController() {
        return tanksController;
    }

    public ProjectilesController getProjectilesController() {
        return projectilesController;
    }

    public BattleMap getMap() {
        return map;
    }

    // Инициализация игровой логики
    public GameController() {
        Assets.getInstance().loadAssets();
        this.map = new BattleMap();
        this.projectilesController = new ProjectilesController(this);
        this.tanksController = new TanksController(this);
        this.tanksController.setup(200, 200, Tank.Owner.PLAYER);
        this.tanksController.setup(400, 400, Tank.Owner.PLAYER);
    }

    public void update(float dt) {
        tanksController.update(dt);
        projectilesController.update(dt);
        map.update(dt);
        checkCollisions(dt);
    }

    public void checkCollisions(float dt) {
        //сравнивать два конкретных танка - просто, но не для игры...
        // А еще и снаряды могут быть. И здания.
        //алгоритм я придумал, но реализовать к сдаче не успеваю.
        // На каждый тайл карты получаем объекты в этом тайле и по соседству(вдруг объект больше тайла).
        // x-тайл плюс-минус 1, y-тайл - то же.
        // Их проверяем на столкновение. Потом следующий тайл.
    }
}
