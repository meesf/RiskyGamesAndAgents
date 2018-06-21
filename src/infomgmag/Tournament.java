package infomgmag;

import java.util.ArrayList;
import java.util.HashMap;

public class Tournament {
	
	public static final boolean VISIBLE = false;
	public static final int SPEED = 10;
	
	public static final int RUNS = 2;
	public static final int STARTING_SEED = 83075;
	
	public static HashMap<String, String> players;
	
	public static void main(String[] args) {
		setPlayers();
		ArrayList<Result> results = new ArrayList<Result>();
		
        
        for(int i = STARTING_SEED; i < RUNS + STARTING_SEED; i++) {
            Risk risk = new Risk(VISIBLE);
        	risk.initialize(i, players, SPEED);
        	HashMap<String, ArrayList<Territory>> startingTerritories = new HashMap<String, ArrayList<Territory>>();
        	for(Player p : risk.getActivePlayers()) {
        	    ArrayList<Territory> starting = new ArrayList<Territory>();
        	    for(Territory t : p.getTerritories()) {
        	        starting.add(t);
        	    }
        	    startingTerritories.put(p.getName(), starting);
        	}
        	
        	risk.run();
        	
        	String winner = risk.getActivePlayers().get(0).getName();
        	results.add(new Result(winner, risk, i, startingTerritories));
        }
        
        printResults(results);
    }
	
	private static void setPlayers() {
	    players = new HashMap<String, String>();
	    players.put("randomA", "random");
	    players.put("randomB", "random");
	    players.put("randomC", "random");
        players.put("aggressiveA", "aggressive");
        players.put("normalA", "normal");
//        players.put("normalB", "normal");
//        players.put("defensiveA", "defensive");
//        players.put("defensiveB", "defensive");
//        players.put("continentA", "continent");
	}
	
	private static void printResults(ArrayList<Result> results) {
	    HashMap<String, Integer> win = new HashMap<String, Integer>();
	    HashMap<String, ArrayList<Double>> captureRatios = new HashMap<String, ArrayList<Double>>();
	    HashMap<String, ArrayList<Integer>> captureCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> loseCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Double>> ownedContinents = new HashMap<String, ArrayList<Double>>();
	    
	    for(String player : players.keySet()) {
	        win.put(player, 0);
	        captureRatios.put(player, new ArrayList<Double>());
	        captureCounts.put(player, new ArrayList<Integer>());
	        loseCounts.put(player, new ArrayList<Integer>());
	        ownedContinents.put(player, new ArrayList<Double>());
	    }
	    
	    for(Result r : results) {
	        win.put(r.winner, win.get(r.winner) + 1);
	        for(String player : players.keySet()) {
	            captureRatios.get(player).add(r.captureRatio.get(player));
	            captureCounts.get(player).add(r.captureTerritoryCount.get(player));
	            loseCounts.get(player).add(r.loseTerritoryCount.get(player));
//	            ownedContinents.get(player).add(r.ownedContinent.get(player));
	            System.out.println(r.ownedContinentMap);
	        }
	    }
	    
	    System.out.println("win:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":"+win.get(player));
	    }
	    System.out.println("captureRatios:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + captureRatios.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("captureCounts:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + captureCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("loseCounts:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + loseCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
//	    System.out.println("ownedContinent:");
//        for(String player : players.keySet()) {
//            System.out.println("   " + player + ":" + ownedContinents.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
//        }
	}
}
