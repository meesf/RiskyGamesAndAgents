package infomgmag.mars;

import infomgmag.Territory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Goal extends ArrayList<CountryAgent> {
    private static final long serialVersionUID = -4131586938852901842L;

    Goal() {
        super();
    }

    public CountryAgent getFirstGoal() {
        if (this.isEmpty()) {
            System.err.println("Returning `null` as goal");
            return null;
        }
        return this.get(this.size() - 1);
    }

    public void addEarlierGoal(CountryAgent ca) {
        this.add(ca);
    }

    public Goal getTail() {
        Goal tail = (Goal) this.clone();
        tail.remove(0);
        return tail;
    }

    public CountryAgent getFinalGoal() {
        if (this.isEmpty()) {
            System.err.println("Returning `null` as finalGoal");
            return null;
        }
        return get(size() - 1);
    }

    public boolean completesContinentFor(Mars mars){
        List<Territory> territories = this.stream().map(ca -> ca.getTerritory()).collect(Collectors.toList());
        for(Territory ter : territories){
            boolean completesContinent = true;
            for(Territory t : ter.getContinent().getTerritories()){
                if(t.getOwner() != mars && !territories.contains(t))
                    completesContinent = false;
            }
            if(completesContinent) return true;
        }
        return false;
    }

    // The mars argument is the player you kill AS, not the player you kill
    public long killsPlayers(Mars mars) {
        return this.stream()
        .map(x -> x.getTerritory().getOwner())
        .distinct()
        .filter(x ->
            x.getTerritories()
            .stream()
            .map(y -> mars.countryAgentsByTerritory.get(y))
            .allMatch(y -> this.contains(y)))
        .count();
    }
}
