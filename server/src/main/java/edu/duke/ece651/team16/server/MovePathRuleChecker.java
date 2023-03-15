package edu.duke.ece651.team16.server;

public class MovePathRuleChecker extends OrderRuleChecker {
    public MovePathRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, Map gameMap) {
        // HashMap<String, List<Territory>>
        return "";
    }
}