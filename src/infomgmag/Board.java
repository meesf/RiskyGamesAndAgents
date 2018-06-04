package infomgmag;

import java.awt.Color;
import java.util.ArrayList;

/**
 * This class contains all the objects on the board and methods that interact
 * with these objects.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class Board {
    private ArrayList<Continent> continents;
    private ArrayList<Territory> territories;

    private Integer artillery, cavalry, infantry, wildcard;

    private int goldenCavalry;

    public Board() {
        continents = new ArrayList<>();

        this.goldenCavalry = 4;
        this.artillery = 14;
        this.cavalry = 14;
        this.infantry = 14;
        this.wildcard = 2;

        Continent northAmerica = new Continent(new Color(1.0f, 1.0f, 0.0f), "northAmerica", 5);
        continents.add(northAmerica);
        Continent southAmerica = new Continent(new Color(0.6f, 0f, 0f), "southAmerica", 2);
        continents.add(southAmerica);
        Continent europe = new Continent(new Color(0.33f, 0f, 1f),"europe", 5);
        continents.add(europe);

        Continent asia = new Continent(new Color(0.1f, 0.5f, 0.1f), "asia", 7);
        continents.add(asia);
        Continent africa = new Continent(new Color(1f, 0.5f, 0f),"africa", 3);
        continents.add(africa);
        Continent australia = new Continent(new Color(1f, 0.4f, 0.55f),"australia", 2);
        continents.add(australia);


        Territory yakutsk = new Territory("Yakutsk", 0.84, 0.79);
        asia.addTerritory(yakutsk);
        Territory kamchatka = new Territory("Kamchatka", 0.92, 0.78);
        asia.addTerritory(kamchatka);
        Territory siberia = new Territory("Siberia", 0.72, 0.8);
        asia.addTerritory(siberia);
        Territory japan = new Territory("Japan", 0.9, 0.6);
        asia.addTerritory(japan);
        Territory irkutsk = new Territory("Irkutsk", 0.79, 0.70);
        asia.addTerritory(irkutsk);
        Territory mongolia = new Territory("Mongolia", 0.80, 0.62);
        asia.addTerritory(mongolia);
        Territory china = new Territory("China", 0.75, 0.55);
        asia.addTerritory(china);
        Territory siam = new Territory("Siam", 0.80, 0.42);
        asia.addTerritory(siam);
        Territory indonesia = new Territory("Indonesia", 0.82, 0.3);
        australia.addTerritory(indonesia);
        Territory westernAustralia = new Territory("Western Australia", 0.85, 0.15);
        australia.addTerritory(westernAustralia);
        Territory easternAustralia = new Territory("Eastern Australia", 0.95, 0.15);
        australia.addTerritory(easternAustralia);
        Territory newGuinea = new Territory("New Guinea", 0.93, 0.28);
        australia.addTerritory(newGuinea);
        Territory alaska = new Territory("Alaska", 0.05, 0.8);
        northAmerica.addTerritory(alaska);
        Territory northWestTerritory = new Territory("North-West Territory", 0.15, 0.78);
        northAmerica.addTerritory(northWestTerritory);
        Territory alberta = new Territory("Alberta", 0.15, 0.7);
        northAmerica.addTerritory(alberta);
        Territory westernUS = new Territory("Western United States", 0.16, 0.6);
        northAmerica.addTerritory(westernUS);
        Territory greenland = new Territory("Greenland", 0.36, 0.8);
        northAmerica.addTerritory(greenland);
        Territory ontario = new Territory("Ontario", 0.23, 0.7);
        northAmerica.addTerritory(ontario);
        Territory quebec = new Territory("Quebec", 0.3, 0.7);
        northAmerica.addTerritory(quebec);
        Territory easternUS = new Territory("Eastern United States", 0.24, 0.6);
        northAmerica.addTerritory(easternUS);
        Territory centralUS = new Territory("Central United States", 0.17, 0.5);
        northAmerica.addTerritory(centralUS);
        Territory venezuela = new Territory("Venezuela", 0.245, 0.43);
        southAmerica.addTerritory(venezuela);
        Territory peru = new Territory("Peru", 0.25, 0.31);
        southAmerica.addTerritory(peru);
        Territory brazil = new Territory("Brazil", 0.31, 0.33);
        southAmerica.addTerritory(brazil);
        Territory argentina = new Territory("Argentina", 0.25, 0.23);
        southAmerica.addTerritory(argentina);
        Territory iceland = new Territory("Iceland", 0.40, 0.73);
        europe.addTerritory(iceland);
        Territory scandinavia = new Territory("Scandinavia", 0.51, 0.74);
        europe.addTerritory(scandinavia);
        Territory unitedKingdom = new Territory("United Kingdom", 0.43, 0.66);
        europe.addTerritory(unitedKingdom);
        Territory northernEurope = new Territory("Northern Europe", 0.47, 0.64);
        europe.addTerritory(northernEurope);
        Territory southernEurope = new Territory("Southern Europe", 0.50, 0.6);
        europe.addTerritory(southernEurope);
        Territory northAfrica = new Territory("North Africa", 0.45, 0.40);
        africa.addTerritory(northAfrica);
        Territory egypt = new Territory("Egypt", 0.52, 0.49);
        africa.addTerritory(egypt);
        Territory congo = new Territory("Congo", 0.51, 0.3);
        africa.addTerritory(congo);
        Territory southAfrica = new Territory("South Africa", 0.52, 0.19);
        africa.addTerritory(southAfrica);
        Territory eastAfrica = new Territory("East Africa", 0.55, 0.39);
        africa.addTerritory(eastAfrica);
        Territory madagascar = new Territory("Madagascar", 0.60, 0.21);
        africa.addTerritory(madagascar);
        Territory ukraine = new Territory("Ukraine", 0.57, 0.68);
        europe.addTerritory(ukraine);
        Territory middleEast = new Territory("Middle East", 0.60, 0.5);
        asia.addTerritory(middleEast);
        Territory afganistan = new Territory("Afganistan", 0.65, 0.59);
        asia.addTerritory(afganistan);
        Territory ural = new Territory("Ural", 0.67, 0.74);
        asia.addTerritory(ural);
        Territory india = new Territory("India", 0.7, 0.48);
        asia.addTerritory(india);
        Territory westernEurope = new Territory("Western Europe", 0.44, 0.57);
        europe.addTerritory(westernEurope);
        makeEdge(siberia, china);
        makeEdge(southernEurope, northAfrica);
        makeEdge(westernEurope, northernEurope);
        makeEdge(scandinavia, unitedKingdom);
        makeEdge(centralUS, easternUS);
        makeEdge(india, siam);
        makeEdge(india, middleEast);
        makeEdge(india, afganistan);
        makeEdge(india, china);
        makeEdge(ural, siberia);
        makeEdge(ural, china);
        makeEdge(ural, afganistan);
        makeEdge(ural, ukraine);
        makeEdge(afganistan, china);
        makeEdge(afganistan, ukraine);
        makeEdge(afganistan, middleEast);
        makeEdge(ukraine, scandinavia);
        makeEdge(ukraine, northernEurope);
        makeEdge(ukraine, southernEurope);
        makeEdge(madagascar, eastAfrica);
        makeEdge(madagascar, southAfrica);
        makeEdge(eastAfrica, northAfrica);
        makeEdge(eastAfrica, egypt);
        makeEdge(eastAfrica, congo);
        makeEdge(eastAfrica, southAfrica);
        makeEdge(egypt, northAfrica);
        makeEdge(egypt, middleEast);
        makeEdge(egypt, southernEurope);
        makeEdge(northAfrica, brazil);
        makeEdge(congo, northAfrica);
        makeEdge(congo, southAfrica);
        makeEdge(brazil, venezuela);
        makeEdge(brazil, peru);
        makeEdge(brazil, argentina);
        makeEdge(venezuela, peru);
        makeEdge(peru, argentina);
        makeEdge(venezuela, centralUS);
        makeEdge(centralUS, westernUS);
        makeEdge(westernUS, alberta);
        makeEdge(westernUS, ontario);
        makeEdge(westernUS, easternUS);
        makeEdge(easternUS, ontario);
        makeEdge(easternUS, quebec);
        makeEdge(alberta, alaska);
        makeEdge(alberta, northWestTerritory);
        makeEdge(alberta, ontario);
        makeEdge(ontario, greenland);
        makeEdge(ontario, quebec);
        makeEdge(quebec, greenland);
        makeEdge(alaska, northWestTerritory);
        makeEdge(greenland, northWestTerritory);
        makeEdge(greenland, iceland);
        makeEdge(iceland, unitedKingdom);
        makeEdge(iceland, scandinavia);
        makeEdge(westernEurope, northAfrica);
        makeEdge(unitedKingdom, westernEurope);
        makeEdge(westernEurope, southernEurope);
        makeEdge(unitedKingdom, northernEurope);
        makeEdge(northernEurope, southernEurope);
        makeEdge(southernEurope, middleEast);
        makeEdge(siberia, irkutsk);
        makeEdge(yakutsk, irkutsk);
        makeEdge(yakutsk, kamchatka);
        makeEdge(irkutsk, kamchatka);
        makeEdge(mongolia, kamchatka);
        makeEdge(siberia, yakutsk);
        makeEdge(irkutsk, mongolia);
        makeEdge(scandinavia, northernEurope);
        makeEdge(siberia, mongolia);
        makeEdge(japan, kamchatka);
        makeEdge(japan, mongolia);
        makeEdge(siam, china);
        makeEdge(china, mongolia);
        makeEdge(siam, indonesia);
        makeEdge(indonesia, newGuinea);
        makeEdge(indonesia, westernAustralia);
        makeEdge(newGuinea, westernAustralia);
        makeEdge(easternAustralia, newGuinea);
        makeEdge(westernAustralia, easternAustralia);
        makeEdge(kamchatka, alaska);

        territories = new ArrayList<>();
        for (Continent continent : continents)
            for (Territory territory : continent.getTerritories())
                territories.add(territory);

        setNrOfContinentsBorderedToTerritory();


    }

    private void setNrOfContinentsBorderedToTerritory(){
        for (Continent continent : continents)
            for (Territory territory : continent.getTerritories()){
                int nr = territory.getAdjacentTerritories().stream().map(x -> x.getBelongsTo()).distinct().toArray().length;
                System.out.println(territory.getName() + " has " + nr + " bordered continents");
                territory.setNrOfContinentsBordered(nr);
            }
    }

    private void makeEdge(Territory t, Territory s) {
        t.addAdjacentTerritory(s);
        s.addAdjacentTerritory(t);
    }

    /**
     * Add the given amount of units to the given territory.
     *
     * @return boolean if the action succeeded, in other words if the action was
     *         possible and has been executed.
     */
    public void addUnits(Player player, Territory territory, int number) {
    	if(player!=territory.getOwner()) {
    		Risk.printError(player.getName()+" tried reinforcing "+territory.getName()+", but does not own this territory");
    	} else if(player.getReinforcements() < number) {
    		Risk.printError(player.getName()+" does not have "+number+" reinforcements");
    	} else {
    		territory.setUnits(territory.getNUnits() + number);
    	}
    }

    /**
     * Attack the defending territory from the attacking territory.
     * 
     * @return boolean if the action succeeded, in other words if the action was
     *         possible and has been executed.
     */
    public boolean Attack(Territory attacker, Territory defender) {
        return false;
    }

    /**
     * Return the number of reinforcements the given player receives.
     */
    public int GetReinforcements(Player player) {
        return 0;
    }

    /**
     * Move the golden cavalry one step forward.
     */
    public void moveGoldenCavalry() {
        if (goldenCavalry < 10)
            goldenCavalry += 2;
        else if (goldenCavalry < 20)
            goldenCavalry += 5;
    }

    /**
     * If there is a winner in the current state, return the winner.
     */

    public ArrayList<Continent> getContinents() {
        return continents;
    }

    public int getGoldenCavalry() {
        return goldenCavalry;
    }

    public int getAndMoveGoldenCavalry() {
        int result = goldenCavalry;
        moveGoldenCavalry();
        return result;
    }

    public ArrayList<Territory> getTerritories() {
        return territories;
    }

    public int getInfantry() {
        return infantry;
    }

    public int getArtillery() {
        return artillery;
    }

    public int getCavalry() {
        return cavalry;
    }

    public int getWildcards(){
        return wildcard;
    }

    public void setInfantry(Integer infantry) {
        this.infantry = infantry;
    }

    public void setArtillery(int artillery) {
        this.artillery = artillery;
    }

    public void setCavalry(int cavalry) {
        this.cavalry = cavalry;
    }

    public void setWildcards(int wildcards){
        this.wildcard = wildcards;
    }

    public void drawCard(Player player) {
        int val = Risk.random.nextInt(this.artillery + this.cavalry + this.infantry + this.wildcard);
        if (val < this.artillery) {
            this.artillery--;
            player.hand.setArtillery(player.hand.getArtillery() + 1);
        } else if (val < this.artillery + this.cavalry) {
            this.cavalry--;
            player.hand.setCavalry(player.hand.getCavalry() + 1);
        } else if (val < this.artillery + this.cavalry + this.infantry) {
            this.infantry--;
            player.hand.setInfantry(player.hand.getInfantry() + 1);
        } else {
            this.wildcard--;
            player.hand.setWildCards(player.hand.getWildcards() + 1);
        }
    }
    
    public void moveUnits(Territory a, Territory b, int units) {
    	if(a.getNUnits() < 2) {
    		Risk.printError("The fortifying territory, "+a.getName()+", doesn't have enough units to fortify "+b.getName());
    	} else if(!Risk.getConnectedTerritories(a).contains(b)) {
    		Risk.printError("The territories "+a.getName()+" and "+b.getName()+" cannot fortify each other, bacause they are not connected.");
    	} else {
    		a.setUnits(a.getNUnits() - units);
            b.setUnits(b.getNUnits() + units);
    	}
    	
    }

    @Override
    public String toString() {
        String result = "";
        for (Territory territory : territories)
            result += territory.toString() + '\n';

        return result;
    }

}
