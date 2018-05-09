/**
 * This class contains the main() method. This class is the bridge between the visual presentation of 
 * the game (RiskVisual) and the data presentation of the game (RiskPhysics).
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Risk {
	
	public static void main(String[] args) {
		RiskPhysics physics = new RiskPhysics();
		RiskVisual visual = new RiskVisual();
		
		// TODO: Let players initialize the board. 
		
		while (!physics.Finished()) {
			physics.PlayTurn();
			visual.Update();			// Possibly even more updates throughout the turn...
		}								// This can be achieved by separating PlayTurn into different methods.
		
		System.out.println("Game is won by a player!");
	}
}
