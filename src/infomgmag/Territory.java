package infomgmag;

import infomgmag.mars.CountryAgent;

import java.util.ArrayList;

public class Territory {
    private Player owner;
    private int units;
    private ArrayList<Territory> adjacentTerritories;
    private String name;
    private Continent continent;
    private CountryAgent territoryCountryAgent;
    private int continentsBorderedAmount;

    public double x, y;

    public Territory(String name, double x, double y) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.units = 0;
        this.adjacentTerritories = new ArrayList<>();
    }

    public void setOwner(Player newOwner) {

        if (this.owner != null)
            this.owner.removeTerritory(this);
        newOwner.addTerritory(this);
        this.owner = newOwner;
    }

    public void setContinentsBorderedAmount(long nr){
        this.continentsBorderedAmount = (int)nr;
    }

    public void setUnits(int units) {
        this.units = units;
    }

    public String getName() {
        return this.name;
    }

    public Player getOwner() {
        return owner;
    }

    public int getUnits() {
        return units;
    }

    public void addAdjacentTerritory(Territory t) {
        this.adjacentTerritories.add(t);
    }

    public ArrayList<Territory> getAdjacentTerritories() {
        return adjacentTerritories;
    }

    public void setContinent(Continent co) {
        this.continent = co;
    }

    public Continent getContinent() {
        return continent;
    }

    public void setTerritoryCountryAgent(CountryAgent countryAgent){
        this.territoryCountryAgent = countryAgent;
    }

    public CountryAgent getCountryAgent(){
        return territoryCountryAgent;
    }

    @Override
    public String toString() {
        return "name: " + name + " nrOfUnits: " + units;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Territory)) return false;
        Territory o = (Territory) obj;
        return o.getName() == this.name;
    }


    public double getContinentsBorderedAmount() {
        return (double) continentsBorderedAmount;
    }
}