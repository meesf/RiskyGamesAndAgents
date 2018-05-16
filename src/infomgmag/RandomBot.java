package infomgmag;

import java.util.ArrayList;
import java.util.Collections;

public class RandomBot extends Player {

    public RandomBot(Objective o, Integer reinforcements, String name) {
        super(o, reinforcements, name);
    }

    @Override
    public void turnInCards(Board board) {
        int useInfantry = 0, useCavalry = 0, useArtillery = 0, useWildcards = 0;
        if(hand.getNumberOfCards() > 4){
            if(hand.getInfantry() > 0 && hand.getArtillery() > 0 && hand.getCavalry() > 0){
                useInfantry = 1; useArtillery = 1; useCavalry = 1;
            } else if(hand.getInfantry() > 2){
                useInfantry = 3;
            } else if(hand.getCavalry() > 2){
                useCavalry = 3;
            } else if(hand.getArtillery() > 2){
                useArtillery = 3;
            } else if(hand.getWildcards() == 1){
                if(hand.getInfantry() > 1){
                    useInfantry = 2; useWildcards = 1;
                } else if(hand.getCavalry() > 1){
                    useCavalry = 2; useWildcards = 1;
                } else if(hand.getArtillery() > 1){
                    useArtillery = 2; useWildcards = 1;
                } else if(hand.getArtillery() > 0 && hand.getCavalry() > 0){
                    useArtillery = 1; useCavalry = 1; useWildcards = 1;
                } else if(hand.getArtillery() > 0 && hand.getInfantry() > 0){
                    useArtillery = 1; useInfantry = 1; useWildcards = 1;
                } else if(hand.getCavalry() > 0 && hand.getInfantry() > 0){
                    useCavalry = 1; useInfantry = 1; useWildcards = 1;
                }
            } else if(hand.getWildcards() == 2){
                if(hand.getInfantry() > 0){
                    useInfantry = 1; useWildcards = 2;
                } else if(hand.getCavalry() > 0){
                    useCavalry = 1; useWildcards = 2;
                } else if(hand.getArtillery() > 0){
                    useArtillery = 1; useWildcards = 2;
                }
            }
        }
        
        if(useInfantry > 0 || useArtillery > 0 || useCavalry > 0){
            int reinforcementsByCards = board.getAndMoveGoldenCavalry();
            hand.setInfantry(hand.getInfantry() - useInfantry);
            hand.setArtillery(hand.getArtillery() - useArtillery);
            hand.setCavalry(hand.getCavalry() - useCavalry);
            hand.setWildCards(hand.getWildcards() - useWildcards);
            board.setArtillery(board.getArtillery() + useArtillery);
            board.setCavalry(board.getCavalry() + useCavalry);
            board.setInfantry(board.getInfantry() + useInfantry);
            board.setWildcards(board.getWildcards() + useWildcards);
            this.reinforcements += reinforcementsByCards;
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

            Collections.shuffle(territories, Risk.random);
            for (Territory at : territories)
                if (at.getNUnits() > 1) {
                    Collections.shuffle(at.getAdjacentTerritories(), Risk.random);
                    for (Territory dt : at.getAdjacentTerritories())
                        if (!territories.contains(dt)) {
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

    @Override
    public void fortifyTerritory(Board board) {
        Collections.shuffle(territories, Risk.random);
        for (Territory t : territories)
            if (t.getNUnits() > 1) {
                int units = Risk.random.nextInt(t.getNUnits() - 1) + 1;
                ArrayList<Territory> connections = getConnectedTerritories(t);
                Territory fortifiedTerritory = connections.get(Risk.random.nextInt(connections.size()));
                t.setUnits(t.getNUnits() - units);
                fortifiedTerritory.setUnits(fortifiedTerritory.getNUnits() + units);
                break;
            }
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
                        if (!result.contains(c) && territories.contains(c)) {
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
        int transferredUnits = nrOfUnitsmovingInAfterInvasion(combatMove);
        combatMove.getDefendingTerritory().setUnits(transferredUnits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredUnits);
    }

    @Override
    public void placeReinforcements(Board board) {
        while (getReinforcements() != 0) {
            Territory randomTer = territories.get(Risk.random.nextInt(territories.size()));
            randomTer.setUnits(randomTer.getNUnits() + 1);
            reinforcements--;
        }
    }

    // TODO: Here the agent is always leaving one unit behind and move the rest to
    // the invaded territory. This should eventually be changed.
    private int nrOfUnitsmovingInAfterInvasion(CombatMove combatMove) {
        return combatMove.getAttackingTerritory().getNUnits() - 1;
    }

}
