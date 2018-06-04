package infomgmag.mars;

import infomgmag.Territory;
import java.util.ArrayList;
import java.util.HashMap;


public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<ArrayList<CountryAgent>> goalList;
    private ArrayList<CountryAgent> finalGoal;
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

    public void calculateOwnershipValue(Double friendliesweight, Double enemyweight, Double farmiesweight, Double earmiesweight) { //calculates value of owning a territory, TODO: the actual final calculation has more factors includings continents and such
        this.value = (((friendlyNeighbours() * friendliesweight) + (enemyNeighbours() * enemyweight) + (friendlyArmies() * farmiesweight) + (enemyArmies() * earmiesweight)));
    }

    public void receivemessagefriendly(ArrayList<CountryAgent> countries){    //adds the route to the goallist when a friendly country is reached
        goalList.add(countries);
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

    public double ownWholeContinent() {
        double percentageofcontinent = 0;
        //todo: calculate how much of the current continent is yours, question: how does a territory know which continent it belongs to?
        return percentageofcontinent;
    }

    public void clearlists(){   //clears the lists used in determining which country gets reinforcements
        goalList.clear();
    }

    public void addAdjacentAgent(CountryAgent ca) {
        this.adjacentAgents.add(ca);
    }

    private double getGoalSuccessOdds(Integer i, ArrayList<CountryAgent> goal) {
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

    private Double getGoalValue(ArrayList<CountryAgent> goal) {
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

    private double getGoalUtility(ArrayList<CountryAgent> goal, Integer i)  {
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
    
    private double getDefendseUtility(Integer i) {
    	double v = getValue();
    	double d = getDefenseOdds(this.getTerritory().getNUnits() + i);
    	if(i == 0) {
    		return v*d;
    	}
    	return (v*d)/i;
    }
    
    public ReinforcementBid getBid(Integer unitsLeft) {
    	ReinforcementBid bestBid = null;
    	for(ArrayList<CountryAgent> goal : goalList) {
    		OffensiveBid offBid = getOffensiveBid(unitsLeft, goal);
    		if(bestBid == null || offBid.getUtility() > bestBid.getUtility()) {
    			bestBid = offBid;
    		}
    	}
    	if (bestBid == null) {
    		this.finalGoal = new ArrayList<>();
    	} else {
    		this.finalGoal = ((OffensiveBid)bestBid).getGoal();
    	}
    	DefensiveBid defBid = getDefensiveBid(null, unitsLeft);
        if(bestBid == null || defBid.getUtility() > bestBid.getUtility()) {
            bestBid = defBid;
        }
    	return bestBid;
    }
    
    public DefensiveBid getDefensiveBid(CountryAgent fortifyingAgent, Integer unitsLeft) {
    	DefensiveBid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getDefendseUtility(i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new DefensiveBid(this, fortifyingAgent, i, bidUtil);
    		}
    	}
    	return bestBid;
    }
    
    private OffensiveBid getOffensiveBid(Integer unitsLeft, ArrayList<CountryAgent> goal) {
    	OffensiveBid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getGoalUtility(goal, i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new OffensiveBid(this, goal, i, bidUtil);
    		}
    	}
    	return bestBid;
    }
    
    public String toString() {
    	return territory.toString();
    }

    public AttackBid getAttackBid() {
        return new AttackBid(territory, finalGoal.get(finalGoal.size() - 1).getTerritory());
    }
    
    public void createGoals() {
        for (CountryAgent ca : adjacentAgents) {
            ArrayList<CountryAgent> goal = new ArrayList<CountryAgent>();
            goal.add(this);
            ca.createGoals(goal);
        }
    }
    
    public void createGoals(ArrayList<CountryAgent> goal) {
        if(goal.size() >= Mars.goalLength)
            return;

        if(territory.getOwner() == mars) {
            goalList.add(goal);
        } else {
            for(CountryAgent ca : adjacentAgents) {
                if (!goal.contains(ca)) {
                    ArrayList<CountryAgent> newGoal = (ArrayList<CountryAgent>) goal.clone();
                    newGoal.add(this);
                    createGoals(newGoal);
                }
            }
        }
    }
}


