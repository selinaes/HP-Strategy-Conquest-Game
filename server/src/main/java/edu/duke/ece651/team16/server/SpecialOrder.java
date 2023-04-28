
package edu.duke.ece651.team16.server;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialOrder implements Order {
    protected Player player;
    protected String option;
    protected Territory target;

    /**
     * Constructor for SpecialOrder
     * 
     * @param player the player
     */
    public SpecialOrder(Player player, String option) {
        this.player = player;
        this.option = option;
    }

    /**
     * Constructor for SpecialOrder with Fiendfyre target
     * 
     * @param player the player
     */
    public SpecialOrder(Player player, String option, Territory target){
        this.player = player;
        this.option = option;
        this.target = target;
    }

    /**
     * Execute the order
     * 
     * @return null
     */
    @Override
    public String tryAction() {
        if (option.equals("Double Resource Production")) {
            System.out.println("Double Resource Production for player: " + player.getColor());
            player.setDoubleResourceSwitch(true);
        } else if (option.equals("Two Units Generation")) {
            System.out.println("Two Units Generation for player: " + player.getColor());
            player.setMoreUnitSwitch(true);
        } else if (option.equals("Disregard Adjacency")) {
            player.setDisregardAdjacencySwitch(true);
        } else if (option.equals("Dice Advantage")) {
            player.setDiceAdvantageSwitch(true);
        } else if (option.equals("Fiendfyre")) {
            // check that the target is an enemy territory
            if (target.getOwner() == player) {
                return "You can not Fiendfyre your own territory";
            }
            // then place bomb at the target (will perform bomb when battling)
            target.placeBomb(player);
            
        }
        return null;
    }

}
