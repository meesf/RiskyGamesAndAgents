package infomgmag;

import infomgmag.mars.Mars;

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
    public static ArrayList<ArrayList<Double>> DICE_ODDS_ONE, DICE_ODDS_TWO;
    
    private int turn = 0;

    private ArrayList<Player> players;
    private Board board;
    private Player currentPlayer;
    private RiskVisual visuals;
    private Integer nrOfStartingUnits;
    private boolean StopGame;

    private ArrayList<Player> defeatedPlayers;
    private boolean visible = false;
    

    public static void main(String[] args) {
        random = new Random(System.currentTimeMillis());
        createDiceOdds();
        Risk risk = new Risk();
        risk.run();
    }
    
    public static void createDiceOdds() {
    	DICE_ODDS_ONE = new ArrayList<ArrayList<Double>>();
    	ArrayList<Double> oneA = new ArrayList<Double>();
    	oneA.add(15.0/36);
    	oneA.add(125.0/216);
    	oneA.add(855.0/1296);
    	DICE_ODDS_ONE.add(oneA);
    	ArrayList<Double> oneD = new ArrayList<Double>();
    	oneD.add(21.0/36);
    	oneD.add(91.0/216);
    	oneD.add(441.0/1296);
    	DICE_ODDS_ONE.add(oneD);
        DICE_ODDS_TWO = new ArrayList<ArrayList<Double>>();
        ArrayList<Double> twoA = new ArrayList<Double>();
        twoA.add(55.0/216);
        twoA.add(295.0/1296);
        twoA.add(2890.0/7776);
    	DICE_ODDS_TWO.add(twoA);
    	ArrayList<Double> twoD = new ArrayList<Double>();
        twoD.add(161.0/216);
        twoD.add(581.0/1296);
        twoD.add(2275.0/7776);
    	DICE_ODDS_TWO.add(twoD);
    	ArrayList<Double> twoL = new ArrayList<Double>();
        twoL.add(null);
        twoL.add(420.0/1296);
        twoL.add(2611.0/7776);
    	DICE_ODDS_TWO.add(twoL);
    }
    
    public Risk() {
        initializeGame();
    }

    public void run() {
        while (!finished()) {
            visuals.update();
            Integer nrOfReinforcements = calculateReinforcements();
            currentPlayer.setReinforcements(nrOfReinforcements);
            currentPlayer.turnInCards(board);
            currentPlayer.placeReinforcements(board);

            int startingNrOfTerritories = currentPlayer.getTerritories().size();
            CombatMove combatMove; // If a territory is claimed the player has to move the units he used during his
                                   // attack to the claimed territoy, he can move more units to the new territory
                                   // (atleast one unit has to stay behind)
            while ((combatMove = currentPlayer.getCombatMove()) != null) {
                visuals.update(combatMove);
                performCombatMove(combatMove);
                if (StopGame)
                    break;
            }
            currentPlayer.fortifyTerritory(board);
            visuals.update();

            int endingNrOfTerritories = currentPlayer.getTerritories().size();
            if (startingNrOfTerritories < endingNrOfTerritories)
                board.drawCard(currentPlayer);
            nextCurrentPlayer();

            turn++;
        }
        visuals.update();
        visuals.log(players.get(0) + " has won!");
    }

    public int getTurn() {
        return turn;
    }

    private boolean playerHasReachedObjective(Player player) {
        if (player.objective.getType() == Objective.type.TOTAL_DOMINATION)
            return players.size() == 1;
        return false;
    }

    private void nextCurrentPlayer() {
        currentPlayer = players.get((players.indexOf(currentPlayer) + 1) % players.size());
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
            combatMove.getDefendingTerritory().setOwner(currentPlayer);
            currentPlayer.movingInAfterInvasion(combatMove);

            if (isPlayerDead(defender)) {
                // Attacker receives all the territory cards of the defender.
                currentPlayer.hand.setWildCards(currentPlayer.hand.getWildcards() + defender.hand.getWildcards());
                currentPlayer.hand.setArtillery(currentPlayer.hand.getArtillery() + defender.hand.getArtillery());
                currentPlayer.hand.setCavalry(currentPlayer.hand.getCavalry() + defender.hand.getCavalry());
                currentPlayer.hand.setInfantry(currentPlayer.hand.getInfantry() + defender.hand.getInfantry());
                while (currentPlayer.hand.getNumberOfCards() > 4)
                    currentPlayer.turnInCards(board);
                currentPlayer.placeReinforcements(board);
                players.remove(defender);
                defeatedPlayers.add(defender);
            }
            StopGame = playerHasReachedObjective(currentPlayer);
        }
    }

    private Integer calculateReinforcements() {
        Integer reinforcements = 0;
        reinforcements += Integer.max(3, currentPlayer.territories.size() / 3);
        reinforcements += calculateContinentBonus();
        return reinforcements;
    }

    private Integer calculateContinentBonus() {
        int bonus = 0;
        for (Continent continent : board.getContinents()) {
            boolean controlsContinent = true;
            for (Territory territory : continent.getMembers())
                if (territory.getOwner() != currentPlayer) {
                    controlsContinent = false;
                    break;
                }
            if (controlsContinent)
                bonus += continent.getNReinforcements();
        }
        return bonus;
    }

    private void initializeGame() {
        visuals = new RiskVisual(this,visible);
        board = new Board();
        defeatedPlayers = new ArrayList<Player>();
        nrOfStartingUnits = 30;
        initializePlayers();
        Integer currentPlayerIndex = divideTerritories();
        initialPlaceReinforcements(currentPlayerIndex);
        currentPlayer = players.get(0);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private void initializePlayers() {
        players = new ArrayList<>();
        // TODO deciding number of startingUnits using number of players and evt. number
        // territorries
        for (int i = 0; i < 2; i++) {
            Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
            RandomBot player = new RandomBot(objective, 0, "player" + i);
            players.add(player);
        }
        addMarsAgent();
    }

    private void addMarsAgent(){
        Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
        Mars player = new Mars(this, objective, 0, "Mars agent");
        players.add(player);
    }

    // Divide players randomly over territories
    private Integer divideTerritories() {
        Collections.shuffle(board.getTerritories(), Risk.random);
        int player = 0;
        for (Territory territory : board.getTerritories()) {
            territory.setOwner(players.get(player % players.size()));
            territory.setUnits(1);
            player++;
        }
        return player % players.size();
    }

    private void initialPlaceReinforcements(Integer currentPlayerIndex) {
        int player = currentPlayerIndex;
        for (int i = 0; i < (players.size() * nrOfStartingUnits) - board.getTerritories().size(); i++) {
            players.get(player % players.size()).setReinforcements(1);
            players.get(player % players.size()).placeReinforcements(board);
            player++;
        }
    }

    public Board getBoard() {
        return this.board;
    }

    public Boolean isPlayerDead(Player player) {
        return player.getTerritories().size() == 0;
    }

    /**
     * Returns true if there is a winner.
     */
    private boolean finished() {
        return players.size() == 1 || StopGame;
    }
    
    public static ArrayList<Territory> getConnectedTerritories(Territory origin) {
        ArrayList<Territory> visited = new ArrayList<>();
        ArrayList<Territory> result = new ArrayList<>();
        result.add(origin);
        boolean foundTerritory = true;
        while (foundTerritory) {
            foundTerritory = false;
            ArrayList<Territory> add = new ArrayList<>();
            for (Territory t : result)
                if (!visited.contains(t)) {
                    for (Territory c : t.getAdjacentTerritories())
                        if (!result.contains(c) && origin.getOwner().getTerritories().contains(c)) {
                            add.add(c);
                            foundTerritory = true;
                        }
                    visited.add(t);
                }
            for (Territory t : add)
                result.add(t);
        }
        return result;
    }

    public static void printError(String str) {
    	System.err.println("Error:"+str);
    	System.exit(1);
    }

    public ArrayList<Player> getDefeatedPlayers(){
        return this.defeatedPlayers;
    }

}
