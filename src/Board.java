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

	private List<Card> drawPile;
	private List<Card> discardPile;
	
	private int goldenCavalry;
	
	
	public Board() {
		continents = new ArrayList<Continent>();
		
		this.goldenCavalry = 4;
	
		Territory ter1 = new Territory(0.1,0.8);
		Territory ter2 = new Territory(0.2,0.8);
		Territory ter3 = new Territory(0.15,0.7);
		Territory ter4 = new Territory(0.3,0.3);
		Territory ter5 = new Territory(0.4,0.3);
		Territory ter6 = new Territory(0.315,0.2);
		Territory ter7 = new Territory(0.6,0.8);
		Territory ter8 = new Territory(0.7,0.8);
		Territory ter9 = new Territory(0.615,0.7);
		Continent con1 = new Continent();
		Continent con2 = new Continent();
		Continent con3 = new Continent();
		con1.addTerritory(ter1);
		con1.addTerritory(ter2);
		con1.addTerritory(ter3);
		con2.addTerritory(ter4);
		con2.addTerritory(ter5);
		con2.addTerritory(ter6);
		con3.addTerritory(ter7);
		con3.addTerritory(ter8);
		con3.addTerritory(ter9);
		ter1.addAdjacentTerritory(ter2);
		ter1.addAdjacentTerritory(ter3);
		ter1.addAdjacentTerritory(ter8);
		ter2.addAdjacentTerritory(ter1);
		ter2.addAdjacentTerritory(ter3);
		ter3.addAdjacentTerritory(ter1);
		ter3.addAdjacentTerritory(ter2);
		ter3.addAdjacentTerritory(ter4);
		ter4.addAdjacentTerritory(ter3);
		ter4.addAdjacentTerritory(ter5);
		ter4.addAdjacentTerritory(ter6);
		ter5.addAdjacentTerritory(ter6);
		ter5.addAdjacentTerritory(ter4);
		ter5.addAdjacentTerritory(ter9);
		ter6.addAdjacentTerritory(ter4);
		ter6.addAdjacentTerritory(ter5);
		ter7.addAdjacentTerritory(ter8);
		ter7.addAdjacentTerritory(ter9);
		ter8.addAdjacentTerritory(ter1);
		ter8.addAdjacentTerritory(ter9);
		ter8.addAdjacentTerritory(ter7);
		ter9.addAdjacentTerritory(ter7);
		ter9.addAdjacentTerritory(ter8);
		ter9.addAdjacentTerritory(ter5);
		continents.add(con1);
		continents.add(con2);
		continents.add(con3);
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

	public List<Card> GetDrawPile() {
		return drawPile;
	}
	
	public int GetGoldenCavalry() {
		return goldenCavalry;
	}

}
