import java.sql.Time;
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
		Risk risk = new Risk();
		risk.Run();
		// TODO: Let players initialize the board. 
		
		// This can be achieved by separating PlayTurn into different methods.
		
		System.out.println("Game is won by a player!");
	}

	public Risk(){
		visuals = new RiskVisual(this);
		players = new ArrayList<Player>();
		board = new Board();
		InitializeGame();
	}

	public void Run(){
		long targetFrameDuration = (long) (1000/60.0);
		long frameDuration = 1000;
		long lastFrameTime = System.currentTimeMillis();
		while (!Finished()) {
			frameDuration = -(lastFrameTime - (lastFrameTime = System.currentTimeMillis() / 1000000000));
			if (frameDuration > targetFrameDuration) {
				try {
					Thread.sleep(targetFrameDuration - frameDuration);
				} catch (InterruptedException e) {
					System.exit(0);
				}
			}
			PlayTurn();
			visuals.update();			// Possibly even more updates throughout the turn...
		}
	}

	public void InitializeGame() {

	}

	public void PlayTurn() {

	}

	public Board getBoard() {
		return this.board;
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
