package edu.duke.ece651.team16.server;

import java.util.HashSet;
import java.util.HashMap;
import java.util.List;

public class MovePathRuleChecker extends OrderRuleChecker {
    private HashSet<Territory> visited;

    public MovePathRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, Map gameMap) {
        // HashMap<String, List<Territory>>
        this.visited = new HashSet<Territory>();
        visited.add(from);
        boolean valid = dfs(from, to, player, gameMap.getMap(), visited);
        if (valid) {
            return null;
        } else {
            return "No valid path";
        }
    }

    private boolean dfs(Territory current, Territory destination, Player player,
            HashMap<String, List<Territory>> gameMap, HashSet<Territory> visited) {
        if (current == destination) {
            return true;
        }

        List<Territory> neighbors = current.getNeighbors();
        for (Territory neighbor : neighbors) {
            System.out.println(player.getColor());
            if (!visited.contains(neighbor)) {
                if (neighbor.getOwner().getColor() == player.getColor()) {
                    visited.add(neighbor);
                    boolean found = dfs(neighbor, destination, player, gameMap, visited);
                    if (found) {
                        return true;
                    }
                }

            }
        }
        return false;
    }
}