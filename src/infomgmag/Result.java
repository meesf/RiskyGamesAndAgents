package infomgmag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.OptionalDouble;

public class Result {
    public HashMap<String, Player> players;
    public String winner;
    public Integer turns;
    public Integer seed;
                                                                        // These are all maps from player -> data
    public HashMap<String, ArrayList<Integer>> captureRatioMap;         // list of every combat result     
    public HashMap<String, Double> captureRatio;                        // attacks that resulted in a capture divided 
                                                                        // by the total amount of attacks
    public HashMap<String, Integer> captureTerritoryCount;              // Count of attacks that resulted in a capture
    public HashMap<String, Integer> loseTerritoryCount;                 // Count of battles that resulted in a capture for the opponent
    public HashMap<String, ArrayList<Integer>> ownedContinentMap;       // List of booleans per turn, whether the player owned a 
                                                                        // whole continent that turn 
    public HashMap<String, Double> ownedContinent;                      // Ratio of what percentage of the game a continent was owned
    public HashMap<String, ArrayList<Integer>> totalArmies;             // Amount of total armies owned per turn
    public HashMap<String, ArrayList<Integer>> receivedReinforcements;  // Amount of reinforcements earned per turn
    
    public Result(Risk risk, Integer seed) {
        this.winner = risk.getActivePlayers().size() == 1 ? risk.getActivePlayers().get(0).getName() : "NoWinner";
        this.turns = risk.getTurn();
        this.seed = seed;
        this.players = getPlayers(risk);
        getInfo(risk, this.players);
    }
    
    private HashMap<String, Player> getPlayers(Risk risk) {
        HashMap<String, Player> players = new HashMap<String, Player>();
        for(Player p : risk.getActivePlayers()) {
            players.put(p.getName(), p);
        }
        for(Player p : risk.getDefeatedPlayers()) {
            players.put(p.getName(), p);
        }
        return players;
    }
    
    public void getInfo(Risk risk, HashMap<String, Player> players) {
        captureRatioMap = new HashMap<String, ArrayList<Integer>>();
        captureTerritoryCount = new HashMap<String, Integer>();
        loseTerritoryCount = new HashMap<String, Integer>();
        ownedContinentMap = new HashMap<String, ArrayList<Integer>>();
        receivedReinforcements = new HashMap<String, ArrayList<Integer>>();
        totalArmies = new HashMap<String, ArrayList<Integer>>();
        
        // Initialize the maps containing information
        for(String p : players.keySet()) {
            captureRatioMap.put(p, new ArrayList<Integer>());
            captureTerritoryCount.put(p, 0);
            loseTerritoryCount.put(p, 0);
            ownedContinentMap.put(p, new ArrayList<Integer>());
            receivedReinforcements.put(p, new ArrayList<Integer>());
            totalArmies.put(p, new ArrayList<Integer>());
        }
        
        for(TurnLog turnLog : risk.turnLog) {
            receivedReinforcements.get(turnLog.player.getName()).add(turnLog.receivedReinforcements);
            for(String p : players.keySet()) {
                totalArmies.get(p).add(turnLog.totalArmies.get(players.get(p)));
                ownedContinentMap.get(p).add(ownsContinent(risk, turnLog.territories.get(players.get(p))));
            }
        }
        
        for(CombatEvent ce : risk.getCombatLog()) {
            String attacker = ce.getAttackingPlayer().getName();
            String defender = ce.getDefendingPlayer().getName();
            captureRatioMap.get(attacker).add(ce.getCombatResult());
            if(ce.getCombatResult() == CombatEvent.CAPTURE) {
                captureTerritoryCount.put(attacker, captureTerritoryCount.get(attacker) + 1);
                loseTerritoryCount.put(defender, loseTerritoryCount.get(defender) + 1);
            }
        }
        
        calcCaptureRatio();
        calcOwnedContinent();
    }
    
    /**
     * Does a player with the given list of territories own a whole continent, return 1. Else return 0.
     */
    public Integer ownsContinent(Risk risk, ArrayList<Territory> ownedTerritories) {
        for(Continent continent : risk.getBoard().getContinents()) {
            if(ownedTerritories.containsAll(continent.getTerritories()))
                return 1;
        }
        return 0;
    }
    
    public void calcCaptureRatio() {
        captureRatio = new HashMap<String, Double>();
        for(String player : captureRatioMap.keySet()) {
            Double average = captureRatioMap.get(player).stream().mapToInt(Integer::intValue).average().getAsDouble();
            captureRatio.put(player, average);
        }
    }
    
    public void calcOwnedContinent() {
        ownedContinent = new HashMap<String, Double>();
        for(String player : ownedContinentMap.keySet()) {
            OptionalDouble oAverage = ownedContinentMap.get(player).stream().mapToInt(Integer::intValue).average();
            double average = 0;
            if(oAverage.isPresent()) {
               average = oAverage.getAsDouble();
            }
            ownedContinent.put(player, average);
        }
    }
}
