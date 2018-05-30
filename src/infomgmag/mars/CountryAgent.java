package infomgmag.mars;

import infomgmag.Continent;
import infomgmag.Territory;
import java.util.ArrayList;
import java.util.HashMap;


public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<ArrayList<CountryAgent>> goalList;
    private ArrayList<CountryAgent> finalGoal;

    CountryAgent(Territory territory) {
        this.territory = territory;
        goalList = new ArrayList<>();
        this.adjacentAgents = new ArrayList<CountryAgent>();
    }

    public Territory getTerritory() {
        return territory;
    }

    public Double calculateOwnershipValue(Double friendliesweight, Double enemyweight, Double farmiesweight, Double earmiesweight
        ,Double continentBorderWeight, Double ownWholeContinentWeight, Double enemyOwnsWholeContinentWeight, Double percentageOfContinentWeight) { //calculates value of owning a territory

        Double territoryvalue = 0.0;
        int x = 0;
        int y = 0;
        if (ownWholeContinent() == true) {
            x = 1;
        }
        if (enemyOwnsAnEntireContinent() == true){
            y = 1;
        }

        territoryvalue = (((friendlyNeighbours() * friendliesweight) + (enemyNeighbours() * enemyweight) + (friendlyArmies() * farmiesweight) + (enemyArmies() * earmiesweight)
                + (numberOfContinentsBordered() * continentBorderWeight) + (x * ownWholeContinentWeight) + (y * enemyOwnsWholeContinentWeight) + (percentageOfContinentOwned() * percentageOfContinentWeight)));
        if (ownWholeContinent() == true){
            territoryvalue += 1;
        }
        return territoryvalue;
        //TODO: Somehow, this value has to be linked to the amount of enemy troops on this territory, I tried Pairs but that didn't work great, maybe a list?
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

    public boolean ownWholeContinent(){     //checks if the agents owns the whole continent except this territory
        int territoriesOwned = 0;
        for (Territory ter : territory.getBelongsTo().getTerritories()){
            if (ter.getOwner() == territory.getOwner()){
                territoriesOwned += 1;
            }
        }
        if (territoriesOwned == territory.getBelongsTo().getTerritories().size() - 1){
            return true;
        }
        else {
            return false;
        }
    }

    public boolean enemyOwnsAnEntireContinent(){
        int territoriesOwned = 0;
        for (Territory ter : territory.getBelongsTo().getTerritories()){
            if (ter.getOwner() == territory.getOwner()){
                territoriesOwned += 1;
            }
        }
        if (territoriesOwned == territory.getBelongsTo().getTerritories().size()){
            return true;
        }
        else {
            return false;
        }
    }

    public int numberOfContinentsBordered(){       //checks the amount of continents bordered that are not the territories 'home' continent
        int continentsBordered = 0;
        ArrayList<Continent> adjacentContinents = new ArrayList<>();
        adjacentContinents.add(territory.getBelongsTo());
        for (Territory ter: territory.getAdjacentTerritories()){
            if (adjacentContinents.contains(ter.getBelongsTo())){ }
            else {
                adjacentContinents.add(ter.getBelongsTo());
                continentsBordered += 1;
            }
        }
        return continentsBordered;
    }

    public double percentageOfContinentOwned() {        //checks the percentage of continent owned
        double percentageofcontinent = 0;
        double territoriesOwned = 0;
        for (Territory ter: territory.getBelongsTo().getTerritories()){
            if (ter.getOwner() == territory.getOwner()){
                territoriesOwned += 1;
            }
        }
        percentageofcontinent = territoriesOwned / territory.getBelongsTo().getTerritories().size();
        return percentageofcontinent;
    }

    public void clearlists(){   //clears the lists used in determining which country gets reinforcements
        goalList.clear();
    }

    public void addAdjacentAgent(CountryAgent ca) {
        this.adjacentAgents.add(ca);
    }
    public ArrayList<CountryAgent> getAdjacentAgents() {
        return adjacentAgents;
    }

    private double getP(Integer i, ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
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

    private Double getW(ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
        double value = 0.0;
        for(CountryAgent ca : goal){
            value += agentValues.get(ca);
        }
    	return value;
    }
    
    private Double getD(int units) {
        int totalEnemyUnits = 0;
        for(Territory t : getTerritory().getAdjacentTerritories()){
            totalEnemyUnits += t.getNUnits();
        }
        ProbabilityGrid grid = new ProbabilityGrid(units, totalEnemyUnits);
    	return grid.chanceOfWin();
    }
    
    private double getPWD(ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues, Integer i)  {
    	double p = getP(i, goal, agentValues);
    	double w = getW(goal, agentValues);
    	double d = getD(i);
    	if(i == 0) {
    		return p*w*d;
    	}
    	return (p*w*d)/i;
    }
    
    private Double getV(HashMap<CountryAgent, Double> agentValues) {
    	return agentValues.get(this);
    }
    
    private double getVD(HashMap<CountryAgent, Double> agentValues, Integer i)  {
    	double v = getV(agentValues);
    	double d = getD(this.getTerritory().getNUnits() + i);
    	if(i == 0) {
    		return v*d;
    	}
    	return (v*d)/i;
    }
    
    public Bid getBid(Integer unitsLeft, HashMap<CountryAgent, Double> agentValues) {
    	Bid bestBid = null;
    	for(ArrayList<CountryAgent> goal : goalList) {
    		Bid offBid = getOffensiveBid(unitsLeft, goal, agentValues);
    		if(bestBid == null || offBid.getUtility() > bestBid.getUtility()) {
    			bestBid = offBid;
    		}
    		
    		Bid defBid = getDefensiveBid(unitsLeft, goal, agentValues);
    		if(bestBid == null || defBid.getUtility() > bestBid.getUtility()) {
    			bestBid = defBid;
    		}
    	}
    	return bestBid;
    }
    
    private Bid getDefensiveBid(Integer unitsLeft, ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
    	Bid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getVD(agentValues, i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new Bid(this, goal, i, bidUtil);
    		}
    	}
    	return bestBid;
    }
    
    private Bid getOffensiveBid(Integer unitsLeft, ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
    	Bid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getPWD(goal, agentValues, i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new Bid(this, goal, i, bidUtil);
    		}
    	}
    	return bestBid;
    }

    public ArrayList<ArrayList<CountryAgent>> getGoalList() {
        return goalList;
    }
    
    public String toString() {
    	return territory.toString();
    }

    public void setFinalGoal(ArrayList<CountryAgent> finalGoal){
        this.finalGoal = finalGoal;
    }

    public ArrayList<CountryAgent> getFinalGoal() {
        return finalGoal;
    }
}


