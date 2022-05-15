package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

public class ShootContext {
    private ShootStrategy shootStrategy;

    public ShootContext(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public void setShootStrategy(ShootStrategy shootStrategy) {
        this.shootStrategy = shootStrategy;
    }

    public List<BaseBullet> executeStrategy(AbstractAircraft aircraft) {
        return shootStrategy.shoot(aircraft);
    }
}
