package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class TerritoryEnoughUnitChecker extends UpgradeRuleChecker {
    public TerritoryEnoughUnitChecker(UpgradeRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Player player, Territory belonging, int numUnits, int initialLevel,
    int upgradeAmount) {
        if (belonging.getUnitsByLevel(initialLevel).size() < numUnits) {
            return "You do not have enough alive units with initial level in the territory where you want to upgrade units";
        }
        return null;
    }

}
