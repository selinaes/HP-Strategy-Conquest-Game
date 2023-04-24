package edu.duke.ece651.team16.server;

import java.util.ArrayList;

import edu.duke.ece651.team16.server.OrderRuleChecker;
import edu.duke.ece651.team16.server.Player;

public class MoveOrder implements Order {

    protected Territory from;
    protected Territory to;
    protected int numUnits;
    protected Player player;
    protected GameMap gameMap;
    protected int level;

    /**
     * Constructor for MoveOrder
     * 
     * @param from     the from
     * @param to       the to
     * @param numUnits the num units
     * @param player   the player
     * @param gameMap  the game map
     */
    public MoveOrder(Territory from, Territory to, int numUnits, Player player, GameMap gameMap, int level) {
        this.from = from;
        this.to = to;
        this.numUnits = numUnits;
        this.player = player;
        this.gameMap = gameMap;
        this.level = level;
    }

    /**
     * Try execute the action
     * 
     * @return the from
     */
    @Override
    public String tryAction() {
        int distance;
        String moveProblem;
        if (player.getDisregardAdjacencySwitch()) {
            // if disregard adjacency is on, then we don't need to check path, just check
            // input
            OrderRuleChecker checker = new MoveInputRuleChecker(null);
            moveProblem = checker.checkOrder(from, to, player, numUnits, gameMap, level);
            if (moveProblem != null)
                return moveProblem;
            // if disregard adjacency is on, then we set the distance as default 5
            distance = 5;
        } else {
            MovePathRuleChecker pathchecker = new MovePathRuleChecker(null);
            OrderRuleChecker checker = new MoveInputRuleChecker(pathchecker);
            moveProblem = checker.checkOrder(from, to, player, numUnits, gameMap, level);
            if (moveProblem != null)
                return moveProblem;
            // otherwise, calculate the real distance
            distance = pathchecker.dijkstraAlgorithm(from, to, gameMap);
        }

        int cost = moveCost(distance);
        if (player.getFoodResource() < cost)
            return "Not enough food resource. Need " + cost + " food resources, but only have "
                    + player.getFoodResource() + " food resource.";
                    
        // subtract food resource
        player.removeFoodResource(cost);
        // remove units from fromTerritory
        ArrayList<Unit> moveUnits = from.tryRemoveUnits(numUnits, player, level);
        // add units to toTerritory
        to.tryAddUnits(moveUnits);
        return null;

    }

    /**
     * Calculate the cost of moving
     */
    public int moveCost(int distance) {
        int C = 1;
        return C * numUnits * distance;
    }

}
