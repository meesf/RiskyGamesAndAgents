package infomgmag.mars;

import java.util.ArrayList;

public class OffensiveBid extends ReinforcementBid {
	private ArrayList<CountryAgent> goal;
	
	public OffensiveBid(CountryAgent reinforcedAgent, ArrayList<CountryAgent> goal, Integer units, double utility) {
		super(reinforcedAgent, null, units, utility);
		this.goal = goal;
	}
	
	public ArrayList<CountryAgent> getGoal() {
		return goal;
	}
}
