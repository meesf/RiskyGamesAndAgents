package infomgmag.mars;

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

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent();
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();


        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t);
            countryAgents.add(ca);
            countryAgentsByTerritory.put(t, ca);
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void movingInAfterInvasion(CombatMove combatMove) {
        // TODO Auto-generated method stub

    }

    @Override
    public void placeReinforcements(Board board) {
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
        System.out.println(bestBid);
        return bestBid;
    }



}
