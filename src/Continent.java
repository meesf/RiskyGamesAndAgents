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
	private int nReinforcements;
	
	public Continent() {
		
	}
	
	public String GetName() {
		return name;
	}
	
	public ArrayList<Territory> GetMembers() {
		return members;
	}
	
	public int GetNReinforcements() {
		return nReinforcements;
	}
}
