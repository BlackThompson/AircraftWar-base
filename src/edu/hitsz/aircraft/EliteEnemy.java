package edu.hitsz.aircraft;

import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.EnemyBullet;
import edu.hitsz.config.Difficulty;
import edu.hitsz.config.Probability;
import edu.hitsz.prop.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * 精英敌机
 * 可以射击，由电脑操控
 *
 * @author Black
 */

public class EliteEnemy extends AbstractAircraft {

    /** 攻击方式 */

    /**
     * 子弹一次发射数量
     */
    private int shootNum = 1;
    /**
     * 子弹一次发射数量
     */
    private int power = Difficulty.enemyBulletPower;
    /**
     * 子弹射击方向 (向上发射：-1，向下发射：1)
     */
    private int direction = 1;

    private PropFactory propFactory;
    private AbstractProp prop;

    private Random random = new Random();

    /**
     * @param locationX 精英敌机位置x坐标
     * @param locationY 精英敌机位置y坐标
     * @param speedX    精英敌机射出的子弹的基准速度（精英敌机有特定速度）
     * @param speedY    精英敌机射出的子弹的基准速度（精英敌机有特定速度）
     * @param hp        初始生命值
     */
    public EliteEnemy(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    @Override
    public void forward() {
        super.forward();
        // 判定 y 轴向下飞行出界
        if (locationY >= Main.WINDOW_HEIGHT) {
            vanish();
        }
    }

    //@Override
    /**
     * 通过射击产生子弹
     * @return 射击出的子弹List
     */
/*
    public List<BaseBullet> shoot() {
        List<BaseBullet> res = new LinkedList<>();
        int x = this.getLocationX();
        int y = this.getLocationY() + direction * 2;
        int speedX = 0;
        int speedY = this.getSpeedY() + direction * 5;
        BaseBullet baseBullet;
        for (int i = 0; i < shootNum; i++) {
            // 子弹发射位置相对飞机位置向前偏移
            // 多个子弹横向分散
            baseBullet = new EnemyBullet(x + (i * 2 - shootNum + 1) * 10, y, speedX, speedY, power);
            res.add(baseBullet);
        }
        return res;
    }
*/

    /**
     * 产生道具
     *
     * @param abstractProps
     * @param enemyAircraft
     */
    public void propGenerate(List<AbstractProp> abstractProps, AbstractAircraft enemyAircraft) {
        //适当降低降落道具的频率

        double generateFlag = Math.random();

        if (generateFlag < Probability.propProbability) {
            int flag = random.nextInt(3);
            switch (flag) {
                case 0:
                    propFactory = new BloodFactory();
                    prop = propFactory.createProp(enemyAircraft.getLocationX(),
                            enemyAircraft.getLocationY(),
                            0,
                            5);
                    abstractProps.add(prop);
                    break;
                case 1:
                    propFactory = new BombFactory();
                    prop = propFactory.createProp(enemyAircraft.getLocationX(),
                            enemyAircraft.getLocationY(),
                            0,
                            5);
                    abstractProps.add(prop);
                    break;
                case 2:
                    propFactory = new BulletFactory();
                    prop = propFactory.createProp(enemyAircraft.getLocationX(),
                            enemyAircraft.getLocationY(),
                            0,
                            5);
                    abstractProps.add(prop);
                    break;
                default:
                    break;
            }
        }


    }
}

