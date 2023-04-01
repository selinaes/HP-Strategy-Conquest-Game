package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class UpgradeTechLevelChecker extends UpgradeRuleChecker {
    public UpgradeTechLevelChecker(UpgradeRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Player player, Territory belonging, int numUnits, int initialLevel,
    int upgradeAmount) {
        if (player.getTechLevel() < initialLevel + upgradeAmount) {
            return "You do not have enough tech level to upgrade desired level amount for these units";
        }
        return null;
    }

}
