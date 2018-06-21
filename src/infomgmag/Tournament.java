package infomgmag;

import java.util.HashMap;

public class Tournament {
	
	public static final boolean VISIBLE = true;
	public static final int SPEED = 10;
	
	public static final int RUNS = 25;
	public static final int STARTING_SEED = 83075;
	public static HashMap<String, String> players;
	
	public static void main(String[] args) {
		setPlayers();
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		
        
        for(int i = STARTING_SEED; i < RUNS + STARTING_SEED; i++) {
            Risk risk = new Risk(VISIBLE);
        	risk.initialize(i, players, SPEED);
        	risk.run();
        	
        	String winner = risk.getActivePlayers().get(0).getName();
        	if(result.containsKey(winner)) {
        	    result.put(winner, result.get(winner) + 1);
             } else {
                 result.put(winner, 1);
             }
        }
        System.out.println(result);
    }
	
	private static void setPlayers() {
	    players = new HashMap<String, String>();
	    players.put("randomA", "random");
	    players.put("randomB", "random");
	    players.put("randomC", "random");
//        players.put("aggressiveA", "aggressive");
        players.put("normalA", "normal");
//        players.put("normalB", "normal");
//        players.put("defensiveA", "defensive");
//        players.put("defensiveB", "defensive");
//        players.put("continentA", "continent");
	}
}
