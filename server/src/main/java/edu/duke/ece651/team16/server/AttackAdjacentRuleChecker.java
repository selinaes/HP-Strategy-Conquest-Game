package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Territory;

/**
 * Constructor for AttackAdjacentRuleChecker
 * class is used to check if the attack path is valid
 */
public class AttackAdjacentRuleChecker extends OrderRuleChecker {
    public AttackAdjacentRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     * check if one attack path is valid
     * 
     * @param from     the territory to move from
     * @param to       the territory to move to
     * @param player   the player who is moving the unit
     * @param numUnits the number of units to be moved
     * @param map      the map of the game
     * @return null if the order rule is valid, otherwise return the error
     *         message
     */
    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, GameMap gameMap, int level) {
        if (from.getNeighbors().contains(to)) {
            return null;
        } else {
            return "Target is not adjcent.";
        }
    }
}
