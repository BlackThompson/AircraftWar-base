package edu.hitsz.gui;

import edu.hitsz.config.DifferentModeConfig;
import edu.hitsz.config.Media;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Black
 */
public class GameStart {

    public JPanel mainPanel;
    //private JPanel mainPanel;
    private JPanel topPanel;
    private JPanel bottomPanel;
    private JButton normalMode;
    private JButton hardMode;
    private JButton easyMode;
    private JLabel sound;
    private JComboBox soundChoose;
    private JLabel welcome;


    public GameStart() {
        easyMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DifferentModeConfig.easyMode();
                FrameChange.closeGameStart();
                FrameChange.showGaming();
            }
        });
        normalMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DifferentModeConfig.normalMode();
                FrameChange.closeGameStart();
                FrameChange.showGaming();
            }
        });
        hardMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                DifferentModeConfig.hardMode();
                FrameChange.closeGameStart();
                FrameChange.showGaming();
            }
        });
        soundChoose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Media.music = (soundChoose.getSelectedIndex() == 1);
            }
        });
    }
}
