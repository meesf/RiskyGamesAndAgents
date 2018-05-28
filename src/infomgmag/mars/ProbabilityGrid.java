package infomgmag.mars;

import infomgmag.Risk;

import java.util.function.DoubleFunction;
import java.util.function.IntToDoubleFunction;
import java.util.stream.IntStream;

public class ProbabilityGrid {
    private double[][] grid;
    int attackingUnits, defendingUnits;
    public static final int ATTACKER_WINS = 0,
            DEFENDER_WINS = 1, ONE_EACH = 2;

    ProbabilityGrid(int attackingUnits, int defendingUnits){
        this.attackingUnits = attackingUnits;
        this.defendingUnits = defendingUnits;
        this.grid = new double[attackingUnits + 1][defendingUnits + 1];
        for(int i = 0; i < grid.length; i++){
            for(int j = 0; j < grid[0].length; j++){
                grid[i][j] = -1;
            }
        }
        this.setValue(attackingUnits, defendingUnits, 1.0);
    }

    @Override
    public String toString(){
        String output = "";
        for(int j = grid[0].length - 1; j >= 0; j--){
            for(int i = grid.length - 1; i >= 0 ; i--){
                if(isSet(i, j)){
                    output += grid[i][j] + " ";
                }
                else{
                    output += "null ";
                }
            }
            output += "\n";
        }
        return output;
    }

    public void setValue(int i, int j, double val){
        this.grid[i][j] = val;
    }

    public double getValue(int a, int d){
        if(a < 0 || d < 0 || a > attackingUnits || d > defendingUnits){
            return 0;
        }
        if(isSet(a,d)){
            return grid[a][d];
        } else {
            double val = 0.0;
            if(a == 0 && d == 0){
                throw new RuntimeException("KYS");
            }
            else if(a == 1 && d == 1){
                val = getValue(a + 1, d + 1) * diceP(a + 1, d + 1, ONE_EACH)
                        + getValue(a + 1, d) * diceP(a + 1, d, DEFENDER_WINS)
                        + getValue(a, d + 1) * diceP(a, d + 1, ATTACKER_WINS);

            }
            else if(a == 1 && d == 0){
                val = getValue(a, d + 1) * diceP(a, d + 1, ATTACKER_WINS);
            }
            else if(a == 0 && d == 1){
                val = getValue(a + 1, d) * diceP(a + 1, d, DEFENDER_WINS);
            }
            else if(d == 1){
                val = getValue(a + 1, d + 1) * diceP(a + 1, d + 1, ONE_EACH)
                        + getValue(a, d + 2) * diceP(a, d + 2, ATTACKER_WINS)
                        + getValue(a + 1, d) * diceP(a + 1, d, DEFENDER_WINS);
            }
            else if (a == 1){
                val = getValue(a+2, d) * diceP(a + 2, d, DEFENDER_WINS)
                        + getValue(a + 1, d + 1) * diceP(a + 1, d + 1, ONE_EACH)
                        + getValue(a, d + 1) * diceP(a, d + 1, ATTACKER_WINS);
            }
            else if (a == 0) {
                val = getValue(a + 2, d) * diceP(a + 2, d, DEFENDER_WINS)
                        + getValue(a + 1, d) * diceP(a + 1, d, DEFENDER_WINS);

            } else if(d == 0){
                val = getValue(a, d + 2) * diceP(a, d + 2, ATTACKER_WINS)
                        + getValue(a, d + 1) * diceP(a, d + 1, ATTACKER_WINS);
            }
            else {
                val = getValue(a + 2, d) * diceP(a + 2, d, DEFENDER_WINS)
                        + getValue(a + 1, d + 1) * diceP(a + 1, d + 1, ONE_EACH)
                        + getValue(a, d + 2) * diceP(a, d + 2, ATTACKER_WINS);

            }
            grid[a][d] = val;
            return val;
        }
    }

    public void calculateGrid(){
        for(int i = 1; i <= attackingUnits; i++) {
            getValue(i,0);
        }
        getValue(0,1);
    }

    private double diceP(int a, int d, int outcome){
        if(a > 3)
            a = 3;
        if(d > 2)
            d = 2;
        if(a == 1 && d == 1){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_ONE.get(0).get(0);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_ONE.get(1).get(0);
            }
            if(outcome == ONE_EACH){
                throw new RuntimeException("Can't lose one each in this situation");
            }
        }
        if(a == 2 && d == 1){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_ONE.get(0).get(1);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_ONE.get(1).get(1);
            }
            if(outcome == ONE_EACH){
                throw new RuntimeException("Can't lose one each in this situation");
            }
        }
        if(a == 3 && d == 1){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_ONE.get(0).get(2);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_ONE.get(1).get(2);
            }
            if(outcome == ONE_EACH){
                throw new RuntimeException("Can't lose one each in this situation");
            }
        }
        if(a == 1 && d == 2){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_TWO.get(0).get(0);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_TWO.get(1).get(0);
            }
            if(outcome == ONE_EACH){
                throw new RuntimeException("Can't lose one each in this situation");
            }
        }
        if(a == 2 && d == 2){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_TWO.get(0).get(1);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_TWO.get(1).get(1);
            }
            if(outcome == ONE_EACH){
                return Risk.DICE_ODDS_TWO.get(2).get(1);
            }
        }
        if(a == 3 && d == 2){
            if(outcome == ATTACKER_WINS){
                return Risk.DICE_ODDS_TWO.get(0).get(2);
            }
            if(outcome == DEFENDER_WINS){
                return Risk.DICE_ODDS_TWO.get(1).get(2);
            }
            if(outcome == ONE_EACH){
                return Risk.DICE_ODDS_TWO.get(2).get(2);
            }
        }
        throw new RuntimeException("Cannot return a dice probability for attacker throwing with: + " + a + " and defender with: " + d);
    }

    public boolean isSet(int i, int j){
        return grid[i][j] != -1;
    }

    public double chanceOfWin() {
        return IntStream.rangeClosed(1, attackingUnits)
                .mapToDouble(a -> getValue(a, 0))
                .sum();
    }
}
