package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.shoot.HeroSpreadShoot;
import edu.hitsz.shoot.ShootContext;

import java.util.List;

/**
 * @author Black
 */
public class Bullet extends AbstractProp {

    public Bullet(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void active(List<BaseBullet> heroBullets, AbstractAircraft heroAircraft) {
        ShootContext shootContext = new ShootContext(new HeroSpreadShoot());
        heroBullets.addAll(shootContext.executeStrategy(heroAircraft));
    }
}
