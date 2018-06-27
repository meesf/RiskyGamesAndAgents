package infomgmag.mars;

public class PersonalityFactory {
    public static Personality agressivePersonality() {
        return new Personality(
                "Aggressive",
                3.5, // defensive multiplier
                100.0, // offensive multiplier
                10.0, // static bonus
                0.5, // friendly neighbor
                -0.3, // enemy neighbor
                0.05, // friendly army
                -0.03, // enemy army
                0.5, // continent border
                5.0, // goal captures whole continent
                2.0, // enemy owns whole continent
                2.0, // percentage of continent owned
                10.0, // goal kills 
                0.1, // clustering
                4, // goal length
                0.0, // hated
                1000); // first attack
    }

    public static Personality normalPersonality() {
        return new Personality(
                "Normal",
                1.0, // defensive multiplier
                1.5, // offensive multiplier
                10.0, // static bonus
                1.2, // friendly neighbor
                -0.3, // enemy neighbor
                0.05, // friendly army
                -0.03, // enemy army
                0.5, // continent border
                5.0, // goal captures whole continent
                3.0, // enemy owns whole continent
                5.0, // percentage of continent owned
                5.0, // goal kills enemy
                0.1, // clustering
                4, // goal length
                0.0, // hated
                1000); // first attack
    }

    public static Personality defensivePersonality() {
        return new Personality(
                "Defensive",
                2.0, // defensive multiplier
                1.3, // offensive multiplier
                10.0, // static bonus
                1.5, // friendly neighbor
                -0.3, // enemy neighbor
                0.1, // friendly army
                -0.03, // enemy army
                2.5, // continent border
                3.0, // goal captures whole continent
                0.5, // enemy owns whole continent
                5.0, // percentage of continent owned
                3.0, // goal kills an enemy
                0.05, // clustering
                4, // goal length
                0.0, // hated
                100); // first attack
    }

    public static Personality continentPersonality() {
        return new Personality(
                "Continent",
                1.0, // defensive multiplier
                1.5, // offensive multiplier
                10.0, // static bonus
                1.2, // friendly neighbor
                -0.3, // enemy neighbor
                0.05, // friendly army
                -0.03, // enemy army
                0.5, // continent border
                15.0, // goal captures whole continent
                4.0, // enemy owns whole continent
                10.0, // percentage of continent owned
                5.0, // goal kills an enemy
                0.05, // clustering
                4, // goal length
                0.0, // hated
                1000); // first attack
    }
}
