package infomgmag.mars;

import infomgmag.Continent;
import infomgmag.Territory;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
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

    public void calculateOwnershipValue(Double offensiveBonus, Double defensiveBonus, Double friendliesweight,
            Double enemyweight, Double farmiesweight, Double earmiesweight, Double continentBorderWeight,
            Double ownWholeContinentWeight, Double enemyOwnsWholeContinentWeight, Double percentageOfContinentWeight) { // calculates
                                                                                                                        // value
                                                                                                                        // of
                                                                                                                        // owning
                                                                                                                        // a
                                                                                                                        // territory

        this.value = (this.territory.getOwner().equals(mars) ? defensiveBonus : offensiveBonus)
                * (friendlyNeighbours() * friendliesweight + enemyNeighbours() * enemyweight
                        + friendlyArmies() * farmiesweight + enemyArmies() * earmiesweight
                        + numberOfContinentsBordered() * continentBorderWeight
                        + (ownWholeContinent() ? 1 : 0) * ownWholeContinentWeight
                        + (enemyOwnsAnEntireContinent() ? 1 : 0) * enemyOwnsWholeContinentWeight
                        + percentageOfContinentOwned() * percentageOfContinentWeight + (ownWholeContinent() ? 1 : 0));

        // TODO: Somehow, this value has to be linked to the amount of enemy troops on
        // this territory, I tried Pairs but that didn't work great, maybe a list?
    }

    public Integer friendlyNeighbours() // calculates how many friendly neighbouring territory border this territory
    {
        Integer friends = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() == territory.getOwner()) {
                friends += 1;
            }
        }
        return friends;
    }

    public Integer enemyNeighbours() // calculates how many enemy neighbouring territory border this territory
    {
        Integer enemies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
                enemies += 1;
            }
        }
        return enemies;
    }

    public Integer friendlyArmies() // calculates how many friendly armies border this territory
    {
        Integer farmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() == territory.getOwner()) {
                farmies += territory.getAdjacentTerritories().get(i).getNUnits();
            }
        }
        return farmies;
    }

    public Integer enemyArmies() // calculates how many enemy armies border this territory
    {
        Integer earmies = 0;
        for (int i = 0; i < territory.getAdjacentTerritories().size(); i++) {
            if (territory.getAdjacentTerritories().get(i).getOwner() != territory.getOwner()) {
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
        return territory.getBelongsTo().getTerritories().stream().allMatch(x -> x.getOwner() == this.mars);
    }

    public boolean enemyOwnsAnEntireContinent() {
        return territory.getBelongsTo().getTerritories().stream().allMatch(x -> x.getOwner() == territory.getOwner())
                && territory.getOwner() != this.mars;
    }

    public int numberOfContinentsBordered() { // checks the amount of continents bordered that are not the territories
                                              // 'home' continent
        int continentsBordered = 0;
        ArrayList<Continent> adjacentContinents = new ArrayList<>();
        adjacentContinents.add(territory.getBelongsTo());
        for (Territory ter : territory.getAdjacentTerritories()) {
            if (adjacentContinents.contains(ter.getBelongsTo())) {
            } else {
                adjacentContinents.add(ter.getBelongsTo());
                continentsBordered += 1;
            }
        }
        return continentsBordered;
    }

    public double percentageOfContinentOwned() { // checks the percentage of continent owned
        double percentageofcontinent = 0;
        double territoriesOwned = 0;
        for (Territory ter : territory.getBelongsTo().getTerritories()) {
            if (ter.getOwner() == territory.getOwner()) {
                territoriesOwned += 1;
            }
        }
        percentageofcontinent = territoriesOwned / territory.getBelongsTo().getTerritories().size();
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
        if (i == 0) {
            return p * w * d;
        }
        return (p * w * d) / i;
    }

    private Double getTerritoryValue(HashMap<CountryAgent, Double> agentValues) {
        return agentValues.get(this);
    }

    private double getDefendseUtility(Integer i) {
        double v = getValue();
        double d = getDefenseOdds(this.getTerritory().getNUnits() + i);
        if (i == 0) {
            return v * d;
        }
        return (v * d) / i;
    }

    public ArrayList<ReinforcementBid> getBids(Integer unitsLeft) {
        ArrayList<ReinforcementBid> result = new ArrayList<>();
        for (Goal goal : goalList) {
            result.addAll(getOffensiveBids(unitsLeft, goal));
        }

        DefensiveBid defBid = getDefensiveBid(null, unitsLeft);
        result.add(defBid);
        return result;
    }

    public ReinforcementBid getBestBid(Integer unitsLeft) {
        return getBids(unitsLeft).stream()
                .max((x, y) -> (x.getUtility() < y.getUtility() ? -1 : (x.getUtility() == y.getUtility() ? 0 : 1)))
                .get();

    }

    public DefensiveBid getDefensiveBid(CountryAgent fortifyingAgent, Integer unitsLeft) {
        DefensiveBid bestBid = null;
        for (int i = 0; i <= unitsLeft; i++) {
            double bidUtil = getDefendseUtility(i);
            if (bestBid == null || bidUtil > bestBid.getUtility()) {
                bestBid = new DefensiveBid(this, fortifyingAgent, i, bidUtil);
            }
        }
        return bestBid;
    }

    private ArrayList<OffensiveBid> getOffensiveBids(Integer unitsLeft, Goal goal) {
        ArrayList<OffensiveBid> result = new ArrayList<>();
        OffensiveBid bestBid = null;
        for (int i = 0; i <= unitsLeft; i++) {
            double bidUtil = getGoalUtility(goal, i);
            OffensiveBid bid = new OffensiveBid(this, goal, i, bidUtil);
            result.add(bid);
            if (bestBid == null || bidUtil > bestBid.getUtility()) {
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
        if (goal.size() >= Mars.goalLength)
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
