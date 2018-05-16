package infomgmag;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class represents a continent in the game.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Continent {
	private String name;
	private ArrayList<Territory> territories;
	private Color color;

	private int nReinforcements; // Number of reinforcements received when a player owns the continent.

	public Continent(Color color) {
		this.color = color;
		this.territories = new ArrayList<>();
	}

	public void addTerritory(Territory ter) {
		this.territories.add(ter);
	}

	public Color getColor() {
		return this.color;
	}

	public ArrayList<Territory> getMembers() {
		return this.territories;
	}

	public String GetName() {
		return this.name;
	}

	public int getNReinforcements() {
		return this.nReinforcements;
	}

	public ArrayList<Territory> getTerritories() {
		return this.territories;
	}
}
