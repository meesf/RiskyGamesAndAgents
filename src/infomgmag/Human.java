package infomgmag;

public class Human extends Player {

	public Human(Objective o, Integer reinforcements, String name) {
		super(o, reinforcements, name);
	}

	@Override
	public void fortifyTerritory(Board board) {

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
	public void turnInCards(Board board) {

	}
}
