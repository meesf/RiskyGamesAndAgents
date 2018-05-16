package infomgmag.mars;

import infomgmag.Board;
import infomgmag.CombatMove;
import infomgmag.Objective;
import infomgmag.Player;

public class Mars extends Player{

    /*
     * This agent can be regarded as the mediator agent from the paper.
     */
    
    public Mars(Objective objective, Integer reinforcements, String name) {
        super(objective, reinforcements, name);
        // TODO Auto-generated constructor stub
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
