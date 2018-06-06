package infomgmag.mars;

public class Personality {
    private Double friendliesweight;
    private Double enemiesweight;
    private Double farmiesweight;
    private Double earmiesweight;
    private Double continentBorderWeight;
    private Double ownWholeContinentWeight;
    private Double enemyOwnsWholeContinentWeight;
    private Double percentageOfContinentWeight;
    private Integer goalLength;
    private Double WIN_PERCENTAGE;
    private Double offensiveBonus;
    private Double defensiveBonus;

    public Personality(Double defensiveBonus,
                       Double offensiveBonus,
                       Double friendliesweight,
                       Double enemiesweight,
                       Double farmiesweight,
                       Double earmiesweight,
                       Double continentBorderWeight,
                       Double ownWholeContinentWeight,
                       Double enemyOwnsWholeContinentWeight,
                       Double percentageOfContinentWeight,
                       Integer goalLength,
                       Double win_percentage){
        this.defensiveBonus = defensiveBonus;
        this.offensiveBonus = offensiveBonus;
        this.friendliesweight = friendliesweight;
        this.enemiesweight = enemiesweight;
        this.farmiesweight = farmiesweight;
        this.earmiesweight = earmiesweight;
        this.continentBorderWeight = continentBorderWeight;
        this.ownWholeContinentWeight = ownWholeContinentWeight;
        this.enemyOwnsWholeContinentWeight = enemyOwnsWholeContinentWeight;
        this.percentageOfContinentWeight = percentageOfContinentWeight;
        this.goalLength = goalLength;
        this.WIN_PERCENTAGE = win_percentage;

    }

    public Double getFriendliesweight() {
        return friendliesweight;
    }

    public Double getEnemiesweight() {
        return enemiesweight;
    }

    public Double getFarmiesweight() {
        return farmiesweight;
    }

    public Double getEarmiesweight() {
        return earmiesweight;
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

    public Double getWIN_PERCENTAGE() {
        return WIN_PERCENTAGE;
    }

    public Double getDefensiveBonus() {
        return defensiveBonus;
    }

    public Double getOffensiveBonus() {
        return offensiveBonus;
    }



}
