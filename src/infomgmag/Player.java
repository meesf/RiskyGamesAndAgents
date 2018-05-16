package infomgmag;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This is an abstract class for a player of the game.
 * 
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
		this.territories = new ArrayList<>();
		this.color = new Color(Risk.random.nextFloat() * 0.8f + 0.2f, Risk.random.nextFloat() * 0.8f + 0.2f,
				Risk.random.nextFloat() * 0.8f + 0.2f);
	}

	public void addTerritory(Territory t) {
		this.territories.add(t);
	}

	public abstract void fortifyTerritory(Board board);

	public Color getColor() {
		return this.color;
	}

	public abstract CombatMove getCombatMove();

	public String getName() {
		return this.name;
	}

	public Objective getObjective() {
		return this.objective;
	}

	public Integer getReinforcements() {
		return this.reinforcements;
	}

	public ArrayList<Territory> getTerritories() {
		return this.territories;
	}

	public abstract void movingInAfterInvasion(CombatMove combatMove);

	public abstract void placeReinforcements(Board board);

	public void removeTerritory(Territory t) {
		this.territories.remove(t);
	}

	public void setReinforcements(Integer nr) {
		this.reinforcements = nr;
	}

	@Override
	public String toString() {
		return this.name;
	}

	public abstract void turnInCards(Board board);

}
