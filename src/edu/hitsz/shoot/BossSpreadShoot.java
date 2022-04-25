package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;

import java.util.LinkedList;
import java.util.List;

public class BossSpreadShoot implements ShootStrategy {
    /**
     * 子弹一次发射数量
     */
    private int shootNum = 5;
    /**
     * 子弹一次发射数量
     */
    private int power = 10;
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;


    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedY = aircraft.getSpeedY() + direction * 4;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y,
                    shootNum == 1 ? 0 : (i * 2 - 3), speedY, power);
            res.add(baseBullet);
        }
        return res;
    }
}
