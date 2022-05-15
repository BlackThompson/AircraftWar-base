package edu.hitsz.config;

/**
 * Parameters for different difficulty
 * @author Black
 */
public class Difficulty {
    /**
     * 0 - easy mode
     * 1 - normal mode
     * 2 - hard mode
     */
    public static int difficulty = 0;

    public static int enemyMaxNumber = 5;
    public static int bossScoreThreshold = 200;

    public static boolean hasBossEnemy = true;
    public static int mobEnemyScore = 10;
    public static int eliteEnemyScore = 20;
    public static int bossEnemyScore = 50;

    public static int heroBulletPower = 20;
    public static int enemyBulletPower = 20;
    public static int bossBulletPower = 10;

    public static int bossBulletCount = 5;

    public static int bulletPropEffectTime = 10000;
    //public static int bulletPropEffectLevel = 2;

}
