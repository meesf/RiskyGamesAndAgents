package infomgmag;

import java.util.ArrayList;

/**
 * This class represents a territory in the game.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Territory {
    private Player owner;
    private Integer nUnits;
    private ArrayList<Territory> adjacentTerritories;
    private String name;

    public double x, y;

    public Territory(String name, double x, double y) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.nUnits = 0;
        this.adjacentTerritories = new ArrayList<>();
    }

    public void setOwner(Player newOwner) {

        if (this.owner != null)
            this.owner.removeTerritory(this);
        newOwner.addTerritory(this);
        this.owner = newOwner;
    }

    public void setUnits(int units) {
        this.nUnits = units;
    }

    public String getName() {
        return this.name;
    }

    public Player getOwner() {
        return owner;
    }

    public int getNUnits() {
        return nUnits;
    }

    public void addAdjacentTerritory(Territory t) {
        this.adjacentTerritories.add(t);
    }

    public ArrayList<Territory> getAdjacentTerritories() {
        return adjacentTerritories;
    }

    @Override
    public String toString() {
        return "name: " + name + " owner: " + owner.toString() + " nrOfUnits: " + nUnits;
    }

}