package edu.duke.ece651.team16.server;

public class MoveOrder extends Order {

    public MoveOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap) {
        super(from, to, numUnits, player, gameMap);
    }

}
