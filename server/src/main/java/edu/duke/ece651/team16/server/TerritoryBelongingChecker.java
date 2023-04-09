package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class TerritoryBelongingChecker extends UpgradeRuleChecker {
    public TerritoryBelongingChecker(UpgradeRuleChecker next) {
        super(next);
    }

    /**
     * check if territory belongs to the player
     * 
     * @param player        the player who is moving the unit
     * @param belonging     the territory to move from
     * @param numUnits      the number of units to be moved
     * @param initialLevel  the initial level of the tech
     * @param upgradeAmount the upgrade amount
     * @return null if the order rule is valid, otherwise return the error
     *         message
     */
    @Override
    public String checkMyRule(Player player, Territory belonging, int numUnits, int initialLevel,
            int upgradeAmount) {
        if (belonging.getOwner() != player) {
            return "You do not own the territory where you want to upgrade units";
        }
        return null;
    }

}
