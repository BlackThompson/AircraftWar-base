package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.prop.AbstractProp;
import edu.hitsz.prop.Blood;
import edu.hitsz.prop.Bomb;
import edu.hitsz.prop.Bullet;
import edu.hitsz.shoot.*;
import rank.PlayerCreate;
import rank.PlayerDao;
import rank.PlayerDaoImpl;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    /**
     * 时间间隔(ms)，控制刷新频率
     */
    private int timeInterval = 40;

    private boolean bossExist = false;

    private int bossFlag = 0;
    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> abstractProps;

    //private AbstractAircraft boss = null;

    private int enemyMaxNumber = 5;

    private boolean gameOverFlag = false;

    public boolean bulletPropFlag = false;
    private int score = 0;
    private int time = 0;

    private int bossScoreThreshold = 200;
    /**
     * 周期（ms)
     * 指示子弹的发射、敌机的产生频率
     */
    private int cycleDuration = 600;
    private int cycleTime = 0;

    //实例化Random类以产生随机数，用于生成道具
    private Random random = new Random();

    //工厂模式，用于生成道具和敌机
//    private PropFactory propFactory;
//    private AbstractProp prop;
    private AircraftFactory aircraftFactory;
    private AbstractAircraft enemyAircraft;

    //数据访问对象模式
    PlayerDao playerDao = new PlayerDaoImpl();

    public Game() {
        //单例模式构造heroAircraft
        heroAircraft = HeroAircraft.getHeroInstance();

        enemyAircrafts = new LinkedList<>();
        heroBullets = new LinkedList<>();
        enemyBullets = new LinkedList<>();
        abstractProps = new LinkedList<>();

        //Scheduled 线程池，用于定时任务调度
        executorService = new ScheduledThreadPoolExecutor(1);

        //启动英雄机鼠标监听
        new HeroController(this, heroAircraft);

    }

    /**
     * 游戏启动入口，执行游戏逻辑
     */
    public void action() {

        // 定时任务：绘制、对象产生、碰撞判定、击毁及结束判定
        Runnable task = () -> {

            time += timeInterval;

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

                // 新敌机产生
                generateNewEnemy();

                // Boss机产生
                generateNewBoss();

                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            //道具移动
            propMoveAction();

            //后处理
            postProcessAction();

            //每个时刻重绘界面
            repaint();

            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                printRank();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, timeInterval, timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void generateNewEnemy() {

        int speedX;
        int flag = random.nextInt(2);

        if (flag == 0) {
            speedX = -3;
        } else {
            speedX = 3;
        }

        if (enemyAircrafts.size() < enemyMaxNumber) {
            //这个也是对的，用random实例化方便一点
            //int flag = (int)( Math.random()*10 ) % 2;
            flag = random.nextInt(4);
            if (flag == 0) {

                aircraftFactory = new EliteFactory();
                enemyAircraft = aircraftFactory.createAircraft(
                        (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                        speedX,
                        8,
                        30);
                enemyAircrafts.add(enemyAircraft);

            } else {
                aircraftFactory = new MobFactory();
                enemyAircraft = aircraftFactory.createAircraft(
                        (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                        0,
                        8,
                        30);
                enemyAircrafts.add(enemyAircraft);
            }
        }
    }

    private void generateNewBoss() {

        int speedX;
        int flag = random.nextInt(2);

        if (flag == 0) {
            speedX = -3;
        } else {
            speedX = 3;
        }

        bossExist = false;

        if (bossFlag != (score / bossScoreThreshold)) {
            for (AbstractAircraft aircraft : enemyAircrafts) {
                if (aircraft instanceof Boss) {
                    bossExist = true;
                    break;
                }
            }
        }

        if (bossFlag == 0 && score > bossScoreThreshold) {
            aircraftFactory = new BossFactory();
            AbstractAircraft boss = aircraftFactory.createAircraft(
                    (int) (Math.random() * (Main.WINDOW_WIDTH) * 1),
                    (int) (Main.WINDOW_WIDTH * 0.2),
                    speedX,
                    0,
                    150);
            enemyAircrafts.add(boss);
            bossFlag = score / bossScoreThreshold;
        } else if (bossExist == false && (bossFlag != (score / bossScoreThreshold))) {
            aircraftFactory = new BossFactory();
            AbstractAircraft boss = aircraftFactory.createAircraft(
                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_IMAGE.getWidth())) * 1,
                    (int) (Main.WINDOW_WIDTH * 0.2),
                    speedX,
                    0,
                    150);
            enemyAircrafts.add(boss);
            bossFlag = score / bossScoreThreshold;
        }
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += timeInterval;
        if (cycleTime >= cycleDuration && cycleTime - timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= cycleDuration;
            return true;
        } else {
            return false;
        }
    }

    private void shootAction() {

        ShootContext shootContext = new ShootContext(new EnemyDirectShoot());

        // 精英敌机射击 (finished）
        // 两个都可以运行，下面一个是增强for循环，简便一些，快捷键：iter
        /*for (int i = 0; i < enemyAircrafts.size(); i++) {
            if(enemyAircrafts.get(i) instanceof EliteEnemy){
                enemyBullets.addAll(enemyAircrafts.get(i).shoot());
            }
        }*/
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof EliteEnemy) {
                enemyBullets.addAll(shootContext.executeStrategy(enemyAircraft));
                //enemyBullets.addAll(enemyAircraft.shoot());
            }
        }

        // 英雄射击

        if (bulletPropFlag == false) {
            shootContext.setShootStrategy(new HeroDirectShoot());
            //shootContext.setShootStrategy(new HeroSpreadShoot());
            heroBullets.addAll(shootContext.executeStrategy(heroAircraft));
        } else {
            shootContext.setShootStrategy(new HeroSpreadShoot());
            heroBullets.addAll(shootContext.executeStrategy(heroAircraft));
        }


        //Boss射击
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            if (enemyAircraft instanceof Boss) {
                shootContext.setShootStrategy(new BossSpreadShoot());
                enemyBullets.addAll(shootContext.executeStrategy(enemyAircraft));
            }
        }
    }


    private void bulletsMoveAction() {
        for (BaseBullet bullet : heroBullets) {
            bullet.forward();
        }
        for (BaseBullet bullet : enemyBullets) {
            bullet.forward();
        }
    }

    private void aircraftsMoveAction() {
        for (AbstractAircraft enemyAircraft : enemyAircrafts) {
            enemyAircraft.forward();
        }
    }

    private void propMoveAction() {
        for (AbstractProp abstractProp : abstractProps) {
            abstractProp.forward();
        }
    }


    /**
     * 碰撞检测：
     * 1. 敌机攻击英雄
     * 2. 英雄攻击/撞击敌机
     * 3. 英雄获得补给
     */
    private void crashCheckAction() {
        // 1. 敌机子弹攻击英雄 （finished）
        for (BaseBullet bullet : enemyBullets) {
            if (bullet.notValid()) {
                continue;
            }
            if (heroAircraft.crash(bullet)) {
                // 英雄机撞到敌机子弹
                // 英雄机损失一定生命值
                heroAircraft.decreaseHp(bullet.getPower());
                bullet.vanish();
            }
        }

        // 2. 英雄子弹攻击敌机
        for (BaseBullet bullet : heroBullets) {
            if (bullet.notValid()) {
                continue;
            }
            for (AbstractAircraft enemyAircraft : enemyAircrafts) {
                if (enemyAircraft.notValid()) {
                    // 已被其他子弹击毁的敌机，不再检测
                    // 避免多个子弹重复击毁同一敌机的判定
                    continue;
                }
                if (enemyAircraft.crash(bullet)) {
                    // 敌机撞击到英雄机子弹
                    // 敌机损失一定生命值
                    enemyAircraft.decreaseHp(bullet.getPower());
                    bullet.vanish();
                    if (enemyAircraft.notValid()) {
                        //if (true) {
                        // 获得分数，产生道具补给 (finished)
                        if (enemyAircraft instanceof EliteEnemy) {
                            ((EliteEnemy) enemyAircraft).propGenerate(abstractProps, enemyAircraft);
                            //精英机额外加10分
                            score += 20;
                        } else if (enemyAircraft instanceof MobEnemy) {
                            score += 10;
                        } else if (enemyAircraft instanceof Boss) {
                            ((Boss) enemyAircraft).propGenerate(abstractProps, enemyAircraft);
                            score += 50;
                        }
                    }
                }
                // 英雄机 与 敌机 相撞，均损毁
                if (enemyAircraft.crash(heroAircraft) || heroAircraft.crash(enemyAircraft)) {
                    enemyAircraft.vanish();
                    heroAircraft.decreaseHp(Integer.MAX_VALUE);
                }
            }
        }

        // 3. 我方获得道具，道具生效 (finished)
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
                    ((Bomb) abstractProp).active(enemyAircrafts);
                    abstractProp.vanish();

                } else if (abstractProp instanceof Bullet) {

                    System.out.println("FireSupply active!");
                    bulletPropFlag = true;
                    abstractProp.vanish();
                }
            }
        }

        //heroAircraft.PropTakeEffect(abstractProps, heroAircraft, heroBullets, enemyAircrafts, bulletPropFlag);
    }

    public void printRank() {

        try {
            playerDao.playerRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playerDao.addNewPlayer(PlayerCreate.create(score));

        Collections.sort(playerDao.getAllPlayers());
        playerDao.printRank(playerDao);

        try {
            playerDao.playerWrite(playerDao.getAllPlayers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 后处理：
     * 1. 删除无效的子弹
     * 2. 删除无效的敌机
     * 3. 检查英雄机生存
     * <p>
     * 无效的原因可能是撞击或者飞出边界
     */
    private void postProcessAction() {
        enemyBullets.removeIf(AbstractFlyingObject::notValid);
        heroBullets.removeIf(AbstractFlyingObject::notValid);
        enemyAircrafts.removeIf(AbstractFlyingObject::notValid);
        abstractProps.removeIf(AbstractFlyingObject::notValid);
    }


    //***********************
    //      Paint 各部分
    //***********************

    /**
     * 重写paint方法
     * 通过重复调用paint方法，实现游戏动画
     *
     * @param g
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // 绘制背景,图片滚动
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(ImageManager.BACKGROUND_IMAGE, 0, this.backGroundTop, null);
        this.backGroundTop += 1;
        if (this.backGroundTop == Main.WINDOW_HEIGHT) {
            this.backGroundTop = 0;
        }

        // 先绘制子弹，后绘制飞机
        // 这样子弹显示在飞机的下层
        paintImageWithPositionRevised(g, abstractProps);
        paintImageWithPositionRevised(g, enemyBullets);
        paintImageWithPositionRevised(g, heroBullets);

        paintImageWithPositionRevised(g, enemyAircrafts);

        g.drawImage(ImageManager.HERO_IMAGE, heroAircraft.getLocationX() - ImageManager.HERO_IMAGE.getWidth() / 2,
                heroAircraft.getLocationY() - ImageManager.HERO_IMAGE.getHeight() / 2, null);

        //绘制得分和生命值
        paintScoreAndLife(g);

    }

    private void paintImageWithPositionRevised(Graphics g, List<? extends AbstractFlyingObject> objects) {
        if (objects.size() == 0) {
            return;
        }

        for (AbstractFlyingObject object : objects) {
            BufferedImage image = object.getImage();
            assert image != null : objects.getClass().getName() + " has no image! ";
            g.drawImage(image, object.getLocationX() - image.getWidth() / 2,
                    object.getLocationY() - image.getHeight() / 2, null);
        }
    }

    private void paintScoreAndLife(Graphics g) {
        int x = 10;
        int y = 25;
        g.setColor(new Color(16711680));
        g.setFont(new Font("SansSerif", Font.BOLD, 22));
        g.drawString("SCORE:" + this.score, x, y);
        y = y + 20;
        g.drawString("LIFE:" + this.heroAircraft.getHp(), x, y);
    }


}
