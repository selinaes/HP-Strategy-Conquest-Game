package edu.duke.ece651.team16.server;

public class AttackInputRuleChecker extends OrderRuleChecker {
    public AttackInputRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, Map map) {
        if (from.getOwner() != player) {
            return "You do not own the from territory";
        }
        if (to.getOwner() == player) {
            return "You do own the to territory";
        }
        if (from.getAliveUnitsFor(player).size() < numUnits) {
            // check units alive & owner is player
            return "You do not have enough alive units in the from territory";
        }
        return null;
    }
}
