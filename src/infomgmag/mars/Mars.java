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
    private Hashtable<CountryAgent, Double> agentValues;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);

        agentValues = new Hashtable<CountryAgent, Double>();
        cardAgent = new CardAgent();
        countryAgents = new ArrayList<>();
        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t);
            countryAgents.add(ca);
            countryAgentsByTerritory.put(t, ca);
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
    public void placeReinforcements(Board board) { //only uses the 'best' country right now
        for (CountryAgent CA: countryAgents)
        {
            agentValues.put(CA, CA.calculateReinforceValue());
        }
        //TODO: Find out how to find the highest value in a hashtable and get key + value
        while(getReinforcements() != 0){
           // board.addUnits(this,);   Needs the territory the units will be getting placed on
        }
    }
}
