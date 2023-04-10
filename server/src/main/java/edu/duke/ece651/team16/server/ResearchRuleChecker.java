package edu.duke.ece651.team16.server;

public class ResearchRuleChecker {
    public ResearchRuleChecker() {
    }

    /*
     * check if the player has already researched in this turn
     * check if the player has reached the max research level
     */
    public String checkMyRule(Player player, int maxLevel) {
        if (player.getHasResearched() == true) {
            return "You have already researched in this turn.";
        }
        if (player.getTechLevel() == maxLevel
                || (player.getTechLevel() == maxLevel - 1 && player.getDelayedTech() > 0)) {
            return "You have already reached the max research level.";
        }
        return null;
    }

}
