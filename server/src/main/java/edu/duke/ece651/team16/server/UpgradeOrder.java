package edu.duke.ece651.team16.server;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team16.server.Territory;
public class UpgradeOrder implements Order {
    protected Player player;
    protected Territory belonging;
    protected int numUnits;
    protected int initialLevel;
    protected int upgradeAmount; // difference between initial level and upgraded level

    public static HashMap<Integer, Integer> upgradeCostTable;

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
        upgradeCostTable = new HashMap<Integer, Integer>();
        initializeTable();
    }

    /**
     * Initialize the cost table
     */
    private void initializeTable() {
        // costTable: startLevel, cost
        upgradeCostTable.put(0, 3);
        upgradeCostTable.put(1, 8);
        upgradeCostTable.put(2, 19);
        upgradeCostTable.put(3, 25);
        upgradeCostTable.put(4, 35);
        upgradeCostTable.put(5, 50);
    }

    /**
     * Execute the order
     * 
     * @return the from
     */
    @Override
    public String tryAction() {
        UpgradeRuleChecker checker = new UpgradeTechLevelChecker(new TerritoryBelongingChecker(new TerritoryEnoughUnitChecker(null)));
        String result = checker.checkOrder(player, belonging, numUnits, initialLevel, upgradeAmount);
        if (result == null) {
            int cost = costCounter();
            if (cost > player.getTechResource()) {
                return "Not enough tech resource. Need " + cost + " tech resource, but only have "
                        + player.getTechResource() + ".";
            }
            player.removeTechResource(cost);
            ArrayList<Unit> to_upgrade_source = belonging.getUnitsByLevel(initialLevel);
            ArrayList<Unit> to_upgrade = new ArrayList<Unit>(to_upgrade_source.subList(0, numUnits));  
            for (Unit unit : to_upgrade) {
                unit.upgrade(upgradeAmount);
            }
            return null;
        }
        return result;
    }

    /**
     * Count the cost of the upgrade
     * 
     * @return the cost
     */
    private int costCounter() {
        int upgradeCounter = upgradeAmount;
        int levelTracker = initialLevel;
        int cost = 0;
        while (upgradeCounter > 0) {
            cost += upgradeCostTable.get(levelTracker++) * numUnits;
            upgradeCounter--;
        }
        return cost;
    }

}
