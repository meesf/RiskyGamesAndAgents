package infomgmag.mars;

import java.lang.reflect.Array;
import java.util.*;

import infomgmag.Board;
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
    private Map<Territory,CountryAgent> countryAgentsByTerritory;
    private HashMap<CountryAgent, Double> agentValues;

    private Double friendliesweight = 1.2;      //parameters used in calculation of territory value
    private Double enemiesweight = -0.3;
    private Double farmiesweight = 0.05;
    private Double earmiesweight = -0.03;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent();
        countryAgents = new ArrayList<>();


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
        for (CountryAgent CA: countryAgents) {
            for (CountryAgent receiver : CA.getAdjacentAgents()) {
                if (receiver.getTerritory().getOwner() != CA.getTerritory().getOwner()){
                    CA.receivemessage(receiver, agentValues.get(CA));
                }
                else {
                    CA.receivemessagefriendly(receiver);
                }
            }
        }

        //TODO: Find out how to find the highest value in a hashtable and get key + value
        while(getReinforcements() != 0){
           // board.addUnits(this,);   Needs the territory the units will be getting placed on
        }
    }
}
