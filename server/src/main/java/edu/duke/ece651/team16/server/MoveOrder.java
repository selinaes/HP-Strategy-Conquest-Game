package edu.duke.ece651.team16.server;


public class MoveOrder extends Order{

   // shortest path
   // private validPath

    public MoveOrder(Territory from, Territory to, int numUnits, Player player, Map gameMap){
        super(from, to, numUnits, player, gameMap);
        // validPath = validPath(gameMap);
    }

    public boolean tryMove() {
        return true;
    }

}
