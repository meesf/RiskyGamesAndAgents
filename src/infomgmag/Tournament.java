package infomgmag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Tournament {

    public static final boolean VISIBLE = false;
    public static final int SPEED = 100;

    public static final int RUNS = 5;
    public static final int STARTING_SEED = 100;
	
	public static HashMap<String, String> players;

	public static Random random;
	public static boolean RANDOMIZE_PLAYERS = false;

	public static void main(String[] args) {
		ArrayList<Result> results = new ArrayList<Result>();
		random = new Random(STARTING_SEED);
        players = new HashMap<String, String>();

        if(!RANDOMIZE_PLAYERS)
            setPlayers();

        for(int i = STARTING_SEED; i < RUNS + STARTING_SEED; i++) {
            Risk risk = new Risk(VISIBLE);
            if(RANDOMIZE_PLAYERS)
                randomizePlayers();
        	risk.initialize(i, players, SPEED);        	
        	risk.run();
        	results.add(new Result(risk, i));
        }
        
        printResults(results);
    }
	
	private static void printResults(ArrayList<Result> results) {
	    HashMap<String, Integer> wins = new HashMap<String, Integer>();
	    HashMap<String, ArrayList<Double>> captureRatios = new HashMap<String, ArrayList<Double>>();
	    HashMap<String, ArrayList<Integer>> captureCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> loseCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Double>> ownedContinents = new HashMap<String, ArrayList<Double>>();
	    // Could also add totalArmies and reinforcements (they are in the Result class already)

        //wins.put("NoWinner", 0);
	    for(String player : players.keySet()) {
	        wins.put(player, 0);
	        captureRatios.put(player, new ArrayList<Double>());
	        captureCounts.put(player, new ArrayList<Integer>());
	        loseCounts.put(player, new ArrayList<Integer>());
	        ownedContinents.put(player, new ArrayList<Double>());
	    }
	    
	    for(Result r : results) {
	        if(r.winner != "NoWinner")
	            wins.put(r.winner, wins.get(r.winner) + 1);
	        for(String player : players.keySet()) {
	            captureRatios.get(player).add(r.captureRatio.get(player));
	            captureCounts.get(player).add(r.captureTerritoryCount.get(player));
	            loseCounts.get(player).add(r.loseTerritoryCount.get(player));
	            ownedContinents.get(player).add(r.ownedContinent.get(player));
	        }
	    }
	    
	    System.out.println("\nWins:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":"+wins.get(player));
	    }
	    System.out.println("\nPercentage of attacks that resulted in a capture:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + captureRatios.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nTotal amount of territory captures:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + captureCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nTotal amount of lost territories:");
	    for(String player : players.keySet()) {
	        System.out.println("   " + player + ":" + loseCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nPercentage of game owning a continent:");
        for(String player : players.keySet()) {
            System.out.println("   " + player + ":" + ownedContinents.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	}

	private static void randomizePlayers() {
	    players.clear();
	    ArrayList<String> playerOptions = new ArrayList<>(Arrays.asList("aggressive", "normal", "defensive", "continent"));
	    for(int i = 0; i < 5; i++){
            String player = playerOptions.get(random.nextInt(playerOptions.size()));
            players.put(player + i, player);
        }
    }
	
	private static void setPlayers() {
        players.put("aggressiveA", "aggressive");
        players.put("normalA", "normal");
        players.put("normalB", "normal");
        players.put("defensiveA", "defensive");
        players.put("continentA", "continent");
    }
    private static void setPlayersNNNN() {
        players.put("normalA", "normal");
        players.put("normalB", "normal");
        players.put("normalC", "normal");
        players.put("normalD", "normal");
    }
    private static void setPlayersDDDD() {
        players.put("defensiveA", "defensive");
        players.put("defensiveB", "defensive");
        players.put("defensiveC", "defensive");
        players.put("defensiveD", "defensive");
    }
    
    private static void setPlayersCCCC() {
        players.put("continentA", "continent");
        players.put("continentB", "continent");
        players.put("continentC", "continent");
        players.put("continentD", "continent");
    }
    private static void setPlayersAAAA() {
        players.put("aggressiveA", "aggressive");
        players.put("aggressiveB", "aggressive");
        players.put("aggressiveC", "aggressive");
        players.put("aggressiveD", "aggressive");
    }
    private static void setPlayersNNDD() {
        players.put("normalA", "normal");
        players.put("normalB", "normal");
        players.put("defensiveA", "defensive");
        players.put("defensiveB", "defensive");
    }
    private static void setPlayersNNCC() {
        players.put("normalA", "normal");
        players.put("normalB", "normal");
        players.put("continentA", "continent");
        players.put("continentB", "continent");
    }
    private static void setPlayersNNAA() {
        players.put("normalA", "normal");
        players.put("normalB", "normal");
        players.put("aggressiveA", "aggressive");
        players.put("aggressiveB", "aggressive");
    }
    private static void setPlayersDDCC() {
        players.put("defensiveA", "defensive");
        players.put("defensiveB", "defensive");
        players.put("continentA", "continent");
        players.put("continentB", "continent");
    }
    private static void setPlayersDDAA() {
        players.put("defensiveA", "defensive");
        players.put("defensiveB", "defensive");
        players.put("aggressiveA", "aggressive");
        players.put("aggressiveB", "aggressive");
    }
    private static void setPlayersCCAA() {
        players.put("continentA", "continent");
        players.put("continentB", "continent");
        players.put("aggressiveA", "aggressive");
        players.put("aggressiveB", "aggressive");
    }
}
