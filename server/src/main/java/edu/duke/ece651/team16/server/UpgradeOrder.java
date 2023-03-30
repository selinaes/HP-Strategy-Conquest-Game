package edu.duke.ece651.team16.server;
import java.util.ArrayList;

import edu.duke.ece651.team16.server.Territory;
public class UpgradeOrder implements Order {
    protected Player player;
    protected Territory belonging;
    protected int numUnits;
    protected int initialLevel;
    protected int upgradeAmount; // difference between initial level and upgraded level

    /**
     * Constructor for MoveOrder
     * 
     * @param belonging the belonging
     * @param numUnits  the num units
     * @param player    the player
     * @param inialLevel the initial level
     * @param upgradeAmount the upgrade amount
     */
    public UpgradeOrder(Player player, Territory belonging, int numUnits, int initialLevel, int upgradeAmount) {
        this.player = player;
        this.belonging = belonging;
        this.numUnits = numUnits;
        this.initialLevel = initialLevel;
        this.upgradeAmount = upgradeAmount;
    }

    /**
     * Execute the order
     * 
     * @return the from
     */
    @Override
    public String tryAction() {
        return null;
    }

}
