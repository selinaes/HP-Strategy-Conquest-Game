package edu.duke.ece651.team16.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MapParser {
    private String player;
    private ObjectMapper objectMapper;
    private HashMap<String, ArrayList<HashMap<String, String>>> myMap;

    MapParser(String m) {
        this.player = "";
        this.objectMapper = new ObjectMapper();
        this.myMap = getMapObject(m);
    }

    /**
     * Set the player for the game
     * 
     * @param p
     */
    public void setPlayer(String p) {
        player = p;
    }

    /**
     * Get the player for the game
     * 
     * @return
     */
    public String getPlayer() {
        return player;
    }

    /**
     * Set the Map for the game
     * 
     * @param m
     */
    public void setMap(String m) {
        myMap = getMapObject(m);
    }

    /**
     * Get the territory info of a given territory
     * 
     * @return
     * @throws IOException
     */
    public List<String> getMyInitTerritory() throws IOException {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String color = entry.getKey();
            if (color.equals(player)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    ans.add(asset.get("TerritoryName"));
                }
            }
        }
        return ans;
    }

    /**
     * Get the territory info of a given territory
     * 
     * @param territoryName
     * @return a hashmap with key: playername, units, neighbors
     */
    public HashMap<String, String> getTerritoryInfo(String territoryName) {
        HashMap<String, String> ans = new HashMap<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                if (territory.equals(territoryName)) {
                    ans.put("Player", playername);
                    ans.put("Unit", asset.get("Unit"));
                    ans.put("Neighbors", asset.get("Neighbors"));
                    ans.put("Rate", asset.get("Rate"));
                    ans.put("Resource", asset.get("Resource"));
                    ans.put("Ally", asset.get("Ally"));
                }
            }
        }
        return ans;
    }

    /**
     * Get the territory info of a given player
     * 
     * @return a list of territory names
     */
    public List<String> getMyTerritory() {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            if (playername.equals(player)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    String territory = asset.get("TerritoryName");
                    ans.add(territory);
                }
            }
        }
        return ans;
    }

    public List<String> getAllyTerritory(String ally) {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            if (playername.equals(ally)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    String territory = asset.get("TerritoryName");
                    ans.add(territory);
                }
            }
        }
        return ans;
    }

    /**
     * Get the map object from a json string
     * 
     * @param map
     * @return HashMap<String, ArrayList<HashMap<String, String>>> mapObject
     */
    private HashMap<String, ArrayList<HashMap<String, String>>> getMapObject(String map) {
        try {
            return objectMapper.readValue(map, HashMap.class);
        } catch (JsonProcessingException e) {
            System.out.println("Failed to convert json string to json object.");
            return new HashMap<>();
        }
    }

    /**
     * Update the units info of a given territory
     * 
     * @param formattedUnits
     * @param territoryName
     */
    public void updateUnitsInTerritory(String formattedUnits, String territoryName) {
        List<String> ans = new ArrayList<>();
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            for (HashMap<String, String> asset : playerAsset) {
                String territory = asset.get("TerritoryName");
                if (territory.equals(territoryName)) {
                    asset.put("Unit", formattedUnits);
                }
            }
        }
    }

    public boolean checkAllowedNumAlly() {
        if (myMap.size() <= 2) {
            return false;
        }
        int numAlivePlayers = 0;
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String playername = entry.getKey();
            ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
            if (playerAsset.size() > 0) {
                numAlivePlayers++;
            }
        }
        return numAlivePlayers > 2;
    }

    public String findAlly() {
        for (Map.Entry<String, ArrayList<HashMap<String, String>>> entry : myMap.entrySet()) {
            String color = entry.getKey();
            if (color.equals(player)) {
                ArrayList<HashMap<String, String>> playerAsset = entry.getValue();
                for (HashMap<String, String> asset : playerAsset) {
                    if (!asset.get("Ally").equals(""))
                        return asset.get("Ally");
                }
            }
        }
        return "";
    }
}
