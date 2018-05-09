public class Objective {
    private type type;

    public static enum type{
        TOTAL_DOMINATION
    }

    public Objective(type type){
        this.type = type;
    }
}
