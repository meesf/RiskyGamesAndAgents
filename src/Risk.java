import sun.rmi.transport.ObjectTable;

import java.util.ArrayList;

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


	public static void main(String[] args) {
		Risk risk = new Risk(args);
		risk.Run();
		System.out.println("Game is won by a player!");
	}

	public Risk(String[] args){
		InitializeGame(args);
	}

	public void Run(){
		while (!Finished()) {
			PlayTurn();
			visuals.Update();
		}
	}

	public void InitializeGame(String[] args) {
        visuals = new RiskVisual();
        players = new ArrayList<Player>();
        for(int i = 0; i < Integer.parseInt(args[0]); i++){
            Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
            Bot player = new Bot(objective);
            players.add(player);
        }
        currentPlayer = players.get(0);
        board = new Board();
        System.out.println(players);
	}

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
}
