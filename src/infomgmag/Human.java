package infomgmag;

import java.awt.Color;

public class Human extends Player {

    public Human(Objective o, Integer reinforcements, String name, Color color) {
        super(o, reinforcements, name, color);
    }

    @Override
    public void turnInCards(Board board) {

    }

    @Override
    public CombatMove getCombatMove() {
        return null;
    }

    @Override
    public void movingInAfterInvasion(CombatMove combatMove) {

    }

    @Override
    public void placeReinforcements(Board board) {

    }

    @Override
    public void fortifyTerritory(Board board) {

    }

    @Override
    public int getDefensiveDice(CombatMove combatMove) {
        // TODO Auto-generated method stub
        return 0;
    }
}
