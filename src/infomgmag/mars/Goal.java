package infomgmag.mars;

import java.util.ArrayList;

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
}
