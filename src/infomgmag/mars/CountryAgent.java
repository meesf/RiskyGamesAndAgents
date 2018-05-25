package infomgmag.mars;

import infomgmag.Risk;
import infomgmag.Territory;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;



public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<ArrayList<CountryAgent>> goalList;
    private ArrayList<CountryAgent> goal;

    CountryAgent(Territory territory) {
        this.territory = territory;
        goalList = new ArrayList<>();
        goal = new ArrayList<CountryAgent>();
        this.adjacentAgents = new ArrayList<CountryAgent>();
    }

    public Territory getTerritory() {
        return territory;
    }

    public Double calculateOwnershipValue(Double friendliesweight, Double enemyweight, Double farmiesweight, Double earmiesweight)  //calculates value of owning a territory, TODO: the actual final calculation has more factors includings continents and such
    {
        Double territoryvalue = 0.0;
        territoryvalue = (((friendlyNeighbours() * friendliesweight) + (enemyNeighbours() * enemyweight) + (friendlyArmies() * farmiesweight) + (enemyArmies() * earmiesweight)));
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
    public ArrayList<CountryAgent> getAdjacentAgents() {
        return adjacentAgents;
    }

    public Pair<Double, Integer> getBid(Integer unitsLeft) {
        Double value = Risk.random.nextDouble() * 10;
        Integer units = Risk.random.nextInt(unitsLeft + 1);
        Integer index = Risk.random.nextInt(goalList.size());
        goal = goalList.get(index);
        return new Pair<Double, Integer>(value, units);
    }
    
    public double getPWD(ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues, Integer i)  {
    	return Risk.random.nextDouble();
    }
    
    public Bid getOffensiveBid(Integer unitsLeft, ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
    	Bid bestBid = null;
    	for(int i=0; i<=unitsLeft; i++) {
    		double bidUtil = getPWD(goal, agentValues, i);
    		if(bestBid == null || bidUtil > bestBid.getUtility()) {
    			bestBid = new Bid(goal, i, bidUtil);
    		}
    	}
    	return bestBid;
    }

    public ArrayList<ArrayList<CountryAgent>> getGoalList() {
        return goalList;
    }
}


