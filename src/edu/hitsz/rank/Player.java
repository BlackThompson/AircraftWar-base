package edu.hitsz.rank;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author Black
 */
public class Player implements Serializable {

    private int difficulty;
    private String name;
    private int score;
    private Date date;
    private UUID uuid;

    public Player(int difficulty, String name, int score, Date date, UUID uuid) {
        this.difficulty = difficulty;
        this.name = name;
        this.score = score;
        this.date = date;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public UUID getUUID(){
        return uuid;
    }


    /*@Override
    public int compareTo(Player player) {
        if (this.score != player.getScore()) {
            return player.getScore() - this.score;
        } else {
            return this.name.compareTo(player.getName());
        }
    }*/

    public static void sortByScore(List<Player> players) {
        players.sort((t1, t2) -> t2.getScore() - t1.getScore());
    }

    public static void classifyByDifficulty(List<Player> players, int difficulty) {
        players.removeIf(player -> player.getDifficulty() != difficulty);
    }

}
