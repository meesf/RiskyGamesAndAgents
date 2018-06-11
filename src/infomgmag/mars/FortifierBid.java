package infomgmag.mars;

public class FortifierBid {

    private CountryAgent fortifier;
    private int units;
    private double utility;
    
    public FortifierBid(CountryAgent fortifier, int units, double utility) {
        this.fortifier = fortifier;
        this.units = units;
        this.utility = utility;
    }

    public int getUnits() {
        return units;
    }
    
    public CountryAgent getFortifier() {
        return fortifier;
    }
    
    public double getUtility() {
        return utility;
    }
}
