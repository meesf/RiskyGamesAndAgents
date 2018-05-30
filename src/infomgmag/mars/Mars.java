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
    private HashMap<Territory,CountryAgent> countryAgentsByTerritory;
    private HashMap<CountryAgent, Double> agentValues;

    private Double friendliesweight = 1.2;      //parameters used in calculation of territory value
    private Double enemiesweight = -0.3;
    private Double farmiesweight = 0.05;
    private Double earmiesweight = -0.03;
    private Integer goalLength = 7;
    
    static final double WIN_PERCENTAGE = 0.5;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name, Color color) {
        super(objective, reinforcements, name, color);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent(hand);
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();


        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t);
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

    @Override
    public void fortifyTerritory(Board board) { //only uses the 'best' country right now
        //todo: everything
    }

    @Override
    public CombatMove getCombatMove() {
        CombatMove combatMove = new CombatMove();

        for (CountryAgent ca : countryAgents) {
            if (ca.getTerritory().getOwner() == this && ca.bordersEnemy() && (ca.getFinalGoal() != null) && ca.getTerritory().getNUnits() > 1){
                System.out.println(ca.getFinalGoal() + " is the final goal");
                for (CountryAgent target : ca.getFinalGoal()) {
                    combatMove.setAttackingTerritory(ca.getTerritory());
                    combatMove.setDefendingTerritory(ca.getFinalGoal().get(ca.getFinalGoal().size() - 1).getTerritory());
                    combatMove.setAttackingUnits(Integer.min(3, ca.getTerritory().getNUnits() - 1));
                    combatMove.setDefendingUnits(Integer.min(2, ca.getFinalGoal().get(ca.getFinalGoal().size()-1).getTerritory().getNUnits()));
                    return combatMove;
                    }
                }
            }
        return null;
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

        System.out.println(combatMove.getDefendingTerritory().getCountryAgent().getFinalGoal() + " final goal of this agent");
        combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().clear();
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
	            createGoal(sender, initialList);
        	}
        }

        while(reinforcements > 0){
            Bid bid = getBestBid(getReinforcements());
            board.addUnits(this, bid.getOrigin().getTerritory(), bid.getUnits());
            reinforcements -= bid.getUnits();
            if(bid.getUnits() == 0)
                break;
        }
    }
    
    private void createGoal(CountryAgent receiver, ArrayList<CountryAgent> countries){
    	if(receiver.getTerritory().getOwner() == this) {
            receiver.receivemessagefriendly(countries);
        } else if(goalLength > countries.size()) {
            ArrayList<CountryAgent> copiedCountries = new ArrayList<CountryAgent>();
            for(CountryAgent ca : countries){
                copiedCountries.add(ca);
            }
            
            copiedCountries.add(receiver);
            for(CountryAgent neighbour : receiver.getAdjacentAgents()) {
            	if(!countries.contains(neighbour)) {
            		createGoal(neighbour, copiedCountries);
            	}
            }
        }
    }

    private Bid getBestBid(int units){
        Bid bestBid = null;
        for(CountryAgent ca : countryAgents){
            if(ca.getTerritory().getOwner() == this && ca.getGoalList().size() > 0){
        		Bid bid = ca.getBid(units, agentValues);
        		if(bestBid == null || bid.getUtility() > bestBid.getUtility()){
                    bestBid = bid;
                }
            }
        }
        bestBid.getOrigin().setFinalGoal(bestBid.getGoal());
        return bestBid;
    }

    @Override
    public int getDefensiveDice(CombatMove combatMove) {
        // TODO Auto-generated method stub
        return Math.min(2,combatMove.getDefendingTerritory().getNUnits());
    }

    @Override
    public void attackPhase(CombatInterface ci) {
        CombatMove cm;
        while(!reachedObjective(ci) && (cm = this.getCombatMove()) != null) {
            ci.performCombatMove(cm);
        }
    }
}
