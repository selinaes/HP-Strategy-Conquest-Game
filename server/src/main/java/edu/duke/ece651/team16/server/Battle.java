package edu.duke.ece651.team16.server;

import java.util.*;

public class Battle {
    private ArrayList<ArrayList<Unit>> parties;
    private Combat combat;

    /*
     * Constructor for the battle
     */
    public Battle() {
        this.parties = new ArrayList<ArrayList<Unit>>();
        this.combat = new Combat();
    }

    /*
     * Add a group of units to the battle
     * 
     * @param playerColor
     * 
     * @param units
     */
    public void addGroup(ArrayList<Unit> units) {
        parties.add(units);
    }

    /**
     * Set the combat
     * 
     * @param combat
     */
    public void setCombat(Combat combat) {
        this.combat = combat;
    }

    /**
     * get the size of parties
     * 
     * @return parties
     */
    public ArrayList<ArrayList<Unit>> getParties() {
        return parties;
    }

    // /*
    // * Check if the party is alive
    // *
    // * @param playerColor
    // *
    // * @return boolean
    // */
    // public boolean checkPartyAlive(String playerName) {
    // for (Unit unit : parties.get(playerName)) {
    // if (unit.getAlive()) {
    // return true;
    // }
    // }
    // parties.remove(playerName);
    // return false;
    // }

    public Player resolveBattle() {
        int index = 0;
        while (parties.size() > 1) {
            // get the first unit of the party
            int indexA = index % parties.size();
            int indexB = (index + 1) % parties.size();
            ArrayList<Unit> unitsA = parties.get(indexA);
            ArrayList<Unit> unitsB = parties.get(indexB);
            Unit unitA = unitsA.get(0);
            // get the second unit of the part
            Unit unitB = unitsB.get(0);

            Unit winner = combat.determineWin(unitA, unitB);

            if (winner != null) {
                if (winner.equals(unitB)) {
                    unitA.setDead();
                    unitsA.remove(0);
                    if (unitsA.isEmpty()) {
                        parties.remove(indexA);
                        index = -1;
                    }
                } else {
                    unitB.setDead();
                    unitsB.remove(0);
                    if (unitsB.isEmpty()) {
                        parties.remove(indexB);
                        index = -1;
                    }
                }
            }
            ++index;
            // at end, then loop again:
        }
        return parties.get(0).get(0).getOwner();
    }
}
