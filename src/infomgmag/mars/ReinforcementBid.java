package infomgmag.mars;

public abstract class ReinforcementBid {
    protected CountryAgent reinforcedAgent, fortifyingAgent;
    protected Integer units;
    protected Double utility;

    public ReinforcementBid(CountryAgent reinforcedAgent, Integer units, double utility) {
        this.reinforcedAgent = reinforcedAgent;
        this.units = units;
        this.utility = utility;
    }

    public Integer getUnits() {
        return units;
    }

    public Double getUtility() {
        return utility;
    }

    public CountryAgent getReinforcedAgent() {
        return reinforcedAgent;
    }
    
    public void setFortifyingAgent(CountryAgent ca)  {
        this.fortifyingAgent = ca;
    }
}
