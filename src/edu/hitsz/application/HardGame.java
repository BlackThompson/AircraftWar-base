package edu.hitsz.application;

import edu.hitsz.config.AircraftHp;
import edu.hitsz.config.Difficulty;
import edu.hitsz.config.GameTimeConfig;
import edu.hitsz.config.Probability;

/**
 * @author Black
 */
public class HardGame extends Game {

    @Override
    public void increaseDifficulty(int time) {
        if (time % GameTimeConfig.difficultyIncreaseTime == 0) {
            if (Difficulty.enemyMaxNumber < 10) {
                Difficulty.enemyMaxNumber++;
                System.out.println("enemyMaxNumber: " + Difficulty.enemyMaxNumber);

            }

            AircraftHp.mobEnemyHp += 10;
            AircraftHp.eliteEnemyHp += 10;

            System.out.println("mob enemy HP: " + AircraftHp.mobEnemyHp);
            System.out.println("elite enemy HP: " + AircraftHp.eliteEnemyHp);

            if (AircraftHp.bossHp <= 400) {
                AircraftHp.bossHp += 20;
                System.out.println("boss hp: " + AircraftHp.bossHp);
            }

            if (Probability.eliteProbability <= 0.5) {
                Probability.eliteProbability += 0.05;
                System.out.println("the proportion of elite enemy: " + Probability.eliteProbability);
            }

            if (Probability.propProbability >= 0.2) {
                Probability.propProbability -= 0.03;
                System.out.println("the probability of prop: " + Probability.propProbability);

            }

            if (Difficulty.bossScoreThreshold >= 150) {
                Difficulty.bossScoreThreshold -= 10;
                System.out.println("the threshold of score to generate boss: " + Difficulty.bossScoreThreshold);
            }
        }
    }
}
