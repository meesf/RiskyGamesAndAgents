package infomgmag;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This is an abstract class for a player of the game.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public abstract class Player {
    protected String name;
    protected Color color;
    protected Objective objective;
    protected ArrayList<Territory> territories;
    protected Hand hand;
    protected int reinforcements;
    protected boolean hasConqueredTerritoryInTurn;


    public Player(Objective objective, Integer reinforcements, String name, Color color) {
        this.objective = objective;
        this.reinforcements = reinforcements;
        this.name = name;
        this.hand = new Hand();
        this.territories = new ArrayList<>();
        this.color = color;
    }

    public abstract void turnInCards(Board board);

    public abstract void fortifyTerritory(Board board);

    public abstract void movingInAfterInvasion(Board board, CombatMove combatMove);

    public abstract int getDefensiveDice(CombatMove combatMove);

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public Objective getObjective() {
        return objective;
    }

    public int getReinforcements() {
        return reinforcements;
    }

    public void setReinforcements(Integer nr) {
        this.reinforcements = nr;
    }

    public ArrayList<Territory> getTerritories() {
        return this.territories;
    }

    public void removeTerritory(Territory t) {
        territories.remove(t);
    }

    public void addTerritory(Territory t) {
        territories.add(t);
    }

    public abstract void placeReinforcements(Board board);

    @Override
    public String toString() {
        return name;
    }
    
    protected boolean reachedObjective(CombatInterface ci) {
        if (objective.getType() == Objective.type.TOTAL_DOMINATION)
            return ci.getActivePlayerAmount() == 1;
        return false;
    }

    abstract public void attackPhase(CombatInterface combatInterface);

    public boolean hasConqueredTerritoryInTurn(){
        return this.hasConqueredTerritoryInTurn;
    }

    public void setHasConqueredTerritoryInTurn(boolean hasConquered){
        this.hasConqueredTerritoryInTurn = hasConquered;
    }

}
