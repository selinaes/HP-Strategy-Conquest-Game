
package edu.duke.ece651.team16.server;

import java.util.ArrayList;
import java.util.HashMap;

public class ResearchOrder implements Order {
    protected Player player;

    public static HashMap<Integer, Integer> researchCostTable;

    /**
     * Constructor for ResearchOrdre
     * 
     * @param player the player
     */
    public ResearchOrder(Player player) {
        this.player = player;
        researchCostTable = new HashMap<Integer, Integer>();
        initializeTable();
    }

    /**
     * Initialize the research cost table
     */
    private void initializeTable() {
        // costTable: startLevel, cost
        researchCostTable.put(1, 20);
        researchCostTable.put(2, 40);
        researchCostTable.put(3, 80);
        researchCostTable.put(4, 160);
        researchCostTable.put(5, 320);
    }

    /**
     * Execute the order
     * 
     * @return null
     */
    @Override
    public String tryAction() {
        ResearchRuleChecker checker = new ResearchRuleChecker();
        String result = checker.checkMyRule(player, researchCostTable.size() + 1);
        if (result == null) {
            int cost = researchCostTable.get(player.getTechLevel());
            if (player.getTechResource() < cost) {
                return "Not enough tech resource. Need " + cost + " tech resource, but only have "
                        + player.getTechResource() + ".";
            }
            player.removeTechResource(cost);
            player.updateTechLevel();
            player.updateResearchRound(true);
            return null;
        }
        return result;
    }

}
