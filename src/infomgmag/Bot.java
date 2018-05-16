package infomgmag;
import java.util.Collections;
import java.util.ArrayList;

public class Bot extends Player{

    public Bot(Objective o, Integer reinforcements, String name){
        super(o, reinforcements, name);
    }

    @Override
    public void turnInCards(Board board) {
        int reinforcements = 0;
        if(hand.getInfantry() > 0 && hand.getArtillery() > 0 && hand.getCavalry() > 0){
            reinforcements = board.getAndMoveGoldenCavalry();
            hand.setArtillery(hand.getArtillery() - 1);
            hand.setCavalry(hand.getCavalry() - 1);
            hand.setInfantry(hand.getInfantry() - 1);
            board.setArtillery(board.getArtillery() + 1);
            board.setCavalry(board.getCavalry() + 1);
            board.setInfantry(board.getInfantry() + 1);
        }
        else if(hand.getInfantry() > 2){
            reinforcements = board.getAndMoveGoldenCavalry();
            hand.setInfantry(hand.getInfantry() - 3);
            board.setInfantry(board.getInfantry() + 3);
        }
        else if(hand.getCavalry() > 2){
            reinforcements = board.getAndMoveGoldenCavalry();
            hand.setCavalry(hand.getCavalry() - 3);
            board.setCavalry(board.getCavalry() + 3);
        }
        else if(hand.getArtillery() > 2){
            reinforcements = board.getAndMoveGoldenCavalry();
            hand.setArtillery(hand.getArtillery() - 3);
            board.setArtillery(board.getArtillery() + 3);
        }
        else if(hand.getWildcards() > 0){
            if(hand.getArtillery() > 0 && hand.getCavalry() > 0){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setArtillery(hand.getArtillery() - 1);
                hand.setCavalry(hand.getCavalry() - 1);
                board.setArtillery(board.getArtillery() + 1);
                board.setCavalry(board.getCavalry() + 1);
            }
            else if(hand.getArtillery() > 0 && hand.getInfantry() > 0){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setArtillery(hand.getArtillery() - 1);
                hand.setInfantry(hand.getInfantry() - 1);
                board.setArtillery(board.getArtillery() + 1);
                board.setInfantry(board.getCavalry() + 1);
            }
            else if(hand.getCavalry() > 0 && hand.getInfantry() > 0){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setCavalry(hand.getCavalry() - 1);
                hand.setInfantry(hand.getInfantry() - 1);
                board.setCavalry(board.getCavalry() + 1);
                board.setInfantry(board.getCavalry() + 1);
            }
            else if(hand.getCavalry() > 1){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setCavalry(hand.getCavalry() - 2);
                board.setCavalry(board.getCavalry() + 2);
            }
            else if(hand.getArtillery() > 1){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setArtillery(hand.getArtillery() - 2);
                board.setArtillery(board.getArtillery() + 2);
            }
            else if(hand.getInfantry() > 1){
                reinforcements = board.getAndMoveGoldenCavalry();
                hand.setInfantry(hand.getInfantry() - 2);
                board.setInfantry(board.getInfantry() + 2);
            }
        }

        this.reinforcements += reinforcements;
    }

    //TODO: deciding whether and which territories to use in an combat is random now, this should be changed when we want to make it smarter
    @Override
    public CombatMove getCombatMove() {
        int val = Risk.random.nextInt(10);
        if(val > 2){
            // return random attack
            CombatMove combatMove = new CombatMove();

            Collections.shuffle(territories, Risk.random);
            for(Territory at : territories) {
                if(at.getNUnits() > 1) {
                    Collections.shuffle(at.getAdjacentTerritories(), Risk.random);
                    for (Territory dt : at.getAdjacentTerritories()) {
                        if (!territories.contains(dt)) {
                            combatMove.setDefendingTerritory(dt);
                            combatMove.setAttackingTerritory(at);
                            combatMove.setAttackingUnits(Integer.min(3,at.getNUnits() - 1));
                            combatMove.setDefendingUnits(Integer.min(2,dt.getNUnits()));
                            return combatMove;
                        }
                    }
                }
            }
        }
        return null;
    }

	@Override
	public void fortifyTerritory(Board board) {
		Collections.shuffle(territories, Risk.random);
		for(Territory t : territories) {
			if(t.getNUnits() > 1) {
				int units = Risk.random.nextInt(t.getNUnits() - 1) + 1;
				ArrayList<Territory> connections = getConnectedTerritories(t);
				Territory fortifiedTerritory = connections.get(Risk.random.nextInt(connections.size()));
				t.setUnits(t.getNUnits() - units);
				fortifiedTerritory.setUnits(fortifiedTerritory.getNUnits() + units);
				break;
			}
		}
	}
	
	// TODO: Maybe this function can be more efficient....
	private ArrayList<Territory> getConnectedTerritories(Territory origin) {
		ArrayList<Territory> visited = new ArrayList<Territory>();
		ArrayList<Territory> result = new ArrayList<Territory>();
		result.add(origin);
		boolean foundTerritory = true;
		while(foundTerritory) {
			foundTerritory = false;
			ArrayList<Territory> add = new ArrayList<Territory>();
			for(Territory t : result) {
				if(!visited.contains(t)) {
					for(Territory c : t.getAdjacentTerritories()) {
						if(!result.contains(c)) {
							add.add(c);
							foundTerritory = true;
						}
					}
					visited.add(t);
				}
			}
			for(Territory t : add) { result.add(t); }
		}
		return result;
	}
    @Override
    public void movingInAfterInvasion(CombatMove combatMove){
        int transferredUnits = nrOfUnitsmovingInAfterInvasion(combatMove);
        combatMove.getDefendingTerritory().setUnits(transferredUnits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredUnits);
    }

    @Override
    public void placeReinforcements(Board board) {
        while(getReinforcements() != 0){
            Territory randomTer = territories.get(Risk.random.nextInt(territories.size()));
            randomTer.setUnits(randomTer.getNUnits() + 1);
            reinforcements--;
        }
    }

    //TODO: Here the agent is always leaving one unit behind and move the rest to the invaded territory. This should eventually be changed.
    private int nrOfUnitsmovingInAfterInvasion(CombatMove combatMove){
        return combatMove.getAttackingTerritory().getNUnits() - 1;
    }

}
