import java.util.ArrayList;

/**
 * This class contains all the objects on the board and methods that interact with these objects.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Board {
	private ArrayList<Continent> continents;
	private ArrayList<Territory> territories;
	
	
	public Board() {
		
	}
	
	/**
	 * Add the given amount of units to the given territory.
	 * @return boolean if the action succeeded, in other words if the action was possible and has been executed.
	 */
	public boolean AddUnits(Territory territory, int number) {
		return false;
	}
	
	/**
	 * Attack the defending territory from the attacking territory.
	 * @return boolean if the action succeeded, in other words if the action was possible and has been executed.
	 */
	public boolean Attack(Territory attacker, Territory defender) {
		return false;
	}
	
	/**
	 * Return the number of reinforcements the given player receives.
	 */
	public int GetReinforcements(Player player) {
		return 0;
	}
	
	public ArrayList<Continent> GetContinents() {
		return continents;
	}
	
	public ArrayList<Territory> GetTerritories() {
		return territories;
	}
}
