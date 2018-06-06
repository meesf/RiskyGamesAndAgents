package infomgmag;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class represents a continent in the game.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Continent {
    private String name;
    private ArrayList<Territory> territories;
    private Color color;

    private int nReinforcements; // Number of reinforcements received when a player owns the continent.

    public Continent(Color color, String name, int bonusArmies) {
        this.color = color;
        this.name = name;
        this.nReinforcements = bonusArmies;
        territories = new ArrayList<>();
    }

    public String GetName() {
        return name;
    }

    public Color getColor() {
        return this.color;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public String getName(){
        return this.name;
    }

    public int getNReinforcements() {
        return nReinforcements;
    }

    public void addTerritory(Territory ter) {
        territories.add(ter);
        ter.setContinent(this);
    }
}
