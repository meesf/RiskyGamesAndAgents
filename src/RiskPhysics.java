import java.util.ArrayList;

/**
 * This class contains the data of the game and methods that manipulate this data.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class RiskPhysics {
	private ArrayList<Player> players;
	private Board board;
	private Player currentPlayer;
	
	public RiskPhysics() {
		
	}
	
	/**
	 * Initialize the game.
	 */
	public void InitializeGame() {
		
	}
	
	/**
	 * Play one turn of the game.
	 */
	public void PlayTurn() {
		
	}
	
	/**
	 * Returns true if there is a winner.
	 */
	public boolean Finished() { 
		if (board.GetWinner() == null) {
			return false;
		}
		return true;
	}
	
	public ArrayList<Player> GetPlayers() {
		return players;
	}
	
	public Board GetBoard() {
		return board;
	}
	
	public Player GetCurrentPlayer() {
		return currentPlayer;
	}
}
