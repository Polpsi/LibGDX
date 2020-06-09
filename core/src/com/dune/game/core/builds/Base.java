package com.dune.game.core.builds;

import com.dune.game.core.*;
import com.dune.game.core.units.Owner;

public class Base extends AbstractBuild {

    private Owner owner;

    public Base(GameController gc) {
        super(gc);
        this.texture = Assets.getInstance().getAtlas().findRegion("base");
    }

    public void update(float dt) {
    }


    @Override
    public void setup(Owner ownerType, float x, float y) {
        this.position.set(x,y);
        this.owner = ownerType;
    }
}