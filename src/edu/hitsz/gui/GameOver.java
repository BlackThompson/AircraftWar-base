package edu.hitsz.gui;

import edu.hitsz.config.Difficulty;
import edu.hitsz.rank.Player;
import edu.hitsz.rank.RankDao;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class GameOver {
    public JPanel mainPanel;
    private JPanel topPanel;
    private JPanel midPanel;
    private JPanel bottomPanel;
    private JLabel rankingLable;
    private JTable rankingTable;
    private JButton deleteBotton;
    private JScrollPane scrollPane;

    GameOver(RankDao rankDao) {
        if (Difficulty.difficulty == 0) {
            rankingLable.setText("Easy Mode Ranking");
        } else if (Difficulty.difficulty == 1) {
            rankingLable.setText("Normal Mode Ranking");
        } else {
            rankingLable.setText("Hard Mode Ranking");
        }

        // load all players ranking
        List<Player> players = rankDao.getAllPlayers();

        // sort
        Player.classifyByDifficulty(players, Difficulty.difficulty);
        Player.sortByScore(players);

        // fill the ranking into the list
        String[] columnName = {"No.", "Name", "Score", "Time"};
        String[][] tableData = new String[players.size()][];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (int i = 0; i < players.size(); i++) {
            tableData[i] = new String[]{
                    String.valueOf(i + 1),
                    players.get(i).getName(),
                    String.valueOf(players.get(i).getScore()),
                    simpleDateFormat.format(players.get(i).getDate())
            };
        }

        DefaultTableModel model = new DefaultTableModel(tableData, columnName) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        rankingTable.setModel(model);
        scrollPane.setViewportView(rankingTable);

        deleteBotton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = rankingTable.getSelectedRow();
                if (row != -1) {
                    int response = JOptionPane.showConfirmDialog(null, "Are you sure you are going to remove this rank?", "Delete Confirmation", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        model.removeRow(row);
                        rankDao.deleteRankByUUID(players.get(row).getUUID());
                    }
                }
                try {
                    rankDao.playerWrite(players);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
