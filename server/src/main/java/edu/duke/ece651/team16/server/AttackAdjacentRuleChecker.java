package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Territory;

public class AttackAdjacentRuleChecker extends OrderRuleChecker {
    public AttackAdjacentRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, GameMap gameMap) {
        if (from.getNeighbors().contains(to)) {
            return null;
        } else {
            return "Target is not adjcent.";
        }
    }
}
