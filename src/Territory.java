import java.util.ArrayList;

/**
 * This class represents a territory in the game.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Territory {
	private Player owner;
	private int nUnits;
	private ArrayList<Territory> adjacentTerritories;
	
	public Territory() {
		
	}
	
	public void Set_Units(int units) {
		this.nUnits = units;
	}
	
	public Player GetOwner() {
		return owner;
	}
	
	public int GetNUnits() {
		return nUnits;
	}
	
	public ArrayList<Territory> GetAdjacentTerritories() {
		return adjacentTerritories;
	}
}