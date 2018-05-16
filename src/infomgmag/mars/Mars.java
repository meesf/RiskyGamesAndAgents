package infomgmag.mars;

import java.util.ArrayList;
import java.util.List;

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

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);

        cardAgent = new CardAgent();
        countryAgents = new ArrayList<>();
        for (Territory t : risk.getBoard().getTerritories())
            countryAgents.add(new CountryAgent(t));
    }

    @Override
    public void turnInCards(Board board) {
        // TODO Auto-generated method stub

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
