package infomgmag.mars;

public class OffensiveBid extends ReinforcementBid {
    private Goal goal;

    public OffensiveBid(CountryAgent reinforcedAgent, Goal goal, Integer units, double utility) {
        super(reinforcedAgent, null, units, utility);
        this.goal = goal;
    }

    public Goal getGoal() {
        return goal;
    }
}
