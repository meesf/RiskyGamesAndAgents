import java.util.ArrayList;
import java.util.Random;

public class Bot extends Player{

    public Bot(Objective o, Integer reinforcements, String name){
        super(o, reinforcements, name);
    }
    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public void placeSingleReinforcement(Board board) {
        Random rand = new Random();
        Territory randomTer = territories.get(rand.nextInt(territories.size()));
        randomTer.setUnits(randomTer.getNUnits() + 1);
        reinforcements--;
    }
}
