import sun.rmi.transport.ObjectTable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class contains the main() method. This class is the bridge between the visual presentation of 
 * the game (RiskVisual) and the data presentation of the game (RiskPhysics).
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Risk {

	private ArrayList<Player> players;
	private Board board;
	private Player currentPlayer;
	private RiskVisual visuals;
	private Integer nrOfStartingUnits;

	public static void main(String[] args) {

		Risk risk = new Risk();
		risk.run();
		System.out.println("Game is won by a player!");
	}

	public Risk(){
		initializeGame();
	}

	public void run(){
		while (!finished()) {
			playTurn();
		}
	}

	public void initializeGame() {
        visuals = new RiskVisual();
		board = new Board();
        initializePlayers();
        Integer currentPlayerIndex = divideTerritories();
		nrOfStartingUnits = 30;
	}

	public void initializePlayers(){
		players = new ArrayList<Player>();
		//TODO deciding number of startingUnits using number of players and evt. number territorries
		for(int i = 0; i < 4; i++){
			Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
			Bot player = new Bot(objective, nrOfStartingUnits);
			players.add(player);
		}
		currentPlayer = players.get(0);
	}

	//Divide players randomly over territories
	public Integer divideTerritories(){
		Collections.shuffle(board.getTerritories());
		int player = 0;
		for(Territory territory : board.getTerritories()){
			territory.setOwner(players.get(player % players.size()));
			territory.setUnits(1);
			territory.getOwner().setReinforcements(territory.getOwner().getReinforcements() - 1);
		}
		return player;
	}

	public void initialPlaceReinforcements(Integer currentPlayerIndex){
		int player = currentPlayerIndex;
		for(int i = 0; i < (players.size() * nrOfStartingUnits) - board.getTerritories().size() ; i++){

		}
	}


	public void playTurn() {

	}

	/**
	 * Returns true if there is a winner.
	 */
	public boolean finished() {
		if (board.GetWinner() == null) {
			return false;
		}
		return true;
	}
}
