package infomgmag.mars;

public class OffensiveBid extends ReinforcementBid {
    private Goal goal;

    public OffensiveBid(CountryAgent reinforcedAgent, Goal goal, int units, double utility) {
        super(reinforcedAgent, units, utility);
        this.goal = goal;
    }

    public Goal getGoal() {
        return goal;
    }
}
