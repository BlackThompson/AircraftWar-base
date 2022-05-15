package edu.hitsz.gui;

import edu.hitsz.application.EasyGame;
import edu.hitsz.application.HardGame;
import edu.hitsz.application.Main;
import edu.hitsz.application.NormalGame;
import edu.hitsz.config.Difficulty;
import edu.hitsz.rank.RankDao;

import javax.swing.*;
import java.awt.*;

/**
 * @author Black
 */
public class FrameChange {

    private static JFrame frame;
    // Get resolution and initialise Frame
    private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    public static void showGameStart() {

        frame = new JFrame("GameStart");

        // Set the window on the centre of the screen
        frame.setBounds(((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2, 0,
                Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        GameStart gameStart = new GameStart();
        frame.setContentPane(gameStart.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

    }

    public static void closeGameStart() {
        frame.setVisible(false);
    }

    public static void showGaming() {
        System.out.println("Welcome to Aircraft War!");

        frame = new JFrame("Aircraft War");
        // Set the window on the centre of the screen
        frame.setBounds(((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2, 0,
                Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        if (Difficulty.difficulty == 0) {
            EasyGame game = new EasyGame();
            frame.add(game);
            frame.setVisible(true);
            game.action();
        } else if (Difficulty.difficulty == 1) {
            NormalGame game = new NormalGame();
            frame.add(game);
            frame.setVisible(true);
            game.action();
        } else {
            HardGame game = new HardGame();
            frame.add(game);
            frame.setVisible(true);
            game.action();
        }

    }

    public static void closeGaming() {
        frame.setVisible(false);
    }

    public static void showGameOver(RankDao rankDao) {
        JFrame frame = new JFrame("GameOver");
        // Set the window on the centre of the screen
        frame.setBounds(((int) screenSize.getWidth() - Main.WINDOW_WIDTH) / 2, 0,
                Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);
        GameOver gameOver = new GameOver(rankDao);
        frame.setContentPane(gameOver.mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Main.WINDOW_WIDTH, Main.WINDOW_HEIGHT);

    }

    public static void closeGameOver() {
        frame.setVisible(false);
    }

}
