package edu.duke.ece651.team16.server;

public class AllianceRuleChecker {
    public AllianceRuleChecker() {
    }

    public String checkMyRule(Player player, Player ally) {
        if (player.getColor().equals(ally.getColor())) {
            return "You can not choose yourself";
        } else if (player.getAlly() != null) {
            return "You have already formed alliance";
        }
        return null;
    }
}