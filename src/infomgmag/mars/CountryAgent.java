package infomgmag.mars;

import infomgmag.Territory;

public class CountryAgent {
    private Territory territory;

    CountryAgent(Territory territory) {
        this.territory = territory;
    }

    public Territory getTerritory() {
        return territory;
    }
}
