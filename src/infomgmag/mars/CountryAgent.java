package infomgmag.mars;

import com.sun.org.apache.xpath.internal.operations.Bool;
import infomgmag.Continent;
import infomgmag.Territory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Stream;


public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<Goal> goalList;
    private Goal finalGoal;
    private Mars mars;
    private double value;

    CountryAgent(Territory territory, Mars mars) {
        this.territory = territory;
        goalList = new ArrayList<>();
        this.adjacentAgents = new ArrayList<CountryAgent>();
        this.mars = mars;
    }

    public Territory getTerritory() {
        return territory;
    }

    public void calculateOwnershipValue(Personality personality) { //calculates value of owning a territory
        this.value =
                friendlyNeighbours() * personality.getFriendliesweight() +
                enemyNeighbours() * personality.getEnemiesweight() +
                friendlyArmies() * personality.getFriendliesweight() +
                enemyArmies() * personality.getEnemiesweight() +
                territory.getContinentsBorderedAmount() * personality.getContinentBorderWeight() +
                (ownWholeContinent() ? 1 : 0) * personality.getOwnWholeContinentWeight() +
                (enemyOwnsAnEntireContinent() ? 1 : 0) * personality.getEnemyOwnsWholeContinentWeight() +
                percentageOfContinentOwned() * personality.getPercentageOfContinentWeight() +
                (ownWholeContinent() ? 1 : 0);
        //TODO: Somehow, this value has to be linked to the amount of enemy troops on this territory, I tried Pairs but that didn't work great, maybe a list?
    }

    public Integer friendlyNeighbours() //calculates how many friendly neighbouring territory border this territory
    {
        Integer friends = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++)
        {
            if (territory.getAdjacentTerritories().get(i).getOwner() == territory.getOwner()) {
                friends += 1;
            }
        }
        return friends;
    }

    public Integer enemyNeighbours()    //calculates how many enemy neighbouring territory border this territory
    {
        Integer enemies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
                enemies += 1;
            }
        }
        return enemies;
    }

    public Integer friendlyArmies() //calculates how many friendly armies border this territory
    {
        Integer farmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() == territory.getOwner()) {
                farmies += territory.getAdjacentTerritories().get(i).getNUnits();
            }
        }
        return farmies;
    }

    public Integer enemyArmies()    //calculates how many enemy armies border this territory
    {
        Integer earmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
                earmies += territory.getAdjacentTerritories().get(i).getNUnits();
            }
        }
        return earmies;
    }

    public boolean bordersEnemy(){  //checks if a territory borders an enemy
        boolean bordersenemies = false;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
                bordersenemies = true;
            }
        }
        return bordersenemies;
    }

    public boolean ownWholeContinent(){     //checks if the agents owns the whole continent except this territory
        return territory.getContinent().getTerritories().stream().allMatch(x -> x.getOwner() == this.mars);
    }

    public boolean enemyOwnsAnEntireContinent(){
        return  territory.getContinent().getTerritories().stream().allMatch(x -> x.getOwner() == territory.getOwner()) && territory.getOwner() != this.mars;
    }

    public double percentageOfContinentOwned() {        //checks the percentage of continent owned
        double percentageofcontinent = 0;
        double territoriesOwned = 0;
        for (Territory ter: territory.getContinent().getTerritories()){
            if (ter.getOwner() == territory.getOwner()){
                territoriesOwned += 1;
            }
        }
        percentageofcontinent = territoriesOwned / territory.getContinent().getTerritories().size();
        return percentageofcontinent;
    }

    public void clearlists(){   //clears the lists used in determining which country gets reinforcements
        goalList.clear();
    }

    public void addAdjacentAgent(CountryAgent ca) {
        this.adjacentAgents.add(ca);
    }

    private double getGoalSuccessOdds(Integer i, Goal goal) {
        Integer attackingUnits = this.getTerritory().getNUnits() + i - goal.size() - 1;
        if(attackingUnits < 1) {
            return 0.0;
        }
        Integer defendingUnits = 0;
        for(CountryAgent ca : goal) {
            defendingUnits += ca.getTerritory().getNUnits();
        }

        ProbabilityGrid grid = new ProbabilityGrid(attackingUnits, defendingUnits);
        return grid.chanceOfWin();
    }

    private Double getGoalValue(Goal goal) {
        double value = 0.0;
        for(CountryAgent ca : goal){
            value += ca.getValue();
        }
    	return value;
    }

    private double getValue() {
        return value;
    }

    public Double getDefenseOdds(int units) {
        int totalEnemyUnits = 0;
        for(Territory t : getTerritory().getAdjacentTerritories()){
            if(t.getOwner() != this.getTerritory().getOwner()){
                totalEnemyUnits += t.getNUnits();
            }
        }
        ProbabilityGrid grid = new ProbabilityGrid(units, totalEnemyUnits);
    	return grid.chanceOfWin();
    }

    private double getGoalUtility(Goal goal, Integer i)  {
    	double p = getGoalSuccessOdds(i, goal);
    	double w = getGoalValue(goal);
    	double d = getDefenseOdds(i);
    	if(i == 0) {
    		return p*w*d;
    	}
    	return (p*w*d)/i;
    }

    private Double getTerritoryValue(HashMap<CountryAgent, Double> agentValues) {
    	return agentValues.get(this);
    }
    
    private double getDefenseUtility(Integer i) {
    	double v = getValue();
    	double d = getDefenseOdds(this.getTerritory().getNUnits() + i);
    	if(i == 0) {
    		return v*d;
    	}
    	return (v*d)/i;
    }
    
    public ArrayList<ReinforcementBid> getBids(Integer unitsLeft) {
        ArrayList<ReinforcementBid> result = new ArrayList<>();
    	for(Goal goal : goalList) {
    		result.addAll(getOffensiveBids(unitsLeft, goal));
    	}
    	
    	DefensiveBid defBid = getDefensiveBid(null, unitsLeft);
    	result.add(defBid);
    	return result;
    }
    
    public DefensiveBid getDefensiveBid(CountryAgent fortifyingAgent, Integer unitsLeft) {
    	DefensiveBid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getDefenseUtility(i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new DefensiveBid(this, fortifyingAgent, i, bidUtil);
    		}
    	}
    	return bestBid;
    }

    private ArrayList<OffensiveBid> getOffensiveBids(Integer unitsLeft, Goal goal) {
        ArrayList<OffensiveBid> result = new ArrayList<>();
    	OffensiveBid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getGoalUtility(goal, i);
    		OffensiveBid bid = new OffensiveBid(this, goal, i, bidUtil);
    		result.add(bid);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = bid;
    		}
    	}
    	this.finalGoal = bestBid.getGoal();
    	return result;
    }

    public String toString() {
    	return territory.toString();
    }

    public AttackBid getAttackBid() {
        return new AttackBid(territory, finalGoal.get(finalGoal.size() - 1).getTerritory());
    }
    
    public void createGoals() {
        for (CountryAgent ca : adjacentAgents) {
            Goal goal = new Goal();
            goal.addEarlierGoal(this);
            ca.createGoals(goal);
        }
    }
    
    public void createGoals(Goal goal) {
        if(goal.size() >= this.mars.getPersonality().getGoalLength())
            return;

        if(territory.getOwner() == mars) {
            goalList.add(goal);
        } else {
            for(CountryAgent ca : adjacentAgents) {
                if (!goal.contains(ca)) {
                    Goal newGoal = (Goal) goal.clone();
                    newGoal.addEarlierGoal(this);
                    createGoals(newGoal);
                }
            }
        }
    }
}


