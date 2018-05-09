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
	private ArrayList<Territory> territories;
	private ArrayList<Card> cards;
	private Integer reinforcements;

	public Player(Objective objective, Integer reinforcements) {
		this.objective = objective;
		this.reinforcements = reinforcements;
		this.territories = new ArrayList<Territory>();
	}

	public abstract Action getAction();
	public abstract void placeSingleReinforcement(Board board);

	public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public Objective getObjective() {
		return objective;
	}

	public Integer getReinforcements(){
		return reinforcements;
	}

	public void setReinforcements(Integer nr){
		this.reinforcements = nr;
	}

	public ArrayList<Territory> getTerritories() { return this.territories; }

	public void removeTerritory(Territory t) {
		territories.remove(t);
	}

	public void addTerritory(Territory t) {
		territories.add(t);
	}
}
