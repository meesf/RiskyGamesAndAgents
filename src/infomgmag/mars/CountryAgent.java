package infomgmag.mars;

import infomgmag.Territory;
import java.util.ArrayList;
import java.util.HashMap;

public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<Goal> goalList;
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
                (this.territory.getOwner().equals(mars) ? personality.getDefensiveBonus() : personality.getOffensiveBonus()) *
                (friendlyNeighbours() * personality.getFriendlyNeighbourWeight() +
                 enemyNeighbours() * personality.getEnemyNeighbourWeight() +
                 friendlyArmies() * personality.getFriendlyArmyWeight() +
                 enemyArmies() * personality.getEnemyArmyWeight() +
                 territory.getContinentsBorderedAmount() * personality.getContinentBorderWeight() +
                 (enemyOwnsAnEntireContinent() ? 1 : 0) * personality.getEnemyOwnsWholeContinentWeight() +
                 percentageOfContinentOwned() * personality.getPercentageOfContinentWeight());
    }

    public Integer friendlyNeighbours() // calculates how many friendly neighbouring territory border this territory
    {
        Integer friends = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() == mars) {
                friends += 1;
            }
        }
        return friends;
    }

    public Integer enemyNeighbours() // calculates how many enemy neighbouring territory border this territory
    {
        Integer enemies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != mars) {
                enemies += 1;
            }
        }
        return enemies;
    }

    public Integer friendlyArmies() // calculates how many friendly armies border this territory
    {
        Integer farmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() == mars) {
                farmies += territory.getAdjacentTerritories().get(i).getNUnits();
            }
        }
        return farmies;
    }

    public Integer enemyArmies() // calculates how many enemy armies border this territory
    {
        Integer earmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != mars) {
                earmies += territory.getAdjacentTerritories().get(i).getNUnits();
            }
        }
        return earmies;
    }

    public boolean bordersEnemy() { // checks if a territory borders an enemy
        boolean bordersenemies = false;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
                bordersenemies = true;
            }
        }
        return bordersenemies;
    }

    public boolean ownWholeContinent() { // checks if the agents owns the whole continent except this territory
        return territory.getContinent().getTerritories().stream().allMatch(x -> x.getOwner() == this.mars);
    }

    public boolean enemyOwnsAnEntireContinent() {
        return territory.getOwner().getTerritories().stream().allMatch(x -> x.getOwner() == territory.getOwner())
                && territory.getOwner() != this.mars;
    }

    public double percentageOfContinentOwned() { // checks the percentage of continent owned
        double percentageofcontinent = 0;
        double territoriesOwned = 0;
        for (Territory ter: territory.getContinent().getTerritories()){
            if (ter.getOwner() == mars) {
                territoriesOwned += 1;
            }
        }
        percentageofcontinent = territoriesOwned / territory.getContinent().getTerritories().size();
        return percentageofcontinent;
    }

    public void clearlists() { // clears the lists used in determining which country gets reinforcements
        goalList.clear();
    }

    public void addAdjacentAgent(CountryAgent ca) {
        this.adjacentAgents.add(ca);
    }

    private double getGoalSuccessOdds(Integer i, Goal goal) {
        Integer attackingUnits = this.getTerritory().getNUnits() + i - goal.size() - 1;
        if (attackingUnits < 1) {
            return 0.0;
        }
        Integer defendingUnits = 0;
        for (CountryAgent ca : goal) {
            defendingUnits += ca.getTerritory().getNUnits();
        }

        ProbabilityGrid grid = new ProbabilityGrid(attackingUnits, defendingUnits);
        return grid.chanceOfWin();
    }

    private Double getGoalValue(Goal goal) {
        double value = 0.0;
        for (CountryAgent ca : goal) {
            value += ca.getValue();
        }
        value += (goal.completesContinentFor(mars) ? 1 : 0) * mars.getPersonality().getOwnWholeContinentWeight();
        return value;
    }

    private double getValue() {
        return value;
    }

    public Double getDefenseOdds(int units) {
        int totalEnemyUnits = 0;
        for (Territory t : getTerritory().getAdjacentTerritories()) {
            if (t.getOwner() != this.getTerritory().getOwner()) {
                totalEnemyUnits += t.getNUnits();
            }
        }
        ProbabilityGrid grid = new ProbabilityGrid(units, totalEnemyUnits);
        return grid.chanceOfWin();
    }

    public Double getDefenseOddsOfCapture(Goal goal, int units) {
        int totalEnemyUnits = goal.getFinalGoal().getTerritory().getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() != this.getTerritory().getOwner()
                        && !goal.stream().map(x -> x.getTerritory()).anyMatch(x -> x == t))
                .mapToInt(t -> t.getNUnits()).sum();
        ProbabilityGrid grid = new ProbabilityGrid(units + this.getTerritory().getNUnits(), totalEnemyUnits);
        return grid.chanceOfWin();
    }

    private double getGoalUtility(Goal goal, Integer i) {
        double p = getGoalSuccessOdds(i, goal);
        double w = getGoalValue(goal);
        double d = getDefenseOddsOfCapture(goal, i);
        return p * w * d;
    }
    
    private double getGoalUtilityPerUnit(Goal goal, Integer i) {
        return (getGoalUtility(goal,i) - getGoalUtility(goal,0)) / i;
    }


    private double getDefenseUtility(Integer i) {
        double v = getValue();
        double d = getDefenseOdds(this.getTerritory().getNUnits() + i);
        return v * d;
    }
    
    private double getDefenseUtilityPerUnit(Integer i) {
        return (getDefenseUtility(i) - getDefenseUtility(0)) / i;
    }

    public ArrayList<ReinforcementBid> getBids(Integer unitsLeft) {
        ArrayList<ReinforcementBid> result = new ArrayList<>();
        for (Goal goal : goalList) {
            result.addAll(getOffensiveBids(unitsLeft, goal));
        }
        result.addAll(getDefensiveBids(unitsLeft));
        return result;
    }

    public ReinforcementBid getBestBid(Integer unitsLeft) {
            ReinforcementBid bestBid = getBids(unitsLeft).stream()
            .max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1)))
            .get();
            
        return getBids(unitsLeft).stream()
                .max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1)))
                .get();

    }
    public ArrayList<DefensiveBid> getDefensiveBids(Integer unitsLeft) {
        ArrayList<DefensiveBid> result = new ArrayList<>();
        for (int i = 0; i <= unitsLeft; i++) {
    		double bidUtil = getDefenseUtilityPerUnit(i);
    		result.add(new DefensiveBid(this, i, bidUtil));
        }
        return result;
    }
    
    public ArrayList<FortifierBid> getFortifierBids () {
        ArrayList<FortifierBid> result = new ArrayList<>();
        
        if (!goalList.isEmpty()) {
            for (Goal goal : goalList) {
                for (int i = 1; i < territory.getNUnits(); i++) {
                    double util = getGoalUtility(goal, -i) * i - getGoalUtilityPerUnit(goal,0);
                    result.add(new FortifierBid(this, i, util));
                }
            }
        }
        for (int i = 1; i < territory.getNUnits(); i++) {
            double util = getDefenseUtility(-i) * i - getDefenseUtilityPerUnit(0);
            result.add(new FortifierBid(this,i,util));
        }
        return result;
    }
    
    public ReinforcementBid getBestMaxBid(Integer unitsLeft) {
        return this.getBids(unitsLeft).stream()
                .filter(x -> x.getUnits() == unitsLeft)
                .max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1)))
                .get();
    }

    private ArrayList<OffensiveBid> getOffensiveBids(Integer unitsLeft, Goal goal) {
        ArrayList<OffensiveBid> result = new ArrayList<>();
        OffensiveBid bestBid = null;
        for (int i = 0; i <= unitsLeft; i++) {
            double bidUtil = getGoalUtilityPerUnit(goal, i);
            OffensiveBid bid = new OffensiveBid(this, goal, i, bidUtil);
            result.add(bid);
            if (bestBid == null || bidUtil > bestBid.getUtility()) {
                bestBid = bid;
            }
        }
        return result;
    }

    public String toString() {
        return territory.toString();
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

        if (territory.getOwner() == mars) {
            goalList.add(goal);
        } else {
            for (CountryAgent ca : adjacentAgents) {
                if (!goal.contains(ca)) {
                    Goal newGoal = (Goal) goal.clone();
                    newGoal.addEarlierGoal(this);
                    createGoals(newGoal);
                }
            }
        }
    }
}
