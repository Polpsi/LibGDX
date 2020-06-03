package com.dune.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class GameController {
    // Единый список владельцев для всех объектов.
    public enum Owner {
        PLAYER, AI
    }

    private BattleMap map;
    private ProjectilesController projectilesController;
    private TanksController tanksController;
    private Vector2 tmp;
    private Vector2 selectionStart;

    private List<Tank> selectedUnits;

    public TanksController getTanksController() {
        return tanksController;
    }

    public List<Tank> getSelectedUnits() {
        return selectedUnits;
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
        this.tmp = new Vector2();
        this.selectionStart = new Vector2();
        this.selectedUnits = new ArrayList<>();
        this.map = new BattleMap();
        this.projectilesController = new ProjectilesController(this);
        this.tanksController = new TanksController(this);
        for (int i = 0; i < 5; i++) {
            this.tanksController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.PLAYER);
        }
        for (int i = 0; i < 2; i++) {
            this.tanksController.setup(MathUtils.random(80, 1200), MathUtils.random(80, 640), Owner.AI);
        }
        prepareInput();
    }

    public void update(float dt) {
        tanksController.update(dt);
        projectilesController.update(dt);
        map.update(dt);
        checkCollisions(dt);
        // checkSelection();
    }

    public void checkCollisions(float dt) {
        for (int i = 0; i < tanksController.activeSize() - 1; i++) {
            Tank t1 = tanksController.getActiveList().get(i);
            for (int j = i + 1; j < tanksController.activeSize(); j++) {
                Tank t2 = tanksController.getActiveList().get(j);
                float dst = t1.getPosition().dst(t2.getPosition());
                if (dst < 30 + 30) {
                    float colLengthD2 = (60 - dst) / 2;
                    tmp.set(t2.getPosition()).sub(t1.getPosition()).nor().scl(colLengthD2);
                    t2.moveBy(tmp);
                    tmp.scl(-1);
                    t1.moveBy(tmp);
                }
            }
            // Танк уже запрошен, сразу на попадания проверим.
            checkProjectileHit(t1);
        }
        // Последний танк тоже надо проверить на попадание снарядов.
        checkProjectileHit(tanksController.getActiveList().get(tanksController.activeSize() - 1));
    }

    private void checkProjectileHit(Tank tank) {
        for (int j = 0; j < projectilesController.activeSize(); j++) {
            Projectile projectile = projectilesController.getActiveList().get(j);
            float dst = tank.getPosition().dst(projectile.getPosition());
            // Отключаем FriendlyFire. Если вместо ownerType снаряду задать таргет,
            // то пуля будет лететь к цели сквозь другие вражеские юниты.
            if (dst < 30 && projectile.getOwnerType() != tank.getOwnerType()) {
                tank.wasDamaged(projectile.getWeaponEnergy());
                projectile.deactivate();
            }
        }
    }

    public boolean isTankSelected(Tank tank) {
        return selectedUnits.contains(tank);
    }

    public void prepareInput() {
        InputProcessor ip = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    selectionStart.set(screenX, 720 - screenY);
                }
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (button == Input.Buttons.LEFT) {
                    tmp.set(Gdx.input.getX(), 720 - Gdx.input.getY());

                    if (tmp.x < selectionStart.x) {
                        float t = tmp.x;
                        tmp.x = selectionStart.x;
                        selectionStart.x = t;
                    }
                    if (tmp.y > selectionStart.y) {
                        float t = tmp.y;
                        tmp.y = selectionStart.y;
                        selectionStart.y = t;
                    }

                    selectedUnits.clear();
                    if (Math.abs(tmp.x - selectionStart.x) > 20 & Math.abs(tmp.y - selectionStart.y) > 20) {
                        for (int i = 0; i < tanksController.getActiveList().size(); i++) {
                            Tank t = tanksController.getActiveList().get(i);
                            if (t.getOwnerType() == GameController.Owner.PLAYER && t.getPosition().x > selectionStart.x && t.getPosition().x < tmp.x
                                    && t.getPosition().y > tmp.y && t.getPosition().y < selectionStart.y
                            ) {
                                selectedUnits.add(t);
                            }
                        }
                    } else {
                        for (int i = 0; i < tanksController.getActiveList().size(); i++) {
                            Tank t = tanksController.getActiveList().get(i);
                            if (t.getPosition().dst(tmp) < 30.0f) {
                                selectedUnits.add(t);
                            }
                        }
                    }
                }
                return true;
            }
        };
        Gdx.input.setInputProcessor(ip);
    }
}
