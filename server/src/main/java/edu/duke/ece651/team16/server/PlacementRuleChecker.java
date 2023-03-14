package edu.duke.ece651.team16.server;

public abstract class PlacementRuleChecker {
    private final PlacementRuleChecker next;

    /*
     * constructor
     * 
     * @param next the next checker in the chain
     */
    public PlacementRuleChecker(PlacementRuleChecker next) {
        this.next = next;
    }

    /*
     * check if one placement rule is valid
     * 
     * @param unit the unit to be placed
     * 
     * @param territory the territory to be placed
     * 
     * @param player the player who is placing the unit
     * 
     * @return null if the placement rule is valid, otherwise return the error
     * message
     */
    protected abstract String checkMyRule(String territory_name, Player player, int amount);

    /*
     * check if the placement is valid
     * 
     * @param unit the unit to be placed
     * 
     * @param territory the territory to be placed
     * 
     * @param player the player who is placing the unit
     * 
     * @return null if the placement is valid, otherwise return the error message
     */
    public String checkPlacement(String territoryName, Player player, int amount) {
        String result = checkMyRule(territoryName, player, amount);
        if (result != null) {
            return result;
        }
        // if (next != null) {
        // return next.checkPlacement(territoryName, player, amount);
        // }
        return null;
    }
}
