package edu.duke.ece651.team16.server;

public class AttackInputRuleChecker extends OrderRuleChecker {
    /**
     * Constructor for AttackInputRuleChecker
     * class is used to check if the attack input is valid
     */
    public AttackInputRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     * check if one attack input is valid
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
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, GameMap map, int level) {
        if (!from.getOwner().getColor().equals(player.getColor())
                && !(player.getAlly() != null && from.getOwner().getColor().equals(player.getAlly().getColor()))) {
            return "You do not own the from territory";
        }
        if (to.getOwner().getColor().equals(player.getColor())) {
            return "You can not attack your own territory";
        }
        if (from.getAliveUnitsFor(player, level).size() < numUnits) {
            // check units alive & owner is player
            return "You do not have enough students of this level in the from territory";
        }
        return null;
    }
}
