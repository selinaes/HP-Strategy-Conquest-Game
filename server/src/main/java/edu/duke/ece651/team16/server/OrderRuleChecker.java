package edu.duke.ece651.team16.server;

public abstract class OrderRuleChecker {
    private final OrderRuleChecker next;

    /**
     * constructor
     * 
     * @param next the next checker in the chain
     */
    public OrderRuleChecker(OrderRuleChecker next) {
        this.next = next;
    }

    /**
     * check if one placement rule is valid
     * 
     * @param from     the territory to move from
     * @param to       the territory to move to
     * @param player   the player who is moving the unit
     * @param numUnits the number of units to be moved
     * @param map      the map of the game
     * @return null if the order rule is valid, otherwise return the error
     *         message
     */
    protected abstract String checkMyRule(Territory from, Territory to, Player player, int numUnits, GameMap map, int level);

    /**
     * check if the placement is valid
     * 
     * @param from     the territory to move from
     * @param to       the territory to move to
     * @param player   the player who is moving the unit
     * @param numUnits the number of units to be moved
     * @param map      the map of the game
     * @return null if the placement is valid, otherwise return the error message
     */
    public String checkOrder(Territory from, Territory to, Player player, int numUnits, GameMap map, int level) {
        String result = checkMyRule(from, to, player, numUnits, map, level);
        if (result != null) {
            return result;
        }
        if (next != null) {
            return next.checkOrder(from, to, player, numUnits, map, level);
        }
        return null;
    }

}
