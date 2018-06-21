package infomgmag;

import java.util.ArrayList;

public class CombatMove {
    private Territory attackingTerritory, defendingTerritory;
    private int attackingUnits, defendingUnits;
    private ArrayList<Integer> attackThrows;

    public CombatMove() {
    }

    public Territory getAttackingTerritory() {
        return attackingTerritory;
    }

    public Territory getDefendingTerritory() {
        return defendingTerritory;
    }

    public int getAttackingUnits() {
        return attackingUnits;
    }

    public int getDefendingUnits() {
        return defendingUnits;
    }

    public void setAttackingTerritory(Territory attackingTerritory) {
        this.attackingTerritory = attackingTerritory;
    }

    public void setAttackingUnits(Integer attackingUnits) {
        this.attackingUnits = attackingUnits;
    }

    public void setDefendingTerritory(Territory defendingTerritory) {
        this.defendingTerritory = defendingTerritory;
    }

    public void setDefendingUnits(Integer defendingUnits) {
        this.defendingUnits = defendingUnits;
    }

    @Override
    public String toString() {
        return "AttackingTerritory: " + attackingTerritory.toString() + " DefendingTerritory: "
                + defendingTerritory.toString() + " DefendingUnits: " + defendingUnits;
    }

    public void setAttackThrows(ArrayList<Integer> attackThrows) {
        this.attackThrows = attackThrows;
    }

    public ArrayList<Integer> getAttackThrows() {
        return attackThrows;
    }
}
