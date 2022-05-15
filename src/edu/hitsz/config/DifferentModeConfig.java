package edu.hitsz.config;

import edu.hitsz.application.ImageManager;

/**
 * @author Black
 */
public class DifferentModeConfig {
    public static void easyMode(){
        Difficulty.difficulty = 0;
        EnemySpeed.enemySpeedY = 5;
        Difficulty.hasBossEnemy = false;
        Difficulty.enemyMaxNumber = 4;
        Media.backgroundImage = (Math.random() > 0.5) ?
                ImageManager.BACKGROUND_IMAGE :
                ImageManager.BACKGROUND_IMAGE_2;
    }

    public static void normalMode(){
        Difficulty.difficulty = 1;
        EnemySpeed.enemySpeedY = 7;
        Difficulty.bossScoreThreshold = 300;
        Media.backgroundImage = (Math.random() > 0.5) ?
                ImageManager.BACKGROUND_IMAGE_3 :
                ImageManager.BACKGROUND_IMAGE_4;
    }

    public static void hardMode(){
        Difficulty.bossScoreThreshold = 200;
        EnemySpeed.enemySpeedY = 8;
        Difficulty.difficulty = 2;
        Media.backgroundImage = ImageManager.BACKGROUND_IMAGE_5;
    }
}
