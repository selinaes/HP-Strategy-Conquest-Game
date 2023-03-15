package edu.duke.ece651.team16.server;

public class MoveOrder extends Order {

    // shortest path
    // private validPath validPath;

    public MoveOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        super(from, to, numUnits, player, gameMap);
        // this.validPath = validPath(gameMap);
    }

    public boolean tryMove() {
        OrderRuleChecker checker = new MoveInputRuleChecker(new MovePathRuleChecker(null));
        if (!checker.check(from, to, numUnits, player, gameMap)) {
            return false;
        }
        return true;
    }

}
