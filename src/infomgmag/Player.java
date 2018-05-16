package infomgmag;
import java.util.ArrayList;
import java.awt.Color;

/**
 * This is an abstract class for a player of the game.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public abstract class Player {
	protected String name;
	protected Color color;
	protected Objective objective;
	protected ArrayList<Territory> territories;
	protected Hand hand;
	protected Integer reinforcements;

	public Player(Objective objective, Integer reinforcements, String name) {
		this.objective = objective;
		this.reinforcements = reinforcements;
		this.name = name;
		this.hand = new Hand();
		this.territories = new ArrayList<Territory>();
		this.color = new Color(Risk.random.nextFloat() * 0.8f + 0.2f, Risk.random.nextFloat() * 0.8f + 0.2f, Risk.random.nextFloat() * 0.8f + 0.2f);
	}

	public abstract void turnInCards(Board board);
	public abstract void fortifyTerritory(Board board);
	public abstract CombatMove getCombatMove();
    public abstract void movingInAfterInvasion(CombatMove combatMove);

    public String getName() {
		return name;
	}

	public Color getColor() {
		return color;
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

	public abstract void placeReinforcements(Board board);


	@Override
	public String toString(){
		return name;
	}



}
