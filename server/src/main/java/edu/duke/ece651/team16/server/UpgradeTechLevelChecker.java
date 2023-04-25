package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class UpgradeTechLevelChecker extends UpgradeRuleChecker {
    public UpgradeTechLevelChecker(UpgradeRuleChecker next) {
        super(next);
    }

    /**
     * check if the player has enough tech level to upgrade the unit
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
        if (player.getTechLevel() < initialLevel + upgradeAmount) {
            return "You do not have enough tech level to upgrade desired level amount for these students";
        }
        return null;
    }

}
