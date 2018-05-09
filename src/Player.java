import java.util.ArrayList;
import java.awt.Color;

/**
 * This is an abstract class for a player of the game.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public abstract class Player {
	private String name;
	private Color color;
	private Objective objective;
	private ArrayList<Card> cards;

	public Player(Objective objective) {
		this.objective = objective;
	}
	
	public abstract Action GetAction();
	
	public String GetName() {
		return name;
	}
	
	public Color GetColor() {
		return color;
	}

	public ArrayList<Card> GetCards() {
		return cards;
	}


}
