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
    private Integer goalLength = 5;

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
        for (CountryAgent CA: countryAgents){
            CA.clearlists();
        }
        for (CountryAgent CA: countryAgents)
        {
            if (CA.getTerritory().getOwner() != this) {
                agentValues.put(CA, CA.calculateOwnershipValue(friendliesweight, enemiesweight, farmiesweight, earmiesweight));
            }
        }
        for (CountryAgent sender: countryAgents) {
            ArrayList<CountryAgent> initialList = new ArrayList<CountryAgent>();
            initialList.add(sender);
            createGoal(sender, initialList);
        }

        //for(CountryAgent ca : countryAgents){
        //    System.out.println(" owner: " + ca.getTerritory().getOwner().toString() + "  name: "  + ca.getTerritory().getName() + " size: " + ca.getGoalList().size());
        //}

        while(getReinforcements() > 0){
            Pair<CountryAgent, Pair<Double, Integer>> bid = getBestBid(getReinforcements());
            board.addUnits(this, bid.getKey().getTerritory(), bid.getValue().getValue());
            reinforcements -= bid.getValue().getValue();
        }
    }

    private void createGoal(CountryAgent receiver, ArrayList<CountryAgent> countries){
        if(receiver.getTerritory().getOwner() != this && !countries.contains(receiver) && goalLength >= countries.size()){
            ArrayList<CountryAgent> copiedCountries = new ArrayList<CountryAgent>();
            for(CountryAgent ca : countries){
                copiedCountries.add(ca);
            }
            copiedCountries.add(receiver);
            for(CountryAgent neighbour : receiver.getAdjacentAgents()){
                createGoal(neighbour, copiedCountries);
            }
        }
        else if(receiver.getTerritory().getOwner() == this){
            receiver.receivemessagefriendly(countries);
        }
    }

    private Pair<CountryAgent, Pair<Double, Integer>> getBestBid(int units){
        CountryAgent bestCountry = countryAgents.get(0);
        Pair<Double, Integer> bestBid = null;
        for(CountryAgent ca : countryAgents){
            if(ca.getTerritory().getOwner() == this && ca.getGoalList().size() > 0){
                Pair<Double, Integer> bid = ca.getBid(units);
                if(bestBid == null || bid.getKey() > bestBid.getKey()){
                    bestBid = bid;
                    bestCountry = ca;
                }
            }
        }
        return new Pair<CountryAgent, Pair<Double, Integer>>(bestCountry,  bestBid);
    }



}
