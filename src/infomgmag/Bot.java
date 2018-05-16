package infomgmag;

import java.util.ArrayList;
import java.util.Collections;

public class Bot extends Player {

	public Bot(Objective o, Integer reinforcements, String name) {
		super(o, reinforcements, name);
	}

	@Override
	public void fortifyTerritory(Board board) {
		Collections.shuffle(this.territories, Risk.random);
		for (Territory t : this.territories)
			if (t.getNUnits() > 1) {
				int units = Risk.random.nextInt(t.getNUnits() - 1) + 1;
				ArrayList<Territory> connections = this.getConnectedTerritories(t);
				Territory fortifiedTerritory = connections.get(Risk.random.nextInt(connections.size()));
				t.setUnits(t.getNUnits() - units);
				fortifiedTerritory.setUnits(fortifiedTerritory.getNUnits() + units);
				break;
			}
	}

	// TODO: deciding whether and which territories to use in an combat is random
	// now, this should be changed when we want to make it smarter
	@Override
	public CombatMove getCombatMove() {
		int val = Risk.random.nextInt(10);
		if (val > 0) {
			// return random attack
			CombatMove combatMove = new CombatMove();

			Collections.shuffle(this.territories, Risk.random);
			for (Territory at : this.territories)
				if (at.getNUnits() > 1) {
					Collections.shuffle(at.getAdjacentTerritories(), Risk.random);
					for (Territory dt : at.getAdjacentTerritories())
						if (!this.territories.contains(dt)) {
							combatMove.setDefendingTerritory(dt);
							combatMove.setAttackingTerritory(at);
							combatMove.setAttackingUnits(Integer.min(3, at.getNUnits() - 1));
							combatMove.setDefendingUnits(Integer.min(2, dt.getNUnits()));
							return combatMove;
						}
				}
		}
		return null;
	}

	// TODO: Maybe this function can be more efficient....
	private ArrayList<Territory> getConnectedTerritories(Territory origin) {
		ArrayList<Territory> visited = new ArrayList<>();
		ArrayList<Territory> result = new ArrayList<>();
		result.add(origin);
		boolean foundTerritory = true;
		while (foundTerritory) {
			foundTerritory = false;
			ArrayList<Territory> add = new ArrayList<>();
			for (Territory t : result)
				if (!visited.contains(t)) {
					for (Territory c : t.getAdjacentTerritories())
						if (!result.contains(c) && this.territories.contains(c)) {
							add.add(c);
							foundTerritory = true;
						}
					visited.add(t);
				}
			for (Territory t : add)
				result.add(t);
		}
		return result;
	}

	@Override
	public void movingInAfterInvasion(CombatMove combatMove) {
		int transferredUnits = this.nrOfUnitsmovingInAfterInvasion(combatMove);
		combatMove.getDefendingTerritory().setUnits(transferredUnits);
		combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredUnits);
	}

	// TODO: Here the agent is always leaving one unit behind and move the rest to
	// the invaded territory. This should eventually be changed.
	private int nrOfUnitsmovingInAfterInvasion(CombatMove combatMove) {
		return combatMove.getAttackingTerritory().getNUnits() - 1;
	}

	@Override
	public void placeReinforcements(Board board) {
		while (this.getReinforcements() != 0) {
			Territory randomTer = this.territories.get(Risk.random.nextInt(this.territories.size()));
			randomTer.setUnits(randomTer.getNUnits() + 1);
			this.reinforcements--;
		}
	}

	@Override
	public void turnInCards(Board board) {
		int reinforcements = 0;
		if (this.hand.getInfantry() > 0 && this.hand.getArtillery() > 0 && this.hand.getCavalry() > 0) {
			reinforcements = board.getAndMoveGoldenCavalry();
			this.hand.setArtillery(this.hand.getArtillery() - 1);
			this.hand.setCavalry(this.hand.getCavalry() - 1);
			this.hand.setInfantry(this.hand.getInfantry() - 1);
			board.setArtillery(board.getArtillery() + 1);
			board.setCavalry(board.getCavalry() + 1);
			board.setInfantry(board.getInfantry() + 1);
		} else if (this.hand.getInfantry() > 2) {
			reinforcements = board.getAndMoveGoldenCavalry();
			this.hand.setInfantry(this.hand.getInfantry() - 3);
			board.setInfantry(board.getInfantry() + 3);
		} else if (this.hand.getCavalry() > 2) {
			reinforcements = board.getAndMoveGoldenCavalry();
			this.hand.setCavalry(this.hand.getCavalry() - 3);
			board.setCavalry(board.getCavalry() + 3);
		} else if (this.hand.getArtillery() > 2) {
			reinforcements = board.getAndMoveGoldenCavalry();
			this.hand.setArtillery(this.hand.getArtillery() - 3);
			board.setArtillery(board.getArtillery() + 3);
		} else if (this.hand.getWildcards() > 0)
			if (this.hand.getArtillery() > 0 && this.hand.getCavalry() > 0) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setArtillery(this.hand.getArtillery() - 1);
				this.hand.setCavalry(this.hand.getCavalry() - 1);
				board.setArtillery(board.getArtillery() + 1);
				board.setCavalry(board.getCavalry() + 1);
			} else if (this.hand.getArtillery() > 0 && this.hand.getInfantry() > 0) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setArtillery(this.hand.getArtillery() - 1);
				this.hand.setInfantry(this.hand.getInfantry() - 1);
				board.setArtillery(board.getArtillery() + 1);
				board.setInfantry(board.getCavalry() + 1);
			} else if (this.hand.getCavalry() > 0 && this.hand.getInfantry() > 0) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setCavalry(this.hand.getCavalry() - 1);
				this.hand.setInfantry(this.hand.getInfantry() - 1);
				board.setCavalry(board.getCavalry() + 1);
				board.setInfantry(board.getCavalry() + 1);
			} else if (this.hand.getCavalry() > 1) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setCavalry(this.hand.getCavalry() - 2);
				board.setCavalry(board.getCavalry() + 2);
			} else if (this.hand.getArtillery() > 1) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setArtillery(this.hand.getArtillery() - 2);
				board.setArtillery(board.getArtillery() + 2);
			} else if (this.hand.getInfantry() > 1) {
				reinforcements = board.getAndMoveGoldenCavalry();
				this.hand.setInfantry(this.hand.getInfantry() - 2);
				board.setInfantry(board.getInfantry() + 2);
			}

		this.reinforcements += reinforcements;
	}

}
