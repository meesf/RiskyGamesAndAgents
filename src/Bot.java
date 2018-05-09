import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player{

    public Bot(Objective o, Integer reinforcements){
        super(o, reinforcements);
    }
    @Override
    public Action GetAction() {
        return null;
    }

    @Override
    public void placeSingleReinforcement(Board board) {
        ArrayList<Territory> ownTerritories = new ArrayList<Territory>();
        for(Territory ter : board.getTerritories()) {
            if(ter.getOwner().equals(this)) {
                ownTerritories.add(ter);
            }
        }
        Random rand = new Random();
        Territory randomTer = ownTerritories.get(rand.nextInt(ownTerritories.size()));
        randomTer.setUnits();

    }
}
