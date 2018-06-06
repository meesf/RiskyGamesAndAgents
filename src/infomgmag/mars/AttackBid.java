package infomgmag.mars;

import infomgmag.CombatMove;
import infomgmag.Territory;

public class AttackBid {
    private double odds;
    private Territory attackingTerritory, defendingTerritory;

    AttackBid(Territory attackingTerritory, Territory defendingTerritory) {
        this.attackingTerritory = attackingTerritory;
        this.defendingTerritory = defendingTerritory;

        ProbabilityGrid grid = new ProbabilityGrid(attackingTerritory.getNUnits() - 1, defendingTerritory.getNUnits());
        this.odds = grid.chanceOfWin();
    }

    public Territory getAttackingTerritory() {
        return attackingTerritory;
    }

    public Territory getDefendingTerritory() {
        return defendingTerritory;
    }

    public double getOdds() {
        return odds;
    }

    public CombatMove toCombatMove() {
        CombatMove cm = new CombatMove();
        cm.setAttackingTerritory(attackingTerritory);
        cm.setDefendingTerritory(defendingTerritory);
        cm.setAttackingUnits(attackingTerritory.getNUnits() - 1);
        return cm;
    }

    @Override
    public String toString() {
        return "Attackbid {Attacking: " + attackingTerritory + "; Defending: " + defendingTerritory + "; Odds: "
                + getOdds();
    }
}
