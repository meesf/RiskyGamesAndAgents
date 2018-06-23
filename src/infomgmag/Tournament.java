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
	public static boolean RANDOMIZE_PLAYERS = true;

    public static ArrayList<String> playerTypes = new ArrayList<>(Arrays.asList("aggressive", "normal", "defensive", "continent"));

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

    private static void getPlayerWithName(String name){
        String player = name.substring(0, name.length() - 1);
    }
	
	private static void printResults(ArrayList<Result> results) {
	    HashMap<String, Integer> wins = new HashMap<String, Integer>();
	    HashMap<String, ArrayList<Double>> captureRatios = new HashMap<String, ArrayList<Double>>();
	    HashMap<String, ArrayList<Integer>> captureCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> loseCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Double>> ownedContinents = new HashMap<String, ArrayList<Double>>();
	    // Could also add totalArmies and reinforcements (they are in the Result class already)

        for(String type : playerTypes){
            wins.put(type, 0);
            captureRatios.put(type, new ArrayList<Double>());
            captureCounts.put(type, new ArrayList<Integer>());
            loseCounts.put(type, new ArrayList<Integer>());
            ownedContinents.put(type, new ArrayList<Double>());
        }

	    for(Result r : results) {
	        if(r.winner != "NoWinner")
	            wins.put(r.winner, wins.get(r.winner) + 1);
	        for(String player : r.players.keySet()) {
	            String type = player.substring(0, player.length() - 1);
	            System.out.println(type);
	            if(type.equals("continent")){
	                int a = 0;
                }
                captureRatios.get(type).add(r.captureRatio.get(player));
	            captureCounts.get(type).add(r.captureTerritoryCount.get(player));
	            loseCounts.get(type).add(r.loseTerritoryCount.get(player));
	            ownedContinents.get(type).add(r.ownedContinent.get(player));
	        }
	    }
	    
	    System.out.println("\nWins:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":"+wins.get(player));
	    }
	    System.out.println("\nPercentage of attacks that resulted in a capture:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + captureRatios.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nTotal amount of territory captures:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + captureCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nTotal amount of lost territories:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + loseCounts.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	    System.out.println("\nPercentage of game owning a continent:");
        for(String player : playerTypes) {
            System.out.println("   " + player + ":" + ownedContinents.get(player).stream().mapToDouble(x -> x).average().getAsDouble());
        }
	}

	private static void randomizePlayers() {
	    players.clear();
	    players = new HashMap<String, String>();
        String str = "ABCDEF";
        char[] ch  = str.toCharArray();
        int aC = 0, nC = 0, dC = 0, cC = 0;
	    for(int i = 0; i < 5; i++){
            String player = playerTypes.get(random.nextInt(playerTypes.size()));
            if(player == "aggressive"){
                players.put(player + ch[aC], player);
                aC++;
            }
            else if(player == "normal"){
                players.put(player + ch[nC], player);
                nC++;
            }
            else if(player == "defensive"){
                players.put(player + ch[dC], player);
                dC++;
            }
            else if(player == "continent"){
                players.put(player + ch[cC], player);
                cC++;
            }
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
