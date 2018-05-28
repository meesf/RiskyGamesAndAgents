package infomgmag.mars;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.*;

import infomgmag.Board;
import infomgmag.CombatMove;
import infomgmag.Objective;
import infomgmag.Player;
import infomgmag.Risk;
import infomgmag.Territory;

import javafx.util.Pair;

import javax.print.attribute.IntegerSyntax;

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
    private Integer goalLength = 1;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name, Color color) {
        super(objective, reinforcements, name, color);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent();
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
        cardAgent.tradeIn(board);
    }

    @Override
    public void fortifyTerritory(Board board) { //only uses the 'best' country right now
        //todo: everything
    }

    @Override
    public CombatMove getCombatMove() {
        CombatMove combatMove = new CombatMove();
        for (CountryAgent ca : countryAgents) {
            if (ca.getTerritory().getOwner() == this && ca.getGoalList().isEmpty() != true && ca.getTerritory().getNUnits() > 1){
                for (CountryAgent target : ca.getGoalList().get(0)) {           //right now it assumes the first goal is the best goal
                    combatMove.setAttackingTerritory(ca.getTerritory());
                    combatMove.setDefendingTerritory(ca.getGoalList().get(0).get(ca.getGoalList().get(0).size() - 1).getTerritory());
                    combatMove.setAttackingUnits(Integer.min(3, ca.getTerritory().getNUnits() - 1));
                    combatMove.setDefendingUnits(Integer.min(2, ca.getGoalList().get(0).get(ca.getGoalList().get(0).size() - 1).getTerritory().getNUnits()));
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
        combatMove.getAttackingTerritory().getCountryAgent().getGoalList().get(0).remove(combatMove.getAttackingTerritory().getCountryAgent().getGoalList().get(0).size() - 1); //removes the last country from a goal
        combatMove.getDefendingTerritory().getCountryAgent().getGoalList().set(0, combatMove.getAttackingTerritory().getCountryAgent().getGoalList().get(0));   //sets goal of conquered territory to goal of attacking territory minus the conquered territory
        combatMove.getAttackingTerritory().getCountryAgent().getGoalList().clear();
    }

    @Override
    public void placeReinforcements(Board board) {
    	System.out.println("----------------- Place "+reinforcements+" reinforcement(s) -----------------");
        for (CountryAgent ca: countryAgents){
            ca.clearlists();
        }
        
        for (CountryAgent ca: countryAgents) {
            if (ca.getTerritory().getOwner() != this) {
                agentValues.put(ca, ca.calculateOwnershipValue(friendliesweight, enemiesweight, farmiesweight, earmiesweight));
            }
        }
        
        for (CountryAgent sender: countryAgents) {
        	if(sender.getTerritory().getOwner() != this) {
	            ArrayList<CountryAgent> initialList = new ArrayList<CountryAgent>();
	            createGoal(sender, initialList);
        	}
        }

        while(reinforcements > 0){
            Bid bid = getBestBid(getReinforcements());
            System.out.println("Winning bid: " + bid);
            board.addUnits(this, bid.getOrigin().getTerritory(), bid.getUnits());
            reinforcements -= bid.getUnits();
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
        /*for(CountryAgent ca : countryAgents){
            if(ca.getTerritory().getOwner() == this && ca.getGoalList().size() > 0){
        		Bid bid = ca.getBid(units, agentValues);
        		if(bestBid == null || bid.getUtility() > bestBid.getUtility()){
                    bestBid = bid;
                }
            }
        }*/
        for (CountryAgent ca : countryAgents) {
            if (ca.getTerritory().getOwner() == this){
                Bid testbid = new Bid(ca, ca.getGoalList().get(0), reinforcements, 10);
                bestBid = testbid;
            }
        }
        return bestBid;
    }



}
