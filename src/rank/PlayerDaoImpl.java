package rank;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Black
 */
public class PlayerDaoImpl implements PlayerDao {

    //模拟数据库存数据
    List<Player> players;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd :hh:mm:ss");

    public PlayerDaoImpl() {
        players = new ArrayList<Player>();
    }

    //获取所有玩家信息
    @Override
    public List<Player> getAllPlayers() {
        return players;
    }

    //新增玩家信息
    @Override
    public void addNewPlayer(Player player) {
        players.add(player);
        System.out.println("Add new rank: name [" + player.getName() + "], score [" + player.getScore() + "]");
    }

    @Override
    public void playerRead() throws IOException {

        FileInputStream fis = new FileInputStream("src/rank/PlayerRecord.txt");
        ObjectInputStream input = new ObjectInputStream(fis);

        try {
            this.players = (ArrayList<Player>) input.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        input.close();
    }

    @Override
    public void playerWrite(List<Player> players) throws IOException {
        FileOutputStream fos = new FileOutputStream("src/rank/PlayerRecord.txt");
        ObjectOutputStream output = new ObjectOutputStream(fos);

        output.writeObject(players);
        output.close();
    }

    @Override
    public void printRank(PlayerDao playerDao) {
        for (Player player : playerDao.getAllPlayers()) {
            System.out.println("Name: " + player.getName() + "    Score: " + player.getScore() + "  Time: " + dateFormat.format(player.getDate()));
        }
    }
}
