public class Human extends Player{

    public Human(Objective o, Integer reinforcements, String name){
        super(o, reinforcements, name);
    }
    @Override
    public Action getAction() {
        return null;
    }

    @Override
    public void placeSingleReinforcement(Board board) {

    }
}
