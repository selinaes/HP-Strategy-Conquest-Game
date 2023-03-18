package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Order;
import java.util.*;

public class AttackOrder extends Order {
    public AttackOrder(Territory from, Territory to, int numUnits, Player player, GameMap gameMap) {
        super(from, to, numUnits, player, gameMap);
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
            return null;
        }
        return attackProblem;
    }
}
