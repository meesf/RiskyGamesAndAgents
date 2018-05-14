import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Bot extends Player{

    public Bot(Objective o, Integer reinforcements, String name){
        super(o, reinforcements, name);
    }

    @Override
    public void placeSingleReinforcement(Board board) {

        Territory randomTer = territories.get(Risk.random.nextInt(territories.size()));
        randomTer.setUnits(randomTer.getNUnits() + 1);
        reinforcements--;
    }

    @Override
    public void turnInCards(Board board) {
        int reinforcements = 0;
        if(hand.getInfantry() > 0 && hand.getArtillery() > 0 && hand.getCavalry() > 0){
            reinforcements = board.getGoldenCavalry();
            board.moveGoldenCavalry();
            hand.setArtillery(hand.getArtillery() - 1);
            hand.setCavalry(hand.getCavalry() - 1);
            hand.setInfantry(hand.getInfantry() - 1);
            board.setArtillery(board.getArtillery() + 1);
            board.setCavalry(board.getCavalry() + 1);
            board.setInfantry(board.getInfantry() + 1);
        }
        else if(hand.getInfantry() > 2){
            reinforcements = board.getGoldenCavalry();
            board.moveGoldenCavalry();
            hand.setInfantry(hand.getInfantry() - 3);
            board.setInfantry(board.getInfantry() + 3);
        }
        else if(hand.getCavalry() > 2){
            reinforcements = board.getGoldenCavalry();
            board.moveGoldenCavalry();
            hand.setCavalry(hand.getCavalry() - 3);
            board.setCavalry(board.getCavalry() + 3);
        }
        else if(hand.getArtillery() > 2){
            reinforcements = board.getGoldenCavalry();
            board.moveGoldenCavalry();
            hand.setArtillery(hand.getArtillery() - 3);
            board.setArtillery(board.getArtillery() + 3);
        }

        this.reinforcements += reinforcements;
    }

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
}
