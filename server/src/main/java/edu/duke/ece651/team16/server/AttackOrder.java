package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Order;
import java.util.*;

public class AttackOrder extends Order {
    public AttackOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        super(from, to, numUnits, player, gameMap);
    }

    /**
     * try to move units from fromTerritory to toTerritory
     * 
     * @return null if the move is valid, otherwise return the error message
     */
    public String tryAttack() {
        OrderRuleChecker checker = new AttackInputRuleChecker(new AttackInputRuleChecker(null));
        String attackProblem = checker.checkOrder(from, to, player, numUnits, gameMap);
        if (attackProblem == null) {
            // remove units from fromTerritory
            ArrayList<Unit> moveUnits = from.tryRemoveUnits(numUnits, player);
            // add units to toTerritory
            to.tryAddUnits(moveUnits);
            return null;
        }
        return attackProblem;
    }
}
