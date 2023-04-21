package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Order;
import java.util.*;

public class AttackOrder implements Order {
    protected Territory from;
    protected Territory to;
    protected int numUnits;
    protected Player player;
    protected GameMap gameMap;
    protected int level;

    /*
     * constructor for AttackOrder
     * 
     * @param fromTerritory the territory to attack from
     * 
     * @param toTerritory the territory to attack
     * 
     * @param numUnits the number of units to attack with
     * 
     * @param player the player who is attacking
     * 
     * @param gameMap the gameMap
     */
    public AttackOrder(Territory from, Territory to, int numUnits, Player player, GameMap gameMap, int level) {
        this.from = from;
        this.to = to;
        this.numUnits = numUnits;
        this.player = player;
        this.gameMap = gameMap;
        this.level = level;
    }

    /**
     * try to move units from fromTerritory to toTerritory
     * 
     * @return null if the move is valid, otherwise return the error message
     */
    @Override
    public String tryAction() {
        OrderRuleChecker checker;
        if (player.getDisregardAdjacencySwitch()){
            checker = new AttackInputRuleChecker(null);
        } else {
            checker = new AttackInputRuleChecker(new AttackAdjacentRuleChecker(null));
        }
        String attackProblem = checker.checkOrder(from, to, player, numUnits, gameMap, level);
        if (attackProblem == null) {
            int cost = attackCost();
            if (cost > player.getFoodResource()) {
                return "Not enough food resource to attack. Need " + cost + " food resources, but only have "
                        + player.getFoodResource() + ".";
            }
            player.removeFoodResource(cost);
            // remove units from fromTerritory
            ArrayList<Unit> attackUnits = from.tryRemoveUnits(numUnits, player, level);
            // add units to parties, wait for battle
            to.tryAddAttackers(attackUnits);
            return null;
        }
        return attackProblem;
    }

    /**
     * calculate the cost of the attack
     * 
     * @return null if the attack is valid, otherwise return the error message
     */
    public int attackCost() {
        int C = 8;
        return C * numUnits;
    }
}
