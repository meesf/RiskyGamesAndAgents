package infomgmag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.OptionalDouble;

public class Result {
    public String winner;
    public Integer turns;
    public Integer seed;
    
    public HashMap<String, ArrayList<Integer>> captureRatioMap;
    public HashMap<String, Double> captureRatio;
    
    public HashMap<String, Integer> captureTerritoryCount;
    public HashMap<String, Integer> loseTerritoryCount;
    public HashMap<String, ArrayList<Integer>> ownedContinentMap;
    public HashMap<String, Double> ownedContinent;
    public HashMap<String, ArrayList<Integer>> totalArmies;
    public HashMap<String, ArrayList<Integer>> receivedReinforcements;
    
    public Result(Risk risk, Integer seed) {
        this.winner = risk.getActivePlayers().get(0).getName();
        this.turns = risk.getTurn();
        this.seed = seed;
        HashMap<String, Player> players = getPlayers(risk);
        getStatistics(risk, players);
        calcCaptureRatio();
        calcOwnedContinent();
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
    
    public void getStatistics(Risk risk, HashMap<String, Player> players) {
        captureRatioMap = new HashMap<String, ArrayList<Integer>>();
        captureTerritoryCount = new HashMap<String, Integer>();
        loseTerritoryCount = new HashMap<String, Integer>();
        ownedContinentMap = new HashMap<String, ArrayList<Integer>>();
        receivedReinforcements = new HashMap<String, ArrayList<Integer>>();
        totalArmies = new HashMap<String, ArrayList<Integer>>();
        
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
    }
    
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
