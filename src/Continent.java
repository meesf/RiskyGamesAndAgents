import java.util.ArrayList;

/**
 * This class represents a continent in the game.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Continent {
	private String name;
	private ArrayList<Territory> members;

	private int nReinforcements;			// Number of reinforcements received when a player owns the continent.

	
	public Continent() {
		members = new ArrayList<Territory>();
	}
	
	public String GetName() {
		return name;
	}
	
	public ArrayList<Territory> getMembers() {
		return members;
	}
	
	public int GetNReinforcements() {
		return nReinforcements;
	}
	
	public void addTerritory(Territory ter) {
		members.add(ter);
	}
}
