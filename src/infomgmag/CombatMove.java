package infomgmag;

public class CombatMove {
    private Territory attackingTerritory, defendingTerritory;
    private Integer attackingUnits, defendingUnits;

    public CombatMove() {
    }

    public Territory getAttackingTerritory() {
        return attackingTerritory;
    }

    public Territory getDefendingTerritory() {
        return defendingTerritory;
    }

    public Integer getAttackingUnits() {
        return attackingUnits;
    }

    public Integer getDefendingUnits() {
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
}
