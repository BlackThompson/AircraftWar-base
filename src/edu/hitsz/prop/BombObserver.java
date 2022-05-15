package edu.hitsz.prop;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.aircraft.EliteEnemy;
import edu.hitsz.aircraft.MobEnemy;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.config.Difficulty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Black
 */
public class BombObserver {

    private int bombScore = 0;

    /**
     * observer list
     */
    private List<AbstractAircraft> aircraftList = new ArrayList<>();
    private List<BaseBullet> bulletList = new ArrayList<>();

    /**
     * add observer
     */
    public void addEnemyAircraft(AbstractAircraft aircraft) {
        aircraftList.add(aircraft);
    }

    public void addEnemyBullet(BaseBullet bullet) {
        bulletList.add(bullet);
    }

    /**
     * notify all observer
     */
    public int vanishAll() {
        bombScore = 0;
        for (AbstractAircraft aircraft : aircraftList) {
            if (!aircraft.notValid()) {
                if (aircraft instanceof EliteEnemy) {
                    bombScore += Difficulty.eliteEnemyScore;
                } else if (aircraft instanceof MobEnemy) {
                    bombScore += Difficulty.mobEnemyScore;
                } else {
                    continue;
                }
                aircraft.vanish();
            }
        }

        for (BaseBullet bullet : bulletList) {
            bullet.vanish();
        }

        return bombScore;
    }


}
