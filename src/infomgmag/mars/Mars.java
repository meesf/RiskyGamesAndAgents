package infomgmag.mars;

import java.awt.Color;
import java.util.*;
import java.util.stream.Stream;

import infomgmag.Board;
import infomgmag.CombatInterface;
import infomgmag.CombatMove;
import infomgmag.Objective;
import infomgmag.Player;
import infomgmag.Risk;
import infomgmag.Territory;
import sun.net.www.content.audio.x_aiff;

public class Mars extends Player {

    /*
     * This agent can be regarded as the mediator agent from the paper.
     */

    private CardAgent cardAgent;
    private List<CountryAgent> countryAgents;
    private HashMap<Territory,CountryAgent> countryAgentsByTerritory;
    private HashMap<CountryAgent, Double> agentValues;

    private Double friendliesweight = 1.2;      //parameters used in calculation of territory value
    private Double enemiesweight = -0.3;
    private Double farmiesweight = 0.05;
    private Double earmiesweight = -0.03;
    public Integer goalLength = 3;
    
    public static final Double WIN_PERCENTAGE = 0.7375;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name, Color color) {
        super(objective, reinforcements, name, color);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent(hand);
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();


        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t,this);
            countryAgents.add(ca);
            countryAgentsByTerritory.put(t, ca);
            t.setTerritoryCountryAgent(ca);
        }

        for (CountryAgent ca : countryAgents){
            for (Territory t : risk.getBoard().getTerritories()){
                if (ca.getTerritory().getAdjacentTerritories().contains(t) && ca.adjacentAgents.contains(t) != true){
                    ca.adjacentAgents.add(countryAgentsByTerritory.get(t));
                }
            }
        }
    }

    @Override
    public void turnInCards(Board board) {
        this.reinforcements += cardAgent.tradeIn(board);
    }
    
    private ArrayList<CountryAgent> getCountryAgentList(ArrayList<Territory> cluster) {
    	ArrayList<CountryAgent> agents = new ArrayList<CountryAgent>();
    	for(Territory t : cluster) {
    		agents.add(countryAgentsByTerritory.get(t));
    	}
    	return agents;
    }
    
    private ArrayList<ArrayList<CountryAgent>> getClusters() {
    	ArrayList<ArrayList<CountryAgent>> clusters = new ArrayList<ArrayList<CountryAgent>>();
    	for(Territory t : territories) {
    		boolean contains = false;
    		for(ArrayList<CountryAgent> cl : clusters) {
    			if(cl.contains(countryAgentsByTerritory.get(t))) {
    				contains = true;
    			}
    		}
    		if(!contains) {
    			ArrayList<Territory> cluster = Risk.getConnectedTerritories(t);
    			clusters.add(getCountryAgentList(cluster));
    		}
    	}
    	return clusters;
    }

    @Override
    public void fortifyTerritory(Board board) { //only uses the 'best' country right now
//    	ArrayList<ArrayList<CountryAgent>> clusters = getClusters();
//    	for(ArrayList<CountryAgent> cluster : clusters) {
//    		HashMap<CountryAgent, Integer> sellers = new HashMap<CountryAgent, Integer>();
//    		
//    		for(CountryAgent a : cluster) {
//    			int bestI = 0;
//    			for(int i = a.getTerritory().getNUnits()-1; i > 0; i--) {
//    				double d = a.getD(i);
//    				if(d > WIN_PERCENTAGE) {
//    					bestI = i;
//    				}
//    			}
//    			if(bestI != 0) {
//    				sellers.put(a, bestI);
//    			}
//    		}
//    		DefensiveBid bestBid = null;
//    		for(CountryAgent a : cluster) {
//    			for(CountryAgent seller : sellers.keySet()) {
//    				DefensiveBid defBid = a.getDefensiveBid(a.getTerritory().getNUnits() + sellers.get(seller), null, agentValues);
//    			}
//    		}
//    		
//    	}
    }

    @Override
    public void movingInAfterInvasion(CombatMove combatMove) {
        int transferredunits = combatMove.getAttackingTerritory().getNUnits() - 1;
        combatMove.getDefendingTerritory().setUnits(transferredunits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredunits);

        combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().remove(combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().size() - 1);

        ArrayList<CountryAgent> newGoals = new ArrayList<>();

        for (CountryAgent ca : combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal()){
            newGoals.add(ca);
        }
        combatMove.getDefendingTerritory().getCountryAgent().setFinalGoal(newGoals);

        combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().clear();
        
        countryAgentsByTerritory.get(combatMove.getAttackingTerritory()).updateFinalGoal();
        
    }

    @Override
    public void placeReinforcements(Board board) {
        for (CountryAgent ca: countryAgents){
            ca.clearlists();
        }
        
        for (CountryAgent ca: countryAgents) {
            //I removed the if statement here, so that all territories get a value instead of only the enemy territories
            agentValues.put(ca, ca.calculateOwnershipValue(friendliesweight, enemiesweight, farmiesweight, earmiesweight));
        }
        
        for (CountryAgent sender: countryAgents) {
        	if(sender.getTerritory().getOwner() != this) {
	            ArrayList<CountryAgent> initialList = new ArrayList<CountryAgent>();
	            sender.createGoal();
        	}
        }

        while(reinforcements > 0){
            ReinforcementBid bid = getBestBid(getReinforcements());
            board.addUnits(this, bid.getReinforcedAgent().getTerritory(), bid.getUnits());
            reinforcements -= bid.getUnits();
            if(bid.getUnits() == 0)
                break;
        }
    }
    
    

    private ReinforcementBid getBestBid(int units){
    	ReinforcementBid bestBid = null;
        for(CountryAgent ca : countryAgents){
            if(ca.getTerritory().getOwner() == this && ca.getGoalList().size() > 0){
            	ReinforcementBid bid = ca.getBid(units, agentValues);
        		if(bestBid == null || bid.getUtility() > bestBid.getUtility()){
                    bestBid = bid;
                }
            }
        }
        if(bestBid instanceof OffensiveBid)
        	bestBid.getReinforcedAgent().setFinalGoal(((OffensiveBid) bestBid).getGoal());
        return bestBid;
    }

    @Override
    public int getDefensiveDice(CombatMove combatMove) {
        // TODO Auto-generated method stub
        return Math.min(2,combatMove.getDefendingTerritory().getNUnits());
    }

    private Optional<AttackBid> bestAttackBid () {
        return countryAgents
                .stream()
                .filter(ca -> ca.getTerritory().getOwner() == this)
                .filter(ca -> ca.bordersEnemy())
                .filter(ca -> ca.getFinalGoal() != null && !ca.getFinalGoal().isEmpty())
                .filter(ca -> ca.getTerritory().getNUnits() > 1)
                .map(ca -> ca.getAttackBid())
                .sorted((x,y) -> x.getOdds() > y.getOdds() ? -1 : (x.getOdds() == y.getOdds() ? 0 : 1))
                .findFirst();
    }
    
    @Override
    public void attackPhase(CombatInterface ci) {
        while(true) {
            if (ci.getActivePlayerAmount() == 1) {
                return;
            }
            Optional<AttackBid> ab = bestAttackBid();
            if (ab.isPresent() && ab.get().getOdds() >= WIN_PERCENTAGE) {
                ci.performCombatMove(ab.get().toCombatMove());
            } else {
                return;
            }
        }
    }
}
