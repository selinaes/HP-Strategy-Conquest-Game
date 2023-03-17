package edu.duke.ece651.team16.server;

import java.util.*;
import java.util.ArrayList;

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

    /**
     * Check if the battlefield has the given party's party
     * 
     * @param units the given party
     * @return boolean true if the battlefield has the given party's party
     */
    public boolean checkGroupExisted(ArrayList<Unit> units) {
        for (ArrayList<Unit> party : parties) {
            if (party.get(0).getOwner().equals(units.get(0).getOwner())) {
                return true;
            }
        }
        return false;
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

    /**
     * Combine units from the same player
     */
    private void combineUnitsFromSamePlayer() {
        System.out.println("============Before Combine:============");
        System.out.println("Parties has: ");
        for (ArrayList<Unit> p : parties) {
            System.out.println("Player " + p.get(0).getOwner().getColor() + "'s Units:");
            for (Unit u : p) {
                System.out.println("Unit :" + u.getId());
            }
        }
        HashMap<Player, ArrayList<Unit>> ownerToUnits = new HashMap<>();
        for (ArrayList<Unit> party : parties) {
            Player owner = party.get(0).getOwner();
            if (ownerToUnits.containsKey(owner)) {
                ownerToUnits.get(owner).addAll(party);
            } else {
                ownerToUnits.put(owner, new ArrayList<>(party));
            }
        }
        this.parties = new ArrayList<>(ownerToUnits.values());
        System.out.println("============After Combine:============");
        System.out.println("Parties has: ");
        for (ArrayList<Unit> p : parties) {
            System.out.println("Player " + p.get(0).getOwner().getColor() + "'s Units:");
            for (Unit u : p) {
                System.out.println("Unit :" + u.getId());
            }
        }
    }

    /**
     * Form a game log that shows the number of units in each party
     * 
     * @return String
     */
    public String GameLog() {
        String log = "";
        for (ArrayList<Unit> party : parties) {
            log += "Player " + party.get(0).getOwner().getColor() + " has " + party.size()
                    + " units in this battle. ";
        }
        return log;
    }

    public Player resolveBattle() {
        combineUnitsFromSamePlayer();
        int index = 0;
        System.out.println("Parties size: " + parties);
        while (parties.size() > 1) {
            // get the first unit of the party
            int indexA = index % parties.size();
            int indexB = (index + 1) % parties.size();
            System.out.println("IndexA: " + indexA);
            System.out.println("IndexB: " + indexB);
            System.out.println("Parties size: " + parties.size());
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
