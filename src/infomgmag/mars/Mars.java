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
    private HashMap<Territory, CountryAgent> countryAgentsByTerritory;
    private Personality personality;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name, Color color, Personality personality) {
        super(objective, reinforcements, name, color);

        cardAgent = new CardAgent(hand);
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();
        this.personality = personality;

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
    public void fortifyTerritory(Board board) { // only uses the 'best' country right now
        ArrayList<ArrayList<CountryAgent>> clusters = getClusters();
        DefensiveBid bestBid = null;
        for (ArrayList<CountryAgent> cluster : clusters) {
            HashMap<CountryAgent, Integer> sellers = new HashMap<CountryAgent, Integer>();

            for (CountryAgent a : cluster) {
                int bestI = 0;
                for (int i = a.getTerritory().getNUnits() - 1; i > 0; i--) {
                    double d = a.getDefenseOdds(i);
    				if(d > personality.getWIN_PERCENTAGE()) {
                        bestI = i;
                    }
                }
                if (bestI != 0) {
                    sellers.put(a, bestI);
                }
            }
            for (CountryAgent a : cluster) {
                for (CountryAgent seller : sellers.keySet()) {
                    DefensiveBid bid = a.getDefensiveBid(seller, a.getTerritory().getNUnits() + sellers.get(seller));
                    if (bestBid == null || bestBid.getUtility() < bid.getUtility())
                        bestBid = bid;
                }
            }
        }

        if (bestBid != null) {
            board.moveUnits(bestBid.getFortifyingAgent().getTerritory(), bestBid.getReinforcedAgent().getTerritory(),
                    bestBid.getUnits());
        }
    }

    @Override
    public void movingInAfterInvasion(CombatMove combatMove) {
        setHasConqueredTerritoryInTurn(true);
        int transferredunits = combatMove.getAttackingTerritory().getNUnits() - 1;
        combatMove.getDefendingTerritory().setUnits(transferredunits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredunits);
    }

    @Override
    public void placeReinforcements(Board board) {
        for (CountryAgent ca : countryAgents) {
            ca.clearlists();
            ca.calculateOwnershipValue(personality);
        }

        for (CountryAgent sender : countryAgents) {
            if (sender.getTerritory().getOwner() != this) {
                sender.createGoals();
            }
        }

        while (reinforcements > 0) {
            ArrayList<ReinforcementBid> bids = new ArrayList<>();
            for (CountryAgent ca : countryAgents) {
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
        // TODO Auto-generated method stub
        return Math.min(2, combatMove.getDefendingTerritory().getNUnits());
    }

    @Override
    public void attackPhase(CombatInterface ci) {
        while (true) {
            if (ci.getActivePlayerAmount() == 1) {
                return;
            }
            for (CountryAgent ca : countryAgents) {
                ca.clearlists();
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
                    .filter(ca -> ca.getTerritory().getNUnits() > 1)
                    // Collect all offensive bids
                    .map(ca -> ca.getBestBid(0)).filter(x -> x instanceof OffensiveBid).map(x -> (OffensiveBid) x)
                    // Create attackbids from the best
                    .max((x, y) -> x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1));
            
            if (!ob.isPresent())
                return;
  
            AttackBid ab = new AttackBid(ob.get().getReinforcedAgent().getTerritory(),
                            ob.get().getGoal().getFirstGoal().getTerritory());
            ci.performCombatMove(ab.toCombatMove());
        }
    }

    public Personality getPersonality() {
        return personality;
    }
}
