import java.util.List;
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
	

	private List<Card> drawPile;
	private List<Card> discardPile;
	
	private int goldenCavalry;
	
	
	public Board(ArrayList<Continent> continents, ArrayList<Territory> territories) {
		this.continents = continents;
		this.territories = territories;
		this.goldenCavalry = 4;
	}
		
	public Board() {
		
	}
	
	/**
	 * Add the given amount of units to the given territory.
   * @return boolean if the action succeeded, in other words if the action was possible and has been executed.
	 */
	public void AddUnits(Territory territory, int number) {
		territory.SetUnits(territory.GetNUnits() + number);
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
	

	/**
	 * Move the golden cavalry one step forward.
	 */
	private void MoveGoldenCavalry() {
		if (goldenCavalry == 4) { this.goldenCavalry = 6; }
		else if (goldenCavalry == 6) { this.goldenCavalry = 8; }
		else if (goldenCavalry == 8) { this.goldenCavalry = 10; }
		else if (goldenCavalry == 10) { this.goldenCavalry = 15; }
		else if (goldenCavalry == 15) { this.goldenCavalry = 20; }
		else if (goldenCavalry == 20) { this.goldenCavalry = 25; }
		else if (goldenCavalry == 25) { this.goldenCavalry = 30; }
		else if (goldenCavalry == 30) { this.goldenCavalry = 35; }
		else if (goldenCavalry == 35) { this.goldenCavalry = 40; }
		else if (goldenCavalry == 40) { this.goldenCavalry = 45; }
		else if (goldenCavalry == 45) { this.goldenCavalry = 50; }
		else if (goldenCavalry == 50) { this.goldenCavalry = 55; }
		else if (goldenCavalry == 55) { this.goldenCavalry = 60; }
		else if (goldenCavalry == 60) { this.goldenCavalry = 65; }
	}
	
	/**
	 * Draw a Card from the draw pile.
	 */
	public Card drawCard() {
		if (drawPile.size() == 1) {
			Card result = drawPile.remove(0);
			// TODO: shuffle discardPile and swap with drawPile...
			return result;
		} else {
			return drawPile.remove(0);
		}
	}
	
	/**
	 * Turn in a cardSet.
	 * @return the number of reinforcements received 
	 */
	public int TurnInCards(ArrayList<Card> cardSet) {
		for (int i = 0; i < cardSet.size(); i++) {
			discardPile.add(cardSet.get(i));
		}
		int result = goldenCavalry;
		MoveGoldenCavalry();
		return result;
	}
	
	/**
	 * If there is a winner in the current state, return the winner.
	 */
	public Player GetWinner() {
		return null;
	}
	

	public ArrayList<Continent> GetContinents() {
		return continents;
	}
	
	public ArrayList<Territory> GetTerritories() {
		return territories;
	}

	public List<Card> GetDrawPile() {
		return drawPile;
	}
	
	public int GetGoldenCavalry() {
		return goldenCavalry;
	}

}
