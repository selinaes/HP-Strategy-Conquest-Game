package edu.duke.ece651.team16.server;

public class AssignUnitRuleChecker extends PlacementRuleChecker {
    @Override
    public String checkMyRule(String territory_name, Player player, int amount) {
        if (amount > player.unplacedUnits()) {
            return "The amount of units you want to place is greater than the number of unplaced units";
        }
        // if (unit.getwhere() != null) {
        // return "Unit already assigned to a territory";
        // }
        if (!player.getTerritoryNames().contains(territory_name)) {
            return "You do not own this territory";
        }
        // if (unit.getOwner() != player) {
        // return "You do not own this unit";
        // }
        return null;
    }

    public AssignUnitRuleChecker(PlacementRuleChecker next) {
        super(next);
    }
}
