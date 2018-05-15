package infomgmag;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class represents a continent in the game.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Continent {
	private String name;
	private ArrayList<Territory> territories;
	private Color color;
	
	private int nReinforcements;			// Number of reinforcements received when a player owns the continent.

	
	public Continent(Color color) {
		this.color = color;
		territories = new ArrayList<Territory>();
		Random rand = new Random();
	}
	
	public String GetName() {
		return name;
	}

	public ArrayList<Territory> getMembers() {
		return territories;
	}

	public Color getColor() {
		return this.color;
	}
	
	public ArrayList<Territory> getTerritories() {
		return territories;
	}
	
	public int getNReinforcements() {
		return nReinforcements;
	}
	
	public void addTerritory(Territory ter) {
		territories.add(ter);
	}
}
