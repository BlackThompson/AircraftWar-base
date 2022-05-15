package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.Boss;
import edu.hitsz.bullet.BaseBullet;

import java.util.List;

/**
 * @author Black
 */
public class Bomb extends AbstractProp {

    public Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void active(List<AbstractAircraft> enemyAircrafts, List<BaseBullet> enemyBullets) {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof Boss) {
                continue;
            } else {
                enemyAircraft.vanish();
            }
        }

        for (BaseBullet enemyBullet : enemyBullets) {
            enemyBullet.vanish();
        }
    }
}
