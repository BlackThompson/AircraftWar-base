package edu.hitsz.rank;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface RankDao {
    List<Player> getAllPlayers();

    void addNewPlayer(Player player);

    void playerRead() throws IOException;

    void playerWrite(List<Player> players) throws IOException;

    void printRank(RankDao rankDao);

    void deleteRankByUUID(UUID uuid);


}
