package infomgmag.mars;

public class PersonalityFactory {
    public static Personality agressivePersonality() {
        return new Personality(
                "Aggressive",
                3.5,
                170.0,
                70.0,
                1.2,
                -0.3,
                0.05,
                -0.03,
                0.5,
                20.0,
                4.0,
                5.0,
                80.0,
                0.25,
                4,
                0.0,
                10);
    }

    public static Personality normalPersonality() {
        return new Personality(
                "Normal",
                1.0,
                1.2,
                70.0,
                1.2,
                -0.3,
                0.05,
                -0.03,
                0.5,
                20.0,
                4.0,
                5.0,
                40.0,
                0.05,
                4,
                0.0,
                10);
    }

    public static Personality defensivePersonality() {
        return new Personality(
                "Defensive",
                1.4,
                1.0,
                70.0,
                1.2,
                -0.3,
                0.05,
                -0.03,
                0.5,
                20.0,
                4.0,
                40.0,
                20.0,
                0.05,
                4,
                0.0,
                100);
    }

    public static Personality continentPersonality() {
        return new Personality(
                "Continent",
                1.0,
                1.2,
                70.0,
                1.2,
                -0.3,
                0.05,
                -0.03,
                0.5,
                60.0,
                4.0,
                80.0,
                40.0,
                0.05,
                4,
                0.0,
                10);
    }
}
