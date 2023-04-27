package edu.duke.ece651.team16.server;

import java.util.*;

public class MovePathRuleChecker extends OrderRuleChecker {
    private HashSet<Territory> visited;

    public MovePathRuleChecker(OrderRuleChecker next) {
        super(next);
    }

    /**
     * check if one placement rule is valid
     * 
     * @param from     the territory to move from
     * @param to       the territory to move to
     * @param player   the player who is moving the unit
     * @param numUnits the number of units to be moved
     * @param map      the map of the game
     * @return null if the order rule is valid, otherwise return the error
     *         message
     */
    @Override
    public String checkMyRule(Territory from, Territory to, Player player, int numUnits, GameMap gameMap, int level) {
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

    /**
     * Use dfs to check if there is a valid path from source to destination
     * 
     * @param current     the current territory
     * @param destination the destination territory
     * @param player      the player who is moving the unit
     * @param gameMap     the map of the game
     * @param visited     the territories that have been visited
     * @return true if there is a valid path, otherwise return false
     */
    private boolean dfs(Territory current, Territory destination, Player player,
            HashMap<String, List<Territory>> gameMap, HashSet<Territory> visited) {
        if (current == destination) {
            return true;
        }
        List<Territory> neighbors = current.getNeighbors();
        for (Territory neighbor : neighbors) {
            // System.out.println(player.getColor());
            if (!visited.contains(neighbor)) {
                if (neighbor.getOwner().getColor().equals(player.getColor())
                        || (player.getAlly() != null
                                && neighbor.getOwner().getColor().equals(player.getAlly().getColor()))) {
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

    /**
     * Use dijkstra algorithm to find the shortest distance between source and
     * destination
     * 
     * @param source
     * @param destination
     * @param gameMap
     * @return the shortest distance between source and destination
     */
    public static int dijkstraAlgorithm(Territory source, Territory destination, GameMap gameMap) {
        Set<Territory> settledTerritories = new HashSet<>();
        Set<Territory> unsettledTerritories = new HashSet<>();
        Map<Territory, Integer> shortestDistances = new HashMap<>();
        ArrayList<Territory> territories = gameMap.getTerritoryList();
        for (Territory territory : territories) {
            shortestDistances.put(territory, Integer.MAX_VALUE);
            unsettledTerritories.add(territory);
        }
        shortestDistances.put(source, 0);
        while (!unsettledTerritories.isEmpty()) {
            Territory currentTerritory = getMinimumDistanceTerritory(unsettledTerritories, shortestDistances);
            unsettledTerritories.remove(currentTerritory);
            settledTerritories.add(currentTerritory);
            relaxNeighbors(currentTerritory, shortestDistances, unsettledTerritories);
        }
        return shortestDistances.get(destination);
    }

    /**
     * Get the territory with the minimum distance from the unsettled territories
     * 
     * @param unsettledTerritories
     * @param shortestDistances
     * @return the territory with the minimum distance
     */
    private static Territory getMinimumDistanceTerritory(Set<Territory> unsettledTerritories,
            Map<Territory, Integer> shortestDistances) {
        int minimumDistance = Integer.MAX_VALUE;
        Territory minimumDistanceTerritory = null;
        for (Territory territory : unsettledTerritories) {
            int territoryDistance = shortestDistances.get(territory);
            if (territoryDistance < minimumDistance) {
                minimumDistance = territoryDistance;
                minimumDistanceTerritory = territory;
            }
        }
        return minimumDistanceTerritory;
    }

    /**
     * Relax the neighbors of the current territory
     * * @param territory
     * 
     * @param shortestDistances
     * @param unsettledTerritories
     * @return the territory with the minimum distance
     */
    private static void relaxNeighbors(Territory territory, Map<Territory, Integer> shortestDistances,
            Set<Territory> unsettledTerritories) {
        List<Territory> neighbors = territory.getNeighbors();
        for (Territory neighbor : neighbors) {
            int distance = shortestDistances.get(territory) + territory.getDistance(neighbor);
            if (distance < shortestDistances.get(neighbor)) {
                shortestDistances.put(neighbor, distance);
                unsettledTerritories.add(neighbor);
            }
        }
    }

}