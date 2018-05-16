package infomgmag;

import java.util.*;
import java.util.ArrayList;

/**
 * This class contains the main() method. This class is the bridge between the visual presentation of 
 * the game (RiskVisual) and the data presentation of the game (RiskPhysics).
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Risk {

	public static Random random;

	private ArrayList<Player> players;
	private Board board;
	private Player currentPlayer;
	private RiskVisual visuals;
	private Integer nrOfStartingUnits;
	private boolean StopGame;

	public static void main(String[] args) {
		random = new Random(System.currentTimeMillis());
		Risk risk = new Risk();
		risk.run();
	}

	public Risk(){
		initializeGame();
	}

	public void run(){
		while (!finished()) {
			visuals.update();

			System.out.println("Current Player: " + currentPlayer.toString());
			Integer nrOfReinforcements = calculateReinforcements();
			currentPlayer.setReinforcements(currentPlayer.getReinforcements() + nrOfReinforcements);
			currentPlayer.turnInCards(board);
			while(currentPlayer.getReinforcements() != 0){
				System.out.println("place single reinforecements " + currentPlayer.getName());
				currentPlayer.placeSingleReinforcement(board);
			}
			CombatMove combatMove;		// If a territory is claimed the player has to move the units he used during his attack to the claimed territoy, he can move more units to the new territory (atleast one unit has to stay behind)   
			while((combatMove = currentPlayer.getCombatMove()) != null){
				visuals.update(combatMove);
				performCombatMove(combatMove);
				if(StopGame){
					break;
				}
			}
			currentPlayer.fortifyTerritory(board);
			visuals.update();

			nextCurrentPlayer();
		}
		visuals.update();
		System.out.println(players.get(0) + " has won!");

	}

	private boolean playerHasReachedObjective(Player player){
		if(player.objective.getType() == Objective.type.TOTAL_DOMINATION){
			return players.size() == 1;
		}
		return false;
	}

	private void nextCurrentPlayer(){
		currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
		System.out.println("next current player set: " + currentPlayer);

	}

	private void performCombatMove(CombatMove combatMove){
		System.out.println("Before performing combatMove: " + combatMove.toString());
		ArrayList<Integer> attackThrows = new ArrayList<Integer>();
		ArrayList<Integer> defenseThrows = new ArrayList<Integer>();

		System.out.println("Attacking units: " + combatMove.getAttackingUnits());
		System.out.println("Defense units: " + combatMove.getDefendingUnits());
		//Attacker throws dices
		for(int i = 0; i < combatMove.getAttackingUnits(); i++){
			int value = Risk.random.nextInt(6) + 1;
			attackThrows.add(value);
		}

		//Defender throws dices
		for(int i = 0; i < combatMove.getDefendingUnits(); i++){
			int value = Risk.random.nextInt(6) + 1;
			defenseThrows.add(value);
		}

		//Determining the losses of both sides and changes those values
		int attackLoss = 0, defenseLoss = 0;

		if(Collections.max(attackThrows) > Collections.max(defenseThrows)){
			defenseLoss++;
		}else{
			attackLoss++;
		}

		attackThrows.remove(Collections.max(attackThrows));
		if(combatMove.getDefendingUnits() > 1 && combatMove.getAttackingUnits() > 1 && Collections.max(attackThrows) > Collections.min(defenseThrows)){
			defenseLoss++;
		}else if(combatMove.getDefendingUnits() > 1 && combatMove.getAttackingUnits() > 1){
			attackLoss++;
		}

		combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - attackLoss);
		combatMove.getDefendingTerritory().setUnits(combatMove.getDefendingTerritory().getNUnits() - defenseLoss);

		//Update number of units on both territories and new owner
		if(combatMove.getDefendingTerritory().getNUnits() == 0){
			Player defender = combatMove.getDefendingTerritory().getOwner();
			System.out.println(currentPlayer + " conquered " + combatMove.getDefendingTerritory().getName());
			combatMove.getDefendingTerritory().setOwner(currentPlayer);
			int transferredUnits = combatMove.getAttackingTerritory().getNUnits() - 1;
			combatMove.getDefendingTerritory().setUnits(transferredUnits);
			combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredUnits);
			//System.out.println("territories of player: " + combatMove.getDefendingTerritory().getOwner().getTerritories());
			if(isPlayerDead(defender)){
				System.out.println("removed player: " + combatMove.getDefendingTerritory().getOwner());
				players.remove(defender);
			}
			StopGame = playerHasReachedObjective(currentPlayer);
		}

		System.out.println("After performing combatMove: " + combatMove.toString());
	}

	private Integer calculateReinforcements(){
		System.out.println("Calculate reinforcements");
		Integer reinforcements = 0;
		reinforcements += Integer.max(3, currentPlayer.territories.size() / 3);
		reinforcements += calculateContinentBonus();
		return reinforcements;
	}

	private Integer calculateContinentBonus(){
		System.out.println("Calculating continent Bonus");
		int bonus = 0;
		for(Continent continent : board.getContinents()){
			boolean controlsContinent = true;
			for(Territory territory : continent.getMembers()){
				if(territory.getOwner() != currentPlayer){
					controlsContinent = false;
					break;
				}
			}
			if(controlsContinent){
				bonus += continent.getNReinforcements();
			}
		}
		return bonus;
	}

	private void initializeGame() {
		System.out.println("Initializing game");
        visuals = new RiskVisual(this);
		board = new Board();
		nrOfStartingUnits = 30;
        initializePlayers();
        Integer currentPlayerIndex = divideTerritories();
		initialPlaceReinforcements(currentPlayerIndex);
		currentPlayer = players.get(0);

		for(int i = 0; i < players.size(); i++){
			System.out.println(players.get(i).toString());
		}
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	private void initializePlayers() {
		System.out.println("Initializing players");
		players = new ArrayList<Player>();
		//TODO deciding number of startingUnits using number of players and evt. number territorries
		for (int i = 0; i < 6; i++) {
			Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
			Bot player = new Bot(objective, nrOfStartingUnits, "player" + i);
			players.add(player);
		}
	}

	//Divide players randomly over territories
	private Integer divideTerritories(){
		System.out.println("Dividing territories");
		Collections.shuffle(board.getTerritories(), Risk.random);
		int player = 0;
		for(Territory territory : board.getTerritories()){
			territory.setOwner(players.get(player % players.size()));
			territory.setUnits(1);
			territory.getOwner().setReinforcements(territory.getOwner().getReinforcements() - 1);
			player++;
		}
		return player % players.size();
	}

	private void initialPlaceReinforcements(Integer currentPlayerIndex){
		System.out.println("Placing initial reinforcements");
		int player = currentPlayerIndex;
		for(int i = 0; i < (players.size() * nrOfStartingUnits) - board.getTerritories().size() ; i++){
			players.get(player % players.size()).placeSingleReinforcement(board);
			player++;
		}
	}

	public Board getBoard() {
		return this.board;
	}

	public Boolean isPlayerDead(Player player){
		return player.getTerritories().size() == 0;
	}
	
	/**
	 * Returns true if there is a winner.
	 */
	private boolean finished() {
		return players.size() == 1 || StopGame;
	}
	
	public static void println(String str) {
		System.out.println(str);
	}
}
