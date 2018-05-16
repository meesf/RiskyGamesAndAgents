package infomgmag;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * This class contains the main() method. This class is the bridge between the
 * visual presentation of the game (RiskVisual) and the data presentation of the
 * game (RiskPhysics).
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Risk {

	public static Random random;

	public static void main(String[] args) {
		random = new Random(System.currentTimeMillis());
		Risk risk = new Risk();
		risk.run();
	}

	public static void println(String str) {
		System.out.println(str);
	}

	private int turn = 0;
	private ArrayList<Player> players;
	private Board board;
	private Player currentPlayer;
	private RiskVisual visuals;

	private Integer nrOfStartingUnits;

	private boolean StopGame;

	public Risk() {
		this.initializeGame();
	}

	private Integer calculateContinentBonus() {
		int bonus = 0;
		for (Continent continent : this.board.getContinents()) {
			boolean controlsContinent = true;
			for (Territory territory : continent.getMembers())
				if (territory.getOwner() != this.currentPlayer) {
					controlsContinent = false;
					break;
				}
			if (controlsContinent)
				bonus += continent.getNReinforcements();
		}
		return bonus;
	}

	private Integer calculateReinforcements() {
		Integer reinforcements = 0;
		reinforcements += Integer.max(3, this.currentPlayer.territories.size() / 3);
		reinforcements += this.calculateContinentBonus();
		return reinforcements;
	}

	// Divide players randomly over territories
	private Integer divideTerritories() {
		Collections.shuffle(this.board.getTerritories(), Risk.random);
		int player = 0;
		for (Territory territory : this.board.getTerritories()) {
			territory.setOwner(this.players.get(player % this.players.size()));
			territory.setUnits(1);
			player++;
		}
		return player % this.players.size();
	}

	/**
	 * Returns true if there is a winner.
	 */
	private boolean finished() {
		return this.players.size() == 1 || this.StopGame;
	}

	public Board getBoard() {
		return this.board;
	}

	public Player getCurrentPlayer() {
		return this.currentPlayer;
	}

	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	public int getTurn() {
		return this.turn;
	}

	private void initializeGame() {
		this.visuals = new RiskVisual(this);
		this.board = new Board();
		this.nrOfStartingUnits = 30;
		this.initializePlayers();
		Integer currentPlayerIndex = this.divideTerritories();
		this.initialPlaceReinforcements(currentPlayerIndex);
		this.currentPlayer = this.players.get(0);
	}

	private void initializePlayers() {
		this.players = new ArrayList<>();
		// TODO deciding number of startingUnits using number of players and evt. number
		// territorries
		for (int i = 0; i < 6; i++) {
			Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
			Bot player = new Bot(objective, 0, "player" + i);
			this.players.add(player);
		}
	}

	private void initialPlaceReinforcements(Integer currentPlayerIndex) {
		int player = currentPlayerIndex;
		for (int i = 0; i < this.players.size() * this.nrOfStartingUnits - this.board.getTerritories().size(); i++) {
			this.players.get(player % this.players.size()).setReinforcements(1);
			this.players.get(player % this.players.size()).placeReinforcements(this.board);
			player++;
		}
	}

	public Boolean isPlayerDead(Player player) {
		return player.getTerritories().size() == 0;
	}

	private void nextCurrentPlayer() {
		this.currentPlayer = this.players.get((this.players.indexOf(this.currentPlayer) + 1) % this.players.size());
	}

	private void performCombatMove(CombatMove combatMove) {
		ArrayList<Integer> attackThrows = new ArrayList<>();
		ArrayList<Integer> defenseThrows = new ArrayList<>();

		// Attacker throws dices
		for (int i = 0; i < combatMove.getAttackingUnits(); i++) {
			int value = Risk.random.nextInt(6) + 1;
			attackThrows.add(value);
		}

		// Defender throws dices
		for (int i = 0; i < combatMove.getDefendingUnits(); i++) {
			int value = Risk.random.nextInt(6) + 1;
			defenseThrows.add(value);
		}

		// Determining the losses of both sides and changes those values
		int attackLoss = 0, defenseLoss = 0;

		if (Collections.max(attackThrows) > Collections.max(defenseThrows))
			defenseLoss++;
		else
			attackLoss++;

		attackThrows.remove(Collections.max(attackThrows));
		if (combatMove.getDefendingUnits() > 1 && combatMove.getAttackingUnits() > 1
				&& Collections.max(attackThrows) > Collections.min(defenseThrows))
			defenseLoss++;
		else if (combatMove.getDefendingUnits() > 1 && combatMove.getAttackingUnits() > 1)
			attackLoss++;

		combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - attackLoss);
		combatMove.getDefendingTerritory().setUnits(combatMove.getDefendingTerritory().getNUnits() - defenseLoss);

		// Update number of units on both territories and new owner
		if (combatMove.getDefendingTerritory().getNUnits() == 0) {
			Player defender = combatMove.getDefendingTerritory().getOwner();
			combatMove.getDefendingTerritory().setOwner(this.currentPlayer);
			this.currentPlayer.movingInAfterInvasion(combatMove);

			if (this.isPlayerDead(defender)) {
				// Attacker receives all the territory cards of the defender.
				this.currentPlayer.hand
						.setWildCards(this.currentPlayer.hand.getWildcards() + defender.hand.getWildcards());
				this.currentPlayer.hand
						.setArtillery(this.currentPlayer.hand.getArtillery() + defender.hand.getArtillery());
				this.currentPlayer.hand.setCavalry(this.currentPlayer.hand.getCavalry() + defender.hand.getCavalry());
				this.currentPlayer.hand
						.setInfantry(this.currentPlayer.hand.getInfantry() + defender.hand.getInfantry());
				while (this.currentPlayer.hand.getNumberOfCards() > 4)
					this.currentPlayer.turnInCards(this.board);
				this.currentPlayer.placeReinforcements(this.board);
				this.players.remove(defender);
			}
			this.StopGame = this.playerHasReachedObjective(this.currentPlayer);
		}
	}

	private boolean playerHasReachedObjective(Player player) {
		if (player.objective.getType() == Objective.type.TOTAL_DOMINATION)
			return this.players.size() == 1;
		return false;
	}

	public void run() {
		while (!this.finished()) {
			this.visuals.update();
			Integer nrOfReinforcements = this.calculateReinforcements();
			this.currentPlayer.setReinforcements(this.currentPlayer.getReinforcements() + nrOfReinforcements);
			this.currentPlayer.turnInCards(this.board);
			this.currentPlayer.placeReinforcements(this.board);

			int startingNrOfTerritories = this.currentPlayer.getTerritories().size();
			CombatMove combatMove; // If a territory is claimed the player has to move the units he used during his
									// attack to the claimed territoy, he can move more units to the new territory
									// (atleast one unit has to stay behind)
			while ((combatMove = this.currentPlayer.getCombatMove()) != null) {
				this.visuals.update(combatMove);
				this.performCombatMove(combatMove);
				if (this.StopGame)
					break;
			}
			this.currentPlayer.fortifyTerritory(this.board);
			this.visuals.update();

			int endingNrOfTerritories = this.currentPlayer.getTerritories().size();
			if (startingNrOfTerritories < endingNrOfTerritories)
				this.board.drawCard(this.currentPlayer);
			this.nextCurrentPlayer();

			this.turn++;
		}
		this.visuals.update();
		System.out.println(this.players.get(0) + " has won!");
	}
}
