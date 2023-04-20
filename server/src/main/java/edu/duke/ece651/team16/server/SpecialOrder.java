
package edu.duke.ece651.team16.server;

import java.util.ArrayList;
import java.util.HashMap;

public class SpecialOrder implements Order {
    protected Player player;
    protected String option;

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
     * Execute the order
     * 
     * @return null
     */
    @Override
    public String tryAction() {
        if (option.equals("Double Resource Production")) {
            player.setDoubleResourceSwitch(true);
        } else if (option.equals("Two Units Generation")) {
            player.setMoreUnitSwitch(true);
        } else if (option.equals("3")) {
            player.setDisregardAdjacencySwitch(true);
        } else if (option.equals("4")) {
            player.setDiceAdvantageSwitch(true);
        }
        return null;
    }

}
