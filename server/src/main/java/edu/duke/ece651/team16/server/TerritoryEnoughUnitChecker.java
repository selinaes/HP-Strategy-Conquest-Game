package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class TerritoryEnoughUnitChecker extends UpgradeRuleChecker {
    public TerritoryEnoughUnitChecker(UpgradeRuleChecker next) {
        super(next);
    }

    /**
     * check if one territory has enough units to upgrade
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
        if (belonging.getUnitsByLevel(initialLevel).size() < numUnits) {
            return "You do not have enough alive units with initial level in the territory where you want to upgrade units";
        }
        return null;
    }

}
