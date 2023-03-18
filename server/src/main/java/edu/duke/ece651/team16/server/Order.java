package edu.duke.ece651.team16.server;

import java.util.ArrayList;

public abstract class Order {
    protected Territory from;
    protected Territory to;
    protected int numUnits;
    protected Player player;
    protected Map gameMap;

    /*
     * Constructor for Order
     *
     * @param from the from
     * 
     * @param to the to
     * 
     * @param numUnits the numUnits
     * 
     * @param player the player
     * 
     * @param gameMap the gameMap
     */
    public Order(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        this.from = from;
        this.to = to;
        this.numUnits = numUnits;
        this.player = player;
        this.gameMap = gameMap;
        // this.OrderRuleChecker = new MoveInputRuleChecker(new MovePathRuleChecker());
    }

    /*
     * Get for players
     * 
     * @return the player
     */
    public Player getPlayer() {
        return player;
    };

    /**
     * Try different action for particular order (move order, attack order, etc.)
     * 
     * @return the from
     */
    public String tryAction() {
        OrderRuleChecker checker = new MoveInputRuleChecker(new MovePathRuleChecker(null));
        String moveProblem = checker.checkOrder(from, to, player, numUnits, gameMap);
        if (moveProblem == null) {
            // remove units from fromTerritory
            ArrayList<Unit> moveUnits = from.tryRemoveUnits(numUnits, player);
            // add units to toTerritory
            to.tryAddUnits(moveUnits);
            return null;
        }
        return moveProblem;
    }
}
