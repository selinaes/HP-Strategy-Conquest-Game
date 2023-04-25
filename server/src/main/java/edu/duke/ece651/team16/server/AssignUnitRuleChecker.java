package edu.duke.ece651.team16.server;

public class AssignUnitRuleChecker {
    /*
     * This class is used to check the rule of assigning units to a territory
     * 
     * @param territory_name the name of the territory
     * 
     * @param player the player who is assigning units
     * 
     * @param amount the amount of units to be assigned
     * 
     * @return null if the rule is satisfied, otherwise return the error message
     */
    public String checkMyRule(String territory_name, Player player, int amount) {
        if (amount > player.unplacedUnits()) {
            return "The amount of students you want to place is greater than the number of unplaced units";
        }

        if (!player.getTerritoryNames().contains(territory_name)) {
            return "You do not own this territory";
        }

        return null;
    }

}
