package rank;

import java.io.IOException;
import java.util.List;

public interface PlayerDao {
     List<Player> getAllPlayers();
     void addNewPlayer(Player player);
     void playerRead() throws IOException;
     void playerWrite(List<Player> players) throws IOException;
     void printRank(PlayerDao playerDao);


}
