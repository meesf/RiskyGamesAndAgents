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

    CountryAgent(Territory territory) {
        this.territory = territory;
        goalList = new ArrayList<>();
        this.adjacentAgents = new ArrayList<CountryAgent>();
    }

    public Territory getTerritory() {
        return territory;
    }

    public Double calculateOwnershipValue(Double friendliesweight, Double enemyweight, Double farmiesweight, Double earmiesweight) { //calculates value of owning a territory, TODO: the actual final calculation has more factors includings continents and such
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
    
    private int getTopD(int a, int attacking, int defending) {
    	if(a%2 == attacking%2) {
    		return defending;
    	}
    	return defending-1;
    }
    
    private void calcTop(double[][] grid, int a, int d) {
    	printGrid(grid);
    	System.out.println("a:"+a);
    	System.out.println("d:"+d);
    	if(a%2 == grid[0].length%2) {
    		grid[a][d] = grid[a-2][d] * Risk.DICE_ODDS_TWO.get(1).get(2);
    	} else {
    		grid[a][d-1] = grid[a-1][d+1] * Risk.DICE_ODDS_TWO.get(2).get(1);
    	}
    }
    
    private double calcDown(double[][] grid, int a, int d) {
    	double prob = grid[a][d];
    	for(int i = d; i > 0; i-=2) {
    		if(i == 1) {
    			if(a > 2) {
    				prob *= Risk.DICE_ODDS_ONE.get(0).get(2);
    			} else if(a > 1) {
    				prob *= Risk.DICE_ODDS_ONE.get(0).get(1);
    			} else {
    				prob *= Risk.DICE_ODDS_ONE.get(0).get(0);
    			}
    			i = 0;
    		} else {
    			if(a > 2) {
    				prob *= Risk.DICE_ODDS_TWO.get(0).get(2);
    			} else if(a > 1) {
    				prob *= Risk.DICE_ODDS_TWO.get(0).get(1);
    			} else {
    				prob *= Risk.DICE_ODDS_TWO.get(0).get(0);
    			}
    		}
    	}
    	return prob;
    }
    
    private void printGrid(double[][] grid) {
    	System.out.println("Grid:");
    	for(int i = grid.length-1; i >= 0 ; i--) {
    		for(int j = grid[0].length-1; j >=0 ; j--) {
    			System.out.print(grid[i][j] + ", ");
    		}
    		System.out.println("");
    	}
    }
    
    private Double getP(Integer i, ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues) {
    	Integer attackingUnits = this.getTerritory().getNUnits() + i - goal.size();
    	if(attackingUnits < 1) {
    		return 0.0;
    	}
    	Integer defendingUnits = 0;
    	for(CountryAgent ca : goal) {
    		defendingUnits += ca.getTerritory().getNUnits();
    	}
    	
    	System.out.println("goal:" + goal);
    	System.out.println("attackingUnits:" + attackingUnits);
    	System.out.println("defendingUnits:" + defendingUnits);
    	
    	double p = 0.0;
    	double[][] grid = new double[attackingUnits+1][defendingUnits+1];
    	grid[attackingUnits][defendingUnits] = 1.0;
    	
    	for(int a = attackingUnits; a > 0; a--) {
    		int d = getTopD(a, attackingUnits, defendingUnits);
    		if(grid[a][d] == 0.0) {
    			calcTop(grid, a, d);
    			p += calcDown(grid, a, d);
    		} else {
    			p += calcDown(grid, a, d);
    		}
    	}
    	return p;
    }
    
    private Double getW(HashMap<CountryAgent, Double> agentValues) {
    	return agentValues.values().stream().mapToDouble(o -> o.doubleValue()).sum();
    }
    
    private Double getD() {
    	return 1.0;
    }
    
    private double getPWD(ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues, Integer i)  {
    	double p = getP(i, goal, agentValues);
//    	double p = Risk.random.nextDouble();
    	double w = getW(agentValues);
    	double d = getD();
    	if(i == 0) {
    		return p*w*d;
    	}
    	return (p*w*d)/i;
    }
    
    private Double getV() {
    	return 0.0;
    }
    
    private double getVD(ArrayList<CountryAgent> goal, HashMap<CountryAgent, Double> agentValues, Integer i)  {
    	double v = getV();
    	double d = getD();
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
    		double bidUtil = getVD(goal, agentValues, i);
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
}


