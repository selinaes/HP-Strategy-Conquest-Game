package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Order;
import java.util.*;

public class AttackOrder implements Order {
    protected Territory from;
    protected Territory to;
    protected int numUnits;
    protected Player player;
    protected GameMap gameMap;

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
    public AttackOrder(Territory from, Territory to, int numUnits, Player player, GameMap gameMap) {
        this.from = from;
        this.to = to;
        this.numUnits = numUnits;
        this.player = player;
        this.gameMap = gameMap;
    }

    /**
     * try to move units from fromTerritory to toTerritory
     * 
     * @return null if the move is valid, otherwise return the error message
     */
    @Override
    public String tryAction() {
        OrderRuleChecker checker = new AttackInputRuleChecker(new AttackAdjacentRuleChecker(null));
        String attackProblem = checker.checkOrder(from, to, player, numUnits, gameMap);
        if (attackProblem == null) {
            // remove units from fromTerritory
            ArrayList<Unit> attackUnits = from.tryRemoveUnits(numUnits, player);
            // add units to parties, wait for battle
            to.tryAddAttackers(attackUnits);
            int cost = attackCost();
            if(cost > player.getFoodResource()) {
                return "Not enough food resource to attack. Need " + cost + " food resources, but only have " + player.getFoodResource() + ".";
            }
            player.removeFoodResource(cost);
            return null;
        }
        return attackProblem;
    }

    public int attackCost() {
        int C = 8;
        return C * numUnits;
    }
}
