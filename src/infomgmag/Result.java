package infomgmag;

import java.util.ArrayList;
import java.util.HashMap;

public class Result {
    public HashMap<String, Player> players;
    public String winner;
    public Integer turns;
    public Integer seed;
                                                                        // These are all maps from player -> data
    public HashMap<String, ArrayList<Integer>> combatCaptures;          // list of every combat result     
    public HashMap<String, Integer> captureTerritoryCount;              // Count of attacks that resulted in a capture
    public HashMap<String, Integer> lostTerritoryCount;                 // Count of battles that resulted in a capture for the opponent
    public HashMap<String, ArrayList<Integer>> ownedContinent;          // List of booleans per turn, whether the player owned a 
                                                                        // whole continent that turn 
    public HashMap<String, ArrayList<Integer>> totalArmies;             // Amount of total armies owned per turn
    public HashMap<String, ArrayList<Integer>> receivedReinforcements;  // Amount of reinforcements earned per turn
    public HashMap<String, Integer> amountOfAttacks;                    // Amount of attacks where the player (key) was the attacker
    
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
        combatCaptures = new HashMap<String, ArrayList<Integer>>();
        captureTerritoryCount = new HashMap<String, Integer>();
        lostTerritoryCount = new HashMap<String, Integer>();
        ownedContinent = new HashMap<String, ArrayList<Integer>>();
        receivedReinforcements = new HashMap<String, ArrayList<Integer>>();
        totalArmies = new HashMap<String, ArrayList<Integer>>();
        amountOfAttacks = new HashMap<String, Integer>();
        
        // Initialize the maps containing information
        for(String p : players.keySet()) {
            combatCaptures.put(p, new ArrayList<Integer>());
            captureTerritoryCount.put(p, 0);
            lostTerritoryCount.put(p, 0);
            ownedContinent.put(p, new ArrayList<Integer>());
            receivedReinforcements.put(p, new ArrayList<Integer>());
            totalArmies.put(p, new ArrayList<Integer>());
            amountOfAttacks.put(p, 0);
        }
        
        for(TurnLog turnLog : risk.turnLog) {
            receivedReinforcements.get(turnLog.player.getName()).add(turnLog.receivedReinforcements);
            for(Player p : turnLog.territories.keySet()) {
                totalArmies.get(p.getName()).add(turnLog.totalArmies.get(p));
                ownedContinent.get(p.getName()).add(ownsContinent(risk, turnLog.territories.get(p)));
            }
        }
        
        for(CombatEvent ce : risk.getCombatLog()) {
            String attacker = ce.getAttackingPlayer().getName();
            String defender = ce.getDefendingPlayer().getName();
            combatCaptures.get(attacker).add(ce.getCombatResult());
            amountOfAttacks.put(attacker, amountOfAttacks.get(attacker) + 1);
            if(ce.getCaptured()) {
                captureTerritoryCount.put(attacker, captureTerritoryCount.get(attacker) + 1);
                lostTerritoryCount.put(defender, lostTerritoryCount.get(defender) + 1);
            }
        }
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
}
