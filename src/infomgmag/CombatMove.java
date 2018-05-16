package infomgmag;

public class CombatMove {
	private Territory attackingTerritory, defendingTerritory;
	private Integer attackingUnits, defendingUnits;

	public CombatMove() {
	}

	public Territory getAttackingTerritory() {
		return this.attackingTerritory;
	}

	public Integer getAttackingUnits() {
		return this.attackingUnits;
	}

	public Territory getDefendingTerritory() {
		return this.defendingTerritory;
	}

	public Integer getDefendingUnits() {
		return this.defendingUnits;
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
		return "AttackingTerritory: " + this.attackingTerritory.toString() + " DefendingTerritory: "
				+ this.defendingTerritory.toString();
	}
}
