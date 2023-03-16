package edu.duke.ece651.team16.server;

public class MoveOrder extends Order {

    public MoveOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        super(from, to, numUnits, player, gameMap);
    }

    // /**
    // * try to move units from fromTerritory to toTerritory
    // *
    // * @return null if the move is valid, otherwise return the error message
    // */
    // @Override
    // public String tryAction() {
    // OrderRuleChecker checker = new MoveInputRuleChecker(new
    // MovePathRuleChecker(null));
    // String moveProblem = checker.checkOrder(from, to, player, numUnits, gameMap);
    // if (moveProblem == null) {
    // // remove units from fromTerritory
    // ArrayList<Unit> moveUnits = from.tryRemoveUnits(numUnits, player);
    // // add units to toTerritory
    // to.tryAddUnits(moveUnits);
    // return null;

    // }
    // return moveProblem;
    // }

}
