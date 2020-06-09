package com.dune.game.core.builds;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.dune.game.core.GameController;
import com.dune.game.core.GameObject;
import com.dune.game.core.Targetable;
import com.dune.game.core.units.Owner;
import com.dune.game.core.units.TargetType;

public abstract class AbstractBuild extends GameObject implements Targetable {

    protected TextureRegion texture;

    public AbstractBuild(GameController gc) {
        super(gc);
    }

    public abstract void setup(Owner ownerType, float x, float y);

    @Override
    public TargetType getType() {
        return TargetType.BUILD;
    }

    public void render(SpriteBatch batch) {
        batch.draw(this.texture,position.x-40,position.y-40);
    }
}
