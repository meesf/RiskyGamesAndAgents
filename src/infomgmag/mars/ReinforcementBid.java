package infomgmag.mars;

public abstract class ReinforcementBid {
    protected CountryAgent reinforcedAgent, fortifyingAgent;
    protected int units;
    protected double utility;

    public ReinforcementBid(CountryAgent reinforcedAgent, int units, double utility) {
        this.reinforcedAgent = reinforcedAgent;
        this.units = units;
        this.utility = utility;
    }

    public int getUnits() {
        return units;
    }

    public double getUtility() {
        return utility;
    }

    public CountryAgent getReinforcedAgent() {
        return reinforcedAgent;
    }
    
    public void setFortifyingAgent(CountryAgent ca)  {
        this.fortifyingAgent = ca;
    }
}
