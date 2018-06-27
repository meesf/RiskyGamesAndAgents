package infomgmag;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class Tournament {

    public static final boolean VISIBLE = false;
    public static final int SPEED = 0;

    public static final int RUNS = 3;
    public static final int STARTING_SEED = 101;
	
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

	private static void printResults(ArrayList<Result> results) {
	    HashMap<String, Integer> wins = new HashMap<String, Integer>();
	    HashMap<String, ArrayList<Integer>> captureRatios = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> captureCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> loseCounts = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, ArrayList<Integer>> ownedContinents = new HashMap<String, ArrayList<Integer>>();
	    HashMap<String, Integer> turnsAlive = new HashMap<String, Integer>();
	    HashMap<String, Integer> amountOfAttacks = new HashMap<>();

        for(String type : playerTypes){
            wins.put(type, 0);
            captureRatios.put(type, new ArrayList<Integer>());
            captureCounts.put(type, new ArrayList<Integer>());
            loseCounts.put(type, new ArrayList<Integer>());
            ownedContinents.put(type, new ArrayList<Integer>());
            turnsAlive.put(type, 0);
            amountOfAttacks.put(type, 0);
        }

	    for(Result r : results) {
	        if(r.winner != "NoWinner"){
	            String type = r.winner.substring(0, r.winner.length() - 1);
                wins.put(type, wins.get(type) + 1);
            }
	        for(String player : r.players.keySet()) {
	            String type = player.substring(0, player.length() - 1);
	            turnsAlive.put(type, turnsAlive.get(type) + r.ownedContinent.get(player).size());
                captureRatios.get(type).addAll(r.combatCaptures.get(player));
	            captureCounts.get(type).add(r.captureTerritoryCount.get(player));
	            loseCounts.get(type).add(r.lostTerritoryCount.get(player));
	            ownedContinents.get(type).addAll(r.ownedContinent.get(player));
	            amountOfAttacks.put(type, amountOfAttacks.get(type) + r.amountOfAttacks.get(player));
	        }
	    }
	    
	    System.out.println("\nWins:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":"+wins.get(player));
	    }
	    System.out.println("\nAmount of attacks that resulted in a capture per attack:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + captureRatios.get(player).stream().mapToInt(x -> x).average().getAsDouble());
        }
	    System.out.println("\nAmount of attacks per turn alive:");
        for(String player : playerTypes) {
            System.out.println("   " + player + ":" + (double) amountOfAttacks.get(player) / (double) turnsAlive.get(player));
        }
	    System.out.println("\nAmount of territory captures per turn alive:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + captureCounts.get(player).stream().mapToDouble(x -> x).sum() / (double) turnsAlive.get(player));
        }
	    System.out.println("\nAmount of lost territories per turn alive:");
	    for(String player : playerTypes) {
	        System.out.println("   " + player + ":" + loseCounts.get(player).stream().mapToDouble(x -> x).sum() / (double) turnsAlive.get(player));
        }
	    System.out.println("\nPercentage of owning a continent while alive:");
        for(String player : playerTypes) {
            System.out.println("   " + player + ":" + (ownedContinents.get(player).stream().mapToDouble(x -> x).sum() / (double) turnsAlive.get(player))*100);
        }
	}

	private static void randomizePlayers() {
	    players.clear();
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
