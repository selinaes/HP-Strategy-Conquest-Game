package edu.duke.ece651.team16.server;

public class AssignUnitRuleChecker {

    public String checkMyRule(String territory_name, Player player, int amount) {
        if (amount > player.unplacedUnits()) {
            return "The amount of units you want to place is greater than the number of unplaced units";
        }

        if (!player.getTerritoryNames().contains(territory_name)) {
            return "You do not own this territory";
        }

        return null;
    }

}
