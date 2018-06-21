package infomgmag;

public class CombatEvent {
    Player attackingPlayer;
    Player defendingPlayer;
    Territory attackingTerritory;
    Territory defendingTerritory;
    int attackingUnits;
    int defendingUnits;
    int attackingCasualties;
    int defendingCasualties;
    int combatResult;
    int turn;
    boolean captured;
    
    public static final int ATTACKER_WINS = 0, DEFENDER_WINS = 1, ONE_EACH = 2;
    public static final int NO_CAPTURE = 0, CAPTURE = 1;

    CombatEvent(
            Player attackingPlayer, 
            Player defendingPlayer, 
            Territory attackingTerritory, 
            Territory defendingTerritory,
            int attackingUnits,
            int defendingUnits,
            int combatResult,
            int turn,
            boolean captured,
            int attackingCasualties,
            int defendingCasualties) {

        this.attackingPlayer = attackingPlayer;
        this.defendingPlayer = defendingPlayer;
        this.attackingTerritory = attackingTerritory;
        this.defendingTerritory = defendingTerritory;
        this.attackingUnits = attackingUnits;
        this.defendingUnits = defendingUnits;
        this.combatResult = combatResult;
        this.turn = turn;
        this.captured = captured;
        this.attackingCasualties = attackingCasualties;
        this.defendingCasualties = defendingCasualties;
    }

    public Player getAttackingPlayer() {
        return attackingPlayer;
    }

    public Player getDefendingPlayer() {
        return defendingPlayer;
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

    public int getCombatResult() {
        return combatResult;
    }
    
    public int getTurn() {
        return turn;
    }

    public int getAttackingCasualties(){
        return attackingCasualties;
    }

    public int getDefendingCasualties(){
        return defendingCasualties;
    }

    public boolean getCaptured() {
        return captured;
    }
}


