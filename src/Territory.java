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
	
	public double x, y;
	

	public Territory(double x, double y) {
		this.x = x;
		this.y = y;
		this.nUnits = 0;
		this.adjacentTerritories = new ArrayList<Territory>();
	}
	
	public void setOwner(Player newOwner) {
		this.owner.removeTerritory(this);
		newOwner.addTerritory(this);
		this.owner = newOwner;
	}
	
	public void setUnits(int units) {
		this.nUnits = units;
	}
	
	public Player getOwner() {
		return owner;
	}
	
	public int getNUnits() {
		return nUnits;
	}
	
	public ArrayList<Territory> getAdjacentTerritories() {
		return adjacentTerritories;
	}

}