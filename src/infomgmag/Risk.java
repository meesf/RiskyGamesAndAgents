package infomgmag;

import infomgmag.mars.Mars;
import infomgmag.mars.PersonalityFactory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;

public class Risk implements CombatInterface {

    // Variables to be customized by user
    private boolean visible = true;

    public static Random random;

    public static ArrayList<ArrayList<Double>> DICE_ODDS_ONE, DICE_ODDS_TWO;

    private ArrayList<Player> activePlayers;
    private ArrayList<Player> defeatedPlayers;
    private Player currentPlayer;

    private Board board;
    private RiskVisual visuals;

    private int turn = 0;
    private int maxTurns = 100;
    private int initialArmies;
    private boolean StopGame;
    
    public ArrayList<CombatEvent> combatLog;
    public ArrayList<TurnLog> turnLog;
    
    public Risk(boolean visible) {
        this.visible = visible;
        createDiceOdds();
        visuals = new RiskVisual(this,visible);
    }
    
    public void initialize(long seed, HashMap<String, String> players, int speed) {
        random = new Random(seed);
        turn = 0;
        StopGame = false;

        board = new Board(visuals);
        combatLog = new ArrayList<>();
        turnLog = new ArrayList<>();
        defeatedPlayers = new ArrayList<Player>();
        initialArmies = getInitialArmies(players);
        initializePlayers(players);
        int currentPlayerIndex = divideTerritories();
        initialPlaceReinforcements(currentPlayerIndex);
        visuals.setTargetFrameDuration(speed);
        currentPlayer = activePlayers.get(random.nextInt(activePlayers.size()));
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

    public void run() {
        while (!finished()) {
            visuals.update();
            currentPlayer.setHasConqueredTerritoryInTurn(false);
            int nrOfReinforcements = calculateReinforcements();
            addTurnLog(nrOfReinforcements);
            currentPlayer.setReinforcements(nrOfReinforcements);
            currentPlayer.turnInCards(board);
            currentPlayer.placeReinforcements(board);

            int startingNrOfTerritories = currentPlayer.getTerritories().size();

            currentPlayer.attackPhase((CombatInterface) this);
            currentPlayer.fortifyTerritory(board);
            visuals.update();

            int endingNrOfTerritories = currentPlayer.getTerritories().size();
            if (startingNrOfTerritories < endingNrOfTerritories)
                board.drawCard(currentPlayer);
            nextCurrentPlayer();

            turn++;
        }
        visuals.log(activePlayers.size() == 1 ? activePlayers.get(0) + " has won!" : "There is no winner, game takes too long");
        visuals.setVisible(false);
    }
    
    public void addTurnLog(Integer reinforcements) {
        HashMap<Player, Integer> totalArmies = new HashMap<Player, Integer>();
        HashMap<Player, ArrayList<Territory>> territories = new HashMap<Player, ArrayList<Territory>>();
        for(Player p : activePlayers) {
            int armies = 0;
            ArrayList<Territory> playerTerritories = new ArrayList<Territory>(); 
            for(Territory t : p.getTerritories()) {
                armies += t.getUnits();
                playerTerritories.add(t);
            }
            totalArmies.put(p, armies);
            territories.put(p, playerTerritories);
        }
        for(Player p : defeatedPlayers) {
            totalArmies.put(p, 0);
            territories.put(p, new ArrayList<Territory>());
        }

        turnLog.add(new TurnLog(turn, currentPlayer, reinforcements, totalArmies, territories));
    }

    public int getTurn() {
        return turn;
    }

    private void nextCurrentPlayer() {
        currentPlayer = activePlayers.get((activePlayers.indexOf(currentPlayer) + 1) % activePlayers.size());
    }

    public void performCombatMove(CombatMove combatMove) {
        if (combatMove.getAttackingUnits() > 3)
            combatMove.setAttackingUnits(3);
        if (combatMove.getAttackingUnits() > combatMove.getAttackingTerritory().getUnits() - 1)
            throw new RuntimeException("Rule breach: Not enough units on attacking territory");

        // Attacker throws dice
        ArrayList<Integer> attackThrows = new ArrayList<>();
        for (int i = 0; i < combatMove.getAttackingUnits(); i++) {
            int value = Risk.random.nextInt(6) + 1;
            attackThrows.add(value);
        }
        combatMove.setAttackThrows(attackThrows);

        int defendingAmount = combatMove.getDefendingTerritory().getOwner().getDefensiveDice(combatMove);
        if (defendingAmount > combatMove.getDefendingTerritory().getUnits() || defendingAmount > 2 || defendingAmount < 1)
            throw new RuntimeException("Rule breach: Defending amount not allowed: " + combatMove);
        combatMove.setDefendingUnits(defendingAmount);

        visuals.update(combatMove);

        ArrayList<Integer> defenseThrows = new ArrayList<>();

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

        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getUnits() - attackLoss);
        combatMove.getDefendingTerritory().setUnits(combatMove.getDefendingTerritory().getUnits() - defenseLoss);

        boolean captured = combatMove.getDefendingTerritory().getUnits() == 0;

        combatLog.add(new CombatEvent(
                combatMove.getAttackingTerritory().getOwner(),
                combatMove.getDefendingTerritory().getOwner(),
                combatMove.getAttackingTerritory(),
                combatMove.getDefendingTerritory(),
                combatMove.getAttackingUnits(),
                combatMove.getDefendingUnits(),
                attackLoss == 0 ? CombatEvent.ATTACKER_WINS :
                        (defenseLoss == 0 ? CombatEvent.DEFENDER_WINS :
                                CombatEvent.ONE_EACH),
                turn,
                captured,
                attackLoss,
                defenseLoss));
        
        // Update number of units on both territories and new owner
        if (captured) {
            Player defender = combatMove.getDefendingTerritory().getOwner();
            combatMove.getDefendingTerritory().setOwner(currentPlayer);
            currentPlayer.movingInAfterInvasion(board, combatMove);

            if (isPlayerDead(defender)) {
                // Attacker receives all the territory cards of the defender.
                currentPlayer.hand.setWildCards(currentPlayer.hand.getWildcards() + defender.hand.getWildcards());
                currentPlayer.hand.setArtillery(currentPlayer.hand.getArtillery() + defender.hand.getArtillery());
                currentPlayer.hand.setCavalry(currentPlayer.hand.getCavalry() + defender.hand.getCavalry());
                currentPlayer.hand.setInfantry(currentPlayer.hand.getInfantry() + defender.hand.getInfantry());
                while (currentPlayer.hand.getNumberOfCards() > 4)
                    currentPlayer.turnInCards(board);
                activePlayers.remove(defender);
                if (activePlayers.size() > 1)
                    currentPlayer.placeReinforcements(board);
                defeatedPlayers.add(defender);
            }
        }
    }

    private int calculateReinforcements() {
        int reinforcements = 0;
        reinforcements += Integer.max(3, currentPlayer.territories.size() / 3);
        reinforcements += calculateContinentBonus();
        return reinforcements;
    }

    private int calculateContinentBonus() {
        int bonus = 0;
        for (Continent continent : board.getContinents()) {
            boolean controlsContinent = true;
            for (Territory territory : continent.getTerritories())
                if (territory.getOwner() != currentPlayer) {
                    controlsContinent = false;
                    break;
                }
            if (controlsContinent)
                bonus += continent.getReinforcements();
        }
        return bonus;
    }

    public ArrayList<Player> getActivePlayers() {
        return activePlayers;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    private Color[] playerColors = {
            Color.RED,
            Color.BLUE,
            Color.BLACK,
            Color.GRAY,
            Color.ORANGE,
            Color.MAGENTA
    };

    private void initializePlayers(HashMap<String, String> players) {
        activePlayers = new ArrayList<>();
        
        for (Entry<String, String> entry : players.entrySet()) {
            Objective objective = new Objective(Objective.type.TOTAL_DOMINATION);
            Color color;
            Player player = null;
            if (activePlayers.size() < playerColors.length) {
                color = playerColors[activePlayers.size()];
            } else {
                color = new Color(Risk.random.nextFloat() * 0.8f + 0.2f, Risk.random.nextFloat() * 0.8f + 0.2f,
                        Risk.random.nextFloat() * 0.8f + 0.2f);
            }

            if(entry.getValue() == "aggressive")
                player = new Mars(this, objective, 0, entry.getKey(), color, PersonalityFactory.agressivePersonality());
            if(entry.getValue() == "normal")
                player = new Mars(this, objective, 0, entry.getKey(), color, PersonalityFactory.normalPersonality());
            if(entry.getValue() == "defensive")
                player = new Mars(this, objective, 0, entry.getKey(), color, PersonalityFactory.defensivePersonality());
            if(entry.getValue() == "continent")
                player = new Mars(this, objective, 0, entry.getKey(), color, PersonalityFactory.continentPersonality());
            if(player == null)
                player = new RandomBot(objective, 0, entry.getKey(), color);
            activePlayers.add(player);
        }
        shufflePlayers();
    }
    
    private void shufflePlayers() {
    	int index;
    	Player temp;
    	for(int i = activePlayers.size() - 1; i > 0; i--) {
    		index = random.nextInt(i + 1);
    		temp = activePlayers.get(index);
    		activePlayers.set(index, activePlayers.get(i));
    		activePlayers.set(i, temp);
    	}
    }

    // Divide players randomly over territories
    private int divideTerritories() {
        Collections.shuffle(board.getTerritories(), Risk.random);
        int player = 0;
        for (Territory territory : board.getTerritories()) {
            territory.setOwner(activePlayers.get(player % activePlayers.size()));
            territory.setUnits(1);
            player++;
        }
        return player % activePlayers.size();
    }

    private void initialPlaceReinforcements(int currentPlayerIndex) {
        int player = currentPlayerIndex;
        for (int i = 0; i < (activePlayers.size() * initialArmies) - board.getTerritories().size(); i++) {
            activePlayers.get(player % activePlayers.size()).setReinforcements(1);
            activePlayers.get(player % activePlayers.size()).placeReinforcements(board);
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
        return activePlayers.size() == 1 || StopGame || turn > maxTurns;
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
    
    public int getInitialArmies(HashMap<String,String> players) {
    	return 50 - (5 * players.size());
    }

    public ArrayList<Player> getDefeatedPlayers() {
        return this.defeatedPlayers;
    }

    public int getActivePlayerAmount() {
        return activePlayers.size();
    }

    public ArrayList<CombatEvent> getCombatLog() {
        return combatLog;
    }
}


