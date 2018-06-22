package infomgmag;

import java.util.ArrayList;
import java.util.HashMap;

public class TurnLog {
    public Integer turn;
    public Player player;
    public Integer receivedReinforcements;
    public HashMap<Player, Integer> totalArmies;
    public HashMap<Player, ArrayList<Territory>> territories;
    
    public TurnLog(Integer turn,
            Player player,
            Integer receivedReinforcements,
            HashMap<Player, Integer> totalArmies,
            HashMap<Player, ArrayList<Territory>> territories) {
        this.turn = turn;
        this.player = player;
        this.receivedReinforcements = receivedReinforcements;
        this.totalArmies = totalArmies;
        this.territories = territories;
    }
}
