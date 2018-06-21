package infomgmag.mars;

import java.awt.Color;
import java.util.*;
import infomgmag.Board;
import infomgmag.CombatInterface;
import infomgmag.CombatMove;
import infomgmag.Objective;
import infomgmag.Player;
import infomgmag.Risk;
import infomgmag.Territory;

public class Mars extends Player {

    /*
     * This agent can be regarded as the mediator agent from the paper.
     */

    private CardAgent cardAgent;
    private List<CountryAgent> countryAgents;
    public HashMap<Territory, CountryAgent> countryAgentsByTerritory;
    private Personality personality;
    private HashMap<Player, Double> playerDispositions;
    private Risk risk;

    public Mars(Risk risk, Objective objective, int reinforcements, String name, Color color, Personality personality) {
        super(objective, reinforcements, name, color);

        cardAgent = new CardAgent(hand);
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();
        this.personality = personality;
        this.risk = risk;
        playerDispositions = new HashMap<>();

        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t, this);
            countryAgents.add(ca);
            countryAgentsByTerritory.put(t, ca);
            t.setTerritoryCountryAgent(ca);
        }

        countryAgents.stream().forEach(ca -> ca.getTerritory().getAdjacentTerritories().stream()
                .forEach(at -> countryAgentsByTerritory.get(at).addAdjacentAgent(ca)));
    }

    @Override
    public void turnInCards(Board board) {
        this.reinforcements += cardAgent.tradeIn(board);
    }

    private ArrayList<CountryAgent> getCountryAgentList(ArrayList<Territory> cluster) {
        ArrayList<CountryAgent> agents = new ArrayList<CountryAgent>();
        for (Territory t : cluster) {
            agents.add(countryAgentsByTerritory.get(t));
        }
        return agents;
    }

    private ArrayList<ArrayList<CountryAgent>> getClusters() {
        ArrayList<ArrayList<CountryAgent>> clusters = new ArrayList<ArrayList<CountryAgent>>();
        for (Territory t : territories) {
            boolean contains = false;
            for (ArrayList<CountryAgent> cl : clusters) {
                if (cl.contains(countryAgentsByTerritory.get(t))) {
                    contains = true;
                }
            }
            if (!contains) {
                ArrayList<Territory> cluster = Risk.getConnectedTerritories(t);
                clusters.add(getCountryAgentList(cluster));
            }
        }
        return clusters;
    }

    @Override
    public void fortifyTerritory(Board board) {
        // First create clusters
        ArrayList<ArrayList<CountryAgent>> clusters = getClusters();

        for (ArrayList<CountryAgent> cluster : clusters) {
            // Get all fortifierBids
            int maxUnits =
                    cluster.stream()
                    .map(CountryAgent :: getTerritory)
                    .mapToInt(Territory :: getUnits)
                    .max()
                    .orElse(0);
            ArrayList<FortifierBid> fortifierBids = new ArrayList<>();
            ArrayList<ReinforcementBid> reinforcementBids = new ArrayList<>();
            for (CountryAgent ca : cluster) {
                fortifierBids.addAll(ca.getFortifierBids());
                reinforcementBids.addAll(ca.getBids(maxUnits));
            }
            // Get biggest possible utility gain
            FortifierBid bestfb = null;
            ReinforcementBid bestrb = null;
            double bestUtilGain = 0;
            for (FortifierBid fb : fortifierBids) {
                for (ReinforcementBid rb : reinforcementBids) {
                    if (fb.getFortifier() != rb.getReinforcedAgent() &&
                            fb.getUnits() == rb.getUnits()) {
                        double utilGain = rb.getUtility() * rb.getUnits() + fb.getUtility();
                        if (utilGain > bestUtilGain) {
                            bestfb = fb;
                            bestrb = rb;
                            bestUtilGain = utilGain;
                        }
                    }
                }
            }
            // If there was something better than 0 then move the units
            if (bestUtilGain > 0) {
                board.moveUnits(bestfb.getFortifier().getTerritory(), bestrb.getReinforcedAgent().getTerritory(), bestfb.getUnits());
            }
        }
    }

    @Override
    public void movingInAfterInvasion(Board board, CombatMove combatMove) {
        setHasConqueredTerritoryInTurn(true);
        combatMove.getDefendingTerritory().setUnits(combatMove.getAttackingUnits());
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getUnits() - combatMove.getAttackingUnits());
        
        CountryAgent fortifier = countryAgentsByTerritory.get(combatMove.getAttackingTerritory());
        CountryAgent reinforced = countryAgentsByTerritory.get(combatMove.getDefendingTerritory());
        
        ArrayList<FortifierBid> fortifierBids = fortifier.getFortifierBids();
        ArrayList<ReinforcementBid> reinforcementBids = reinforced.getBids(combatMove.getAttackingTerritory().getUnits() - 1);
        
        FortifierBid bestfb = null;
        double bestUtilGain = 0;
        for (FortifierBid fb : fortifierBids) {
            for (ReinforcementBid rb : reinforcementBids) {
                if (fb.getFortifier() != rb.getReinforcedAgent() &&
                        fb.getUnits() == rb.getUnits()) {
                    double utilGain = rb.getUtility() * rb.getUnits() + fb.getUtility();
                    if (utilGain > bestUtilGain) {
                        bestfb = fb;
                        bestUtilGain = utilGain;
                    }
                }
            }
        }

        if (bestUtilGain > 0) {
        	int transferredUnits = bestfb.getUnits();
            combatMove.getDefendingTerritory().setUnits(combatMove.getDefendingTerritory().getUnits() + transferredUnits);
            combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getUnits() - transferredUnits);
        }
    }

    @Override
    public void placeReinforcements(Board board) {
        this.calculateHatedPlayer();
        for (CountryAgent ca : countryAgents) {
            ca.clearGoals();
        }

        for (CountryAgent sender : countryAgents) {
            if (sender.getTerritory().getOwner() != this) {
                sender.createGoals();
            }
        }

        while (reinforcements > 0) {
            ArrayList<ReinforcementBid> bids = new ArrayList<>();
            for (CountryAgent ca : countryAgents) {
                ca.calculateOwnershipValue(personality);
                if (ca.getTerritory().getOwner() == this) {
                    bids.addAll(ca.getBids(reinforcements));
                }
            }

            ReinforcementBid bid = bids.stream()
                    .max((x, y) -> x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1))
                    .get();
            if (bid.getUnits() == 0)
                bid = bids.stream().filter(x -> x.getUnits() > 0).max(
                        (x, y) -> x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1))
                        .get();

            board.addUnits(this, bid.getReinforcedAgent().getTerritory(), bid.getUnits());
            reinforcements -= bid.getUnits();

        }
    }

    @Override
    public int getDefensiveDice(CombatMove combatMove) {
        if (combatMove.getAttackThrows().stream().mapToDouble(x -> x).average().getAsDouble() < 3.5)
            return Math.min(combatMove.getDefendingTerritory().getUnits(), 2);
        return 1;
    }

    @Override
    public void attackPhase(CombatInterface ci) {
        while (true) {
            if (ci.getActivePlayerAmount() == 1) {
                return;
            }
            for (CountryAgent ca : countryAgents) {
                ca.clearGoals();
                ca.calculateOwnershipValue(personality);
            }
            for (CountryAgent sender : countryAgents) {
                if (sender.getTerritory().getOwner() != this) {
                    sender.createGoals();
                }
            }

            Optional<OffensiveBid> ob = countryAgents.stream()
                    // Need to own territory, border an enemy, and have units to attack with
                    .filter(ca -> ca.getTerritory().getOwner() == this).filter(ca -> ca.bordersEnemy())
                    .filter(ca -> ca.getTerritory().getUnits() > 1)
                    // Collect all offensive bids
                    .map(ca -> ca.getAttackBid()).filter(x -> x instanceof OffensiveBid).map(x -> (OffensiveBid) x)
                    // Create attackbids from the best
                    .max((x, y) -> x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1));
            
            if (!ob.isPresent())
                return;

            CombatMove cm = new CombatMove();
            cm.setAttackingTerritory(ob.get().reinforcedAgent.getTerritory());
            cm.setDefendingTerritory(ob.get().getGoal().getFirstGoal().getTerritory());
            cm.setAttackingUnits(ob.get().reinforcedAgent.getTerritory().getUnits() - 1);
            ci.performCombatMove(cm);
        }
    }

    public void calculateHatedPlayer(){
        Double playerHatred = 0.0;
        for (Player enemyPlayer : risk.getActivePlayers()){
            playerDispositions.put(enemyPlayer, playerHatred);
        }
        if (risk.combatLog.isEmpty()){ }
        else {
            for (int i = 1; i <= Math.min(125, risk.combatLog.size()); i++) {
                if (risk.combatLog.get(risk.combatLog.size() - i ).getDefendingPlayer() == this) {
                    playerHatred += 0.5;
                    if (risk.combatLog.get(risk.combatLog.size() - i).getCaptured()) {
                        playerHatred += 2.0;
                    }
                    playerHatred += (risk.combatLog.get(risk.combatLog.size() - i).getDefendingCasualties()) * 0.25;
                    playerDispositions.put(risk.combatLog.get(risk.combatLog.size() - i).getAttackingPlayer(), playerHatred);
                }
            }
        }
        for (Player player : risk.getDefeatedPlayers()) {
            if (playerDispositions.containsKey(player)){
                playerDispositions.remove(player);
            }
        }
        if (playerHatred != 0) {
            for (CountryAgent ca : countryAgents){
                ca.isHated(playerDispositions.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey());
            }
        }
    }

    public Personality getPersonality() {
        return personality;
    }
}
