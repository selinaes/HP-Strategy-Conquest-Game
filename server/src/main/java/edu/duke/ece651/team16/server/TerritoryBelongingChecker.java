package edu.duke.ece651.team16.server;

import edu.duke.ece651.team16.server.Player;

public class TerritoryBelongingChecker extends UpgradeRuleChecker {
    public TerritoryBelongingChecker(UpgradeRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Player player, Territory belonging, int numUnits, int initialLevel,
    int upgradeAmount) {
        if (belonging.getOwner() != player) {
            return "You do not own the territory where you want to upgrade units";
        }
        return null;
    }

}
