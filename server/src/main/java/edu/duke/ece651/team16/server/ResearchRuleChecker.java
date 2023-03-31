package edu.duke.ece651.team16.server;

public class ResearchRuleChecker {
    public ResearchRuleChecker() {
    }

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
