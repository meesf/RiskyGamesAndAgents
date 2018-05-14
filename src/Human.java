public class Human extends Player{

    public Human(Objective o, Integer reinforcements, String name){
        super(o, reinforcements, name);
    }

    @Override
    public void placeSingleReinforcement(Board board) {

    }

    @Override
    public void turnInCards(Board board) {

    }

    @Override
    public CombatMove getCombatMove() {
        return null;
    }
}
