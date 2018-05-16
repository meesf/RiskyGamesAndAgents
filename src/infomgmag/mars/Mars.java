package infomgmag.mars;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);

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
    public void fortifyTerritory(Board board) {
        // TODO Auto-generated method stub

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
        // TODO Auto-generated method stub

    }
}
