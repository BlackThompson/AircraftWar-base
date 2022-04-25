package edu.hitsz.aircraft;

import edu.hitsz.application.ImageManager;
import edu.hitsz.application.Main;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.bullet.HeroBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Blood;
import edu.hitsz.prop.Bomb;
import edu.hitsz.prop.Bullet;

import java.util.LinkedList;
import java.util.List;

/**
 * 英雄飞机，游戏玩家操控
 *
 * @author hitsz
 */
public class HeroAircraft extends AbstractAircraft {

    /**
     * 攻击方式
     */
    private int shootNum = 1;     //子弹一次发射数量
    private int power = 30;       //子弹伤害
    private int direction = -1;  //子弹射击方向 (向上发射：-1，向下发射：1)

    /**
     * 单例模式：懒汉式
     * 因为构造函数为有参构造，故不可以使用饿汉式（笔记）
     */
    private static HeroAircraft heroInstance = null;

    /**
     * @param locationX 英雄机位置x坐标
     * @param locationY 英雄机位置y坐标
     * @param speedX    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param speedY    英雄机射出的子弹的基准速度（英雄机无特定速度）
     * @param hp        初始生命值
     */
    private HeroAircraft(int locationX, int locationY, int speedX, int speedY, int hp) {
        super(locationX, locationY, speedX, speedY, hp);
    }

    public static synchronized HeroAircraft getHeroInstance() {
        int locationX = Main.WINDOW_WIDTH / 2, locationY = Main.WINDOW_HEIGHT - ImageManager.HERO_IMAGE.getHeight(),
                speedX = 0, speedY = 0, hp = 100;
        if (heroInstance == null) {
            heroInstance = new HeroAircraft(locationX, locationY, speedX, speedY, hp);
        }
        return heroInstance;
    }

    @Override
    public void forward() {
        // 英雄机由鼠标控制，不通过forward函数移动
    }


   /* *//**
     * 道具生效
     *
     * @param abstractProps
     * @param heroAircraft
     *//*
    public void PropTakeEffect(List<AbstractProp> abstractProps, HeroAircraft heroAircraft,
                               List<BaseBullet> herobullets, List<AbstractAircraft> enemyAircrafts,
                               boolean bulletPropFlag) {
        for (AbstractProp abstractProp : abstractProps) {
            if (abstractProp.notValid()) {
                continue;
            }
            if (heroAircraft.crash(abstractProp) || abstractProp.crash(heroAircraft)) {
                if (abstractProp instanceof Blood) {

                    ((Blood) abstractProp).active(heroAircraft);
                    abstractProp.vanish();

                } else if (abstractProp instanceof Bomb) {

                    System.out.println("BombSupply active!");
                    abstractProp.vanish();

                } else if (abstractProp instanceof Bullet) {

                    System.out.println("FireSupply active!");
                    ((Bullet) abstractProp).active(herobullets, heroAircraft);
                    bulletPropFlag = true;
                    abstractProp.vanish();
                }
            }
        }
    }*/
}
