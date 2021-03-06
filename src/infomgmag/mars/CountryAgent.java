package infomgmag.mars;

import infomgmag.Player;
import infomgmag.Territory;

import java.util.ArrayList;

public class CountryAgent {
    private Territory territory;
    public ArrayList<CountryAgent> adjacentAgents;
    private ArrayList<Goal> goalList;
    private Mars mars;
    private double value;
    private boolean ownedByHatedEnemy;
    private double staticValue;

    CountryAgent(Territory territory, Mars mars) {
        this.territory = territory;
        goalList = new ArrayList<>();
        this.adjacentAgents = new ArrayList<CountryAgent>();
        this.mars = mars;
        setStaticValue();
    }

    public Territory getTerritory() {
        return territory;
    }
    
    public void setStaticValue() {
        ArrayList<Territory> borders = new ArrayList<>();
        for(Territory t : territory.getContinent().getTerritories()) {
            for(Territory ta : t.getAdjacentTerritories()) {
                if(!territory.getContinent().getTerritories().contains(ta) && !borders.contains(ta))
                    borders.add(ta);
            }
        }
        this.staticValue = (double) territory.getContinent().getReinforcements() / ((double) borders.size() * (double) territory.getContinent().getTerritories().size());
    }

    //calculates value of owning a territory
    public void calculateOwnershipValue(Personality personality) {
        this.value =
                (this.territory.getOwner().equals(mars) ? personality.getDefensiveBonus() : personality.getOffensiveBonus()) *
                (friendlyNeighbours() * personality.getFriendlyNeighbourWeight() +
                 enemyNeighbours() * personality.getEnemyNeighbourWeight() +
                 friendlyArmies() * personality.getFriendlyArmyWeight() +
                 enemyArmies() * personality.getEnemyArmyWeight() +
                 territory.getContinentsBorderedAmount() * personality.getContinentBorderWeight() +
                 (enemyOwnsAnEntireContinent() ? 1 : 0) * personality.getEnemyOwnsWholeContinentWeight() +
                 percentageOfContinentOwned() * personality.getPercentageOfContinentWeight() +
                 (ownedByHatedEnemy ? 1 : 0) * personality.getHatedBonus() +
                 this.staticValue * personality.getStaticBonus());
    }

    // calculates how many friendly neighbouring territories border this territory
    public long friendlyNeighbours() {
        return territory.getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() == mars)
                .count();
    }

    // calculates how many enemy neighbouring territories border this territory
    public long enemyNeighbours() {
        return territory.getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() != mars)
                .count();
    }

    // calculates how many friendly armies border this territory
    public long friendlyArmies() {
        return territory.getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() == mars)
                .mapToInt(t -> t.getUnits())
                .sum();
    }

    // calculates how many enemy armies border this territory
    public long enemyArmies() {
        return territory.getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() != mars)
                .mapToInt(t -> t.getUnits())
                .sum();
    }

    // checks if this territory borders an enemy
    public boolean bordersEnemy() { 
        return territory.getAdjacentTerritories().stream()
                .anyMatch(t -> t.getOwner() != mars);
    }

    // checks if the agent owns the whole continent except this territory
    public boolean ownWholeContinent() {
        return territory.getContinent().getTerritories().stream().allMatch(x -> x.getOwner() == this.mars);
    }

    public boolean enemyOwnsAnEntireContinent() {
        return territory.getOwner().getTerritories().stream().allMatch(x -> x.getOwner() == territory.getOwner())
                && territory.getOwner() != this.mars;
    }

    // calculates the percentage of continent owned
    public double percentageOfContinentOwned() {
        double owned = territory.getContinent().getTerritories().stream()
                .filter(t -> t.getOwner() == mars)
                .count();
        return owned / territory.getContinent().getTerritories().size();
    }

    // clears the goals
    public void clearGoals() { 
        goalList.clear();
    }

    public void addAdjacentAgent(CountryAgent ca) {
        this.adjacentAgents.add(ca);
    }

    // Returns the odds of reaching a certain goal with a certain amount of extra units
    private double getGoalSuccessOdds(int i, Goal goal) {
        int attackingUnits = this.getTerritory().getUnits() + i - goal.size() - 1;
        if (attackingUnits < 1) {
            return 0.0;
        }
        int defendingUnits = 0;
        for (CountryAgent ca : goal) {
            defendingUnits += ca.getTerritory().getUnits();
        }

        ProbabilityGrid grid = new ProbabilityGrid(attackingUnits, defendingUnits);
        return grid.chanceOfWin();
    }

    private double getGoalValue(Goal goal) {
        double goalValue = 0.0;
        for (CountryAgent ca : goal) {
            goalValue += ca.value;
        }

        // Adds extra bonuses to the goal
        goalValue += (goal.completesContinentFor(mars) ? 1 : 0) * mars.getPersonality().getOwnWholeContinentWeight();
        goalValue += goal.killsPlayers(mars) * mars.getPersonality().getKillEnemyWeight();
        goalValue += territory.getUnits() * mars.getPersonality().getClusteringWeight();
        goalValue += (mars.hasConqueredTerritoryInTurn() ? 0 : 1) * mars.getPersonality().getAttackFirstCountryWeight();
        return goalValue;
    }

    // Returns the odds of preserve the territory with a certain amount of extra units
    public double getDefenseOdds(int units) {
        int totalEnemyUnits = 0;
        for (Territory t : getTerritory().getAdjacentTerritories()) {
            if (t.getOwner() != this.getTerritory().getOwner()) {
                totalEnemyUnits += t.getUnits();
            }
        }
        ProbabilityGrid grid = new ProbabilityGrid(units, totalEnemyUnits);
        return grid.chanceOfWin();
    }

    // Returns the odds of preserving the last territory in a goal with a certain amount of extra units after completing that goal
    public double getDefenseOddsOfCapture(Goal goal, int units) {
        int totalEnemyUnits = goal.getFinalGoal().getTerritory().getAdjacentTerritories().stream()
                .filter(t -> t.getOwner() != this.getTerritory().getOwner()
                        && !goal.stream().map(x -> x.getTerritory()).anyMatch(x -> x == t))
                .mapToInt(t -> t.getUnits()).sum();
        ProbabilityGrid grid = new ProbabilityGrid(units + this.getTerritory().getUnits(), totalEnemyUnits);
        return grid.chanceOfWin();
    }

    private double getGoalUtility(Goal goal, int i) {
        double p = getGoalSuccessOdds(i, goal);
        double w = getGoalValue(goal);
        double d = getDefenseOddsOfCapture(goal, i);
        return p * w * d;
    }

    private double getGoalUtilityPerUnit(Goal goal, int i) {
        return (getGoalUtility(goal,i) - getGoalUtility(goal,0)) / (i > 0 ? i : 1);
    }

    private double getDefenseUtilityPerUnit(int i) {
        return (getDefenseUtility(i) - getDefenseUtility(0)) / (i > 0 ? i : 1);
    }

    private double getDefenseUtility(int i) {
        double v = value;
        double d = getDefenseOdds(this.getTerritory().getUnits() + i);
        return v * d;
    }

    public ArrayList<ReinforcementBid> getBids(int unitsLeft) {
        ArrayList<ReinforcementBid> result = new ArrayList<>();
        for (Goal goal : goalList) {
            result.addAll(getOffensiveBids(unitsLeft, goal));
        }
        result.addAll(getDefensiveBids(unitsLeft));
        return result;
    }

    public ReinforcementBid getBestBid(int unitsLeft) {
        return getBids(unitsLeft).stream()
            .max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1)))
            .get();
    }

    public ArrayList<DefensiveBid> getDefensiveBids(int unitsLeft) {
        ArrayList<DefensiveBid> result = new ArrayList<>();
        for (int i = 0; i <= unitsLeft; i++) {
    		double bidUtil = getDefenseUtilityPerUnit(i);
    		result.add(new DefensiveBid(this, i, bidUtil));
        }
        return result;
    }

    public ArrayList<FortifierBid> getFortifierBids () {
        // Creating fortifier bids using the loss of utility of the best goal when moving a certain amount of units to another territory
        ArrayList<FortifierBid> result = new ArrayList<>();
        for (int i = 1; i < territory.getUnits(); i++) {
            double worstUtil = Double.POSITIVE_INFINITY;
            if (!goalList.isEmpty()) {
                for (Goal goal : goalList) {
                    double util = getGoalUtility(goal, -i) - getGoalUtility(goal, 0);
                    if (util < worstUtil) {
                        worstUtil = util;
                    }
                }
            }
            double defensiveUtil = getDefenseUtility(-i) - getDefenseUtility(0);
            if (defensiveUtil < worstUtil) {
                result.add(new FortifierBid(this,i, defensiveUtil));
            } else {
                result.add(new FortifierBid(this, i, worstUtil));
            }
        }
        return result;
    }

    private ArrayList<OffensiveBid> getOffensiveBids(int unitsLeft, Goal goal) {
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
        if(territory.getOwner() == mars)
            return;
        for (CountryAgent ca : adjacentAgents) {
            Goal goal = new Goal();
            goal.addEarlierGoal(this);
            ca.createGoals(goal);
        }
    }

    public void isHated(Player player){
        ownedByHatedEnemy = false;
        if (territory.getOwner() == player) {
            ownedByHatedEnemy = true;
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

    public ReinforcementBid getAttackBid() {
        ArrayList<ReinforcementBid> result = new ArrayList<>();
        for (Goal goal : goalList) {
            double bidUtil = getGoalUtility(goal, 0);
            OffensiveBid bid = new OffensiveBid(this, goal, 0, bidUtil);
            result.add(bid);
        }
        double bidUtil = getDefenseUtility(0);
        result.add(new DefensiveBid(this, 0, bidUtil));
        return result.stream().max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1))).get();
    }

    public Double getValue(){
        return value;
    }
}
