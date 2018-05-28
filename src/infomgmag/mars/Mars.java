package infomgmag.mars;

import java.awt.Color;
import java.lang.reflect.Array;
import java.util.*;

import infomgmag.Board;
import infomgmag.CombatMove;
import infomgmag.Objective;
import infomgmag.Player;
import infomgmag.Risk;
import infomgmag.Territory;

import javafx.util.Pair;

import javax.print.attribute.IntegerSyntax;

public class Mars extends Player {

    /*
     * This agent can be regarded as the mediator agent from the paper.
     */

    private CardAgent cardAgent;
    private List<CountryAgent> countryAgents;
    private HashMap<Territory,CountryAgent> countryAgentsByTerritory;
    private HashMap<CountryAgent, Double> agentValues;

    private Double friendliesweight = 1.2;      //parameters used in calculation of territory value
    private Double enemiesweight = -0.3;
    private Double farmiesweight = 0.05;
    private Double earmiesweight = -0.03;
    private Integer goalLength = 1;

    public Mars(Risk risk, Objective objective, Integer reinforcements, String name, Color color) {
        super(objective, reinforcements, name, color);

        agentValues = new HashMap<CountryAgent, Double>();
        cardAgent = new CardAgent();
        countryAgents = new ArrayList<>();
        countryAgentsByTerritory = new HashMap<Territory, CountryAgent>();


        for (Territory t : risk.getBoard().getTerritories()) {
            CountryAgent ca = new CountryAgent(t);
            countryAgents.add(ca);
            countryAgentsByTerritory.put(t, ca);
            t.setTerritoryCountryAgent(ca);
        }

        for (CountryAgent ca : countryAgents){
            for (Territory t : risk.getBoard().getTerritories()){
                if (ca.getTerritory().getAdjacentTerritories().contains(t) && ca.adjacentAgents.contains(t) != true){
                    ca.adjacentAgents.add(countryAgentsByTerritory.get(t));
                }
            }
        }
    }

    @Override
    public void turnInCards(Board board) {
        int useInfantry = 0, useCavalry = 0, useArtillery = 0, useWildcards = 0;
        if(hand.getNumberOfCards() > 4){
            if(hand.getInfantry() > 0 && hand.getArtillery() > 0 && hand.getCavalry() > 0){
                useInfantry = 1; useArtillery = 1; useCavalry = 1;
            } else if(hand.getInfantry() > 2){
                useInfantry = 3;
            } else if(hand.getCavalry() > 2){
                useCavalry = 3;
            } else if(hand.getArtillery() > 2){
                useArtillery = 3;
            } else if(hand.getWildcards() == 1){
                if(hand.getInfantry() > 1){
                    useInfantry = 2; useWildcards = 1;
                } else if(hand.getCavalry() > 1){
                    useCavalry = 2; useWildcards = 1;
                } else if(hand.getArtillery() > 1){
                    useArtillery = 2; useWildcards = 1;
                } else if(hand.getArtillery() > 0 && hand.getCavalry() > 0){
                    useArtillery = 1; useCavalry = 1; useWildcards = 1;
                } else if(hand.getArtillery() > 0 && hand.getInfantry() > 0){
                    useArtillery = 1; useInfantry = 1; useWildcards = 1;
                } else if(hand.getCavalry() > 0 && hand.getInfantry() > 0){
                    useCavalry = 1; useInfantry = 1; useWildcards = 1;
                }
            } else if(hand.getWildcards() == 2){
                if(hand.getInfantry() > 0){
                    useInfantry = 1; useWildcards = 2;
                } else if(hand.getCavalry() > 0){
                    useCavalry = 1; useWildcards = 2;
                } else if(hand.getArtillery() > 0){
                    useArtillery = 1; useWildcards = 2;
                }
            }
        }

        if(useInfantry > 0 || useArtillery > 0 || useCavalry > 0){
            int reinforcementsByCards = board.getAndMoveGoldenCavalry();
            hand.setInfantry(hand.getInfantry() - useInfantry);
            hand.setArtillery(hand.getArtillery() - useArtillery);
            hand.setCavalry(hand.getCavalry() - useCavalry);
            hand.setWildCards(hand.getWildcards() - useWildcards);
            board.setArtillery(board.getArtillery() + useArtillery);
            board.setCavalry(board.getCavalry() + useCavalry);
            board.setInfantry(board.getInfantry() + useInfantry);
            board.setWildcards(board.getWildcards() + useWildcards);
            this.reinforcements += reinforcementsByCards;
        }
    }

    @Override
    public void fortifyTerritory(Board board) { //only uses the 'best' country right now
        //todo: everything
    }

    @Override
    public CombatMove getCombatMove() {
        CombatMove combatMove = new CombatMove();
        for (CountryAgent ca : countryAgents) {
            System.out.println(ca.getFinalGoal() + " is the final goal");
            if (ca.getTerritory().getOwner() == this && (ca.getFinalGoal().size() >= 1) && ca.getTerritory().getNUnits() > 1){
                for (CountryAgent target : ca.getFinalGoal()) {           //right now it assumes the first goal is the best goal
                    combatMove.setAttackingTerritory(ca.getTerritory());
                    combatMove.setDefendingTerritory(ca.getFinalGoal().get(ca.getFinalGoal().size() - 1).getTerritory());
                    combatMove.setAttackingUnits(Integer.min(3, ca.getTerritory().getNUnits() - 1));
                    combatMove.setDefendingUnits(Integer.min(2, ca.getFinalGoal().get(ca.getFinalGoal().size()-1).getTerritory().getNUnits()));
                    return combatMove;
                    }
                }
            }
        return null;
    }

    @Override
    public void movingInAfterInvasion(CombatMove combatMove) {
        int transferredunits = combatMove.getAttackingTerritory().getNUnits() - 1;
        combatMove.getDefendingTerritory().setUnits(transferredunits);
        combatMove.getAttackingTerritory().setUnits(combatMove.getAttackingTerritory().getNUnits() - transferredunits);

        combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().remove(combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().size() - 1);
        combatMove.getDefendingTerritory().getCountryAgent().setFinalGoal(combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal());
        //System.out.println(combatMove.getDefendingTerritory().getCountryAgent().getFinalGoal() + " final goal of this agent");
        combatMove.getAttackingTerritory().getCountryAgent().getFinalGoal().clear();
    }

    @Override
    public void placeReinforcements(Board board) {
        for (CountryAgent ca: countryAgents){
            ca.clearlists();
        }
        
        for (CountryAgent ca: countryAgents) {
            if (ca.getTerritory().getOwner() != this) {
                agentValues.put(ca, ca.calculateOwnershipValue(friendliesweight, enemiesweight, farmiesweight, earmiesweight));
            }
        }
        
        for (CountryAgent sender: countryAgents) {
        	if(sender.getTerritory().getOwner() != this) {
	            ArrayList<CountryAgent> initialList = new ArrayList<CountryAgent>();
	            createGoal(sender, initialList);
        	}
        }

        while(reinforcements > 0){
            Bid bid = getBestBid(getReinforcements());
            board.addUnits(this, bid.getOrigin().getTerritory(), bid.getUnits());
            reinforcements -= bid.getUnits();
            if(bid.getUnits() == 0)
                break;
        }
    }
    
    private void createGoal(CountryAgent receiver, ArrayList<CountryAgent> countries){
    	if(receiver.getTerritory().getOwner() == this) {
            receiver.receivemessagefriendly(countries);
        } else if(goalLength > countries.size()) {
            ArrayList<CountryAgent> copiedCountries = new ArrayList<CountryAgent>();
            for(CountryAgent ca : countries){
                copiedCountries.add(ca);
            }
            
            copiedCountries.add(receiver);
            for(CountryAgent neighbour : receiver.getAdjacentAgents()) {
            	if(!countries.contains(neighbour)) {
            		createGoal(neighbour, copiedCountries);
            	}
            }
        }
    }

    private Bid getBestBid(int units){
        Bid bestBid = null;
        for(CountryAgent ca : countryAgents){
            if(ca.getTerritory().getOwner() == this && ca.getGoalList().size() > 0){
        		Bid bid = ca.getBid(units, agentValues);
        		if(bestBid == null || bid.getUtility() > bestBid.getUtility()){
                    bestBid = bid;
                }
            }
        }
        bestBid.getOrigin().setFinalGoal(bestBid.getGoal());
        return bestBid;
    }
}
