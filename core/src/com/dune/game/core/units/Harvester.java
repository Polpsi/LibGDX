package com.dune.game.core.units;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.dune.game.core.*;

public class Harvester extends AbstractUnit {
    private Vector2 lastResource = new Vector2();
    private float speedUnload = 10;
    private float timer;

    public Harvester(GameController gc) {
        super(gc);
        this.textures = Assets.getInstance().getAtlas().findRegion("tankcore").split(64, 64)[0];
        this.weaponTexture = Assets.getInstance().getAtlas().findRegion("harvester");
        this.containerCapacity = 3;
        this.minDstToActiveTarget = 5.0f;
        this.speed = 120.0f;
        this.weapon = new Weapon(4.0f, 1);
        this.hpMax = 500;
        this.unitType = UnitType.HARVESTER;
    }

    @Override
    public void setup(Owner ownerType, float x, float y) {
        this.position.set(x, y);
        this.ownerType = ownerType;
        this.hp = this.hpMax;
        this.destination = new Vector2(position);
    }

    public void updateWeapon(float dt) {
        if (gc.getMap().getResourceCount(position) > 0) {
            int result = weapon.use(dt);
            if (result > -1) {
                container += gc.getMap().harvestResource(position, result);
                if (container >= containerCapacity) {
                    container = containerCapacity;
                    if (this.ownerType==Owner.PLAYER) {
                        lastResource.set(position);
                        destination.set(gc.getPlayerBase().getPosition());
                    }
                }
            }
        } else if ((this.ownerType==Owner.PLAYER) && position.dst(gc.getPlayerBase().getPosition())< 30) {
            timer +=dt;
            if (timer >= speedUnload) {
                gc.playerGetResources(container);
                container=0;
                timer=0;
                if (lastResource.x>0) {
                    destination.set(lastResource);
                    lastResource.set(0,0);
                }
            }
        } else {
            weapon.reset();
        }
    }

    @Override
    public void commandAttack(Targetable target) {
        commandMoveTo(target.getPosition());
    }

    @Override
    public void renderGui(SpriteBatch batch) {
        super.renderGui(batch);
        if (weapon.getUsageTimePercentage() > 0.0f) {
            batch.setColor(0.2f, 0.2f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 32, position.y + 22, 64, 8);
            batch.setColor(1.0f, 1.0f, 0.0f, 1.0f);
            batch.draw(progressbarTexture, position.x - 30, position.y + 24, 60 * weapon.getUsageTimePercentage(), 4);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
