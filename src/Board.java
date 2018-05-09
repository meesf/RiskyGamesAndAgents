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
		
		Continent northAmerica = new Continent();
		continents.add(northAmerica);
		Continent southAmerica = new Continent();
		continents.add(southAmerica);
		Continent europe = new Continent();
		continents.add(europe);
		Continent asia = new Continent();
		continents.add(asia);
		Continent africa = new Continent();
		continents.add(africa);
		Continent australia = new Continent();
		continents.add(australia);
		
		Territory yakutsk = new Territory("Yakutsk",0.94,0.9);
		asia.addTerritory(yakutsk);
		Territory kamchatka = new Territory("Kamchatka", 0.99,0.89);
		asia.addTerritory(kamchatka);
		Territory siberia = new Territory("Siberia", 0.9, 0.89);
		asia.addTerritory(siberia);
		Territory japan = new Territory("Japan", 1.0, 0.6);
		asia.addTerritory(japan);
		Territory irkutsk = new Territory("Irkutsk", 0.9, 0.77);
		asia.addTerritory(irkutsk);
		Territory mongolia = new Territory("Mongolia", 0.92, 0.65);
		asia.addTerritory(mongolia);
		Territory china = new Territory("China", 0.83, 0.6);
		asia.addTerritory(china);
		Territory siam = new Territory("Siam", 0.89, 0.5);
		asia.addTerritory(siam);
		Territory indonesia = new Territory("Indonesia", 0.9, 0.3);
		australia.addTerritory(indonesia);
		Territory westernAustralia = new Territory("Western Australia", 0.85, 0.1);
		australia.addTerritory(westernAustralia);
		Territory easternAustralia = new Territory("Eastern Australia", 0.95, 0.1);
		australia.addTerritory(easternAustralia);
		Territory newGuinea = new Territory("New Guinea", 0.99, 0.28);
		australia.addTerritory(newGuinea);
		Territory alaska = new Territory("Alaska", 0.1, 0.9);
		northAmerica.addTerritory(alaska);
		Territory northWestTerritory = new Territory("North-West Territory", 0.2, 0.925);
		northAmerica.addTerritory(northWestTerritory);
		Territory alberta = new Territory("Alberta",0.18,0.8);
		northAmerica.addTerritory(alberta);
		Territory westernUS = new Territory("Western United States", 0.15, 0.7);
		northAmerica.addTerritory(westernUS);
		Territory greenland = new Territory("Greenland", 0.345, 1.0);
		northAmerica.addTerritory(greenland);
		Territory ontario = new Territory("Ontario", 0.29, 0.815);
		northAmerica.addTerritory(ontario);
		Territory quebec = new Territory("Quebec", 0.45, 0.8);
		northAmerica.addTerritory(quebec);
		Territory easternUS = new Territory("Eastern United States", 0.4, 0.71);
		northAmerica.addTerritory(easternUS);
		Territory centralUS = new Territory("Central United States", 0.275, 0.63);
		northAmerica.addTerritory(centralUS);
		Territory venezuela = new Territory("Venezuela", 0.28, 0.54);
		southAmerica.addTerritory(venezuela);
		Territory peru = new Territory("Peru",0.19,0.42);
		southAmerica.addTerritory(peru);
		Territory brazil = new Territory("Brazil", 0.4, 0.42);
		southAmerica.addTerritory(brazil);
		Territory argentina = new Territory("Argentina", 0.28, 0.29);
		southAmerica.addTerritory(argentina);
		Territory iceland = new Territory("Iceland", 0.48, 0.83);
		europe.addTerritory(iceland);
		Territory scandinavia = new Territory("Scandinavia", 0.55, 0.9);
		europe.addTerritory(scandinavia);
		Territory unitedKingdom = new Territory("United Kingdom", 0.5, 0.71);
		europe.addTerritory(unitedKingdom);
		Territory northernEurope = new Territory("Northern Europe", 0.54, 0.715);
		europe.addTerritory(northernEurope);
		Territory southernEurope = new Territory("Southern Europe", 0.55, 0.67);
		europe.addTerritory(southernEurope);
		Territory northAfrica = new Territory ("North Africa", 0.47, 0.5);
		africa.addTerritory(northAfrica);
		Territory egypt = new Territory ("Egypt", 0.61, 0.6);
		africa.addTerritory(egypt);
		Territory congo = new Territory ("Congo", 0.55, 0.4);
		africa.addTerritory(congo);
		Territory southAfrica = new Territory("South Africa", 0.6, 0.21);
		africa.addTerritory(southAfrica);
		Territory eastAfrica = new Territory("East Africa", 0.66, 0.48);
		africa.addTerritory(eastAfrica);
		Territory madagascar = new Territory("Madagascar", 0.68, 0.3);
		africa.addTerritory(madagascar);
		Territory ukraine = new Territory("Ukraine", 0.66, 0.83);
		europe.addTerritory(ukraine);
		Territory middleEast = new Territory("Middle East", 0.62, 0.66);
		asia.addTerritory(middleEast);
		Territory afganistan = new Territory("Afganistan", 0.73, 0.75);
		asia.addTerritory(afganistan);
		Territory ural = new Territory("Ural", 0.78, 0.89);
		asia.addTerritory(ural);
		Territory india = new Territory("India", 0.77, 0.58);
		asia.addTerritory(india);
		Territory westernEurope = new Territory("Western Europe", 0.5, 0.65);
		europe.addTerritory(westernEurope);
		makeEdge(india, siam);
		makeEdge(india, middleEast);
		makeEdge(india, afganistan);
		makeEdge(india, china);
		makeEdge(ural, siberia);
		makeEdge(ural, china);
		makeEdge(ural, afganistan);
		makeEdge(ural, ukraine);
		makeEdge(afganistan, china);
		makeEdge(afganistan, ukraine);
		makeEdge(afganistan, middleEast);
		makeEdge(ukraine,scandinavia);
		makeEdge(ukraine,northernEurope);
		makeEdge(ukraine,southernEurope);
		makeEdge(madagascar,eastAfrica);
		makeEdge(madagascar,southAfrica);
		makeEdge(eastAfrica,northAfrica);
		makeEdge(eastAfrica,egypt);
		makeEdge(eastAfrica,congo);
		makeEdge(eastAfrica,southAfrica);
		makeEdge(egypt,northAfrica);
		makeEdge(egypt,middleEast);
		makeEdge(egypt,southernEurope);
		makeEdge(northAfrica,brazil);
		makeEdge(congo,northAfrica);
		makeEdge(congo,southAfrica);
		makeEdge(brazil,venezuela);
		makeEdge(brazil,peru);
		makeEdge(brazil,argentina);
		makeEdge(venezuela,peru);
		makeEdge(peru,argentina);
		makeEdge(venezuela,centralUS);
		makeEdge(centralUS,westernUS);
		makeEdge(westernUS,alberta);
		makeEdge(westernUS,ontario);
		makeEdge(westernUS,easternUS);
		makeEdge(easternUS,ontario);
		makeEdge(easternUS,quebec);
		makeEdge(alberta,alaska);
		makeEdge(alberta,northWestTerritory);
		makeEdge(alberta,ontario);
		makeEdge(ontario,greenland);
		makeEdge(ontario,quebec);
		makeEdge(quebec,greenland);
		makeEdge(alaska,northWestTerritory);
		makeEdge(greenland,northWestTerritory);
		makeEdge(greenland,iceland);
		makeEdge(iceland,unitedKingdom);
		makeEdge(iceland,scandinavia);
		makeEdge(westernEurope,northAfrica);
		makeEdge(unitedKingdom,westernEurope);
		makeEdge(westernEurope,southernEurope);
		makeEdge(unitedKingdom,northernEurope);
		makeEdge(northernEurope,southernEurope);
		makeEdge(southernEurope,middleEast);
		makeEdge(siberia,irkutsk);
		makeEdge(yakutsk,irkutsk);
		makeEdge(yakutsk,kamchatka);
		makeEdge(irkutsk,kamchatka);
		makeEdge(mongolia,kamchatka);
		makeEdge(siberia,yakutsk);
		makeEdge(irkutsk,mongolia);
		makeEdge(scandinavia,northernEurope);
		makeEdge(siberia,mongolia);
		makeEdge(japan,kamchatka);
		makeEdge(japan,mongolia);
		makeEdge(siam,china);
		makeEdge(china,mongolia);
		makeEdge(siam,indonesia);
		makeEdge(indonesia,newGuinea);
		makeEdge(indonesia,westernAustralia);
		makeEdge(newGuinea,westernAustralia);
		makeEdge(easternAustralia,newGuinea);
		makeEdge(westernAustralia,easternAustralia);
		makeEdge(kamchatka,alaska);
	}
	
	private void makeEdge(Territory t, Territory s) {
		t.addAdjacentTerritory(s);
		s.addAdjacentTerritory(t);
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
