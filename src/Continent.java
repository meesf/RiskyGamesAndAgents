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

	
	public Continent() {
		territories = new ArrayList<Territory>();
		Random rand = new Random();
		color = new Color((float) (rand.nextFloat()*0.5+0.5),
				(float) (rand.nextFloat()*0.5+0.5), 
				(float) (rand.nextFloat()*0.5+0.5));
	}
	
	public String GetName() {
		return name;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	public ArrayList<Territory> getTerritories() {
		return territories;
	}
	
	public int GetNReinforcements() {
		return nReinforcements;
	}
	
	public void addTerritory(Territory ter) {
		territories.add(ter);
	}
}
