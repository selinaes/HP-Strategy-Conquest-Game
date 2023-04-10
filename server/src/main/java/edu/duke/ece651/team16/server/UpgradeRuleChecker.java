package edu.duke.ece651.team16.server;

public abstract class UpgradeRuleChecker {
    private final UpgradeRuleChecker next;

    /**
     * constructor
     * 
     * @param next the next checker in the chain
     */
    public UpgradeRuleChecker(UpgradeRuleChecker next) {
        this.next = next;
    }

    /**
     * check if one placement rule is valid
     * 
     * @param player        the player who is moving the unit
     * @param belonging     the territory to move from
     * @param numUnits      the number of units to be moved
     * @param initialLevel  the initial level of the tech
     * @param upgradeAmount the upgrade amount
     * @return null if the order rule is valid, otherwise return the error
     *         message
     */
    protected abstract String checkMyRule(Player player, Territory belonging, int numUnits, int initialLevel,
            int upgradeAmount);

    /**
     * check if the placement is valid
     * 
     * @param player        the player who is moving the unit
     * @param belonging     the territory to move from
     * @param numUnits      the number of units to be moved
     * @param initialLevel  the initial level of the tech
     * @param upgradeAmount the upgrade amount
     * @return null if the placement is valid, otherwise return the error message
     */
    public String checkOrder(Player player, Territory belonging, int numUnits, int initialLevel, int upgradeAmount) {
        String result = checkMyRule(player, belonging, numUnits, initialLevel, upgradeAmount);
        if (result != null) {
            return result;
        }
        if (next != null) {
            return next.checkOrder(player, belonging, numUnits, initialLevel, upgradeAmount);
        }
        return null;
    }

}
