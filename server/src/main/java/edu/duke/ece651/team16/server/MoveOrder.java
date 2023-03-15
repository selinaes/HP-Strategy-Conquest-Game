package edu.duke.ece651.team16.server;

import java.util.ArrayList;

public class MoveOrder extends Order {

    // shortest path
    // private validPath validPath;

    public MoveOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        super(from, to, numUnits, player, gameMap);
        // this.validPath = validPath(gameMap);
    }

    /**
     * try to move units from fromTerritory to toTerritory
     * 
     * @return null if the move is valid, otherwise return the error message
     */
    public String tryMove() {
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
