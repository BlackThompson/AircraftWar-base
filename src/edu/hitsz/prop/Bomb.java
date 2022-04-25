package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.Boss;

import java.util.List;

/**
 * @author Black
 */
public class Bomb extends AbstractProp {

    public Bomb(int locationX, int locationY, int speedX, int speedY) {
        super(locationX, locationY, speedX, speedY);
    }

    public void active(List<AbstractAircraft> enemyAircrafts) {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof Boss){
                continue;
            }else{
                enemyAircraft.vanish();
            }
        }
    }
}
