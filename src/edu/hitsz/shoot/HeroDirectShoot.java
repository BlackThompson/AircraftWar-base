package edu.hitsz.shoot;

import edu.hitsz.aircraft.AbstractAircraft;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.config.Difficulty;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Black
 */
public class HeroDirectShoot implements ShootStrategy {

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;
    /**
     * 子弹一次发射数量
     */
    private int power = Difficulty.heroBulletPower;
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = -1;


    @Override
    public List<BaseBullet> shoot(AbstractAircraft aircraft) {
        List<BaseBullet> res = new LinkedList<>();
        int x = aircraft.getLocationX();
        int y = aircraft.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = aircraft.getSpeedY() + direction * 5;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new HeroBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(baseBullet);
        }
        return res;
    }

}

