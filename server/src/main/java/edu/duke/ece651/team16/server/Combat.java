package edu.duke.ece651.team16.server;

import java.util.*;

public class Combat {
    private int seed;
    private int diceNum;

    /**
     * 
     * Constructs a new Combat instance between two units, and sets the winner
     * 
     * @param A      the first unit involved in the combat
     * @param B      the second unit involved in the combat
     * @param ifSeed if give the random seed
     */
    public Combat() {
        this.seed = 0;
        this.diceNum = 20;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public void setDiceNum(int diceNum) {
        this.diceNum = diceNum;
    }

    /**
     * 
     * Rolls a 20-sided die and returns the result.
     * 
     * @return the result of the dice roll
     */
    public int rollDice() {
        Random dice;
        if (this.seed != 0) {
            dice = new Random(seed);
        } else {
            dice = new Random();
        }
        return dice.nextInt(diceNum);
    }

    /**
     * 
     * Determines the winner of the combat between two units based on the result of
     * a dice roll.
     * 
     * @param A the first unit involved in the combat
     * @param B the second unit involved in the combat
     * @return the winner of the combat
     */
    public Unit determineWin(Unit A, Unit B) {
        int extraA = 0;
        int extraB = 0;
        // check whether the two owners have special ability extra bonus
        if (A.getOwner().getDiceAdvantageSwitch()) {
            extraA = 5;
            System.out.println("Player " + A.getOwner().getColor() +" has extra bonus");
        }
        if (B.getOwner().getDiceAdvantageSwitch()) {
            extraB = 5;
            System.out.println("Player " + B.getOwner().getColor() +" has extra bonus");
        }
        int resA = rollDice() + A.getBonus() + extraA;
        int resB = rollDice() + B.getBonus() + extraB;
        // Tied and both units are attackers
        if (resA == resB && A.getisAttacker() && B.getisAttacker()) {
            return null;
        }
        // Tied and unit A is not an attacker
        else if (resA == resB && !A.getisAttacker() && B.getisAttacker()) {
            return A;
        }
        // Tied and unit B is not an attacker
        else if (resA == resB && A.getisAttacker() && !B.getisAttacker()) {
            return B;
        }
        // Unit A wins
        else if (resA > resB) {
            return A;
        }
        // Unit B wins
        else {
            return B;
        }
    }
}
