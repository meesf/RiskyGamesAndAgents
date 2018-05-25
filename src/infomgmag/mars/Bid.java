package infomgmag.mars;

import java.util.ArrayList;

public class Bid {
	private ArrayList<CountryAgent> goal;
	private Integer units;
	private Double utility;
	
	public Bid(ArrayList<CountryAgent> goal, Integer units, double utility) {
		this.goal = goal;
		this.units = units;
		this.utility = utility;
	}
	
	public ArrayList<CountryAgent> getGoal() {
		return goal;
	}
	
	public Integer getUntis() {
		return units;
	}
	
	public Double getUtility() {
		return utility;
	}
}
