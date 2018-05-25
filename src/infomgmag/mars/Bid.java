package infomgmag.mars;

import java.util.ArrayList;

public class Bid {
	private CountryAgent origin;
	private ArrayList<CountryAgent> goal;
	private Integer units;
	private Double utility;
	
	public Bid(CountryAgent origin, ArrayList<CountryAgent> goal, Integer units, double utility) {
		this.origin = origin;
		this.goal = goal;
		this.units = units;
		this.utility = utility;
	}
	
	public ArrayList<CountryAgent> getGoal() {
		return goal;
	}
	
	public Integer getUnits() {
		return units;
	}
	
	public Double getUtility() {
		return utility;
	}
	
	public CountryAgent getOrigin() {
		return origin;
	}
}
