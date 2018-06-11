package infomgmag.mars;

public class Personality {
    private Double friendlyNeighbourWeight;
    private Double enemyNeighbourWeight;
    private Double friendlyArmyWeight;
    private Double enemyArmyWeight;
    private Double continentBorderWeight;
    private Double ownWholeContinentWeight;
    private Double enemyOwnsWholeContinentWeight;
    private Double percentageOfContinentWeight;
    private Integer goalLength;
    private Double offensiveMultiplier;
    private Double defensiveMultiplier;
    private Double killEnemyWeight;

    public Personality(Double defensiveMultiplier,
                       Double offensiveMultiplier,
                       Double friendlyNeighbourWeight,
                       Double enemyNeighbourWeight,
                       Double friendlyArmyWeight,
                       Double enemyArmyWeight,
                       Double continentBorderWeight,
                       Double ownWholeContinentWeight,
                       Double enemyOwnsWholeContinentWeight,
                       Double percentageOfContinentWeight,
                       Double killEnemyWeight,
                       Integer goalLength,
                       Double win_percentage){
        this.defensiveMultiplier = defensiveMultiplier;
        this.offensiveMultiplier = offensiveMultiplier;
        this.friendlyNeighbourWeight = friendlyNeighbourWeight;
        this.enemyNeighbourWeight = enemyNeighbourWeight;
        this.friendlyArmyWeight = friendlyArmyWeight;
        this.enemyArmyWeight = enemyArmyWeight;
        this.continentBorderWeight = continentBorderWeight;
        this.ownWholeContinentWeight = ownWholeContinentWeight;
        this.enemyOwnsWholeContinentWeight = enemyOwnsWholeContinentWeight;
        this.percentageOfContinentWeight = percentageOfContinentWeight;
        this.killEnemyWeight = killEnemyWeight;
        this.goalLength = goalLength;
    }

    public Double getFriendlyNeighbourWeight() {
        return friendlyNeighbourWeight;
    }

    public Double getEnemyNeighbourWeight() {
        return enemyNeighbourWeight;
    }

    public Double getFriendlyArmyWeight() {
        return friendlyArmyWeight;
    }

    public Double getEnemyArmyWeight() {
        return enemyArmyWeight;
    }

    public Double getContinentBorderWeight() {
        return continentBorderWeight;
    }

    public Double getOwnWholeContinentWeight() {
        return ownWholeContinentWeight;
    }

    public Double getEnemyOwnsWholeContinentWeight() {
        return enemyOwnsWholeContinentWeight;
    }

    public Double getPercentageOfContinentWeight() {
        return percentageOfContinentWeight;
    }

    public Integer getGoalLength() {
        return goalLength;
    }

    public Double getDefensiveBonus() {
        return defensiveMultiplier;
    }

    public Double getOffensiveBonus() {
        return offensiveMultiplier;
    }

    public Double getKillEnemyWeight() {
        return killEnemyWeight;
    }
}
