package infomgmag.mars;

public class DefensiveBid extends ReinforcementBid {

    public DefensiveBid(CountryAgent reinforcedAgent, Integer units, double utility) {
        super(reinforcedAgent, units, utility);
    }

    public String toString() {
        return "fortifyingTerritory:" + (fortifyingAgent == null ? "null" : fortifyingAgent.getTerritory().getName())
                + ", reinforcedTerritory:" + reinforcedAgent.getTerritory().getName() + " with " + units + " units"
                + " Utility: " + utility;
    }
}