package infomgmag.mars;

public class DefensiveBid extends ReinforcementBid {
	
	public DefensiveBid(CountryAgent reinforcedAgent, Integer units, double utility) {
		super(reinforcedAgent, null, units, utility);
	}
	
	public DefensiveBid(CountryAgent reinforcedAgent, CountryAgent fortifyingAgent, Integer units, double utility) {
		super(reinforcedAgent, fortifyingAgent, units, utility);
	}
	
	public String toString() {
		return "fortifyingTerritory:"+fortifyingAgent.getTerritory().getName()+", reinforcedTerritory:" + reinforcedAgent.getTerritory().getName()+ " with "+units+ " units" + " Utility: " + utility;
	}
}