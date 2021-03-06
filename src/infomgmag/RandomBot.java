package infomgmag;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;

public class RandomBot extends Player {

    public RandomBot(Objective o, int reinforcements, String name, Color color) {
        super(o, reinforcements, name, color);
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

    public CombatMove getCombatMove() {
        int val = Risk.random.nextInt(10);
        if (val > 0) {
            // Return random attack
            CombatMove combatMove = new CombatMove();

            Collections.shuffle(territories, Risk.random);
            for (Territory at : territories)
                if (at.getUnits() > 1) {
                    Collections.shuffle(at.getAdjacentTerritories(), Risk.random);
                    for (Territory dt : at.getAdjacentTerritories())
                        if (!territories.contains(dt)) {
                            combatMove.setDefendingTerritory(dt);
                            combatMove.setAttackingTerritory(at);
                            combatMove.setAttackingUnits(Integer.min(3, at.getUnits() - 1));
                            combatMove.setDefendingUnits(Integer.min(2, dt.getUnits()));
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
            if (t.getUnits() > 1) {
                int units = Risk.random.nextInt(t.getUnits() - 1) + 1;
                ArrayList<Territory> connections = Risk.getConnectedTerritories(t);
                Territory fortifiedTerritory = connections.get(Risk.random.nextInt(connections.size()));
                board.moveUnits(t, fortifiedTerritory, units);
                break;
            }
    }

    @Override
    public void movingInAfterInvasion(Board board, CombatMove combatMove) {
        int transferredUnits = nrOfUnitsmovingInAfterInvasion(combatMove);
        combatMove.getDefendingTerritory().setUnits(transferredUnits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getUnits() - transferredUnits);
    }

    @Override
    public void placeReinforcements(Board board) {
        while (getReinforcements() != 0) {
            Territory randomTer = territories.get(Risk.random.nextInt(territories.size()));
            board.addUnits(randomTer, 1);
            reinforcements--;
        }
    }

    private int nrOfUnitsmovingInAfterInvasion(CombatMove combatMove) {
        // Note that the value is random int *strictly* smaller than the argument
        return Risk.random.nextInt(combatMove.getAttackingTerritory().getUnits() - combatMove.getAttackingUnits()) + combatMove.getAttackingUnits();
    }

    public int getDefensiveDice(CombatMove combatMove) {
        if (combatMove.getDefendingTerritory().getUnits() >= 2) {
            return Risk.random.nextInt(2) + 1;
        } else {
            return 1;
        }
    }

    @Override
    public void attackPhase(CombatInterface ci) {
        CombatMove cm;
        while(!reachedObjective(ci) && (cm = this.getCombatMove()) != null) {
            ci.performCombatMove(cm);
        }
    }

}
