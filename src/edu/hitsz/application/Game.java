package edu.hitsz.application;

import edu.hitsz.aircraft.*;
import edu.hitsz.basic.AbstractFlyingObject;
import edu.hitsz.bullet.BaseBullet;
import edu.hitsz.config.*;
import edu.hitsz.gui.FrameChange;
import edu.hitsz.music.PlaySound;
import edu.hitsz.prop.*;
import edu.hitsz.rank.Player;
import edu.hitsz.rank.RankDao;
import edu.hitsz.rank.RankDaoImpl;
import edu.hitsz.shoot.*;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 游戏主面板，游戏启动
 *
 * @author hitsz
 */
public abstract class Game extends JPanel {

    private int backGroundTop = 0;

    /**
     * Scheduled 线程池，用于任务调度
     */
    private final ScheduledExecutorService executorService;

    private boolean bossExist = false;
    private int bossFlag = 0;

    private final HeroAircraft heroAircraft;
    private final List<AbstractAircraft> enemyAircrafts;
    private final List<BaseBullet> heroBullets;
    private final List<BaseBullet> enemyBullets;
    private final List<AbstractProp> abstractProps;


    private boolean gameOverFlag = false;
    public boolean bulletPropFlag = false;

    private int score = 0;
    private int time = 0;

    private int cycleTime = 0;

    //实例化Random类以产生随机数，用于生成道具
    private Random random = new Random();

    //工厂模式，用于生成道具和敌机
//    private PropFactory propFactory;
//    private AbstractProp prop;
    private AircraftFactory aircraftFactory;
    private AbstractAircraft enemyAircraft;
    private static BombObserver bombObserver = new BombObserver();


    public Game() {

        PlaySound.startPlayBGM();
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

            time += GameTimeConfig.timeInterval;

            // 难度增加
            increaseDifficulty(time);

            // 周期性执行（控制频率）
            if (timeCountAndNewCycleJudge()) {
                System.out.println(time);

                // 新敌机产生
                generateNewEnemy();

                // Boss机产生
                if (Difficulty.hasBossEnemy == true) {
                    generateNewBoss();
                }

                // 飞机射出子弹
                shootAction();
            }

            // 子弹移动
            bulletsMoveAction();

            // 飞机移动
            aircraftsMoveAction();

            // 撞击检测
            crashCheckAction();

            // 道具移动
            propMoveAction();

            // 后处理
            postProcessAction();

            // 每个时刻重绘界面
            repaint();


            // 游戏结束检查
            if (heroAircraft.getHp() <= 0) {
                // 游戏结束
                executorService.shutdown();
                gameOverFlag = true;
                System.out.println("Game Over!");
                gameOver();

                //printRank();
            }

        };

        /**
         * 以固定延迟时间进行执行
         * 本次任务执行完成后，需要延迟设定的延迟时间，才会执行新的任务
         */
        executorService.scheduleWithFixedDelay(task, GameTimeConfig.timeInterval, GameTimeConfig.timeInterval, TimeUnit.MILLISECONDS);

    }

    //***********************
    //      Action 各部分
    //***********************

    private void generateNewEnemy() {

        double flag = Math.random();

        if (enemyAircrafts.size() < Difficulty.enemyMaxNumber) {

            if (flag < Probability.eliteProbability) {
                aircraftFactory = new EliteFactory();
                enemyAircraft = aircraftFactory.createAircraft(
                        (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.ELITE_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                        EnemySpeed.enemySpeedX,
                        EnemySpeed.enemySpeedY,
                        AircraftHp.eliteEnemyHp);
                enemyAircrafts.add(enemyAircraft);
                bombObserver.addEnemyAircraft(enemyAircraft);

            } else {
                aircraftFactory = new MobFactory();
                enemyAircraft = aircraftFactory.createAircraft(
                        (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.MOB_ENEMY_IMAGE.getWidth())) * 1,
                        (int) (Math.random() * Main.WINDOW_HEIGHT * 0.2) * 1,
                        0,
                        EnemySpeed.enemySpeedY,
                        AircraftHp.mobEnemyHp);
                enemyAircrafts.add(enemyAircraft);
                bombObserver.addEnemyAircraft(enemyAircraft);
            }
        }
    }

    private void generateNewBoss() {

        bossExist = false;

        if (bossFlag != (score / Difficulty.bossScoreThreshold)) {
            for (AbstractAircraft aircraft : enemyAircrafts) {
                if (aircraft instanceof Boss) {
                    bossExist = true;
                    break;
                }
            }
        }

        if (bossFlag == 0 && score > Difficulty.bossScoreThreshold) {
            aircraftFactory = new BossFactory();
            AbstractAircraft boss = aircraftFactory.createAircraft(
                    (int) (Math.random() * (Main.WINDOW_WIDTH) * 1),
                    (int) (Main.WINDOW_WIDTH * 0.2),
                    EnemySpeed.enemySpeedX,
                    0,
                    AircraftHp.bossHp
            );
            enemyAircrafts.add(boss);
            bossFlag = score / Difficulty.bossScoreThreshold;
        } else if (bossExist == false && (bossFlag != (score / Difficulty.bossScoreThreshold))) {
            aircraftFactory = new BossFactory();
            AbstractAircraft boss = aircraftFactory.createAircraft(
                    (int) (Math.random() * (Main.WINDOW_WIDTH - ImageManager.BOSS_IMAGE.getWidth())) * 1,
                    (int) (Main.WINDOW_WIDTH * 0.2),
                    EnemySpeed.enemySpeedX,
                    0,
                    AircraftHp.bossHp);
            enemyAircrafts.add(boss);
            bossFlag = score / Difficulty.bossScoreThreshold;
        }
    }

    private boolean timeCountAndNewCycleJudge() {
        cycleTime += GameTimeConfig.timeInterval;
        if (cycleTime >= GameTimeConfig.cycleDuration && cycleTime - GameTimeConfig.timeInterval < cycleTime) {
            // 跨越到新的周期
            cycleTime %= GameTimeConfig.cycleDuration;
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
            PlaySound.playBulletSound();
            shootContext.setShootStrategy(new HeroDirectShoot());
            //shootContext.setShootStrategy(new HeroSpreadShoot());
            heroBullets.addAll(shootContext.executeStrategy(heroAircraft));
        } else {
            PlaySound.playBulletSound();
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
            bombObserver.addEnemyBullet(bullet);
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
                PlaySound.playBulletHitSound();
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
                            score += Difficulty.eliteEnemyScore;
                        } else if (enemyAircraft instanceof MobEnemy) {
                            score += Difficulty.mobEnemyScore;
                        } else if (enemyAircraft instanceof Boss) {
                            ((Boss) enemyAircraft).propGenerate(abstractProps, enemyAircraft);
                            PlaySound.stopPlayBossBGM();
                            score += Difficulty.bossEnemyScore;
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

                    PlaySound.playGetSupplySound();
                    ((Blood) abstractProp).active(heroAircraft);
                    abstractProp.vanish();

                } else if (abstractProp instanceof Bomb) {

                    System.out.println("BombSupply active!");
                    PlaySound.playBombExplosionSound();
                   /* for (AbstractAircraft aircraft : enemyAircrafts) {
                        if (aircraft instanceof EliteEnemy) {
                            score += Difficulty.eliteEnemyScore;
                        } else if (aircraft instanceof MobEnemy) {
                            score += Difficulty.mobEnemyScore;
                        } else {
                            continue;
                        }
                    }
                    ((Bomb) abstractProp).active(enemyAircrafts, enemyBullets);*/
                    score += bombObserver.vanishAll();
                    abstractProp.vanish();


                } else if (abstractProp instanceof Bullet) {

                    PlaySound.playGetSupplySound();
                    System.out.println("FireSupply active!");

                    Runnable r = () -> {
                        try {
                            bulletPropFlag = true;
                            Thread.sleep(Difficulty.bulletPropEffectTime);
                            bulletPropFlag = false;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    };

                    new Thread(r, "BulletPropTimer").start();

                    abstractProp.vanish();
                }
            }
        }

        //heroAircraft.PropTakeEffect(abstractProps, heroAircraft, heroBullets, enemyAircrafts, bulletPropFlag);
    }

    public abstract void increaseDifficulty(int time);

    public void gameOver() {
        // stop all BGMs
        PlaySound.stopPlayBGM();
        PlaySound.stopPlayBossBGM();

        // play gameover sound
        PlaySound.playGameOverSound();

        // ask for name
        String name = JOptionPane.showInputDialog(null, "Game over!\nYour score is " + score + ".\nPlease input your name." +
                "\nLeave empty or choose [Cancel] will not record your score.");

        // load ranking
        RankDao rankDao = new RankDaoImpl();

        try {
            rankDao.playerRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // record the score
        if (name != null && name.compareTo("") != 0) {
            rankDao.addNewPlayer(new Player(
                    Difficulty.difficulty,
                    name,
                    score,
                    new Date(),
                    UUID.randomUUID()
            ));

        }

        try {
            rankDao.playerWrite(rankDao.getAllPlayers());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // close the game frame and load gameover frame
        FrameChange.closeGaming();
        FrameChange.showGameOver(rankDao);
    }

    /*public void printRank() {

        try {
            rankDao.playerRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        rankDao.addNewPlayer(PlayerCreate.create(score));

        Collections.sort(rankDao.getAllPlayers());
        rankDao.printRank(rankDao);

        try {
            rankDao.playerWrite(rankDao.getAllPlayers());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

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
        g.drawImage(Media.backgroundImage, 0, this.backGroundTop - Main.WINDOW_HEIGHT, null);
        g.drawImage(Media.backgroundImage, 0, this.backGroundTop, null);
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
