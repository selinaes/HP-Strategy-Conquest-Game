package edu.duke.ece651.team16.server;
import java.util.ArrayList;
public class ResearchOrder implements Order {
    protected Player player;

    /**
     * Constructor for MoveOrder
     * 
     * @param player   the player
     */
    public ResearchOrder(Player player) {
        this.player = player;
    }

    /**
     * Execute the order
     * 
     * @return null
     */
    @Override
    public String tryAction() {
        return null;
    }

}
