package edu.duke.ece651.team16.server;
import java.util.ArrayList;
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
        MovePathRuleChecker pathchecker = new MovePathRuleChecker(null);
        OrderRuleChecker checker = new MoveInputRuleChecker(pathchecker);
        String moveProblem = checker.checkOrder(from, to, player, numUnits, gameMap, level);
        if (moveProblem == null) {
            // remove units from fromTerritory
            ArrayList<Unit> moveUnits = from.tryRemoveUnits(numUnits, player, level);
            // add units to toTerritory
            to.tryAddUnits(moveUnits);
            // subtract food resource
            int distance = pathchecker.dijkstraAlgorithm(from, to, gameMap);
            // int stub_distance = 5;
            int cost = moveCost(distance);
            if(player.getFoodResource() < cost)
                return "Not enough food resource. Need " + cost + " food resources, but only have " + player.getFoodResource() + " food resource.";
            player.removeFoodResource(cost);
            return null;
        }
        return moveProblem;
    }

    public int moveCost(int distance){
        int C = 1;
        return C * numUnits * distance;
    }

}
