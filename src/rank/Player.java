package rank;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Black
 */
public class Player implements Serializable, Comparable<Player> {
    private String name;
    private int score;
    private Date date;

    Player(String name, int score, Date date) {
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getName(){
        return  name;
    }

    public void setName(String name){
        this.name = name;
    }

    public int getScore(){
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public Date getDate(){
        return date;
    }

    public void setDate(Date date){
        this.date = date;
    }

    @Override
    public int compareTo(Player player) {
        if (this.score != player.getScore()){
            return player.getScore() - this.score;
        }else{
            return this.name.compareTo(player.getName());
        }
    }
}
